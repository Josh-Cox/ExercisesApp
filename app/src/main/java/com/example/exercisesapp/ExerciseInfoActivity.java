package com.example.exercisesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ExerciseInfoActivity extends AppCompatActivity {

    // filename for saved Exercises
    public static String filename = "saved";

    // array for saved exercises
    ArrayList<ExInfoModel> savedExInfoModels = new ArrayList<>();

    // activity accessed from
    String clickedFrom;

    /**
     * on activity creation
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_info);

        // unwrap the exercise
        ExInfoModel exInfoModel = Parcels.unwrap(getIntent().getParcelableExtra("exercise"));

        // unwrap the context
        clickedFrom = Parcels.unwrap(getIntent().getParcelableExtra("clickedFrom"));

        // assign values for views
        TextView tvTitle = findViewById(R.id.pageTitle);
        TextView tvMuscle = findViewById(R.id.tvMuscle);
        TextView tvEquipment = findViewById(R.id.tvEquipment);
        TextView tvType = findViewById(R.id.tvType);
        TextView tvDifficulty = findViewById(R.id.tvDifficulty);
        TextView tvInstr = findViewById(R.id.tvInstr);
        ImageView backIcon = findViewById(R.id.backIcon);
        ImageView addIcon = findViewById(R.id.menuOrAddIcon);

        // set text views
        tvTitle.setText(exInfoModel.getName());
        tvMuscle.setText(exInfoModel.getMuscle());
        tvEquipment.setText(exInfoModel.getEquipment());
        tvType.setText(exInfoModel.getType());
        tvDifficulty.setText(exInfoModel.getDifficulty());
        tvInstr.setText(exInfoModel.getInstructions());

        // set context
        Context context = ExerciseInfoActivity.this;

        // get exercises from file
        savedExInfoModels = getSavedEx(ExerciseInfoActivity.this);

        // top action bar
        if(clickedFrom.equals("profile")) {

            // change to delete icon
            addIcon.setImageResource(R.drawable.ic_delete);

            // onClick listener for deletion of an exercise from saved
            addIcon.setOnClickListener(new View.OnClickListener() {
                /**
                 * on delete icon clicked, delete exercise
                 * @param view
                 */
                @Override
                public void onClick(View view) {
                    delEx(context, exInfoModel);
                }
            });
        }
        else {
            // change to add icon
            addIcon.setImageResource(R.drawable.ic_add);

            // onClick listener for addition of an exercise to saved
            addIcon.setOnClickListener(new View.OnClickListener() {
                /**
                 * on add icon clicked, add exercise
                 * @param view
                 */
                @Override
                public void onClick(View view) {
                    addEx(context, exInfoModel);
                }
            });
        }

        // action bar listeners
        backIcon.setOnClickListener(new View.OnClickListener() {
            /**
             * on back icon clicked, go to previous page
             * @param view
             */
            @Override
            public void onClick(View view) {
                onBackPressed();

                // for the recyclerView refresh
                ProfileActivity.refreshed = false;
            }
        });
    }

    /**
     * check if exercise already exists in saved exercises
     * @param obj
     * @param listObj
     * @return boolean (false if not exists)
     */
    public boolean checkExists(ExInfoModel obj, ArrayList<ExInfoModel> listObj) {
        boolean contains = false;
        // loop through list of saved exercises
        for(int i = 0; i < listObj.size(); i++) {
            // if exercises are equal set contains to true
            if (listObj.get(i).getInstructions().equals(obj.getInstructions())) {contains = true;}
        }
        return contains;
    }

    /**
     * add exercise to saved exercises
     * @param context
     * @param exInfoModel
     */
    public void addEx(Context context, ExInfoModel exInfoModel) {

        // get saved exercises from file
        savedExInfoModels = getSavedEx(context);

        // check if exercise is already saved
        if(!checkExists(exInfoModel, savedExInfoModels)) {

            // add exercise to saved exercise list
            savedExInfoModels.add(exInfoModel);

            // save new exercise list to file
            saveEx(context, savedExInfoModels);

            // display exercise saved
            Toast.makeText(context, "Saved to Collection", Toast.LENGTH_SHORT).show();
        }

        //else display exercise already saved
        else {
            Toast.makeText(context, "Exercise already saved", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * delete exercise from saved exercises
     * @param context
     * @param exInfoModel
     */
    public void delEx(Context context, ExInfoModel exInfoModel) {

        // get saved exercises
        savedExInfoModels = getSavedEx(context);

        boolean itemRemoved = false;

        // loop through saved exercises
        for(int i = 0; i < savedExInfoModels.size(); i++) {

            // if exercises are equal
            if (savedExInfoModels.get(i).getInstructions().equals(exInfoModel.getInstructions())) {

                // remove exercise from saved
                savedExInfoModels.remove(i);
                itemRemoved = true;
                break;
            }
        }

        // if item was removed
        if(itemRemoved) {
            // save new exercises list to file
            saveEx(context, savedExInfoModels);

            // display exercise removed
            Toast.makeText(context, "Removed from collection", Toast.LENGTH_SHORT).show();
        }
        else {
            // else display exercise already removed
            Toast.makeText(context, "Exercise already removed", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * save exercises list to file
     * @param context
     * @param savedExInfoModels
     */
    public void saveEx(Context context, ArrayList<ExInfoModel> savedExInfoModels) {

        try {

            // write list to file
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(savedExInfoModels);
            oos.close();

            // catch exceptions
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * get saved exercises from file
     * @param context
     * @return saved exercises
     */
    public static ArrayList<ExInfoModel> getSavedEx(Context context) {

        // create file
        File file = new File(context.getFilesDir(), filename);

        // new list of exercises
        ArrayList<ExInfoModel> savedExInfoModels = new ArrayList<>();

        // check file is not empty
        if(file.length() != 0) {
            try {
                // get saved exercises from file
                FileInputStream fis = context.openFileInput(filename);
                ObjectInputStream ois = new ObjectInputStream(fis);

                // assign values from file to list
                savedExInfoModels = (ArrayList<ExInfoModel>) ois.readObject();
                ois.close();

                // catch exceptions
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        // return new list of exercises
        return savedExInfoModels;
    }

}