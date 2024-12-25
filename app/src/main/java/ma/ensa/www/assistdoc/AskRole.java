package ma.ensa.www.assistdoc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import java.util.Locale;


public class AskRole extends AppCompatActivity {

    private ImageView doctorButton ;
    private TextView doctorview, patientview;
    public int n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_role);
        doctorButton = findViewById(R.id.doctorLogin);
        doctorview = findViewById(R.id.doctortextView);
        patientview = findViewById(R.id.PatienttextView2);

        doctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AskRole.this, SignIn_Doctor.class));
                //checkDoctorSession();
            }

        });

        doctorview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AskRole.this, SignIn_Doctor.class));
                // checkDoctorSession();
            }
        });


        patientview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AskRole.this, Activity_SignIn.class));
                //checkPatientSession();
            }
        });


    }
    private void showChangeLanguageDialog() {
        final String[] listItems = {"French", "English"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AskRole.this);
        mBuilder.setTitle("Choose Language...");
        mBuilder.setSingleChoiceItems(listItems, -1, (dialogInterface, i) -> {
            switch (i) {
                case 0: // French
                    n = 0;
                    setLocale("fr");
                    break;
                case 1: // English
                    n = 1;
                    setLocale("en");
                    break;
            }
            recreate(); // Redémarrer l'activité pour appliquer les changements
            dialogInterface.dismiss(); // Fermer la boîte de dialogue
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    public int getSelectedLanguageIndex() {
        return n;
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        // API 24 et supérieur
        config.setLocale(locale);

        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        // Sauvegarder la préférence de langue
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }
}