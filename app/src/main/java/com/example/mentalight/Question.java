package com.example.mentalight;
// Eine Klasse zur Darstellung einer Frage
public class Question {
    private int id;
    private String[] inputText;
    private String questionText;
    private String inputTypeText;
    private InputType inputType;

    public Question(String[] inputText, String questionText, String inputTypeText, int id) {
        this.id = id;
        this.inputText = inputText;
        this.questionText = questionText;
        this.inputType = getEnumType(inputTypeText);
    }
    // Methode zur Umwandlung des Eingabetyps in eine Enumeration
    private InputType getEnumType(String input) {
        switch (input) {
            case "likert_scale":
                inputType = InputType.LIKERT;
                break;
            case "single_choice":
                inputType = InputType.SINGLE;
                break;
            case "chips":
                inputType = InputType.CHIPS;
                break;
            case "checkbox":
                inputType = InputType.CHECKBOX;
                break;
            default:
                inputType = InputType.FREETEXT;
                break;
        }
        return inputType;
    }
    // Methode zum Abrufen des Eingabetyps
    public InputType getInputType() {
        return inputType;
    }

    // Methode zum Abrufen des Fragetexts
    public String getQuestionText() {
        return questionText;
    }

    // Methode zum Abrufen der Eingabetexte
    public String[] getInputText() {
        return inputText;
    }
}
