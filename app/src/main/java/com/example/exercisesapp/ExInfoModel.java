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
     * @param name
     * @param type
     * @param muscle
     * @param equipment
     * @param difficulty
     * @param instructions
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
     * @param name
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
     * @param type
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
     * @param muscle
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
     * @param equipment
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
     * @param difficulty
     */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * get exercise instructions
     * @return
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     * set exercise instructions
     * @param instructions
     */
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
