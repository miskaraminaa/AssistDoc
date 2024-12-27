package ma.ensa.www.assistdoc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import ma.ensa.www.assistdoc.adapter.messagesAdpter;
import ma.ensa.www.assistdoc.model.msgModelclass;

public class Chat_Windo extends AppCompatActivity {
    // Variables pour stocker les informations du destinataire et de l'expéditeur
    String reciverimg, reciverUid, reciverName, SenderUID;
    CircleImageView profile;
    TextView reciverNName;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    public static String senderImg;
    public static String reciverIImg;
    CardView sendbtn;
    EditText textmsg;
    ImageView chatIcon;
    String senderRoom, reciverRoom;
    RecyclerView messageAdpter;
    ArrayList<msgModelclass> messagesArrayList;
    messagesAdpter mmessagesAdpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatwindo);

        // Cacher la barre d'action si elle existe
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Initialisation des composants de l'interface utilisateur
        chatIcon = findViewById(R.id.chat_icon);
        chatIcon.setOnClickListener(view ->
                startActivity(new Intent(this, Chat_Activity.class)) // Redirection vers Chat_Activity
        );

        // Initialisation de Firebase et de l'authentification
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        // Récupérer les données passées via l'Intent
        reciverName = getIntent().getStringExtra("nameeee");
        reciverimg = getIntent().getStringExtra("reciverImg");
        reciverUid = getIntent().getStringExtra("uid");

        // Initialisation de la liste des messages
        messagesArrayList = new ArrayList<>();

        // Récupération des vues du layout
        sendbtn = findViewById(R.id.sendbtnn);
        textmsg = findViewById(R.id.textmsg);
        reciverNName = findViewById(R.id.recivername);
        profile = findViewById(R.id.profileimgg);
        messageAdpter = findViewById(R.id.msgadpter);

        // Initialisation du layout manager pour le RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true); // Afficher les nouveaux messages en bas
        messageAdpter.setLayoutManager(linearLayoutManager);

        // Création de l'adaptateur pour les messages
        mmessagesAdpter = new messagesAdpter(Chat_Windo.this, messagesArrayList);
        messageAdpter.setAdapter(mmessagesAdpter);

        // Charger l'image de profil du destinataire avec Picasso
        Picasso.get().load(reciverimg).into(profile);
        reciverNName.setText(""+reciverName);

        // Récupérer l'UID de l'utilisateur actuellement connecté
        SenderUID = firebaseAuth.getUid();

        // Créer des "rooms" uniques pour l'expéditeur et le destinataire
        senderRoom = SenderUID + reciverUid;
        reciverRoom = reciverUid + SenderUID;

        // Références Firebase pour récupérer les informations de l'utilisateur et les messages
        DatabaseReference reference = database.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference chatreference = database.getReference().child("chats").child(senderRoom).child("messages");

        // Ajouter un ValueEventListener pour récupérer les messages en temps réel
        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear(); // Vider la liste avant d'ajouter de nouveaux messages
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    msgModelclass messages = dataSnapshot.getValue(msgModelclass.class); // Convertir les données en objet msgModelclass
                    messagesArrayList.add(messages); // Ajouter les messages à la liste
                }
                mmessagesAdpter.notifyDataSetChanged(); // Rafraîchir l'adaptateur pour afficher les nouveaux messages
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gérer l'erreur si la récupération des données échoue
            }
        });

        // Récupérer l'image de profil de l'expéditeur depuis Firebase
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderImg = snapshot.child("profilepic").getValue().toString(); // Récupérer l'URL de l'image de profil
                reciverIImg = reciverimg; // Stocker l'image du destinataire
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gérer l'erreur si la récupération des données échoue
            }
        });

        // Événement lors du clic sur le bouton "Envoyer"
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = textmsg.getText().toString(); // Récupérer le texte du message
                if (message.isEmpty()) {
                    // Si le message est vide, afficher un toast
                    Toast.makeText(Chat_Windo.this, "Enter The Message First", Toast.LENGTH_SHORT).show();
                    return;
                }
                textmsg.setText(""); // Effacer le champ de texte après l'envoi

                // Créer un nouvel objet message avec l'heure actuelle
                Date date = new Date();
                msgModelclass messagess = new msgModelclass(message, SenderUID, date.getTime());

                // Ajouter le message à Firebase dans la "room" de l'expéditeur
                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push().setValue(messagess)
                        .addOnCompleteListener(task ->
                                database.getReference().child("chats")
                                        .child(reciverRoom)
                                        .child("messages")
                                        .push().setValue(messagess)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                // Le message a été envoyé avec succès
                                            }
                                        })
                        );
            }
        });

    }
}
