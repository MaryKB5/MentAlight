package com.example.mentalight;

import java.util.ArrayList;

public class Subsection {

    private String title, subtitle, intro, prefix;
    private int counter = 1;
    private int id;
    private int numQuest;
    private ArrayList<Question> questions;

    public Subsection(String title, String subtitle, String intro, String prefix, int numQuest, ArrayList<Question> questions){
        this.title = title;
        this.id = counter;
        this.numQuest = numQuest;
        this.questions = questions;
        this.subtitle = subtitle;
        this.prefix = prefix;
        this.intro = intro;
        counter++;
    }

    public int getNumQuest() {
        return numQuest;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }
}
