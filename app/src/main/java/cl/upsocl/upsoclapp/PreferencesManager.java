package cl.upsocl.upsoclapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by upsocl on 24-10-16.
 */

public class PreferencesManager {
    private SharedPreferences prefs ;
    private Context context;
    public static final String TAG= "PreferencesManager";

    public PreferencesManager(Context context) {
        this.context = context;

    }

    public Boolean SavePreferencesString(String typeKey, String key, String value){
        Boolean flag = false;

        try{
            prefs = context.getSharedPreferences(typeKey, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor =  prefs.edit();
            editor.putString(key, value);
            editor.apply();
            flag = true;
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }

        return flag;

    }
    public void RemovePreferences(String typeKey, String key){
        prefs =  context.getSharedPreferences(typeKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  prefs.edit();
        editor.remove(String.valueOf(key)).apply();
    }

    public void SavePreferencesInt(String typeKey, String key, int value) {

        try{
            prefs =  context.getSharedPreferences(typeKey, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor =  prefs.edit();
            editor.putInt(key, value);
            editor.apply();
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
    }
    public String getPreferenceString(String typeKey, String key){
        String objeto =  null;
        try{
            prefs = context.getSharedPreferences(typeKey, Context.MODE_PRIVATE);
            objeto = prefs.getString(key,null);
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
        return objeto;
    }

    public int getPreferenceInt(String typeKey, String key){
        int objeto =  0;
        try{
            prefs = context.getSharedPreferences(typeKey, Context.MODE_PRIVATE);
            objeto = prefs.getInt(key,0);
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
        return objeto;
    }
}
