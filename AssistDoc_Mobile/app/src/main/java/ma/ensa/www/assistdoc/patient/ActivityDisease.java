package ma.ensa.www.assistdoc.patient;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import ma.ensa.www.assistdoc.Chat_Activity;
import ma.ensa.www.assistdoc.R;


public class ActivityDisease extends AppCompatActivity {

    TextView head;
    TextView diseaseInfo;
    ImageView chatIcon ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease);

        head = findViewById(R.id.card_head);
        diseaseInfo = findViewById(R.id.disease_info);


        chatIcon = findViewById(R.id.chat_icon);
        chatIcon.setOnClickListener(view ->
                startActivity(new Intent(this, Chat_Activity.class))
        );
        // Récupérer les données transmises par l'intent
        String name = getIntent().getStringExtra("name");
        String gender = getIntent().getStringExtra("gender");
        int max = getIntent().getIntExtra("max", 0);
        int[] c = getIntent().getIntArrayExtra("c");

        // Définir l'en-tête
        head.setText(gender + " " + name + ", you may have:");

        // Identifier les maladies les plus probables avec leurs descriptions détaillées et recommandations
        StringBuilder probableDiseases = new StringBuilder();

        if (max == c[0]) {
            probableDiseases.append("Diarrhoea - ")
                    .append("A condition characterized by frequent, loose, or watery stools. Common causes include bacterial infections, viral infections, food poisoning, and digestive disorders. ")
                    .append("Symptoms may include abdominal cramps, fever, and dehydration.\n")
                    .append("Recommendation: Drink plenty of fluids to prevent dehydration. Avoid dairy and fatty foods. Seek medical advice if symptoms persist or worsen.\n")
                    .append("Learn more: ").append("https://www.google.com/search?q=diarrhoea").append("\n\n");
        }
        if (max == c[1]) {
            probableDiseases.append("Malaria - ")
                    .append("A mosquito-borne infectious disease caused by parasites. Symptoms include fever, chills, headache, and vomiting. Malaria can be life-threatening if not treated. ")
                    .append("It is common in tropical and subtropical regions.\n")
                    .append("Recommendation: Take antimalarial medication as prescribed. Use mosquito nets and insect repellent to prevent mosquito bites. Seek immediate medical attention if symptoms occur.\n")
                    .append("Learn more: ").append("https://www.google.com/search?q=malaria").append("\n\n");
        }
        if (max == c[2]) {
            probableDiseases.append("Typhoid - ")
                    .append("A bacterial infection caused by *Salmonella typhi*, leading to fever, weakness, abdominal pain, and diarrhea. It spreads through contaminated food or water. ")
                    .append("Symptoms include fever, nausea, and loss of appetite.\n")
                    .append("Recommendation: Ensure access to clean water and good sanitation. Take antibiotics as prescribed. Seek medical attention immediately if symptoms develop.\n")
                    .append("Learn more: ").append("https://www.google.com/search?q=typhoid").append("\n\n");
        }
        if (max == c[3]) {
            probableDiseases.append("Diabetes - ")
                    .append("A chronic condition where the body either cannot produce enough insulin or cannot effectively use it. This leads to high blood sugar levels. Symptoms include excessive thirst, frequent urination, and fatigue. ")
                    .append("There are two main types: Type 1 and Type 2 diabetes.\n")
                    .append("Recommendation: Maintain a healthy diet and exercise regularly. Monitor blood sugar levels and follow your doctor's advice regarding medications. Educate yourself on managing diabetes to avoid complications.\n")
                    .append("Learn more: ").append("https://www.google.com/search?q=diabetes").append("\n\n");
        }
        if (max == c[4]) {
            probableDiseases.append("Blood Pressure Issues - ")
                    .append("Conditions like hypertension (high blood pressure) or hypotension (low blood pressure) can affect heart health. Symptoms of hypertension may include headaches, dizziness, and shortness of breath. ")
                    .append("It is often referred to as the 'silent killer' because it may not show symptoms until significant damage occurs.\n")
                    .append("Recommendation: Regularly monitor your blood pressure. Follow a low-salt diet, exercise regularly, and avoid smoking and excessive alcohol consumption. Consult a doctor for medication if necessary.\n")
                    .append("Learn more: ").append("https://www.google.com/search?q=blood+pressure").append("\n\n");
        }
        if (max == c[5]) {
            probableDiseases.append("Cardiovascular Disease - ")
                    .append("Conditions affecting the heart and blood vessels, such as coronary artery disease, heart attacks, and strokes. Symptoms may include chest pain, shortness of breath, and fatigue. ")
                    .append("It is often related to lifestyle factors like poor diet, lack of exercise, and smoking.\n")
                    .append("Recommendation: Maintain a heart-healthy diet, exercise regularly, and avoid smoking. Seek immediate medical care if you experience chest pain or other symptoms. Regular check-ups can help in early detection.\n")
                    .append("Learn more: ").append("https://www.google.com/search?q=cardiovascular+disease").append("\n\n");
        }

        // Afficher les maladies avec descriptions détaillées et recommandations dans le TextView
        diseaseInfo.setText(probableDiseases.toString());

        // Ajouter un écouteur de clic pour les liens
        diseaseInfo.setMovementMethod(LinkMovementMethod.getInstance());  // Pour permettre les liens cliquables
    }

}
