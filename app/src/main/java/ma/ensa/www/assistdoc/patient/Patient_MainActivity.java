package ma.ensa.www.assistdoc.patient;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import ma.ensa.www.assistdoc.Activity_SignIn;
import ma.ensa.www.assistdoc.Chat_Activity;
import ma.ensa.www.assistdoc.R;
import ma.ensa.www.assistdoc.chatbot.MainActivity;

public class Patient_MainActivity extends AppCompatActivity {

    Toolbar t;
    DrawerLayout drawer;
    EditText nametext;
    EditText agetext;
    private ImageView enter,enterr, chatIcon;
    RadioButton male;
    RadioButton female;
    RadioGroup rg;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_patient);
        drawer = findViewById(R.id.draw_activity);
        t = findViewById(R.id.toolbar);
        nametext = findViewById(R.id.nametext);
        agetext = findViewById(R.id.agetext);
        enter = findViewById(R.id.imageView7);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        rg=findViewById(R.id.rg1);
        chatIcon = findViewById(R.id.chat_icon);

        mAuth = FirebaseAuth.getInstance();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, t, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView nav = findViewById(R.id.nav_view);

        chatIcon.setOnClickListener(view ->
                startActivity(new Intent(Patient_MainActivity.this, Chat_Activity.class))
        );

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nametext.getText().toString();
                String age = agetext.getText().toString();
                String gender= new String();
                int id=rg.getCheckedRadioButtonId();
                switch(id)
                {
                    case R.id.male:
                        gender = "Mr.";
                        break;
                    case R.id.female:
                        gender = "Ms.";
                        break;
                }
                Intent symp = new Intent(Patient_MainActivity.this, Activity_Symptoms.class);
                symp.putExtra("name",name);
                symp.putExtra("gender",gender);
                startActivity(symp);

            }
        });

        nav.setNavigationItemSelectedListener(menuItem -> {
            switch(menuItem.getItemId())
            {
                case R.id.nav_sos:
                    Intent in = new Intent(Patient_MainActivity.this, SosCall_Activity.class);
                    startActivity(in);
                    break;

                case R.id.nav_share:
                    Intent myIntent = new Intent(Intent.ACTION_SEND);
                    myIntent.setType("text/plain");
                    startActivity(Intent.createChooser(myIntent,"SHARE USING"));
                    break;



                case R.id.nav_hosp:
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                    browserIntent.setData(Uri.parse("https://www.google.com/maps/search/hospitals+near+me"));
                    startActivity(browserIntent);
                    break;
                case R.id.nav_cntus:
                    Intent c_us = new Intent(Patient_MainActivity.this,ActivityContactUs.class);
                    startActivity(c_us);
                    break;
                case R.id.nav_medication:
                    startActivity(new Intent(Patient_MainActivity.this, Medications_Add.class));
                    break;

                case R.id.chatbot:
                    startActivity(new Intent(Patient_MainActivity.this, MainActivity.class));
                    break;



                case R.id.logout:
                    signOut();
                    break;

            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
    }
    private void signOut() {
        mAuth.signOut();
        Toast.makeText(this, getString(R.string.logged_out_successfully), Toast.LENGTH_SHORT).show();

        Intent logoutIntent = new Intent(Patient_MainActivity.this, Activity_SignIn.class);
        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logoutIntent);
    }


}