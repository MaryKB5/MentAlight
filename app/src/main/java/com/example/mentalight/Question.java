package com.example.mentalight;

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

    public InputType getInputType() {
        return inputType;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getInputText() {
        return inputText;
    }
}
