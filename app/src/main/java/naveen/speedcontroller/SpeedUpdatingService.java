package naveen.speedcontroller;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class SpeedUpdatingService extends Service implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,LocationListener{
    public SpeedUpdatingService() {
    }
    //Initializing the constants
    protected final String TAG = "naveen.speedcontroller";
    protected final long LOCATION_UPDATE_INTERVAL = 10000;
    protected final long FASTEST_LOCATION_UPDATE_INTERVAL = LOCATION_UPDATE_INTERVAL/2;
    protected final int LOCATION_REQUEST_MINIMUM_DISTANCE=2;

    protected Location mCurrentLocation;
    protected LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Runnable r = new Runnable() {
            @Override
            public void run() {

                buildGoogleApiClient();
                mGoogleApiClient.connect();
            }
        };
        Thread GoogleClientThread = new Thread(r);
        GoogleClientThread.start();
        return Service.START_STICKY;
    }
    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
        Log.i(TAG, "buildGoogleApiClient();");

    }
    private void createLocationRequest(){
        Log.i(TAG,"createLocationRequest");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setSmallestDisplacement(LOCATION_REQUEST_MINIMUM_DISTANCE);
        mLocationRequest.setInterval(LOCATION_UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_LOCATION_UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onConnected(Bundle bundle) {
        mCurrentLocation=LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        startLocationUpdate();

    }
    public void startLocationUpdate(){
        Log.i(TAG,"startLocationUpdates");
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        Log.i(TAG, "startLocationUpdates completed");

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation=location;
        float speed = mCurrentLocation.getSpeed();
        //speed threshold is 1m/s so that you can check while walking
        if(speed >=1){
            NotificationCompat.Builder mbuilder = new NotificationCompat.Builder(this)
                    .setContentTitle("Speed Exceeding 80 km/hr")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setDefaults(NotificationCompat.DEFAULT_SOUND)
                    .setContentText("Please decrease your Speed to less than 80km/hr")
                    .setAutoCancel(true);
            NotificationManager manager =(NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0,mbuilder.build());

        }
        Log.i(TAG, "onLocationChanged = " + String.valueOf(mCurrentLocation));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }
}
