package ma.ensa.www.assistdoc;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ma.ensa.www.assistdoc.adapter.UserAdapter;
import ma.ensa.www.assistdoc.model.Users;

public class Chat_Activity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;

    RecyclerView mainUserRecyclerView;
    UserAdapter adapter;
    ArrayList<Users> usersArrayList;
    ImageView imglogout  ;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Hide ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Initialize Firebase and UI components
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        DatabaseReference reference = database.getReference().child("user");
        usersArrayList = new ArrayList<>();

        mainUserRecyclerView = findViewById(R.id.mainUserRecyclerView);
        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(Chat_Activity.this, usersArrayList);
        mainUserRecyclerView.setAdapter(adapter);

        // Fetch current user data and filter users based on status
        String currentUserId = auth.getCurrentUser().getUid();
        checkUserRoleAndFetchData(currentUserId);

        // Logout button handling
        imglogout = findViewById(R.id.logoutimg);
        imglogout.setOnClickListener(v -> {
            Dialog dialog = new Dialog(Chat_Activity.this, R.style.dialoge);
            dialog.setContentView(R.layout.dialog_layout);
            Button no, yes;
            yes = dialog.findViewById(R.id.yesbnt);
            no = dialog.findViewById(R.id.nobnt);
            yes.setOnClickListener(v1 -> {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Chat_Activity.this, Activity_SignIn.class);
                startActivity(intent);
                finish();
            });
            no.setOnClickListener(v12 -> dialog.dismiss());
            dialog.show();
        });



        // Check if the user is logged in
        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(Chat_Activity.this, Activity_SignIn.class);
            startActivity(intent);
            finish();
        }
    }

    private void checkUserRoleAndFetchData(String userId) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("user");
        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String status = dataSnapshot.child("status").getValue(String.class);
                    Log.d("Doctors", "User status: " + status);

                    if ("DOCTOR".equals(status)) {
                        // If the current user is a doctor, fetch all patients
                        fetchPatients();
                    } else {
                        // If the current user is not a doctor, fetch the unique doctor
                        fetchDoctor();
                    }
                } else {
                    Log.e("Doctors", "Utilisateur non trouvé.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Doctors", "Erreur de récupération des données utilisateur: " + databaseError.getMessage());
            }
        });
    }

    private void fetchPatients() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("user");
        usersArrayList.clear(); // Clear the list before adding new data

        // Fetch all patients for doctors
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users user = dataSnapshot.getValue(Users.class);

                    if (user != null) {
                        String status = dataSnapshot.child("status").getValue(String.class);
                        if ("PATIENT".equals(status)) {
                            usersArrayList.add(user); // Add patient to the list
                        }
                    }
                }

                // Notify the adapter that the data has changed
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors here
                Toast.makeText(Chat_Activity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchDoctor() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("user");
        usersArrayList.clear(); // Clear the list before adding the doctor

        // Fetch the doctor (since there should only be one in the database)
        reference.orderByChild("status").equalTo("DOCTOR").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users doctor = dataSnapshot.getValue(Users.class);
                    if (doctor != null) {
                        usersArrayList.add(doctor); // Add the doctor to the list
                    }
                }

                // Notify the adapter that the data has changed
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors here
                Toast.makeText(Chat_Activity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}