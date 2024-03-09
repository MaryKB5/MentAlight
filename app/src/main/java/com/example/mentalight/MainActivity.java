package com.example.mentalight;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Questionnaire questionnaire;
    private final QuestionnaireManager manager = new QuestionnaireManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (shouldDisplayScreening()) {
            displayScreening();
        } else {
            // Fragebogen wurde bereits ausgefüllt, zeige andere Ansicht oder führe andere Logik aus
        }
    }

    private boolean shouldDisplayScreening() {
        // Hier überprüfen Sie den Status des Fragebogens, um festzustellen, ob er angezeigt werden soll
        // Zum Beispiel überprüfen Sie eine Variable in SharedPreferences oder den Status in einer Datenbank
        // Rückgabe true, wenn der Fragebogen noch nicht ausgefüllt wurde, sonst false
        return true;
    }

    private void displayScreening() {
        Questionnaire questionnaireZTPB = getQuestionnaireFromFile("ZTPB.json");
        ArrayList<Question> list = questionnaireZTPB.getQuestions();
        for(Question question: list) {
            Log.d("jiha", question.getQuestionText());
        }
    }

    private Questionnaire getQuestionnaireFromFile(String fileName){
        String json = AssetsReader.loadJsonFromAssets(this, fileName);
        try {
            questionnaire = manager.parseQuestionnaireJson(json);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return questionnaire;
    }

}