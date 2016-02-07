package naveen.speedcontroller;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Rishikesh on 2/6/2016.
 */
public class SessionSharedPrefs {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    public static final String servicevariable = "servicevariable";
    public static final String distance_total = "distance_total";
    public static final String previous_lat="previous_lat";
    public static final String previous_long="previous_long";



    public SessionSharedPrefs(Context context){
        this._context = context;
        pref=_context.getSharedPreferences("naveen.speedcontroller",Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.commit();
    }

    public void setServicevariable(int ser_variable){
        editor.putInt(servicevariable,ser_variable);
        editor.commit();
    }

    public int getServicevariable(){
        return pref.getInt(servicevariable,1);
    }

    public void setDistance_total(float distance){
        editor.putFloat(distance_total,distance);
        editor.commit();
    }

    public float getDistance_total(){
        return pref.getFloat(distance_total,0.0f);
    }
    public void setPrevious_lat(Float pre_lat){
        editor.putFloat(previous_lat,pre_lat);
        editor.commit();
    }

    public float getPrevious_lat(){
        return pref.getFloat(previous_lat,-200.0f);
    }

    public void setPrevious_long(Float pre_lat){
        editor.putFloat(previous_long,pre_lat);
        editor.commit();
    }
    public float getPrevious_long(){
        return pref.getFloat(previous_long,-200.0f);
    }

}
