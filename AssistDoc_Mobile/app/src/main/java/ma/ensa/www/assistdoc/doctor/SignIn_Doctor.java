package ma.ensa.www.assistdoc.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ma.ensa.www.assistdoc.R;

public class SignIn_Doctor extends AppCompatActivity {
    Button button;
    EditText email, password;
    FirebaseAuth auth;
    DatabaseReference dbRef; // Reference to Realtime Database
    String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}";
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_doctor);

        // Hide the ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Initialize FirebaseAuth and DatabaseReference
        auth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("user");

        // Initialize the UI components
        button = findViewById(R.id.LoginBtn);
        email = findViewById(R.id.editText2);
        password = findViewById(R.id.editText);

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing In...");

        // Set onClick listener for login button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString();
                String pass = password.getText().toString();

                // Validation checks
                if (TextUtils.isEmpty(Email)) {
                    Toast.makeText(SignIn_Doctor.this, "Enter The Email", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(SignIn_Doctor.this, "Enter The Password", Toast.LENGTH_SHORT).show();
                } else if (!Email.matches(emailPattern)) {
                    email.setError("Provide a valid Email Address");
                } else if (password.length() < 6) {
                    password.setError("Password must be more than six characters");
                    Toast.makeText(SignIn_Doctor.this, "Password Needs To Be Longer Than Six Characters", Toast.LENGTH_SHORT).show();
                } else {
                    // Show progress dialog
                    progressDialog.show();

                    // Attempt to sign in with email and password
                    auth.signInWithEmailAndPassword(Email, pass).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Check Realtime Database for doctor data
                            validateDoctor(Email);
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(SignIn_Doctor.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void validateDoctor(String email) {
        // Get the current user's Firebase UID
        String userId = auth.getCurrentUser().getUid(); // Get current user ID
        dbRef.child(userId) // Query using the dynamic userId
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressDialog.dismiss();

                        if (snapshot.exists()) {
                            // Retrieve doctor's data from Realtime Database
                            String dbEmail = snapshot.child("useremail").getValue(String.class);
                            String status = snapshot.child("status").getValue(String.class);

                            Log.d("SignIn_Doctor", "Fetched Email: " + dbEmail + ", Status: " + status);

                            if (dbEmail != null && dbEmail.equals(email) && "DOCTOR".equals(status)) {
                                // Navigate to the doctor's main activity
                                Intent intent = new Intent(SignIn_Doctor.this, Doctors.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SignIn_Doctor.this, "Access Denied: Unauthorized User", Toast.LENGTH_SHORT).show();
                                auth.signOut(); // Log out the unauthorized user
                            }
                        } else {
                            Toast.makeText(SignIn_Doctor.this, "Doctor data not found in Realtime Database", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismiss();
                        Toast.makeText(SignIn_Doctor.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
