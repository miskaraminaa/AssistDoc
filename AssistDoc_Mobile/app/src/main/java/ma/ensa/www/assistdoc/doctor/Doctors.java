package ma.ensa.www.assistdoc.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

    private FirebaseAuth auth;
    private RecyclerView mainUserRecyclerView;
    private UserAdapter adapter;
    private FirebaseDatabase database;
    private ArrayList<Users> usersArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);

        initializeUI();
        setupToolbar();
        setupNavigationDrawer();
        setupChatIcon();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usersArrayList = new ArrayList<>();

        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(this, usersArrayList);
        mainUserRecyclerView.setAdapter(adapter);

        String currentUserId = (auth.getCurrentUser() != null) ? auth.getCurrentUser().getUid() : null;
        if (currentUserId != null) {
            Log.d("DoctorsActivity", "Current User ID: " + currentUserId);
            checkUserRoleAndFetchData(currentUserId);
        } else {
            Log.e("DoctorsActivity", "No authenticated user.");
            finish(); // End activity if no authenticated user is found
        }

        setupSearchView();
    }

    private void initializeUI() {
        drawerLayout = findViewById(R.id.draw_activity);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        chatIcon = findViewById(R.id.chat_icon);
        mainUserRecyclerView = findViewById(R.id.rc_patients);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    private void setupNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            switch (item.getItemId()) {
                case R.id.nav_home:
                    startActivity(new Intent(Doctors.this, Doctors.class));
                    break;
                case R.id.appointment:
                    startActivity(new Intent(Doctors.this, PatientDetailsActivity.class));
                    break;
                case R.id.chats:
                    startActivity(new Intent(Doctors.this, Chat_Activity.class));
                    break;
                case R.id.logout:
                    auth.signOut();
                    startActivity(new Intent(Doctors.this, SignIn_Doctor.class));
                    finish();
                    break;
                default:
                    return false;
            }
            return true;
        });
    }

    private void setupChatIcon() {
        if (chatIcon != null) {
            chatIcon.setOnClickListener(view -> startActivity(new Intent(this, Chat_Activity.class)));
        } else {
            Log.e("DoctorsActivity", "Chat icon not found.");
        }
    }

    private void setupSearchView() {
        SearchView searchView = findViewById(R.id.search_button);
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    filterPatients(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filterPatients(newText);
                    return true;
                }
            });
        }
    }

    private void filterPatients(String query) {
        ArrayList<Users> filteredList = new ArrayList<>();
        for (Users user : usersArrayList) {
            if (user.getUsername() != null && user.getUsername().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(user);
            }
        }
        adapter.updateList(filteredList);
    }

    private void checkUserRoleAndFetchData(String userId) {
        DatabaseReference usersRef = database.getReference("user");
        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String status = dataSnapshot.child("status").getValue(String.class);
                if ("DOCTOR".equalsIgnoreCase(status)) {
                    fetchPatients();
                } else {
                    fetchDoctor();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DoctorsActivity", "Failed to fetch user data: " + error.getMessage());
            }
        });
    }

    private void fetchPatients() {
        DatabaseReference reference = database.getReference("user");
        usersArrayList.clear();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users user = dataSnapshot.getValue(Users.class);
                    if (user != null && "PATIENT".equalsIgnoreCase(dataSnapshot.child("status").getValue(String.class))) {
                        usersArrayList.add(user);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Doctors.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchDoctor() {
        DatabaseReference reference = database.getReference("user");
        usersArrayList.clear();
        reference.orderByChild("status").equalTo("DOCTOR").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users doctor = dataSnapshot.getValue(Users.class);
                    if (doctor != null) {
                        usersArrayList.add(doctor);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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
