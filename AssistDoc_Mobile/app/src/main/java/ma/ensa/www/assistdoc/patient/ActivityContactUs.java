package ma.ensa.www.assistdoc.patient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ma.ensa.www.assistdoc.R;


public class ActivityContactUs extends AppCompatActivity {

    TextView t1;
    TextView t2;
    TextView t3;
    ImageView i1;
    ImageView i2;
    ImageView i3;
    private static final int REQUEST_CALL = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        t1 = findViewById(R.id.call_cs);
        t2 = findViewById(R.id.mail_cs);
        t3 = findViewById(R.id.map_cs);
        i1 = findViewById(R.id.final_call);
        i2 = findViewById(R.id.final_mail);
        i3 = findViewById(R.id.final_map);

        final String num = "8980688666";
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePhoneCall(num);
            }
        });

        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePhoneCall(num);
            }
        });

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SENDTO);
                i.setType("message/rfc822");
                i.setData(Uri.parse("mailto:" + "miskar.a899@ucd.ac.ma"));
                i.putExtra(Intent.EXTRA_SUBJECT, "Query or Feedback");
                try {
                    startActivity(Intent.createChooser(i, "Choose an Email client :"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ActivityContactUs.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SENDTO);
                i.setType("message/rfc822");
                i.setData(Uri.parse("mailto:" + "miskar.a899@ucd.ac.ma"));
                i.putExtra(Intent.EXTRA_SUBJECT, "Query or Feedback");
                try {
                    startActivity(Intent.createChooser(i, "Choose an Email client :"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ActivityContactUs.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bIntent = new Intent(Intent.ACTION_VIEW);
                bIntent.setData(Uri.parse("https://www.google.com/maps/place/National+School+of+Applied+Sciences+-+al-Jadida/@33.2510353,-8.4366865,17z/data=!3m1!4b1!4m6!3m5!1s0xda91dc4c0413d23:0xc8dbb36f4b2d2cbc!8m2!3d33.2510353!4d-8.4341116!16s%2Fg%2F1z44bcxph?entry=ttu&g_ep=EgoyMDI0MTIxMS4wIKXMDSoASAFQAw%3D%3D"));
                startActivity(bIntent);
            }
        });

        i3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bIntent = new Intent(Intent.ACTION_VIEW);
                bIntent.setData(Uri.parse("https://www.google.com/maps/place/National+School+of+Applied+Sciences+-+al-Jadida/@33.2510353,-8.4366865,17z/data=!3m1!4b1!4m6!3m5!1s0xda91dc4c0413d23:0xc8dbb36f4b2d2cbc!8m2!3d33.2510353!4d-8.4341116!16s%2Fg%2F1z44bcxph?entry=ttu&g_ep=EgoyMDI0MTIxMS4wIKXMDSoASAFQAw%3D%3D"));
                startActivity(bIntent);
            }
        });


    }

    private void makePhoneCall(String num)
    {
        if(ContextCompat.checkSelfPermission(ActivityContactUs.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(ActivityContactUs.this, new String[] {Manifest.permission.CALL_PHONE},REQUEST_CALL );
        }
        else
        {
            String dial = "tel:" +num;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

}
