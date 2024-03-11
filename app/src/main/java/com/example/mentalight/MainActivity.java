package com.example.mentalight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mentalight.fragments.CheckboxFragment;
import com.example.mentalight.fragments.ChipsFragment;
import com.example.mentalight.fragments.FreeTextFragment;
import com.example.mentalight.fragments.IntroFragment;
import com.example.mentalight.fragments.LikertFragment;
import com.example.mentalight.fragments.SingleChoiceFragment;

import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnStartButtonClickListener {
    private Questionnaire questionnaire;
    private final QuestionnaireManager manager = new QuestionnaireManager();
    private int currentQuestion = 0;
    private int numberOfQuestions;

    private TextView questionText;
    private TextView progressText;
    private Button continueButton;
    private ImageButton backButton;
    private Button exitButton;
    private ProgressBar progressBar;

    private ArrayList<Fragment> fragments;
    private ArrayList<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionText = findViewById(R.id.question_text);
        continueButton = findViewById(R.id.continue_button);
        backButton = findViewById(R.id.back_button);


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
        questions = questionnaireZTPB.getQuestions();
        for(Question question: questions){
            Log.d("Frage", question.getQuestionText());
        }
        questions = manager.loadQuestionsFromQuestionnaire(questionnaireZTPB);
        initUI(questionnaireZTPB, questions);
    }

    private Questionnaire getQuestionnaireFromFile(String fileName) {
        String json = AssetsReader.loadJsonFromAssets(this, fileName);
        try {
            questionnaire = manager.parseQuestionnaireJson(json);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return questionnaire;
    }

    private void initUI(Questionnaire questionnaire, ArrayList<Question> questions) {
        showIntro(questionnaire);
        numberOfQuestions = questionnaire.getNumQuest();
        initProgressBar();
        continueButton.setOnClickListener(v -> {
            continueButtonClicked();
        });
        backButton.setOnClickListener(v -> {
            backButtonClicked();
        });

        fragments = new ArrayList<>();

        for (Question question : questions) {
            Fragment fragment = createFragmentForInputType(question.getInputType().inputName, question);
            fragments.add(fragment);
        }

        for (Fragment fragment : fragments) {
            Log.d("jadaswärs", "Fragment: " + fragment.getClass().getSimpleName());
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragments.get(0))
                .commit();
    }

    private void initProgressBar() {

        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(numberOfQuestions);

        progressText = findViewById(R.id.progress_text);
        progressText.setText(currentQuestion + "/" + numberOfQuestions);
        progressBar.setProgress(0);
    }

    private void updateProgressBar() {
        progressBar.setProgress(currentQuestion);
        progressText.setText(currentQuestion + "/" + numberOfQuestions);
    }

    private void showIntro(Questionnaire questionnaire) {
        IntroFragment fragment = new IntroFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", questionnaire.getTitle());
        bundle.putString("intro", questionnaire.getIntro());
        fragment.setArguments(bundle);


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.intro_container, fragment)
                .commit();
    }

    //muss noch in den Manager ausgelagert werden


    private Fragment createFragmentForInputType(String inputType, Question question) {
        switch (inputType) {
            case "likert_scale":
                return LikertFragment.newInstance(question.getInputText());
            case "single_choice":
                return SingleChoiceFragment.newInstance("", "");
            case "checkbox":
                return CheckboxFragment.newInstance("", "");
            case "free_text":
                return FreeTextFragment.newInstance("", "");
            case "chips":
                return ChipsFragment.newInstance("", "");
            default:
                return null;
        }
    }

    private void continueButtonClicked() {
        currentQuestion++;
        questionText.setText(questions.get(currentQuestion).getQuestionText());
        //for (Fragment fragment : fragments) {
            //Log.d("neueListe", "Fragment: " + fragment.getClass().getSimpleName());
        //}
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragments.get(currentQuestion))
                .commit();

        updateProgressBar();
    }

    private void backButtonClicked() {
        if ((currentQuestion - 1) >= 0) {
            questionText.setText(questions.get(currentQuestion - 1).getQuestionText());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragments.get(currentQuestion - 1))
                    .commit();
            currentQuestion--;
            updateProgressBar();
        } else{
            Toast.makeText(this, "Anfang des Fragebogens erreicht!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onStartButtonClicked() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentById(R.id.intro_container);
        if (fragment != null) {
            transaction.remove(fragment).commit();
        }
    }
}