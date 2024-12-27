package ma.ensa.www.assistdoc.doctor;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ma.ensa.www.assistdoc.Chat_Activity;
import ma.ensa.www.assistdoc.R;
import ma.ensa.www.assistdoc.model.Appointment;

public class PatientDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView chatIcon;

    private DatabaseReference appointmentsRef;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    String reciverName, reciverimg, reciverUid, reciverEmail;

    ListView rc_rdv;
    ArrayList<Appointment> rdvList;
    EditText etAppointmentDate, etAppointmentTime, etDoctorNotes;
    private final Calendar calendar = Calendar.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        appointmentsRef = database.getReference("appointments_patient"); // Reference to the "appointments" node

        reciverName = getIntent().getStringExtra("nameeee");
        reciverimg = getIntent().getStringExtra("reciverImg");
        reciverUid = getIntent().getStringExtra("uid");
        reciverEmail = getIntent().getStringExtra("mail");

        toolbar = findViewById(R.id.toolbar);
        chatIcon = findViewById(R.id.chat_icon);


        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        rdvList = new ArrayList<>();

        // Set up the toolbar
        setSupportActionBar(toolbar);  // This works with androidx.appcompat.widget.Toolbar
        chatIcon.setOnClickListener(view -> startActivity(new Intent(PatientDetailsActivity.this, Chat_Activity.class)));

        // Champs du formulaire rendez-vous
        etAppointmentDate = findViewById(R.id.et_appointment_date);
        etAppointmentTime = findViewById(R.id.et_appointment_time);
        etDoctorNotes = findViewById(R.id.et_doctor_notes);
        // Open DatePickerDialog when date EditText is clicked
        etAppointmentDate.setOnClickListener(v -> openDatePicker());

        // Open TimePickerDialog when time EditText is clicked
        etAppointmentTime.setOnClickListener(v -> openTimePicker());

        Button btnAddAppointment = findViewById(R.id.btn_add_appointment);

        // Ajouter un rendez-vous
        btnAddAppointment.setOnClickListener(v -> {
            String date = etAppointmentDate.getText().toString();
            String time = etAppointmentTime.getText().toString();
            String notes = etDoctorNotes.getText().toString();

            if (date.isEmpty() || time.isEmpty() || notes.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a new Appointment object
            Appointment appointment = new Appointment(currentUserId, reciverName, date, time, notes);

            // Push the appointment to Firebase Realtime Database
            String appointmentId = appointmentsRef.push().getKey(); // Generate a unique key for the appointment
            if (appointmentId != null) {
                appointmentsRef.child(appointmentId).setValue(appointment)
                        .addOnSuccessListener(aVoid -> {
                            sendEmail();
                            Toast.makeText(PatientDetailsActivity.this, "Appointment saved successfully!", Toast.LENGTH_SHORT).show();
                            clearFields(); // Clear input fields after saving
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(PatientDetailsActivity.this, "Failed to save appointment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

        rc_rdv = findViewById(R.id.rc_rdv);
        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance();

        // Initialize appointment list
        rdvList = new ArrayList<>();

        // Fetch appointments
        fetchAppointments();

    }

    private void fetchAppointments() {
        appointmentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                rdvList.clear(); // Clear the list to avoid duplicates

                for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                    Appointment appointment = appointmentSnapshot.getValue(Appointment.class);
                    if (appointment != null) {
                        rdvList.add(appointment);
                    }
                }

                // Update the ListView
                updateListView();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(PatientDetailsActivity.this, "Failed to fetch appointments: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to clear input fields
    private void clearFields() {
        etAppointmentDate.setText("");
        etAppointmentTime.setText("");
        etDoctorNotes.setText("");
    }

    private void updateListView() {
        List<String> appointmentStrings = new ArrayList<>();
        for (Appointment appointment : rdvList) {
            appointmentStrings.add(
                    "Patient: " + appointment.getPatientName() +
                            "\nDate: " + appointment.getDate() +
                            "\nTime: " + appointment.getTime()+
                            "\nNotes: " + appointment.getDoctorNotes()+
                            "\n-------------------------------------------- "
            );
        }

        // Create an ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, appointmentStrings);

        // Set the adapter to the ListView
        rc_rdv.setAdapter(adapter);
    }

    @SuppressLint("QueryPermiswsionsNeeded")
    private void sendEmail() {
        // Email details
        String[] recipients = {reciverEmail}; // Replace with actual recipient email(s)

        String body = "Dear " + reciverName+",\n\n" +
                "This is a reminder about your upcoming appointment:\n\n" +
                "Date: "+etAppointmentDate.getText().toString() + "\n"+
                "Time: "+etAppointmentTime.getText().toString() + "\n\n"+
                "Please bring your insurance documents with you, if available.\n\n" +
                "Best regards,";

        // Create the intent
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","abc@mail.com", null));
        intent.setData(Uri.parse("mailto:")); // Use mailto scheme
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, "AssistDoc_appointment");
        intent.putExtra(Intent.EXTRA_TEXT, body);

        // Verify there is an email app to handle the intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, "Choose an email client"));
        } else {
            // Handle the case where no email app is available
            System.out.println("No email app found.");
        }
    }

    private void openDatePicker() {
        // Initialize DatePickerDialog
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            // Update the Calendar instance
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // Format the selected date
            String formattedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
            etAppointmentDate.setText(formattedDate);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void openTimePicker() {
        // Initialize TimePickerDialog
        new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            // Update the Calendar instance
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);

            // Format the selected time
            String formattedTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(calendar.getTime());
            etAppointmentTime.setText(formattedTime);
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
    }
}