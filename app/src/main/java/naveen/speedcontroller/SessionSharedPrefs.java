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

    public void setDistance_total(String distance){
        editor.putString(distance_total,distance);
        editor.commit();
    }

    public String getDistance_total(){
        return pref.getString(distance_total,"0.0");
    }
}
