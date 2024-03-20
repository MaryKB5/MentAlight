package com.example.mentalight;

import java.util.ArrayList;

public class Section {
    private static int counter = 1;
    private int id;
    private String title;
    private String subtitle;
    private String intro;
    private String prefix;
    private  int numQuest = 0;
    private Subsection[] subsections;
    private ArrayList<Question> questions;


    // Konstruktor für einen Abschnitt mit einer Liste von Fragen ohne Unterabschnitte
    public Section(String title, String subtitle, String intro, String prefix, int numQuest, ArrayList<Question> questions){
        this.id = counter;
        this.title = title;
        this.subtitle = subtitle;
        this.intro = intro;
        this.questions = questions;
        this.prefix = prefix;
        this.numQuest = numQuest;
        counter++;
    }

    // Konstruktor für einen Abschnitt mit einer Liste von Unterabschnitten
    public Section(String title, String subtitle, String intro, String prefix, int numQuest, Subsection[] subsections){
        this.title = title;
        this.subtitle = subtitle;
        this.intro = intro;
        this.subsections = subsections;
        this.prefix = prefix;
        this.numQuest = numQuest;
    }

    // Methode zum Abrufen der Liste von Fragen im Abschnitt
    public ArrayList<Question> getQuestions() {
        return questions;
    }

    // Methode zum Abrufen der ID des Abschnitts
    public int getID() {
        return id;
    }

    // Methode zum Abrufen des Titels des Abschnitts
    public String getTitle() {
        return title;
    }

    // Methode zum Abrufen des Untertitels des Abschnitts
    public String getSubtitle() {
        return subtitle;
    }

    // Methode zum Abrufen des Präfix des Abschnitts
    public String getPrefix() {
        return prefix;
    }

    // Methode zum Abrufen des Einführungstexts des Abschnitts
    public String getIntro() {
        return intro;
    }

    // Methode zum Abrufen der Anzahl der Fragen im Abschnitt
    public int getNumQuest() {
        return numQuest;
    }

    // Methode zum Abrufen der Liste von Unterelementen des Abschnitts
    public Subsection[] getSubsections() {
        return subsections;
    }

}
