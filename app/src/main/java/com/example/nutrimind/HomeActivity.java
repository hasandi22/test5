package com.example.nutrimind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeActivity extends AppCompatActivity {

    // Declare CardView variables for the different sections
    CardView cardStressRelief, cardNutritionPlans, cardFeelingsDiary, cardLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);  // Enable edge-to-edge UI for immersive full-screen experience
        setContentView(R.layout.activity_home);  // Set the content view for the activity

        // Apply window insets to handle system bars (status bar, navigation bar, etc.)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());  // Get insets for system bars
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);  // Set padding to avoid content being hidden under system bars
            return insets;  // Return the insets after applying the padding
        });

        // Initialize the CardViews by finding them from the layout
        cardStressRelief = findViewById(R.id.cardStressRelief);
        cardNutritionPlans = findViewById(R.id.cardNutritionPlans);
        cardFeelingsDiary = findViewById(R.id.cardFeelingsDiary);
        cardLogout = findViewById(R.id.cardLogout);

        // Set OnClickListeners for each card to handle card click actions
        cardStressRelief.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, StressRelief.class)));
        cardNutritionPlans.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, NutritionPlans.class)));
        cardFeelingsDiary.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, FeelingDiary.class)));

        // Set OnClickListener for the logout card to redirect the user to the Login screen
        cardLogout.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, Login.class)));
    }
}
