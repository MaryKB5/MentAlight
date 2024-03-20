package com.example.mentalight;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
// Eine Klasse zur Verwaltung von Fragebögen und deren Parsing aus JSON
public class QuestionnaireManager {
    Questionnaire questionnaire;

    // Methode zum Parsen eines Fragebogens aus JSON
    public Questionnaire parseQuestionnaireJson(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        String title = jsonObject.getString("title");

        // Untertitel, Präfix, Einleitungstext und Anzahl der Fragen aus dem JSON-Objekt extrahieren
        String subtitle, prefix, intro;
        if(jsonObject.has("intro_text")){
            intro = jsonObject.getString("intro_text");
        } else {
            intro = "";
        }
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

        // Prüfen, ob der Fragebogen Abschnitte enthält
        if(jsonObject.has("sections")){
            Section[] sectionArray = getSections(jsonObject);
            questionnaire = new Questionnaire(title, intro, subtitle, prefix, numQuest , sectionArray);
        } else {
            ArrayList<Question> questionList = getQuestionList(jsonObject);
            questionnaire = new Questionnaire(title, intro, subtitle, prefix, numQuest, questionList);
        }

        return questionnaire;
    }

    // Methode zum Extrahieren einer Liste von Fragen aus einem JSON-Objekt
    private ArrayList<Question> getQuestionList(JSONObject object) throws JSONException {
        JSONArray questionsArray = object.getJSONArray("questions");
        ArrayList<Question> questions = new ArrayList<>();

        for (int i = 0; i < questionsArray.length(); i++) {
            JSONObject questionObject = questionsArray.getJSONObject(i);
            JSONArray inputTextArray = questionObject.getJSONArray("input_text");

            // Eingabetexte der Frage in ein String-Array umwandeln
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

    // Methode zum Extrahieren von Abschnitten aus einem JSON-Objekt
    private Section[] getSections(JSONObject object) throws JSONException {
        JSONArray sectionsArray = object.getJSONArray("sections");
        Section[] sections = new Section[sectionsArray.length()];
        for (int i = 0; i < sectionsArray.length(); i++) {
            JSONObject sectionObject = sectionsArray.getJSONObject(i);
            String title = sectionObject.getString("sec_title");
            String subtitle, prefix, intro;
            int numQuest;
            if(sectionObject.has("sec_intro")){
                intro = sectionObject.getString("sec_intro");
            } else {
                intro="";
            }
            if(sectionObject.has("sub_title")){
                subtitle = sectionObject.getString("sub_title");
            } else {
                subtitle = "";
            }
            if(sectionObject.has("prefix")){
                prefix = sectionObject.getString("prefix");
            } else {
                prefix = "";
            }
            if(sectionObject.has("num_quest")){
                numQuest = sectionObject.getInt("num_quest");
            } else {
                numQuest = 0;
            }

            // Prüfen, ob der Abschnitt Unterelemente enthält
            if(sectionObject.has("subsections")){
                Log.d("moinmoin","");
                Subsection[] subsectionsArray = getSubsections(sectionObject);
                Section section = new Section(title, subtitle, intro, prefix, numQuest, subsectionsArray);
                sections[i] = section;
            } else {
                ArrayList<Question> questions = getQuestionList(sectionObject);
                Section section = new Section(title, subtitle, intro, prefix, numQuest, questions);
                sections[i] = section;
            }
        }
        return sections;
    }

    // Methode zum Extrahieren von Subabschnitten aus einem JSON-Objekt
    private Subsection[] getSubsections(JSONObject object) throws JSONException {
        JSONArray subsectionsArray = object.getJSONArray("subsections");
        Subsection[] subsections = new Subsection[subsectionsArray.length()];
        for (int i = 0; i < subsectionsArray.length(); i++) {
            JSONObject subsectionObject = subsectionsArray.getJSONObject(i);
            String title = subsectionObject.getString("sub_title");
            String subtitle, prefix, intro;
            int numQuest;
            if(subsectionObject.has("sub_intro")){
                intro = subsectionObject.getString("sub_intro");
            } else {
                intro="";
            }
            if(subsectionObject.has("sub_subtitle")){
                subtitle = subsectionObject.getString("sub_subtitle");
            } else {
                subtitle = "";
            }
            if(subsectionObject.has("prefix")){
                prefix = subsectionObject.getString("prefix");
            } else {
                prefix = "";
            }
            if(subsectionObject.has("num_quest")){
                numQuest = subsectionObject.getInt("num_quest");
            } else {
                numQuest = 0;
            }
            // Fragenliste aus dem JSON-Objekt extrahieren
            ArrayList<Question> questions = getQuestionList(subsectionObject);
            // Neuen Subabschnitt mit den extrahierten Fragen erstellen
            Subsection subsection = new Subsection(title, subtitle, intro, prefix, numQuest, questions);
            subsections[i] = subsection;
        }
        return subsections;
    }
}
