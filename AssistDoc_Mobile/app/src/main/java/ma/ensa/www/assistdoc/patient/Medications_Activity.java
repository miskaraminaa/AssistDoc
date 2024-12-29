package ma.ensa.www.assistdoc.patient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.ensa.www.assistdoc.Chat_Activity;
import ma.ensa.www.assistdoc.R;
import ma.ensa.www.assistdoc.adapter.MedicamentAdapter;
import ma.ensa.www.assistdoc.model.Medicament;

public class Medications_Activity extends AppCompatActivity {

    private ListView listViewMedications;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private MedicamentAdapter medicamentAdapter;
    private List<Medicament> medications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_list);

        // Initialize chat icon
        ImageView chatIcon = findViewById(R.id.chat_icon);
        chatIcon.setOnClickListener(view -> startActivity(new Intent(this, Chat_Activity.class)));

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("medications");

        listViewMedications = findViewById(R.id.listViewMedications);

        medications = new ArrayList<>();
        medicamentAdapter = new MedicamentAdapter(this, medications);
        listViewMedications.setAdapter(medicamentAdapter);

        loadMedications();

        setupSearchView();
    }

    private void setupSearchView() {
        SearchView searchView = findViewById(R.id.search_button);
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    filterMedications(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filterMedications(newText);
                    return true;
                }
            });
        }
    }

    private void loadMedications() {
        String receiverUid = getIntent().getStringExtra("uid");

        // Load medications based on the passed user ID or authenticated user
        if (receiverUid != null) {
            loadMedicationsForUser(receiverUid);
        } else if (mAuth.getCurrentUser() != null) {
            String userId = mAuth.getCurrentUser().getUid();
            loadMedicationsForUser(userId);
        } else {
            Toast.makeText(this, "Veuillez vous connecter pour voir les médicaments.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMedicationsForUser(String userId) {
        dbRef.child(userId).get()
                .addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        HashMap<String, Object> medicationsMap = (HashMap<String, Object>) dataSnapshot.getValue();
                        if (medicationsMap != null) {
                            List<Medicament> loadedMedications = new ArrayList<>();
                            for (Map.Entry<String, Object> entry : medicationsMap.entrySet()) {
                                Medicament medicament = convertToMedicament(entry.getValue());
                                loadedMedications.add(medicament);
                            }

                            if (!loadedMedications.isEmpty()) {
                                medications.clear();
                                medications.addAll(loadedMedications);
                                medicamentAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(this, "Aucun médicament trouvé", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Medications_Activity", "Erreur lors du chargement des médicaments", e);
                    Toast.makeText(this, "Erreur lors du chargement des médicaments", Toast.LENGTH_SHORT).show();
                });
    }

    private Medicament convertToMedicament(Object value) {
        HashMap<String, Object> medicamentMap = (HashMap<String, Object>) value;
        String nom = (String) medicamentMap.get("nom");
        String dosage = (String) medicamentMap.get("dosage");
        String frequence = (String) medicamentMap.get("frequence");
        String heurePris = (String) medicamentMap.get("heurePris");
        return new Medicament(nom, dosage, frequence, heurePris);
    }

    private void filterMedications(String query) {
        ArrayList<Medicament> filteredList = new ArrayList<>();
        for (Medicament medicament : medications) {
            if (medicament.getNom() != null && medicament.getNom().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(medicament);
            }
        }
        medicamentAdapter.updateList(filteredList);
    }

    public void deleteMedication(Medicament medicament, int position) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null || medicament.getId() == null) {
            Toast.makeText(this, "Erreur : Utilisateur non connecté ou ID invalide.", Toast.LENGTH_SHORT).show();
            return;
        }

        dbRef.child(user.getUid()).child(medicament.getId()).removeValue()
                .addOnSuccessListener(aVoid -> {
                    medicamentAdapter.removeItem(position);
                    Toast.makeText(this, "Médicament supprimé avec succès.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Erreur lors de la suppression.", Toast.LENGTH_SHORT).show());
    }
}