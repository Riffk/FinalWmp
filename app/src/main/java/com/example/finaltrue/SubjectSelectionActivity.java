package com.example.finaltrue;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubjectSelectionActivity extends AppCompatActivity {

    private List<Subject> availableSubjects;
    private List<Subject> selectedSubjects;
    private int totalCredits = 0;
    private static final int MAX_CREDITS = 24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_selection);


        availableSubjects = new ArrayList<>();
        availableSubjects.add(new Subject("Mathematics", 3));
        availableSubjects.add(new Subject("Physics", 4));
        availableSubjects.add(new Subject("Biology", 3));
        availableSubjects.add(new Subject("History", 3));
        availableSubjects.add(new Subject("Software Engineering", 4)); // Changed from Chemistry to Software Engineering
        availableSubjects.add(new Subject("English", 2));
        availableSubjects.add(new Subject("Network Security", 3)); // New subject
        availableSubjects.add(new Subject("3D Design", 5)); // New subject

        selectedSubjects = new ArrayList<>();

        // Initialize checkboxes for each subject
        CheckBox mathCheckBox = findViewById(R.id.mathCheckBox);
        CheckBox physicsCheckBox = findViewById(R.id.physicsCheckBox);
        CheckBox biologyCheckBox = findViewById(R.id.biologyCheckBox);
        CheckBox historyCheckBox = findViewById(R.id.historyCheckBox);
        CheckBox softwareEngineeringCheckBox = findViewById(R.id.softwareEngineeringCheckBox);
        CheckBox englishCheckBox = findViewById(R.id.englishCheckBox);
        CheckBox networkSecurityCheckBox = findViewById(R.id.networkSecurityCheckBox);
        CheckBox designCheckBox = findViewById(R.id.designCheckBox);

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            selectedSubjects.clear();
            totalCredits = 0;


            if (mathCheckBox.isChecked()) {
                addSubject(availableSubjects.get(0));
            }
            if (physicsCheckBox.isChecked()) {
                addSubject(availableSubjects.get(1));
            }
            if (biologyCheckBox.isChecked()) {
                addSubject(availableSubjects.get(2));
            }
            if (historyCheckBox.isChecked()) {
                addSubject(availableSubjects.get(3));
            }
            if (softwareEngineeringCheckBox.isChecked()) {
                addSubject(availableSubjects.get(4));
            }
            if (englishCheckBox.isChecked()) {
                addSubject(availableSubjects.get(5));
            }
            if (networkSecurityCheckBox.isChecked()) {
                addSubject(availableSubjects.get(6));
            }
            if (designCheckBox.isChecked()) {
                addSubject(availableSubjects.get(7));
            }


            if (totalCredits > MAX_CREDITS) {
                Toast.makeText(this, "Total credits exceed 24. Please select fewer subjects.", Toast.LENGTH_SHORT).show();
            } else {
                saveEnrollmentData();
            }
        });
    }

    private void addSubject(Subject subject) {
        selectedSubjects.add(subject);
        totalCredits += subject.credits;
    }

    private void saveEnrollmentData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            String userId = user.getUid();

            Map<String, Object> enrollmentData = new HashMap<>();
            List<String> subjectsList = new ArrayList<>();

            for (Subject subject : selectedSubjects) {
                subjectsList.add(subject.name + " - " + subject.credits + " Credits");
            }

            enrollmentData.put("subjects", subjectsList);

            db.collection("users").document(userId).collection("enrollments").add(enrollmentData)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Enrollment saved successfully.", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error saving enrollment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }
}
