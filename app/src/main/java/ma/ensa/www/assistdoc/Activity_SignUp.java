package ma.ensa.www.assistdoc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;
import ma.ensa.www.assistdoc.model.Users;
import ma.ensa.www.assistdoc.patient.Patient_MainActivity;

public class Activity_SignUp extends AppCompatActivity {

    // Firebase instances
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseDatabase database;
    private FirebaseStorage storage;

    // UI elements
    private EditText usernameText, emailText, passwordText;
    private Button signUpBtn;
    private TextView signIn;
    private CircleImageView rg_profileImg;

    // Profile image
    private Uri imageURI;
    private String imageuri;

    // Other variables
    private ProgressDialog progressDialog;
    private final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private final String status = "PATIENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(Activity_SignUp.this);
        progressDialog.setMessage("Signing Up...");
        progressDialog.setCancelable(false);  // Optional: prevent dismissal manually

        // Redirect user if already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(Activity_SignUp.this, Patient_MainActivity.class));
            finish();
        }

        // Initialize UI elements
        usernameText = findViewById(R.id.username);
        emailText = findViewById(R.id.email_reg);
        passwordText = findViewById(R.id.password_reg);
        signUpBtn = findViewById(R.id.reg_btn);
        signIn = findViewById(R.id.sign_up);
        rg_profileImg = findViewById(R.id.profilerg0);

        // Set listeners
        signIn.setOnClickListener(v -> startActivity(new Intent(Activity_SignUp.this, Activity_SignIn.class)));

        signUpBtn.setOnClickListener(v -> {
            String username = usernameText.getText().toString().trim();
            String email = emailText.getText().toString().trim();
            String password = passwordText.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty() && !username.isEmpty()) {
                if (email.matches(emailPattern)) {
                    signUpUser(email, password, username);
                } else {
                    Toast.makeText(Activity_SignUp.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Activity_SignUp.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        rg_profileImg.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK && data != null) {
            imageURI = data.getData();
            rg_profileImg.setImageURI(imageURI);
        }
    }

    private void signUpUser(String email, String password, String username) {
        progressDialog.show();  // Show the progress dialog
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String id = task.getResult().getUser().getUid();
                        DatabaseReference reference = database.getReference().child("user").child(id);
                        StorageReference storageReference = storage.getReference().child("Upload").child(id);

                        if (imageURI != null) {
                            storageReference.putFile(imageURI).addOnCompleteListener(uploadTask -> {
                                if (uploadTask.isSuccessful()) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                        imageuri = uri.toString();
                                        createUserInDatabase(reference, id, username, email, password, imageuri);
                                    });
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(Activity_SignUp.this, "Error uploading image", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            imageuri = "https://firebasestorage.googleapis.com/v0/b/av-messenger-dc8f3.appspot.com/o/man.png?alt=media&token=880f431d-9344-45e7-afe4-c2cafe8a5257";
                            createUserInDatabase(reference, id, username, email, password, imageuri);
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(Activity_SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createUserInDatabase(DatabaseReference reference, String id, String username, String email, String password, String imageuri) {
        Users users = new Users(id, username, email, password, imageuri, status);
        reference.setValue(users).addOnCompleteListener(task -> {
            progressDialog.dismiss();  // Dismiss the progress dialog once task is done
            if (task.isSuccessful()) {
                Toast.makeText(Activity_SignUp.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Activity_SignUp.this, Activity_SignIn.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(Activity_SignUp.this, "Error creating user in database", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
