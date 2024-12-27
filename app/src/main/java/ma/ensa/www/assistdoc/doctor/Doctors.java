package ma.ensa.www.assistdoc.doctor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;  // Correct import
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ma.ensa.www.assistdoc.Chat_Activity;
import ma.ensa.www.assistdoc.R;
import ma.ensa.www.assistdoc.adapter.UserAdapter;
import ma.ensa.www.assistdoc.model.Users;

public class Doctors extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ImageView chatIcon;

    FirebaseAuth auth;
    RecyclerView mainUserRecyclerView;
    UserAdapter adapter;
    FirebaseDatabase database;
    ArrayList<Users> usersArrayList;


    @SuppressLint({"MissingInflatedId", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);

        drawerLayout = findViewById(R.id.draw_activity);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        chatIcon = findViewById(R.id.chat_icon);

        // Set up the toolbar
        setSupportActionBar(toolbar);  // This works with androidx.appcompat.widget.Toolbar
        chatIcon = findViewById(R.id.chat_icon);
        if (chatIcon != null) {
            chatIcon.setOnClickListener(view -> startActivity(new Intent(this, Chat_Activity.class)));
        } else {
            Log.e("Chat_Activity", "chatIcon not found!");
        }

        // Récupérer l'ID de l'utilisateur actuellement authentifié
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("Doctors", "Current User ID: " + currentUserId);

        // Handle navigation item clicks
        navigationView.setNavigationItemSelectedListener(item -> {
            // Close the drawer after an item is selected
            drawerLayout.closeDrawer(GravityCompat.START);

            switch (item.getItemId()) {
                case R.id.nav_home:
                    // startActivity(new Intent(Doctors.this, HomeActivity.class));
                    break;

                case R.id.appointment:
                    // startActivity(new Intent(Doctors.this, AppointmentActivity.class));
                    break;
                case R.id.chats:
                    startActivity(new Intent(Doctors.this, Chat_Activity.class));
                    break;

                case R.id.logout:
                    // Handle logout logic (e.g., clear session or token)
                    startActivity(new Intent(Doctors.this, SignIn_Doctor.class));
                    finish();
                    break;

                default:
                    return false;
            }
            return true;
        });

        // Initialize Firebase and UI components
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        DatabaseReference reference = database.getReference().child("user");
        usersArrayList = new ArrayList<>();

        mainUserRecyclerView = findViewById(R.id.rc_patients);
        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(Doctors.this, usersArrayList);
        mainUserRecyclerView.setAdapter(adapter);

        checkUserRoleAndFetchData(currentUserId);

    }

    private void checkUserRoleAndFetchData(String userId) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("user");
        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String status = dataSnapshot.child("status").getValue(String.class);

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
                Toast.makeText(Doctors.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Doctors.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
