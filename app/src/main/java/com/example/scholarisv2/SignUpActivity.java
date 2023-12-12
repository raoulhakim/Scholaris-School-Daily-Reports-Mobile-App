package com.example.scholarisv2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.sign_up_activity);

        /* Initiate the editText for name */
        EditText nameInput = (EditText) findViewById (R.id.nameInput);

        /* Check if the email valid with the format */
        EditText emailInput = (EditText) findViewById (R.id.emailInput);
        TextView emailStatus = (TextView) findViewById (R.id.emailStatus);
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (!emailFormatCheck(editable.toString())) {
                    emailStatus.setText("Email is invalid!");
                } else {
                    emailStatus.setText("");
                }
            }
        });

        /* Set the event listener for password revealing */
        ImageButton passwordReveal = (ImageButton) findViewById (R.id.revealPassword);
        EditText password = (EditText) findViewById(R.id.passwordInput);
        passwordReveal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revealPassword(passwordReveal, password);
            }
        });

        ImageButton confirmPasswordReveal = (ImageButton) findViewById (R.id.revealConfirmPassword);
        EditText confirmPassword = (EditText) findViewById (R.id.confirmPasswordInput);
        confirmPasswordReveal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revealPassword(confirmPasswordReveal, confirmPassword);
            }
        });

        /* Check if the password valid with the format */
        TextView passwordStatus = (TextView) findViewById (R.id.passwordStatus);
        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (!(confirmPassword.getText().toString().equals(password.getText().toString()))) {
                    passwordStatus.setText("Password is not match!");
                } else {
                    passwordStatus.setText("");
                }
            }
        });

        /* Overall check with nextButton */
        Button nextButton = (Button) findViewById (R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (overAllCheck(nameInput, emailInput, emailStatus, password, confirmPassword, passwordStatus)) {
                    Toast.makeText(SignUpActivity.this, "Invalid Input!", Toast.LENGTH_SHORT).show();
                } else {
                    /* Check availability of email from firestore */
                    FirebaseFirestore database = FirebaseFirestore.getInstance();
                    database.collection("user_list")
                            .document(emailInput.getText().toString()).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        Toast.makeText(SignUpActivity.this, "Email has been used!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent chooseRoleIntent = new Intent(SignUpActivity.this, ChooseRoleActivity.class);
                                        chooseRoleIntent.putExtra("name", nameInput.getText().toString());
                                        chooseRoleIntent.putExtra("email", emailInput.getText().toString());
                                        chooseRoleIntent.putExtra("password", confirmPassword.getText().toString());
                                        startActivity(chooseRoleIntent);
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignUpActivity.this, "Connection Failed! Please try again", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }

    private void revealPassword(ImageButton button, EditText editText) {
        if (editText.getTransformationMethod() == null) {
            /* unreveal password if the text visible */
            editText.setTransformationMethod(new PasswordTransformationMethod());
            button.setImageResource(R.drawable.password_visible);
        } else {
            editText.setTransformationMethod(null);
            button.setImageResource(R.drawable.password_invisible);
        }
    }

    private boolean emailFormatCheck(String emailInput) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return emailInput.matches(emailPattern);
    }

    private boolean overAllCheck(EditText name, EditText email, TextView emailStatus, EditText password, EditText confirmPassword, TextView passwordStatus) {
        return name.getText().toString().equals("") ||
                email.getText().toString().equals("") ||
                !(emailStatus.getText().toString().equals("")) ||
                password.getText().toString().equals("") ||
                confirmPassword.getText().equals("") ||
                !(passwordStatus.getText().equals(""));
    }
}