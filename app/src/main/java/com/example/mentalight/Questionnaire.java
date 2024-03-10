package com.example.mentalight;

import java.util.ArrayList;

public class Questionnaire {

    private ArrayList<Question> questions = new ArrayList<Question>();
    private Section[] sections;
    private String title;
    private String subtitle;
    private String intro;
    private String prefix;

    private int numQuest = 0;

    public Questionnaire(String title, String intro, String subtitle, String prefix, int numQuest, ArrayList<Question> questions) {
        this.questions = questions;
        this.title = title;
        this.subtitle = subtitle;
        this.prefix = prefix;
        this.intro = intro;
        this.numQuest = numQuest;
    }

    public Questionnaire(String title, String intro, String subtitle, String prefix, int numQuest, Section[] sections) {
            this.sections = sections;
            this.title = title;
            this.subtitle = subtitle;
            this.prefix = prefix;
            this.intro = intro;
            this.numQuest = numQuest;
    }

    public String getTitle() {
        return title;
    }

    public String getIntro() {
        return intro;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getPrefix() {
        return prefix;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public int getNumQuest() {
        return numQuest;
    }
}
