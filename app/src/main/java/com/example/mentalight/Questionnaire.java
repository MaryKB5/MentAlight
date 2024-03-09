package com.example.mentalight;

import java.util.ArrayList;

public class Questionnaire {

    private ArrayList<Question> questions = new ArrayList<Question>();
    private Section[] sections;
    private String title;
    private String subtitle;
    private String intro;
    private String prefix;

    public Questionnaire(String title, String intro, String subtitle, String prefix, ArrayList<Question> questions) {
        this.questions = questions;
        this.title = title;
        this.subtitle = subtitle;
        this.prefix = prefix;
        this.intro = intro;
    }

    public Questionnaire(String title, String intro, String subtitle, String prefix, Section[] sections) {
            this.sections = sections;
            this.title = title;
            this.subtitle = subtitle;
            this.prefix = prefix;
            this.intro = intro;
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
}
