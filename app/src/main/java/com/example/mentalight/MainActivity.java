package com.example.mentalight;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mentalight.fragments.IntroFragment;

import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Questionnaire questionnaire;
    private int answeredQuestions = 0;
    private final QuestionnaireManager manager = new QuestionnaireManager();

    private TextView questionText;
    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionText = findViewById(R.id.question_text);
        continueButton = findViewById(R.id.continue_button);


        if (shouldDisplayScreening()) {
            displayScreening();
        } else {
            //@TODO switch case, der ZTPB auswertet und entsprechende Fragebögen übergibt als Liste
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

        }
        showIntro(questionnaireZTPB);
        initUI(questionnaireZTPB);
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

    private void initUI(Questionnaire questionnaire){
        showIntro(questionnaire);
        initProgressBar(questionnaire.getNumQuest());
    }

    private void initProgressBar(int numberOfQuestions){
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(numberOfQuestions);

        TextView progressText = findViewById(R.id.progress_text);
        progressText.setText(answeredQuestions + "/" + numberOfQuestions);

        // TODO muss hochgesetzt werden, wenn eine Frage beantwortet wurde
        answeredQuestions++;
        progressBar.setProgress(answeredQuestions);

    }

    private void showIntro(Questionnaire questionnaire){
        IntroFragment fragment = new IntroFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", questionnaire.getTitle());
        bundle.putString("intro", questionnaire.getIntro());
        fragment.setArguments(bundle);


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.intro_container, fragment)
                .commit();
    }


}