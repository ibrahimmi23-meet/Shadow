package com.example.shadow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ImageView passwordVisibilityIcon;

    private ImageButton btnPhotoLog;

    private FirebaseAuth firebaseAuth;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();

        // Find views
        emailEditText = findViewById(R.id.editTextEmailLogin);
        passwordEditText = findViewById(R.id.editTextPasswordLogin);
        loginButton = findViewById(R.id.buttonLogin);
        passwordVisibilityIcon = findViewById(R.id.ivPasswordVisibility);
        btnPhotoLog = findViewById(R.id.btnPhotoLog);

        // Set up the click listener for the password visibility icon
        passwordVisibilityIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });

        // Set up the click listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        // Set up the click listener for the photo button
        btnPhotoLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    // Method to toggle the visibility of the password
    private void togglePasswordVisibility() {
        // Toggle the password visibility
        if (passwordEditText.getInputType() == 129) {
            passwordEditText.setInputType(1);
            passwordVisibilityIcon.setImageResource(R.drawable.visibility);
        } else {
            passwordEditText.setInputType(129);
            passwordVisibilityIcon.setImageResource(R.drawable.visibility_off);
        }

        // Move the cursor to the end of the text to maintain cursor position
        passwordEditText.setSelection(passwordEditText.getText().length());
    }

    // Method to handle user login
    private void loginUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Validate the inputs (e.g., check for empty fields)
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            // Handle validation errors
            Toast.makeText(LogInActivity.this, "Email and password are required", Toast.LENGTH_SHORT).show();
        } else {
            // Perform Firebase authentication and login here
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // User login successful
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                Toast.makeText(LogInActivity.this, "Logged in as " + user.getEmail(), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LogInActivity.this, HomeActivity.class));
                                finish(); // Close the LoginActivity
                            } else {
                                // User login failed
                                Toast.makeText(LogInActivity.this, "Login failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    // Method to open the gallery and pick an image
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    // Handle the result of image selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            try {
                // Handle the selected image as needed
                // For example, you can use the selectedImageUri to display or upload the image
            } catch (Exception e) {
                // Handle any errors that may occur
                Log.e("LogInActivity", "Error handling selected image: " + e.getMessage());
            }
        }
    }
}
