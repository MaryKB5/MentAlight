package com.example.mentalight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.mentalight.fragments.OverviewFragment;
import com.example.mentalight.fragments.SingleChoiceFragment;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements OnStartButtonClickListener, OnQuestionnaireClickedListener{
    private Questionnaire questionnaire, rosenbergSelfEsteem, dassQuestionnaire, sek27, wirf, questionnaireZTPB, emotionsanalyse;
    private final QuestionnaireManager manager = new QuestionnaireManager();
    private ArrayList<Questionnaire> relevantQuestionnaires = new ArrayList<>();
    private ArrayList<Questionnaire> furtherQuestionnaires = new ArrayList<>();
    private int currentQuestion = 1;
    private int currentFrag = 0;
    private int numberOfQuestions;
    private int sectionNumber = 0;
    private int subsectionNumber = 0;
    private TextView questionText, progressText;
    private Button continueButton;
    private ImageButton backButton;
    private boolean firstSectionIntroAlreadyShown = false;
    private boolean introShown = false;
    private boolean goOn = true;
    private boolean lastQuestionReached;
    private boolean isScreeningFinished = false;
    private boolean overviewShown = false;
    private ImageButton exitButton;
    private ProgressBar progressBar;
    private ArrayList<Fragment> fragments;
    private ArrayList<Question> questions;
    private String[] relevantQuestionnairesTitles;
    private Subsection[] subsections;
    private HashMap<String, String> savedResults = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialisierung der UI-Elemente
        questions = new ArrayList<>();
        questionnaire = new Questionnaire("", "", "", "", 0, questions);
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
        exitButton = findViewById(R.id.exit_button);
        exitButton.setVisibility(View.GONE);


        // Überprüfen, ob das Screening abgeschlossen ist
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isScreeningFinished = sharedPreferences.getBoolean("screeningFinished", false);

        if (!isScreeningFinished) {
            displayScreening();
        } else {
            // Überprüfen, ob relevante Fragebögen vorhanden sind
            sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            int size = sharedPreferences.getInt("questionnaireTitles_size", 0);
            String[] relevantQuestionnairesTitles = new String[size];
            for (int i = 0; i < size; i++) {
                relevantQuestionnairesTitles[i] = sharedPreferences.getString("questionnaireTitle_" + i, null);
            }
            initOverview(relevantQuestionnairesTitles);
        }
    }


    //@TODO wirf und emotionsanalyse nochmakl neu reinladne wenn die app fertig ist !!!



    // Anzeige des ZTPB-Screening-Fragebogens
    private void displayScreening() {
        questionnaireZTPB = getQuestionnaireFromFile("ZTPB.json");
        questions = questionnaireZTPB.getQuestions();
        initUI(questionnaireZTPB, questions);
    }

    // Initialisierung der Benutzeroberfläche
    private void initUI(Questionnaire questionnaire, ArrayList<Question> questions) {
        // Anzeigen das Fragebogenintro mit Einführungstext an
        showIntro(questionnaire);
        questionText.setText(questions.get(0).getQuestionText());
        numberOfQuestions = questionnaire.getNumQuest();
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
        if(questionnaire.getPrefix().length() > 0){
            prefix.setVisibility(View.VISIBLE);
            prefix.setText(questionnaire.getPrefix());
            } else{
            prefix.setVisibility(View.GONE);
        }
    }

    // Initialisierung der Benutzeroberfläche, speziell für Abschnitte
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

        if(sections[sectionNumber].getSubsections() != null){
            subsections = sections[sectionNumber].getSubsections();
            questions = subsections[subsectionNumber].getQuestions();
            numberOfQuestions = subsections[subsectionNumber].getNumQuest();
        } else {
            questions = sections[sectionNumber].getQuestions();
            numberOfQuestions = sections[sectionNumber].getNumQuest();
        }

        questionText.setText(questions.get(0).getQuestionText());
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
        if(sections[sectionNumber].getSubsections() == null){
            sectionNumber++;
        } else {
            if(subsectionNumber == subsections.length-1){
                sectionNumber++;
                subsectionNumber = 0;
            }
        }
        subsectionNumber++;
    }

    // Initialisierung des Fortschrittsindikators
    private void initProgressBar() {

        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(numberOfQuestions);

        progressText = findViewById(R.id.progress_text);
        progressText.setText(currentQuestion + "/" + numberOfQuestions);
        progressBar.setProgress(0);
    }

    // Aktualisierung des Fortschrittsindikators
    private void updateProgressBar() {
        progressBar.setProgress(currentQuestion);
        progressText.setText(currentQuestion + "/" + numberOfQuestions);
    }

    // Anzeigen der Fragebogeneinführung
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

    // Einführung für Abschnitte
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

    // Fragmenterstellung je nach Input-Typ
    private Fragment createFragmentForInputType(String inputType, Question question) {
        switch (inputType) {
            case "likert_scale":
                return LikertFragment.newInstance(question.getInputText());
            case "single_choice":
                return SingleChoiceFragment.newInstance(question.getInputText());
            case "checkbox":
                return CheckboxFragment.newInstance(question.getInputText());
            case "free_text":
                return FreeTextFragment.newInstance("", "");
            case "chips":
                return ChipsFragment.newInstance(question.getInputText());
            default:
                return null;
        }
    }

    // Fortsetzungsbutton wurde geklickt
    private void continueButtonClicked() {

        if(lastQuestionReached){
            currentQuestion = 1;
            lastQuestionReached = false;
            currentFrag = 0;
            continueButton.setText("Weiter");

            if(questionnaire.getSections() != null && overviewShown){
                initUIsections(questionnaire, questionnaire.getSections());
            }
            // Falls man sich im Anfangsscreening befindet, Speichern und Auswerten
            if (questionnaire == questionnaireZTPB) {
                initFurtherQuestionnaires();
                saveInputs();
                isScreeningFinished = true;
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("screeningFinished", isScreeningFinished);
                editor.apply();
                relevantQuestionnaires =  assessZTPB();
                relevantQuestionnairesTitles = new String[relevantQuestionnaires.size()];
                for(int i = 0; i < relevantQuestionnairesTitles.length; i++){
                   relevantQuestionnairesTitles[i] = relevantQuestionnaires.get(i).getTitle();
                }

                sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putInt("questionnaireTitles_size", relevantQuestionnairesTitles.length);
                for (int i = 0; i < relevantQuestionnairesTitles.length; i++) {
                    editor.putString("questionnaireTitle_" + i, relevantQuestionnairesTitles[i]);
                }
                editor.apply();

                initOverview(relevantQuestionnairesTitles);
            }
        } else{
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

             if(currentFragment instanceof LikertFragment){
                              LikertFragment currentLikertFragment = (LikertFragment) currentFragment;
                              if(!currentLikertFragment.oneRadioButtonChecked()) {
                                  Toast.makeText(this, "Bitte eine Antwort auswählen", Toast.LENGTH_SHORT).show();
                                  goOn = false;
                              } else {
                                  goOn = true;
                              }
                          } else if(currentFragment instanceof ChipsFragment){
                             ChipsFragment currentLikertFragment = (ChipsFragment) currentFragment;
                              if(!currentLikertFragment.oneChipChecked()) {
                                  Toast.makeText(this, "Bitte eine Antwort auswählen", Toast.LENGTH_SHORT).show();
                                  goOn = false;
                              } else {
                                  goOn = true;
                              }

                          } else if(currentFragment instanceof SingleChoiceFragment){
                              SingleChoiceFragment currentSingleFragment = (SingleChoiceFragment) currentFragment;
                              if(!currentSingleFragment.oneRadioButtonChecked()) {
                                  Toast.makeText(this, "Bitte eine Antwort auswählen", Toast.LENGTH_SHORT).show();
                                  goOn = false;
                              } else{
                                  goOn = true;
                              }
                          }
             //Wenn Antwort ausgewählt ist, weitermachen...
            if(goOn){
                currentFrag++;

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragments.get(currentFrag))
                        .commit();
                questionText.setText(questions.get(currentQuestion).getQuestionText());
                if(currentQuestion == numberOfQuestions-1){
                    continueButton.setText("Abschließen");
                    lastQuestionReached = true;
                }
                currentQuestion++;
                updateProgressBar();
            }
        }
    }

    // Zurückbutton geklickt, Anzeigen der vorhergehenden Frage
    private void backButtonClicked() {
        currentQuestion--;
        currentFrag--;

        if ((currentQuestion - 1) >= 0) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragments.get(currentFrag))
                    .commit();
            questionText.setText(questions.get(currentQuestion - 1).getQuestionText());
            updateProgressBar();
        } else{
            Toast.makeText(this, "Anfang des Fragebogens erreicht!", Toast.LENGTH_SHORT).show();
        }
    }

    // Speichern der inputs des Anfangscreenings
    private void saveInputs(){
        int i = 1;
        for(Fragment fragment: fragments){

            LikertFragment likert = (LikertFragment) fragment;
            SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            String input = likert.getCheckedRadioButtonText();
            String key = "question_" + i;

            editor.putString(key, input);
            editor.apply();

            String savedResult = preferences.getString(key, "default_value");

            savedResults.put(key, savedResult);
            i++;
        }
    }

    // Einlesen des Fragebogens aus einer JSON-Datei
    public Questionnaire getQuestionnaireFromFile(String fileName) {
        String json = AssetsReader.loadJsonFromAssets(this, fileName);
        try {
            questionnaire = manager.parseQuestionnaireJson(json);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return questionnaire;
    }


    // Erstellen aller weiteren Fragebögen
    private void initFurtherQuestionnaires(){
        dassQuestionnaire = getQuestionnaireFromFile("DASS_fragebogen.json");
        furtherQuestionnaires.add(dassQuestionnaire);
        rosenbergSelfEsteem = getQuestionnaireFromFile("rosenberg_self_esteem_scale.json");
        furtherQuestionnaires.add(rosenbergSelfEsteem);
        sek27 = getQuestionnaireFromFile("SEK-27_emotionale_Kompetenzen.json");
        furtherQuestionnaires.add(sek27);
        wirf = getQuestionnaireFromFile("WIRF_ressourcen.json");
        furtherQuestionnaires.add(wirf);
        emotionsanalyse = getQuestionnaireFromFile("Emotionsanalyse.json");
        furtherQuestionnaires.add(emotionsanalyse);
    }

    // Initialisierung der Übersichtsseite der weiteren, relevanten Fragebögen
    private void initOverview(String[] titles){
        OverviewFragment overview = OverviewFragment.newInstance(titles);
        overview.setQuestionnaireClickedListener(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.intro_container,overview)
                .commit();
        exitButton.setVisibility(View.VISIBLE);
        exitButton.setOnClickListener(v -> {
            exitButtonClicked();
        });
    }

    // Auswertung des Anfangsscreenings und Rückgabe der Liste mit nutzerspezifischen Folgefragebögen je nach Eingaben beim Anfangsscreening
    private ArrayList<Questionnaire> assessZTPB(){
        ArrayList<Questionnaire> furtherQuestionnaires = new ArrayList<>();

        for (HashMap.Entry<String, String> savedResult : savedResults.entrySet()) {
            String key = savedResult.getKey();
            String value = savedResult.getValue();

            boolean criticalValue = value.equals("Weder noch") || value.equals("Trifft eher nicht zu") || value.equals("Trifft nicht zu");
            if(key.equals("question_1") || key.equals("question_2") || key.equals("question_3") || key.equals("question_4")){
                if(criticalValue){
                    if(!furtherQuestionnaires.contains(wirf)){
                        furtherQuestionnaires.add(wirf);
                    }
                }
            }
            if(key.equals("question_5") || key.equals("question_6") || key.equals("question_7") || key.equals("question_8")) {
                if(criticalValue){
                    if(!furtherQuestionnaires.contains(dassQuestionnaire)){
                        furtherQuestionnaires.add(dassQuestionnaire);
                    }
                }
            }
            if(key.equals("question_9") || key.equals("question_10") || key.equals("question_11") || key.equals("question_12")) {
                if (criticalValue) {
                    if(!furtherQuestionnaires.contains(sek27)){
                        furtherQuestionnaires.add(sek27);
                    }
                    if(!furtherQuestionnaires.contains(emotionsanalyse)){
                        furtherQuestionnaires.add(emotionsanalyse);
                    }
                }
            }
            if(key.equals("question_37") || key.equals("question_38") || key.equals("question_39") || key.equals("question_40")) {
                if (criticalValue) {
                    if(!furtherQuestionnaires.contains(rosenbergSelfEsteem)){
                        furtherQuestionnaires.add(rosenbergSelfEsteem);
                    }
                }
            }
        }
        return furtherQuestionnaires;
    }

    // Methode, die aufgerufen wird, wenn der Startknopf des Fragebogens geklickt wird
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

    // Methode, die aufgerufen wird, wenn ein Fragebogen in der Übersicht geklickt wird, Vorbereitung des Fragebogen
    @Override
    public void onQuestionnaireClicked(String title) {
        overviewShown = true;

        for(Questionnaire currentQuestionnaire: relevantQuestionnaires){
            if(title.equals(currentQuestionnaire.getTitle())){
                if(currentQuestionnaire.getSections() != null){
                    questionnaire = currentQuestionnaire;
                    initUIsections(currentQuestionnaire, currentQuestionnaire.getSections());
                } else {
                    questions = currentQuestionnaire.getQuestions();
                    questionnaire = currentQuestionnaire;
                    initUI(currentQuestionnaire, questions);
                }
            }
        }
    }
    // Verlassenknopf gedrückt, Zurückkommen zur Übersicht
    private void exitButtonClicked(){
        overviewShown = false;
        sectionNumber = 0;
        subsectionNumber = 0;
        currentQuestion = 1;
        currentFrag = 0;
        initOverview(relevantQuestionnairesTitles);
    }
}