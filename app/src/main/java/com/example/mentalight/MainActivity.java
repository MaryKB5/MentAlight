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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mentalight.fragments.CheckboxFragment;
import com.example.mentalight.fragments.ChipsFragment;
import com.example.mentalight.fragments.FreeTextFragment;
import com.example.mentalight.fragments.IntroFragment;
import com.example.mentalight.fragments.LikertFragment;
import com.example.mentalight.fragments.SingleChoiceFragment;

import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnStartButtonClickListener{
    private Questionnaire questionnaire;
    private int answeredQuestions = 0;
    private final QuestionnaireManager manager = new QuestionnaireManager();

    private TextView questionText;
    private Button continueButton;

    private ArrayList<Fragment> fragments;

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
        ArrayList<Question> questions = loadQuestionsFromQuestionnaire(questionnaireZTPB);
        initUI(questionnaireZTPB, questions);
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

    private void initUI(Questionnaire questionnaire, ArrayList<Question> questions){
        showIntro(questionnaire);
        initProgressBar(questionnaire.getNumQuest());



        //test
        for (String text : questions.get(1).getInputText()) {
            Log.d("sisi", text);
        }


        LikertFragment test = LikertFragment.newInstance(questions.get(1).getInputText());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, test)
                .commit();



        fragments = new ArrayList<>();

        for (Question question : questions) {
            Fragment fragment = createFragmentForInputType(question.getInputType().inputName, question);
            // Fügen Sie hier ggf. Argumente hinzu, die das Fragment benötigt
            fragments.add(fragment);


        }
        for (Fragment fragment : fragments) {
            Log.d("jadaswärs", "Fragment: " + fragment.getClass().getSimpleName());
        }


        //getSupportFragmentManager().beginTransaction()
                //.replace(R.id.fragment_container, fragments.get(2))
                //.commit();
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

    private ArrayList<Question> loadQuestionsFromQuestionnaire(Questionnaire questionnaire){
        ArrayList<Question> list = questionnaire.getQuestions();
        return list;
    }

    private Fragment createFragmentForInputType(String inputType, Question question) {
        Log.d("inputtype", inputType);
        switch (inputType) {
            case "likert_scale":
                return LikertFragment.newInstance(question.getInputText());
            case "single_choice":
                return SingleChoiceFragment.newInstance("","");
            case "checkbox":
                return CheckboxFragment.newInstance("","");
            case "free_text":
                return FreeTextFragment.newInstance("","");
            case "chips":
                return ChipsFragment.newInstance("","");
            default:
                return null;
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