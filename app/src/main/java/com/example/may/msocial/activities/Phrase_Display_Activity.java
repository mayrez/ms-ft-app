package com.example.may.msocial.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.may.msocial.R;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;


import java.util.Locale;


public class Phrase_Display_Activity extends Activity implements TextToSpeech.OnInitListener {
    String actor;
    String medicalfield;
    String patientLanguage;
    String phrase_to_display;
    String phrase_selected;
    private TextToSpeech tts;
    private Language translateTo;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            actor = extras.getString("actor");
            medicalfield = extras.getString("medicalfield");
            patientLanguage = extras.getString("patientLanguage");
            phrase_to_display = extras.getString("phrase_to_display");
            phrase_selected = extras.getString("phrase_selected");
        }
        setContentView(R.layout.phrase_display_activity);
        tts = new TextToSpeech(this, this);
        setTexts();
        setPhrases();
        setTranslateTo();
        addListenButtonListener();
        addTranslateButonListener();
    }

    private void setTexts() {
        Button trnslateBtn = (Button)findViewById(R.id.trnslateBtn);
        trnslateBtn.setText((!actor.equalsIgnoreCase("patient"))?getResources().getString(R.string.phrases):"Traducir");
        TextView response = (TextView)findViewById(R.id.response);
        response.setText((!actor.equalsIgnoreCase("patient"))?getResources().getString(R.string.response):"Respuesta:");
    }

    private void setTranslateTo() {
        if(!actor.equalsIgnoreCase("patient")) {
            translateTo = Language.SPANISH;
            tts.setLanguage(new Locale("es"));
        }else {

            switch (patientLanguage) {
                case "es":
                    translateTo = Language.SPANISH;
                    break;
                case "en":
                    translateTo = Language.ENGLISH;
                    break;
                case "sv":
                    translateTo = Language.SWEDISH;
                    break;
                case "zh":
                    translateTo = Language.CHINESE_SIMPLIFIED;
                    break;
                case "ar":
                    translateTo = Language.ARABIC;
                    break;
                case "fr":
                    translateTo = Language.FRENCH;
                    break;
                case "de":
                    translateTo = Language.GERMAN;
                    break;
                case "ru":
                    translateTo = Language.RUSSIAN;
                    break;
                default:
                    translateTo = Language.SPANISH;
            }
        }
    }

    private void addTranslateButonListener() {
        Button trnslateBtn = (Button)findViewById(R.id.trnslateBtn);
        trnslateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                class bgStuff extends AsyncTask<Void, Void, Void> {

                    String translatedText = "";
                    @Override
                    protected Void doInBackground(Void... params) {

                        try {
                            String text = ((EditText) findViewById(R.id.responseTrnsltion)).getText().toString();
                            translatedText = translate(text);
                        } catch (Exception e) {

                            e.printStackTrace();
                            translatedText = e.toString();
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {

                        ((TextView) findViewById(R.id.responseTranslated)).setText(translatedText);
                        super.onPostExecute(result);
                    }

                }

                new bgStuff().execute();
            }
        });
    }

    private void addListenButtonListener() {
        ImageButton listenBtn = (ImageButton)findViewById(R.id.listenPhrases);
        listenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakOut(((TextView) findViewById(R.id.phraseDisplayed)).getText().toString());
            }
        });
    }

    private void setPhrases() {
        TextView displayed = (TextView) findViewById(R.id.phraseDisplayed);
        displayed.setText(phrase_selected);
        TextView selected = (TextView) findViewById(R.id.phraseSelected);
        selected.setText(phrase_to_display);
    }

    public String translate(String text) throws Exception{

        Translate.setClientId("msocial-123456789");
        Translate.setClientSecret("M5Ah1/fWnBwr10Gd57rEdjy3qfu4Tc5rmzRvkOsmwrc=");

        String translatedText =  Translate.execute(text, translateTo);

        return translatedText;

    }
    private int setTextToSpeechLanguage() {
        if(actor.equalsIgnoreCase("patient")){
            return tts.setLanguage(new Locale("es"));
        }else{
            switch (patientLanguage) {
                case "es":
                    return tts.setLanguage(new Locale("es"));

                case "en":
                    return tts.setLanguage(Locale.ENGLISH);

                case "sv":
                    return tts.setLanguage(new Locale("sv"));

                case "zh":
                    return tts.setLanguage(Locale.SIMPLIFIED_CHINESE);

                case "ar":
                    return tts.setLanguage(new Locale("ar"));

                case "fr":
                    return tts.setLanguage(Locale.FRENCH);

                case "de":
                    return tts.setLanguage(Locale.GERMAN);
                case "ru":
                    return tts.setLanguage(new Locale("ru"));

                default:
                    return tts.setLanguage(new Locale("es"));
            }
        }

    }

    @Override
    public void onInit(int status) {


        if (status == TextToSpeech.SUCCESS) {

            int result = setTextToSpeechLanguage();

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");

            } else {

            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    private void speakOut(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onDestroy() {

        if(tts != null) {

            tts.stop();
            tts .shutdown();

        }
        super.onDestroy();
    }
}
