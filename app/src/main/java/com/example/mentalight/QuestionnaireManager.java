package com.example.mentalight;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuestionnaireManager {
    Questionnaire questionnaire;
    public Questionnaire parseQuestionnaireJson(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        String title = jsonObject.getString("title");
        String intro = jsonObject.getString("intro_text");
        String subtitle = jsonObject.getString("subtitle");
        String prefix = jsonObject.getString("prefix");

        if(jsonObject.has("sections")){
            ArrayList<Question> test = new ArrayList<Question>();
            questionnaire = new Questionnaire(title, intro, subtitle, prefix, test);
        } else {
            ArrayList<Question> questionList = getQuestionList(jsonObject);
            questionnaire = new Questionnaire(title, intro, subtitle, prefix, questionList);
        }

        return questionnaire;
    }

    private ArrayList<Question> getQuestionList(JSONObject object) throws JSONException {
        JSONArray questionsArray = object.getJSONArray("questions");
        ArrayList<Question> questions = new ArrayList<>();

        for (int i = 0; i < questionsArray.length(); i++) {
            JSONObject questionObject = questionsArray.getJSONObject(i);
            JSONArray inputTextArray = questionObject.getJSONArray("input_text");

            String[] inputText = new String[inputTextArray.length()];
            for (int j = 0; j < inputTextArray.length(); j++) {
                inputText[j] = inputTextArray.getString(j);
            }
            String questionText = questionObject.getString("question_text");
            String inputType = questionObject.getString("type");
            int id = questionObject.getInt("quest_id");

            Question question = new Question(inputText, questionText, inputType, id);

            questions.add(question);
        }
        return questions;
    }
}
