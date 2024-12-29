package ma.ensa.www.assistdoc.chatbot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ma.ensa.www.assistdoc.Chat_Activity;
import ma.ensa.www.assistdoc.R;
import ma.ensa.www.assistdoc.patient.Patient_MainActivity;

public class MainActivity extends AppCompatActivity {

    private TextView textViewAnswer, inputPrompt;
    private Button buttonSend;
    private ProgressBar progressBar;
    private ImageView chatIcon , navHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot); // Remove data binding and set content view

        textViewAnswer = findViewById(R.id.textView_answer);
        inputPrompt = findViewById(R.id.input_prompt);
        buttonSend = findViewById(R.id.button_send);
        progressBar = findViewById(R.id.progressBar);
        chatIcon = findViewById(R.id.chat_icon);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        chatIcon.setOnClickListener(view ->
                startActivity(new Intent(this, Chat_Activity.class))
        );
        navHome = findViewById(R.id.nav_home);
        navHome.setOnClickListener(view ->
                startActivity(new Intent(this, Patient_MainActivity.class)) // Redirection vers Chat_Activity
        );


        buttonSend.setOnClickListener(v -> {
            String query = inputPrompt.getText().toString();
            progressBar.setVisibility(View.VISIBLE);
            textViewAnswer.setText("");
            inputPrompt.setText("");

            GeminiPro model = new GeminiPro();
            model.getResponse(query, new ResponseCallback() {
                @Override
                public void onResponse(String response) {
                    textViewAnswer.setText(response);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError(Throwable throwable) {
                    Toast.makeText(MainActivity.this, "Error! " + throwable.toString(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        });
    }
}