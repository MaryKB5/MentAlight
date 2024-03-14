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

    public Section(String title, String subtitle, String intro, String prefix, int numQuest, Subsection[] subsections){
        this.title = title;
        this.subtitle = subtitle;
        this.intro = intro;
        this.subsections = subsections;
        this.prefix = prefix;
        this.numQuest = numQuest;
    }

    public int getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getIntro() {
        return intro;
    }

    public int getNumQuest() {
        return numQuest;
    }

    public Subsection[] getSubsections() {
        return subsections;
    }
}
