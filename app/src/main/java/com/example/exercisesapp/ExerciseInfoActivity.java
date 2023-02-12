package com.example.exercisesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ExerciseInfoActivity extends AppCompatActivity {

    // array for saved exercises
    ArrayList<ExInfoModel> savedExInfoModels = new ArrayList<ExInfoModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_info);


        // set attributes
        TextView tvTitle = findViewById(R.id.pageTitle);
        TextView tvMuscle = findViewById(R.id.tvMuscle);
        TextView tvEquipment = findViewById(R.id.tvEquipment);
        TextView tvType = findViewById(R.id.tvType);
        TextView tvDifficulty = findViewById(R.id.tvDifficulty);
        TextView tvInstr = findViewById(R.id.tvInstr);
        Button btnSave = findViewById(R.id.btnSaveTest);
        ImageView backIcon = findViewById(R.id.backIcon);
        ImageView menuIcon = findViewById(R.id.menuIcon);

        // set context
        Context context = ExerciseInfoActivity.this;

        // unwrap the exercise
        ExInfoModel exInfoModel = Parcels.unwrap(getIntent().getParcelableExtra("exercise"));

        // set text views
        tvTitle.setText(exInfoModel.getName());
        tvMuscle.setText(exInfoModel.getMuscle());
        tvEquipment.setText(exInfoModel.getEquipment());
        tvType.setText(exInfoModel.getType());
        tvDifficulty.setText(exInfoModel.getDifficulty());
        tvInstr.setText(exInfoModel.getInstructions());

        // get exercises from file
        savedExInfoModels = MainActivity.getSavedEx(ExerciseInfoActivity.this);
        String filename = "saved";

        // action bar listeners
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Back Pressed", Toast.LENGTH_SHORT).show();
            }
        });

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Menu Pressed", Toast.LENGTH_SHORT).show();
            }
        });

        // save btn on click listener
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add exercise to saved exercise list
                savedExInfoModels.add(exInfoModel);

                // write saved exercise list to file
                try {
                    FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(savedExInfoModels);
                    oos.close();

                // catch exceptions
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // save btn on click listener
//        btnRead.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                try {
//                    FileInputStream fis = context.openFileInput(filename);
//                    ObjectInputStream ois = new ObjectInputStream(fis);
//                    savedExInfoModels = (ArrayList<ExInfoModel>) ois.readObject();
//                    ois.close();
//                } catch (FileNotFoundException e) {
//                    throw new RuntimeException(e);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                } catch (ClassNotFoundException e) {
//                    throw new RuntimeException(e);
//                }
//
//                for(int i = 0; i < savedExInfoModels.size(); i++) {
//                    Toast.makeText(ExerciseInfoActivity.this, savedExInfoModels.get(i).getName(), Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });

    }
}