package com.example.may.msocial.activities;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.may.msocial.R;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class QuickTranslator_Activity extends Activity implements OnInitListener {

    private String actor;
    private String patientLanguage;
    private TextToSpeech tts;
    private Language translateTo;
    private String[] tabs;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quick_translator_activity);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            actor = extras.getString("actor");
            patientLanguage = extras.getString("patientLanguage");
            tabs = extras.getStringArray("tabs");
        }

        Spinner MySpinner = (Spinner)findViewById(R.id.languagesspinner);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this, R.array.patient_professional, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MySpinner.setAdapter(adapter);
        addListenerOnSpinnerItemSelection();
        tts = new TextToSpeech(this, this);
        ((Button) findViewById(R.id.bSpeak)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                speakOut(((TextView) findViewById(R.id.tvTranslatedText)).getText().toString());
            }
        });

        ((Button) findViewById(R.id.bTranslate)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {




                class bgStuff extends AsyncTask<Void, Void, Void>{

                    String translatedText = "";
                    @Override
                    protected Void doInBackground(Void... params) {

                        try {
                            String text = ((EditText) findViewById(R.id.etUserText)).getText().toString();
                            translatedText = translate(text);
                        } catch (Exception e) {

                            e.printStackTrace();
                            translatedText = e.toString();
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {

                        ((TextView) findViewById(R.id.tvTranslatedText)).setText(translatedText);
                        super.onPostExecute(result);
                    }

                }

                new bgStuff().execute();
            }
        });
    }



    public void addListenerOnSpinnerItemSelection() {
        Spinner mySpinner = (Spinner) findViewById(R.id.languagesspinner);
        mySpinner.setOnItemSelectedListener(new myOnItemSelectedListener());
    }

    public class myOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View arg1, int pos,long arg3)     {

            if(pos == 0) {
                translateTo = Language.SPANISH;
                tts.setLanguage(new Locale("es"));
            }else {

                switch (patientLanguage) {
                    case "es":
                        translateTo = Language.SPANISH;
                        tts.setLanguage(new Locale("es"));
                        break;
                    case "en":
                        translateTo = Language.ENGLISH;
                        tts.setLanguage(Locale.ENGLISH);
                        break;
                    case "sv":
                        translateTo = Language.SWEDISH;
                        tts.setLanguage(new Locale("sv"));
                        break;
                    case "zh":
                        translateTo = Language.CHINESE_SIMPLIFIED;
                        tts.setLanguage(Locale.SIMPLIFIED_CHINESE);
                        break;
                    case "ar":
                        translateTo = Language.ARABIC;
                        tts.setLanguage(new Locale("ar"));
                        break;
                    case "fr":
                        translateTo = Language.FRENCH;
                        tts.setLanguage(Locale.FRENCH);
                        break;
                    case "de":
                        translateTo = Language.GERMAN;
                        tts.setLanguage(Locale.GERMAN);
                        break;
                    case "ru":
                        translateTo = Language.RUSSIAN;
                        tts.setLanguage(new Locale("ru"));
                        break;
                    default:
                        translateTo = Language.SPANISH;
                        tts.setLanguage(new Locale("es"));
                }


            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
         }
    }



    public String translate(String text) throws Exception{

            Translate.setClientId("clientId");
            Translate.setClientSecret("clientsecret");

            String translatedText =  Translate.execute(text, translateTo);

            return translatedText;

    }

    @Override
    public void onInit(int status) {


        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.getDefault());

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
