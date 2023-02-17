package com.example.exercisesapp;

import org.parceler.Parcel;

import java.io.Serializable;

@Parcel
public class ExInfoModel implements Serializable {

    // attributes
    private String name;
    private String type;
    private String muscle;
    private String equipment;
    private String difficulty;
    private String instructions;

    /**
     * constructor for exercise model
     * @param name name of exercise
     * @param type type of exercise
     * @param muscle muscle that exercise works
     * @param equipment equipment that exercise uses
     * @param difficulty difficulty of exercise
     * @param instructions instructions for exercise
     */
    public ExInfoModel(String name, String type, String muscle, String equipment, String difficulty, String instructions) {
        this.name = name;
        this.type = type;
        this.muscle = muscle;
        this.equipment = equipment;
        this.difficulty = difficulty;
        this.instructions = instructions;
    }

    /**
     * empty constructor for exercise model
     */
    public ExInfoModel() {

    }

    /**
     * toString
     * @return formatted string of values
     */
    @Override
    public String toString() {
        return  name + '\n' +
                "muscle: " + muscle + '\n' +
                "type: " + type + '\n' +
                "difficulty: " + difficulty + '\n' +
                "equipment: " + equipment + '\n' +
                "instructions: " + instructions + '\n';
    }

    /**
     * get exercise name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * set exercise name
     * @param name name of exercise
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get exercise type
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * set exercise type
     * @param type type of exercise
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * get exercise muscle
     * @return muscle
     */
    public String getMuscle() {
        return muscle;
    }

    /**
     * set exercise muscle
     * @param muscle exercise that muscle works
     */
    public void setMuscle(String muscle) {
        this.muscle = muscle;
    }

    /**
     * get exercise equipment
     * @return equipment
     */
    public String getEquipment() {
        return equipment;
    }

    /**
     * set exercise equipment
     * @param equipment equipment that exercise uses
     */
    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    /**
     * get exercise difficulty
     * @return difficulty
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * set exercise difficulty
     * @param difficulty difficulty of exercise
     */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * get exercise instructions
     * @return instructions for the exercise
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     * set exercise instructions
     * @param instructions instructions for exercise
     */
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
