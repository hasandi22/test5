package com.example.nutrimind;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    // Declare UI elements: input fields, button, and text view
    EditText edUsername, edPassword;
    Button btnLogin;
    TextView tViewSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  // Set the layout for this activity

        // Initialize UI elements by linking them with their respective views in the layout
        edUsername = findViewById(R.id.editTextLoginUserName);
        edPassword = findViewById(R.id.editTextLoginPassword);
        btnLogin = findViewById(R.id.buttonLogin);
        tViewSignUp = findViewById(R.id.textViewSignUp);

        // Set up the login button click listener
        btnLogin.setOnClickListener(v -> {
            String username = edUsername.getText().toString().trim();  // Get input from the user
            String password = edPassword.getText().toString().trim();

            // Check if both username and password are entered
            if (username.isEmpty() || password.isEmpty()) {
                // Show a message if either field is empty
                Toast.makeText(getApplicationContext(), "Please enter both username and password", Toast.LENGTH_SHORT).show();
            } else {
                // Retrieve saved username and password from SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                String savedUsername = sharedPreferences.getString("username", "");
                String savedPassword = sharedPreferences.getString("password", "");

                // Check if the entered credentials match the saved ones
                if (username.equals(savedUsername) && password.equals(savedPassword)) {
                    // If the credentials are correct, show success and navigate to the HomeActivity
                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this, HomeActivity.class));  // Navigate to the home page
                    finish();  // Close the login activity
                } else {
                    // If the credentials don't match, show an error message
                    Toast.makeText(Login.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set up the sign-up link to navigate to the Signup activity
        tViewSignUp.setOnClickListener(v -> startActivity(new Intent(Login.this, Signup.class)));
    }
}
