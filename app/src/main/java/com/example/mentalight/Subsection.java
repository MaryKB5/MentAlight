package com.example.mentalight;

public class Subsection {

    private String title;
    private int counter = 1;
    private int id;

    public Subsection(String title){
        this.title = title;
        this.id = counter;
        counter++;
    }
}
