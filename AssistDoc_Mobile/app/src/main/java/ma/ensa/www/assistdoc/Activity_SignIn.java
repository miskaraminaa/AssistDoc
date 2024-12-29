package ma.ensa.www.assistdoc;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import ma.ensa.www.assistdoc.patient.Patient_MainActivity;

public class Activity_SignIn extends AppCompatActivity {
    TextView logsignup;
    Button button;
    EditText email, password;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;  // Initialize the ProgressDialog properly

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Hide the ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance();

        // Initialize the UI components
        button = findViewById(R.id.LoginBtn);
        email = findViewById(R.id.editText2);
        password = findViewById(R.id.editText);
        logsignup = findViewById(R.id.sign_up);

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing In...");  // Set a message for the ProgressDialog

        // Set onClick listener for sign-up TextView
        logsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_SignIn.this, Activity_SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        // Set onClick listener for login button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString();
                String pass = password.getText().toString();

                // Check if the email is empty
                if (TextUtils.isEmpty(Email)) {
                    Toast.makeText(Activity_SignIn.this, "Enter The Email", Toast.LENGTH_SHORT).show();
                }
                // Check if the password is empty
                else if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(Activity_SignIn.this, "Enter The Password", Toast.LENGTH_SHORT).show();
                }
                // Check if the email is valid
                else if (!Email.matches(emailPattern)) {
                    email.setError("Provide a valid Email Address");
                }
                // Check if the password length is less than 6
                else if (password.length() < 6) {
                    password.setError("Password must be more than six characters");
                    Toast.makeText(Activity_SignIn.this, "Password Needs To Be Longer Than Six Characters", Toast.LENGTH_SHORT).show();
                } else {
                    // Show progress dialog
                    progressDialog.show();

                    // Attempt to sign in with email and password
                    auth.signInWithEmailAndPassword(Email, pass).addOnCompleteListener(task -> {
                        // Dismiss the progress dialog once the task is complete
                        progressDialog.dismiss();

                        // If sign-in is successful
                        if (task.isSuccessful()) {
                            try {
                                // Start Patient_MainActivity
                                Intent intent = new Intent(Activity_SignIn.this, Patient_MainActivity.class);
                                startActivity(intent);
                                finish();
                            } catch (Exception e) {
                                Toast.makeText(Activity_SignIn.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Show error message if sign-in failed
                            Toast.makeText(Activity_SignIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
