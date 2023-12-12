package com.example.scholarisv2;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);

        ImageButton revealPassword = (ImageButton) findViewById (R.id.signInPasswordReveal);
        EditText passwordInput = (EditText) findViewById (R.id.signInPasswordInput);
        revealPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revealPassword(revealPassword, passwordInput);
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
}