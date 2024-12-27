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

import ma.ensa.www.assistdoc.doctor.SignIn_Doctor;


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

}