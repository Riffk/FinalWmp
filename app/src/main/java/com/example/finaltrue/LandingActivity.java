package com.example.finaltrue;




import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        Button subjectSelectionButton = findViewById(R.id.subjectSelectionButton);
        Button enrollmentSummaryButton = findViewById(R.id.enrollmentSummaryButton);

        subjectSelectionButton.setOnClickListener(v -> {
            startActivity(new Intent(LandingActivity.this, SubjectSelectionActivity.class));
        });

        enrollmentSummaryButton.setOnClickListener(v -> {
            startActivity(new Intent(LandingActivity.this, EnrollmentSummaryActivity.class));
        });
    }
}
