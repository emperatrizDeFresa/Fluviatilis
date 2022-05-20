package emperatriz.fluviatilis.widgets.pypots;

import android.content.Context;
import android.content.SharedPreferences;

public class SysPypots {

    private static String PYPOTS="fluviatilis";
    public static final String BATTERY_KEY = "/phone_battery";
    public static final String LOCATION_KEY = "/location";
    public static final String DND_KEY = "/dnd";
    public static final String SETINGS_KEY = "/wear_settings";
    public static final String SETINGS_UP_KEY = "/wear_settingsUp";

    public static final String SETTINGS_NUMERO_NOTIFICACIONES="numNot";
    public static final String SETTINGS_NOTIFICACIONES_NO_LEIDAS="noLeidaNot";
    public static final String SETTINGS_PASOS="pasos";
    public static final String SETTINGS_TORCH="torch";
    public static final String SETTINGS_DND="dnd";
    public static final String SETTINGS_DIVISIONES="divisiones";
    public static final String SETTINGS_HALO="halo";
    public static final String SETTINGS_DISCRETO="discreto";
    public static final String SETTINGS_NUMEROS_SOMBREADOS="numeros";

    public static int POLLING_INTERVAL=10;
    public static int LOCATION_INTERVAL=60*6;

    private static SysPypots instance;

    public static SysPypots init(){
        if (instance==null)
            instance = new SysPypots();
        return instance;
    }

    public static float size(float size, int totalSize){
        return (size*totalSize)/454;
    }

    public static int size(int size, int totalSize){
        return Math.round((size*totalSize)/454);
    }

    public static void save(String key, String value, Context context){
        SharedPreferences preferences = context.getSharedPreferences(PYPOTS, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(String key, String defValue, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PYPOTS, context.MODE_PRIVATE);
        return preferences.getString(key, defValue);
    }

    public static void save(String key, int value, Context context){
        SharedPreferences preferences = context.getSharedPreferences(PYPOTS, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(String key, int defValue, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PYPOTS, context.MODE_PRIVATE);
        return preferences.getInt(key, defValue);
    }

    public static void save(String key, long value, Context context){
        SharedPreferences preferences = context.getSharedPreferences(PYPOTS, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static long getLong(String key, long defValue, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PYPOTS, context.MODE_PRIVATE);
        return preferences.getLong(key, defValue);
    }

    public static void save(String key, boolean value, Context context){
        SharedPreferences preferences = context.getSharedPreferences(PYPOTS, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(String key, boolean defValue, Context context) {
        try{
            SharedPreferences preferences = context.getSharedPreferences(PYPOTS, context.MODE_PRIVATE);
            return preferences.getBoolean(key, defValue);
        }catch (Exception ex){
            return false;
        }

    }

}
