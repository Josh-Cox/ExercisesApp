package com.example.exercisesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

public class ExerciseInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_info);

        // set attributes
        TextView tvName = findViewById(R.id.tvName);
        TextView tvMuscle = findViewById(R.id.tvMuscle);
        TextView tvEquipment = findViewById(R.id.tvEquipment);
        TextView tvType = findViewById(R.id.tvType);
        TextView tvDifficulty = findViewById(R.id.tvDifficulty);
        TextView tvInstr = findViewById(R.id.tvInstr);

        ExInfoModel exInfoModel = Parcels.unwrap(getIntent().getParcelableExtra("exercise"));

        tvName.setText(exInfoModel.getName());
        tvMuscle.setText("Muscle: " + exInfoModel.getMuscle());
        tvEquipment.setText("Equipment: " + exInfoModel.getEquipment());
        tvType.setText("Type: " + exInfoModel.getType());
        tvDifficulty.setText("Difficulty: " + exInfoModel.getDifficulty());
        tvInstr.setText(exInfoModel.getInstructions());

        // set instructions to be scrollable
        tvInstr.setMovementMethod(new ScrollingMovementMethod());


    }
}