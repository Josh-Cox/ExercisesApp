package com.example.exercisesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ExerciseInfoActivity extends AppCompatActivity {

    // array for saved exercises
    ArrayList<ExInfoModel> savedExInfoModels = new ArrayList<ExInfoModel>();

    // activity accessed from
    String clickedFrom;

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
        ImageView backIcon = findViewById(R.id.backIcon);
        ImageView addIcon = findViewById(R.id.menuOrAddIcon);

        // set context
        Context context = ExerciseInfoActivity.this;

        // unwrap the exercise
        ExInfoModel exInfoModel = Parcels.unwrap(getIntent().getParcelableExtra("exercise"));

        // unwrap the context
        clickedFrom = Parcels.unwrap(getIntent().getParcelableExtra("clickedFrom"));

        if(clickedFrom.equals("profile")) {
            addIcon.setVisibility(View.GONE);
        }

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

        // action bar icons
        addIcon.setImageResource(R.drawable.ic_add);

        // action bar listeners
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get saved exercises from file
                savedExInfoModels = MainActivity.getSavedEx(context);

                //check if exercise is already saved
                if(!checkExists(exInfoModel, savedExInfoModels)) {

                    // add exercise to saved exercise list
                    savedExInfoModels.add(exInfoModel);

                    // write saved exercise list to file
                    try {
                        FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                        oos.writeObject(savedExInfoModels);
                        oos.close();
                        Toast.makeText(context, "Saved to Collection", Toast.LENGTH_SHORT).show();

                        // catch exceptions
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                 //else display exercise already saved
                else {
                    Toast.makeText(context, "Exercise already saved", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    public boolean checkExists(ExInfoModel obj, ArrayList<ExInfoModel> listObj) {
        Boolean contains = false;
        for(int i = 0; i < listObj.size(); i++) {
            if (listObj.get(i).getInstructions().equals(obj.getInstructions())) {contains = true;}
        }

        return contains;
    }
}