package com.example.mentalight;
// Eine Enumeration zur Darstellung verschiedener Arten von Eingabemöglichkeiten
public enum InputType {
    LIKERT("likert_scale"),
    SINGLE("single_choice"),
    CHECKBOX("checkbox"),
    FREETEXT("free_text"),
    CHIPS("chips");

    public final String inputName;
    InputType(String inputName){
        this.inputName = inputName;
    }
}
