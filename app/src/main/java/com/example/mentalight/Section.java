package com.example.mentalight;

public class Section {
    private static int counter = 1;
    private int id;
    private String title;
    private String subtitle;
    private String intro;
    private Subsection[] subsections;
    private Question[] questions;


    public Section(String title, String subtitle, String intro, Question[] questions){
        this.id = counter;
        this.title = title;
        this.subtitle = subtitle;
        this.intro = intro;
        this.questions = questions;
        counter++;
    }

    public Section(String title, String subtitle, String intro, Subsection[] subsections){
        this.title = title;
        this.subtitle = subtitle;
        this.intro = intro;
        this.subsections = subsections;
    }

    public int getID() {
        return id;
    }
}
