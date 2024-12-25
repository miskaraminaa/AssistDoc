package ma.ensa.www.assistdoc.doctor;

import android.content.Context;
import android.content.SharedPreferences;

public class Doctor_Session_Management {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public Doctor_Session_Management(Context context) {
        sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(String email) {
        editor.putString("email", email);
        editor.apply();
    }

    public String[] getDoctorSession() {
        return new String[] { sharedPreferences.getString("email", null) };
    }

    public void removeSession() {
        editor.clear();
        editor.apply();
    }
}
