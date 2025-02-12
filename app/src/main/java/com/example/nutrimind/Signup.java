package com.example.nutrimind;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {

    // UI Components (EditText for user inputs, Button for submission, TextView for existing user navigation)
    EditText edUsername, edEmail, edPassword, edConfirm;
    Button btn;
    TextView tView;

    // Firebase Authentication & Firestore instances for handling user registration
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);  // Set the layout for this activity

        // Initialize Firebase components for authentication and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        edUsername = findViewById(R.id.editTextSignUpUserName);
        edEmail = findViewById(R.id.editTextSignUpEmail);
        edPassword = findViewById(R.id.editTextSignUpPassword);
        edConfirm = findViewById(R.id.editTextSignUpConfirmPassword);
        btn = findViewById(R.id.buttonSignUp);
        tView = findViewById(R.id.textViewExistingUser);

        // OnClickListener for the Signup button
        btn.setOnClickListener(v -> {
            String username = edUsername.getText().toString().trim();
            String email = edEmail.getText().toString().trim();
            String password = edPassword.getText().toString();
            String confirm = edConfirm.getText().toString();

            // Validate the user inputs before proceeding with registration
            if (validateInputs(username, email, password, confirm)) {
                // If inputs are valid, proceed to register the user
                registerUser(username, email, password);
            }
        });

        // OnClickListener for navigating to the Login page (if the user already has an account)
        tView.setOnClickListener(v -> startActivity(new Intent(Signup.this, Login.class)));
    }

    // Method to validate user inputs (username, email, password, confirm password)
    private boolean validateInputs(String username, String email, String password, String confirm) {
        // Check if any input field is empty
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter all details", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Validate email format using regex
        else if (!isValidEmail(email)) {
            Toast.makeText(getApplicationContext(), "Invalid email format", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Validate password strength using regex
        else if (!isValidPassword(password)) {
            Toast.makeText(getApplicationContext(), "Password must be at least 8 characters long and include a number, a special character, and an uppercase letter", Toast.LENGTH_LONG).show();
            return false;
        }
        // Ensure the password and confirm password match
        else if (!password.equals(confirm)) {
            Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;  // If all validations pass, return true
    }

    // Method to register the user with Firebase Authentication
    private void registerUser(String username, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Log.d("SignupSuccess", "User created with UID: " + user.getUid());
                            saveUserToFirestore(user.getUid(), username, email);  // After successful authentication, save user data to Firestore
                        }
                    } else {
                        // Handle registration failure
                        Log.e("SignupError", "Error: " + task.getException().getMessage());
                        Toast.makeText(getApplicationContext(), "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Method to save user data to Firestore and SharedPreferences after successful registration
    private void saveUserToFirestore(String userId, String username, String email) {
        // Create a map of user data to store in Firestore
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("username", username);
        userMap.put("email", email);

        // Save the data in Firestore under the "users" collection
        db.collection("users").document(userId)
                .set(userMap)
                .addOnSuccessListener(aVoid -> {
                    // On success, save additional user info in SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", username);
                    editor.putString("email", email);
                    editor.putString("password", edPassword.getText().toString().trim());  // Storing password in SharedPreferences (though not a best practice)
                    editor.apply();

                    Log.d("FirestoreSuccess", "User saved successfully!");
                    Toast.makeText(getApplicationContext(), "Signup successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Signup.this, Login.class));  // Redirect to Login page after successful signup
                    finish();  // Close the signup activity
                })
                .addOnFailureListener(e -> {
                    // Handle Firestore save failure
                    Log.e("FirestoreError", "Error saving user: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Error saving user: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    // Method to validate email format using a regular expression
    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();  // Return true if the email matches the pattern
    }

    // Method to validate password format using a regular expression
    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=!]).{8,}$";
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();  // Return true if the password matches the pattern
    }
}
