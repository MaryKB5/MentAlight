package com.example.mentalight;

import android.util.Log;

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
        String subtitle, prefix;
        int numQuest;
        if(jsonObject.has("subtitle")){
            subtitle = jsonObject.getString("subtitle");
        } else {
            subtitle = "";
        }
        if(jsonObject.has("prefix")){
            prefix = jsonObject.getString("prefix");
        } else {
            prefix = "";
        }
        if(jsonObject.has("num_quest")){
            numQuest = jsonObject.getInt("num_quest");
        } else {
            numQuest = 0;
        }

        if(jsonObject.has("sections")){
            ArrayList<Question> test = new ArrayList<>();
            questionnaire = new Questionnaire(title, intro, subtitle, prefix, numQuest , test);
        } else {
            ArrayList<Question> questionList = getQuestionList(jsonObject);
            questionnaire = new Questionnaire(title, intro, subtitle, prefix, numQuest, questionList);
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

    public ArrayList<Question> loadQuestionsFromQuestionnaire(Questionnaire questionnaire){
        ArrayList<Question> list = questionnaire.getQuestions();
        for(Question question: list){
            Log.d("ehm", question.getQuestionText());
        }
        return list;
    }
}
