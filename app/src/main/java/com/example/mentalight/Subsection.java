package com.example.mentalight;

import java.util.ArrayList;

public class Subsection {

    private String title, subtitle, intro, prefix;
    private int counter = 1;
    private int id;
    private int numQuest;
    private ArrayList<Question> questions;

    // Konstruktor für eine Untersektion mit einer Liste von Fragen
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

    // Methode zum Abrufen der Anzahl der Fragen in der Untersektion
    public int getNumQuest() {
        return numQuest;
    }

    // Methode zum Abrufen des Titels der Untersektion
    public String getTitle() {
        return title;
    }

    // Methode zum Abrufen der Liste von Fragen in der Untersektion
    public ArrayList<Question> getQuestions() {
        return questions;
    }
}
