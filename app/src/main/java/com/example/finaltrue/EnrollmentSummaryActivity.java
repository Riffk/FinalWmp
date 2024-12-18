package com.example.finaltrue;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class EnrollmentSummaryActivity extends AppCompatActivity {

    private TextView subjectsList, totalCredits;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment_summary);


        subjectsList = findViewById(R.id.subjectsList);
        totalCredits = findViewById(R.id.totalCredits);


        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();


        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            db.collection("users").document(userId).collection("enrollments").get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<String> selectedSubjects = new ArrayList<>();
                            int total = 0;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                List<String> subjects = (List<String>) document.get("subjects");
                                if (subjects != null) {
                                    for (String subject : subjects) {
                                        selectedSubjects.add(subject);

                                        String[] parts = subject.split(" - ");
                                        if (parts.length == 2) {
                                            total += Integer.parseInt(parts[1].replace(" Credits", ""));
                                        }
                                    }
                                }
                            }


                            if (!selectedSubjects.isEmpty()) {
                                subjectsList.setText(String.join("\n", selectedSubjects));
                            } else {
                                subjectsList.setText("No subjects selected");
                            }
                            totalCredits.setText("Total Credits: " + total);
                        } else {
                            Toast.makeText(EnrollmentSummaryActivity.this, "Error retrieving data", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }
}

