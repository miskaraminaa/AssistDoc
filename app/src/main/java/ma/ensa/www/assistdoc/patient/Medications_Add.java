package ma.ensa.www.assistdoc.patient;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import ma.ensa.www.assistdoc.Chat_Activity;
import ma.ensa.www.assistdoc.R;
import ma.ensa.www.assistdoc.entities.Medicament;
import ma.ensa.www.assistdoc.model.MedicationReminderReceiver;

public class Medications_Add extends AppCompatActivity {
    private EditText editNom, editDosage, editFrequence, editHeurePris;
    private Button buttonAdd, buttonViewMeds;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private ImageView chatIcon ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_medi);

        editNom = findViewById(R.id.editNom);
        editDosage = findViewById(R.id.editDosage);
        editFrequence = findViewById(R.id.editFrequence);
        editHeurePris = findViewById(R.id.editHeurePris);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonViewMeds = findViewById(R.id.buttonViewMeds);
        chatIcon = findViewById(R.id.chat_icon);
        chatIcon.setOnClickListener(view ->
                startActivity(new Intent(this, Chat_Activity.class))
        );
        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("medications");

        buttonAdd.setOnClickListener(view -> {
            String nom = editNom.getText().toString();
            String dosage = editDosage.getText().toString();
            String frequence = editFrequence.getText().toString();
            String heurePris = editHeurePris.getText().toString();

            if (nom.isEmpty() || dosage.isEmpty() || frequence.isEmpty() || heurePris.isEmpty()) {
                Toast.makeText(Medications_Add.this, "Fill All fields Please", Toast.LENGTH_SHORT).show();
                return;
            }

            Medicament medicament = new Medicament(nom, dosage, frequence, heurePris);

            if (mAuth.getCurrentUser() != null) {
                String userId = mAuth.getCurrentUser().getUid();

                dbRef.child(userId).push().setValue(medicament)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(Medications_Add.this, "Medication well added!", Toast.LENGTH_SHORT).show();
                            scheduleMedicationReminders(medicament);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(Medications_Add.this, "Erreur while adding!", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(Medications_Add.this, "Connect to add medication", Toast.LENGTH_SHORT).show();
            }
        });

        buttonViewMeds.setOnClickListener(view -> {
            Intent intent = new Intent(this, Medications_Activity.class);
            startActivity(intent);
        });
    }

    private void scheduleMedicationReminders(Medicament medicament) {
        String[] times = medicament.getHeurePris().split(",");
        for (String time : times) {
            String formattedTime = time.trim();
            String[] parts = formattedTime.split(":");
            if (parts.length == 2) {
                try {
                    int hour = Integer.parseInt(parts[0]);
                    int minute = Integer.parseInt(parts[1]);

                    if (hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59) {
                        scheduleMedicationReminder(hour, minute, medicament.getNom());
                    }
                } catch (NumberFormatException e) {
                    Log.e("MainActivity", "Erreur de format pour l'heure: " + e.getMessage());
                }
            }
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private void scheduleMedicationReminder(int hour, int minute, String medicamentName) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MedicationReminderReceiver.class);
        intent.putExtra("medicament_name", medicamentName);
        intent.putExtra("notification_type", "reminder");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                medicamentName.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Log.d("MainActivity", "Rappel programmé pour " + medicamentName + " à " + calendar.getTime());
    }
}
