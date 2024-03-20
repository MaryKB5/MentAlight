package com.example.mentalight;

import java.util.ArrayList;
// Eine Klasse zur Darstellung eines Fragebogens
public class Questionnaire {

    private ArrayList<Question> questions = new ArrayList<Question>();
    private Section[] sections;
    private String title;
    private String subtitle;
    private String intro;
    private String prefix;

    private int numQuest = 0;

    // Konstruktor für einen Fragebogen mit einer Liste von Fragen
    public Questionnaire(String title, String intro, String subtitle, String prefix, int numQuest, ArrayList<Question> questions) {
        this.questions = questions;
        this.title = title;
        this.subtitle = subtitle;
        this.prefix = prefix;
        this.intro = intro;
        this.numQuest = numQuest;
    }

    // Konstruktor für einen Fragebogen mit Abschnitten
    public Questionnaire(String title, String intro, String subtitle, String prefix, int numQuest, Section[] sections) {
            this.sections = sections;
            this.title = title;
            this.subtitle = subtitle;
            this.prefix = prefix;
            this.intro = intro;
            this.numQuest = numQuest;
    }

    // Methode zum Abrufen des Titels des Fragebogens
    public String getTitle() {
        return title;
    }

    // Methode zum Abrufen des Einleitungstexts des Fragebogens
    public String getIntro() {
        return intro;
    }

    // Methode zum Abrufen des Untertitels des Fragebogens
    public String getSubtitle() {
        return subtitle;
    }

    // Methode zum Abrufen des Präfix des Fragebogens
    public String getPrefix() {
        return prefix;
    }

    // Methode zum Abrufen der Fragenliste des Fragebogens
    public ArrayList<Question> getQuestions() {
        return questions;
    }

    // Methode zum Abrufen der Anzahl der Fragen im Fragebogen
    public int getNumQuest() {
        return numQuest;
    }

    // Methode zum Abrufen der Abschnitte des Fragebogens
    public Section[] getSections() {
        return sections;
    }
}
