package ma.ensa.www.assistdoc.patient;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import ma.ensa.www.assistdoc.Chat_Activity;
import ma.ensa.www.assistdoc.R;


public class Activity_Symptoms extends AppCompatActivity {

    Button dis;
    Spinner s1, s2, s3, s4, s5, s6, s7;
    String d[] = new String[7];
    ImageView chatIcon ;
    // Diseases and their respective symptoms
    Map<String, String[]> diseases = new HashMap<String, String[]>() {{
        put("Diarrhoea", new String[]{"Stomach Ache", "Nausea", "Vomiting", "Fever", "Sudden Weight Loss"});
        put("Malaria", new String[]{"Fever", "Vomiting", "Sweating", "Muscle And Body Pain", "Headaches"});
        put("Typhoid", new String[]{"Fever", "Headaches", "Weakness/Fatigue", "Abdominal Pain", "Muscle Pain", "Dry Cough", "Diarrhoea/Constipation"});
        put("Diabetes", new String[]{"Frequent Urination", "Hunger", "Thirsty Than Usual", "Sudden Weight Loss", "Blurred Vision", "Skin Itching"});
        put("Blood Pressure", new String[]{"Headaches", "Blurred Vision", "Chest Pain", "Shortness in Breath", "Dizziness", "Nausea", "Vomiting"});
        put("Cardio Disease", new String[]{"Shortness in Breath", "Fast Heartbeat", "Indigestion", "Pressure Or Heaviness In Chest", "Anxiety"});
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptoms);
        dis = findViewById(R.id.disease);
        chatIcon = findViewById(R.id.chat_icon);
        chatIcon.setOnClickListener(view ->
                startActivity(new Intent(this, Chat_Activity.class))
        );
        s1 = findViewById(R.id.syp1);
        s2 = findViewById(R.id.syp2);
        s3 = findViewById(R.id.syp3);
        s4 = findViewById(R.id.syp4);
        s5 = findViewById(R.id.syp5);
        s6 = findViewById(R.id.syp6);
        s7 = findViewById(R.id.syp7);

        final String name = getIntent().getStringExtra("name");
        final String gender = getIntent().getStringExtra("gender");

        dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d[0] = s1.getSelectedItem().toString();
                d[1] = s2.getSelectedItem().toString();
                d[2] = s3.getSelectedItem().toString();
                d[3] = s4.getSelectedItem().toString();
                d[4] = s5.getSelectedItem().toString();
                d[5] = s6.getSelectedItem().toString();
                d[6] = s7.getSelectedItem().toString();

                // Initialize counters for each disease
                int[] c = new int[diseases.size()];

                // Loop through selected symptoms and check against disease symptoms
                for (int i = 0; i < 7; i++) {
                    for (Map.Entry<String, String[]> entry : diseases.entrySet()) {
                        String disease = entry.getKey();
                        String[] symptoms = entry.getValue();

                        for (String symptom : symptoms) {
                            if (d[i].equals(symptom)) {
                                // Increment the counter for the matching disease
                                c[Arrays.asList(diseases.keySet().toArray()).indexOf(disease)]++;
                            }
                        }
                    }
                }

                // Find the disease with the highest score
                int max = c[0];
                for (int score : c) {
                    if (score > max) {
                        max = score;
                    }
                }

                // Pass the results to the next activity
                Intent dis_page = new Intent(Activity_Symptoms.this, ActivityDisease.class);
                dis_page.putExtra("name", name);
                dis_page.putExtra("gender", gender);
                dis_page.putExtra("max", max);
                dis_page.putExtra("c", c);
                startActivity(dis_page);
            }
        });
    }
}
