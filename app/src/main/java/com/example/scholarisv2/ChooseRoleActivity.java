package com.example.scholarisv2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChooseRoleActivity extends AppCompatActivity {
    /* Role selected */
    private String roleSelectedIndex = "Teacher";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.choose_role_activity);

        /* Add data to Spinner */
        Spinner roleSpinner = (Spinner) findViewById(R.id.role);
        ArrayList <String> roleList = new ArrayList<>();
        ArrayAdapter measurementAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roleList);
        roleList.add("Teacher");
        roleList.add("Parent");

        measurementAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(measurementAdapter);
        roleSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                roleSelectedIndex = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });


        /* Set click listener to sign up button */
        Button processButton = (Button) findViewById (R.id.processButton);
        processButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog();
            }
        });
    }

    /* Function to show an alert dialog */
    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Make sure you have entered the right data")
                .setTitle("Sign Up Confirmation")
                .setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                /* Get Data from previous intent */
                Intent previous = getIntent();
                String userName = previous.getExtras().getString("name");
                String userEmail = previous.getExtras().getString("email");
                String userPass = previous.getExtras().getString("password");

                /* Put all data to an arrayList */
                Map<String, Object> userSignUp = new HashMap<>();
                userSignUp.put("name", userName);
                userSignUp.put("email", userEmail);
                userSignUp.put("password", userPass);
                userSignUp.put("role", roleSelectedIndex);

                /* Store data to firebase */
                FirebaseFirestore userListDatabase = FirebaseFirestore.getInstance();
                userListDatabase.collection("user_list").document(""+userEmail)
                        .set(userSignUp)
                                .addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Intent successIntent = new Intent(ChooseRoleActivity.this, SignUpSuccessActivity.class);
                                        startActivity(successIntent);
                                    }
                                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ChooseRoleActivity.this, "Connection Failed! Please try again", Toast.LENGTH_SHORT).show();
                            }
                        });

                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
