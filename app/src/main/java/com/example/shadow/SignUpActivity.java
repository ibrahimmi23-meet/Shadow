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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private EditText fullNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText passwordConfirmEditText;
    private EditText phoneEditText;
    private Button signUpButton;
    private ImageView passwordVisibilityIcon;
    private ImageButton photoButton;

    private FirebaseAuth firebaseAuth;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();

        // Find views
        fullNameEditText = findViewById(R.id.editTextFullName);
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        passwordConfirmEditText = findViewById(R.id.editTextPasswordConfirm);
        phoneEditText = findViewById(R.id.editTextPhone);
        signUpButton = findViewById(R.id.buttonSignUp);
        passwordVisibilityIcon = findViewById(R.id.ivPasswordVisibility);
        photoButton = findViewById(R.id.btnPhoto);

        // Set up the click listener for the password visibility icon
        passwordVisibilityIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });

        // Set up the click listener for the sign-up button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = fullNameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = passwordConfirmEditText.getText().toString();
                String phone = phoneEditText.getText().toString();

                // Validate the inputs (e.g., check for empty fields)
                if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(phone)) {
                    // Handle validation errors
                    Toast.makeText(SignUpActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    // Handle password mismatch
                    Toast.makeText(SignUpActivity.this, "Password and Confirm Password must match", Toast.LENGTH_SHORT).show();
                } else {
                    // Perform Firebase authentication and user creation here
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // User registration successful
                                        // You can save the phone number to Firebase Firestore or Realtime Database here.

                                        // Redirect to AdminActivity if the email is the specified admin email
                                        if (email.equals("ibrah@gmail.com")) {
                                            startActivity(new Intent(SignUpActivity.this, AdminActivity.class));
                                        } else {
                                            // Redirect to HomeActivity for non-admin users
                                            startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                                        }

                                        finish(); // Close the SignUpActivity
                                    } else {
                                        // User registration failed
                                        Toast.makeText(SignUpActivity.this, "Registration failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        // Set up the click listener for the photo button
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        // Set up the click listener for the "Sign in." TextView
        TextView signInTextView = findViewById(R.id.textViewSignIn);
        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the login page when the "Sign in." TextView is clicked
                Intent loginIntent = new Intent(SignUpActivity.this, LogInActivity.class);
                startActivity(loginIntent);
            }
        });
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
                // Set the selected image on the ImageButton
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                photoButton.setImageBitmap(bitmap);
            } catch (Exception e) {
                Log.e("SignUpActivity", "Error loading image: " + e.getMessage());
            }
        }
    }

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
}
