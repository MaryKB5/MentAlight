package com.example.mentalight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
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
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements OnStartButtonClickListener {
    private Questionnaire questionnaire, rosenbergSelfEsteem, dassQuestionnaire, sek27, wirf;

    private final QuestionnaireManager manager = new QuestionnaireManager();
    private int currentQuestion = 1;
    private int currentFrag = 0;
    private int numberOfQuestions;

    private TextView questionText, progressText;
    private Button continueButton;
    private ImageButton backButton;
    private Button exitButton;
    private ProgressBar progressBar;

    private ArrayList<Fragment> fragments;

    private ArrayList<Question> questions = new ArrayList<>();
    private Questionnaire questionnaireZTPB;
    private boolean lastQuestionReached;
    private LikertFragment currentFragmentZTPB = new LikertFragment();
    private boolean screeningFinished = true;
    private LikertFragment currentFragment;

    private boolean firstSectionIntroAlreadyShown = false;
    private boolean introShown = false;
    private int sectionNumber = 0;
    private HashMap<String, String> savedResults = new HashMap<>();
    ArrayList<Integer> selectedRadioButtonIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        questionText = findViewById(R.id.question_text);
        continueButton = findViewById(R.id.continue_button);
        backButton = findViewById(R.id.back_button);
        continueButton.setOnClickListener(v -> {
            continueButtonClicked();
        });
        backButton.setOnClickListener(v -> {
            backButtonClicked();
        });

        Log.d("bonjo", "ja");

        if (!screeningFinished) {
            displayScreening();
        } else {
            //@TODO switch case, der ZTPB auswertet und entsprechende Fragebögen übergibt als Liste
            initFurtherQuestionnaires();
            //questions = dassQuestionnaire.getQuestions();
            //questions = manager.loadQuestionsFromQuestionnaire(dassQuestionnaire);
            //initUI(dassQuestionnaire, questions);
            //Section[] test = wirf.getSections();
            //Log.d("huhuuu", test[0].getTitle());

            //questions = rosenbergSelfEsteem.getQuestions();
            //questions = manager.loadQuestionsFromQuestionnaire(rosenbergSelfEsteem);

            //questions = sek27.getQuestions();
            //questions = manager.loadQuestionsFromQuestionnaire(sek27);

            //for(Question quest: questions){
                //Log.d("wuri", quest.getQuestionText());
            //}

            questionnaire = wirf;

            if(questionnaire.getSections() != null){
                initUIsections(questionnaire, questionnaire.getSections());
            }



            //initUI(sek27, questions);

        }
    }

    private void displayScreening() {
        questionnaireZTPB = getQuestionnaireFromFile("ZTPB.json");
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

        questionText.setText(questions.get(0).getQuestionText());

        numberOfQuestions = questionnaire.getNumQuest();
        initProgressBar();


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

        TextView prefix = findViewById(R.id.question_prefix);
        if(questionnaire.getPrefix().length() > 0){
            prefix.setVisibility(View.VISIBLE);
            prefix.setText(questionnaire.getPrefix());
            } else{
            prefix.setVisibility(View.GONE);
        }
    }

    private void initUIsections(Questionnaire questionnaire, Section[] sections){
        if(!introShown){
            showIntro(questionnaire);
            introShown = true;
        }
        if(firstSectionIntroAlreadyShown){
            showIntroSection(sections[sectionNumber]);
        }
        if(lastQuestionReached){
            currentQuestion = 1;
            lastQuestionReached = false;
            currentFrag = 0;
            continueButton.setText("Weiter");
        }

        questions = sections[sectionNumber].getQuestions();

        for(Question lol: questions){
            Log.d("rofl", lol.getQuestionText());
        }

        questionText.setText(questions.get(0).getQuestionText());
        numberOfQuestions = sections[sectionNumber].getNumQuest();
        initProgressBar();

        fragments = new ArrayList<>();

        for (Question question : questions) {
            Fragment fragment = createFragmentForInputType(question.getInputType().inputName, question);
            fragments.add(fragment);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragments.get(0))
                .commit();

        TextView prefix = findViewById(R.id.question_prefix);
        if(sections[sectionNumber].getPrefix().length() > 0){
            prefix.setVisibility(View.VISIBLE);
            prefix.setText(sections[sectionNumber].getPrefix());
        } else{
            prefix.setVisibility(View.GONE);
        }

        sectionNumber++;

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

    private void showIntroSection(Section section) {
        IntroFragment fragment = new IntroFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", section.getTitle());
        bundle.putString("intro", section.getIntro());
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.intro_container, fragment)
                .commit();
    }


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
        Log.d("xd", Integer.toString(currentQuestion));
        if(questionnaire == questionnaireZTPB){
            if (lastQuestionReached) {
                saveInputs();
                screeningFinished = true;
                //assessZTPB();
            }
        }
        if(lastQuestionReached){
            if(questionnaire.getSections() != null){
                initUIsections(questionnaire, questionnaire.getSections());
            }
        } else{
            currentFrag++;
            currentFragment = (LikertFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            if(!currentFragment.oneRadioButtonChecked()){
                Toast.makeText(this, "Bitte eine Antwort auswählen", Toast.LENGTH_SHORT).show();
            } else{
                questionText.setText(questions.get(currentQuestion).getQuestionText());
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragments.get(currentFrag))
                        .commit();
                if(currentQuestion == numberOfQuestions-1){
                    continueButton.setText("Abschließen");
                    lastQuestionReached = true;
                }
                currentQuestion++;
                updateProgressBar();
            }
        }



        //@TODO questionnaire Variable muss dann unbedingt überschrieben werden für die anderen Fragebögen
    }

    private void backButtonClicked() {
        currentQuestion--;
        currentFrag--;
        Log.d("current", Integer.toString(currentQuestion));

        if ((currentQuestion - 1) >= 0) {
            Log.d("sers", questions.get(currentQuestion - 1).getQuestionText());
            questionText.setText(questions.get(currentQuestion - 1).getQuestionText());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragments.get(currentFrag))
                    .commit();

            updateProgressBar();
        } else{
            Toast.makeText(this, "Anfang des Fragebogens erreicht!", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveInputs(){
        for(Fragment fragment: fragments){

            LikertFragment likert = (LikertFragment) fragment;
            SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            String input = likert.getCheckedRadioButtonText();
            String key = "question_" + currentQuestion;

            editor.putString(key, input);
            editor.apply();

            String savedResult = preferences.getString(key, "default_value");

            savedResults.put(key, savedResult);
        }
    }

    private void initFurtherQuestionnaires(){
        dassQuestionnaire = getQuestionnaireFromFile("DASS_fragebogen.json");
        //Questionnaire emotionsanalyse = getQuestionnaireFromFile("Emotionsanalyse.json");
        rosenbergSelfEsteem = getQuestionnaireFromFile("rosenberg_self_esteem_scale.json");
        sek27 = getQuestionnaireFromFile("SEK-27_emotionale_Kompetenzen.json");
        wirf = getQuestionnaireFromFile("WIRF_ressourcen.json");
        Log.d("quest", wirf.getTitle());
    }

    //private ArrayList<Questionnaire> assessZTPB(){
    //}

    @Override
    public void onStartButtonClicked() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentById(R.id.intro_container);
        if (fragment != null) {
            transaction.remove(fragment).commit();
        }
        if(questionnaire.getSections() != null && !firstSectionIntroAlreadyShown){
            Section[] sections = questionnaire.getSections();
            showIntroSection(sections[0]);
            firstSectionIntroAlreadyShown = true;
        }
    }
}