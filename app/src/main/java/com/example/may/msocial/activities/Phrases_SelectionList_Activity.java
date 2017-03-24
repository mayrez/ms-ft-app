package com.example.may.msocial.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.may.msocial.R;
import com.example.may.msocial.arrayAdapters.PhrasesListArrayAdapter;
import com.example.may.msocial.models.JSONParser;
import com.example.may.msocial.models.Phrase;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class Phrases_SelectionList_Activity extends Activity {
    private String actor;
    private String medicalfield;
    private String patientLanguage;
    private List<Phrase> phraseSpanish;
    private List<Phrase> phrasesPatientLanguage;
    private ProgressDialog pDialog;
    private JSONParser jParser = new JSONParser();

    private static String phrases_url = "http://10.0.2.2:80/Services/phrases/get_phrases.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PHRASES_TRANSLATIONS = "phrase_translations";
    private static final String TAG_PHRASES = "phrases";
    private static final String TAG_PHRASEID = "phrase_ID";
    private static final String TAG_PHRASE = "phrase";
    private static final String TAG_PHRASE_TRANSLATION = "phrase_translation";
    private JSONArray phrases = null;
    private JSONArray phrases_translated = null;
    private String section;
    private Phrase phrase_to_display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            actor = extras.getString("actor");
            medicalfield = extras.getString("medicalfield");
            patientLanguage = extras.getString("patientLanguage");
            section = extras.getString("section");
        }
        setContentView(R.layout.phrases_listview);
        TextView tv = (TextView) findViewById(R.id.phraseListTitle);
        if(!actor.equals("patient"))tv.setText("Expresiones");
        ListView listView = (ListView)findViewById(R.id.phraseslist);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String phraseid = (actor.equals("patient"))? phrasesPatientLanguage.get(position).getId(): phraseSpanish.get(position).getId();
                getPhraseToDisplay(phraseid);
                Intent phrase_display = new Intent(view.getContext(),Phrase_Display_Activity.class);
                phrase_display.putExtra("phrase_to_display", getPhraseToDisplay(phraseid));
                phrase_display.putExtra("phrase_selected",
                        (actor.equals("patient"))? phraseSpanish.get(position).getPhrase() : phrasesPatientLanguage.get(position).getPhrase());
                phrase_display.putExtra("patientLanguage",patientLanguage);
                phrase_display.putExtra("actor",actor);
                phrase_display.putExtra("medicalfield",medicalfield);
                startActivity(phrase_display);
                Toast.makeText(Phrases_SelectionList_Activity.this,
                        (actor.equals("patient"))? phrasesPatientLanguage.get(position).getPhrase(): phraseSpanish.get(position).getPhrase(), Toast.LENGTH_SHORT).show();
            }
        });

        new LoadPhrases().execute();

    }





    class LoadPhrases extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            super.onPreExecute();
            pDialog = new ProgressDialog(Phrases_SelectionList_Activity.this);
            //pDialog.setMessage(getResources().getString(R.string.LoadingSymptoms));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            int this_actor = (actor.equalsIgnoreCase("patient"))? 0 : 1;
            params.add(new BasicNameValuePair("actor", Integer.toString(this_actor)));
            section = (section == null)? "phrases" : section;
            params.add(new BasicNameValuePair("section", section));
            params.add(new BasicNameValuePair("lang",patientLanguage));

            JSONObject json = jParser.makeHttpRequest(phrases_url, "GET", params);

            Log.d("Phrases: ", json.toString());

            try {

                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {

                    phrases = json.getJSONArray("phrases");
                    phrases_translated = json.getJSONArray("phrase_translations");
                    getPhrasesSpanish();
                    getPhrasesTranslated();

                }else{
                    phrasesPatientLanguage =new ArrayList<>(30);
                    phraseSpanish =new ArrayList<>(30);
                    for (int i = 0; i < 30 ; i++) {
                        Phrase row = new Phrase();
                        row.setId(Integer.toString(i));
                        row.setPhrase(getResources().getString(R.string.phrases) + i);
                        phrasesPatientLanguage.add(row);
                        row.setId(Integer.toString(i));
                        row.setPhrase("frases"+i);
                        phraseSpanish.add(row);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return null;
        }




        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

            runOnUiThread(new Runnable() {
                public void run() {
                    ListView lv = (ListView) findViewById(R.id.phraseslist);
                    if(actor.equals("patient")){
                        lv.setAdapter(new PhrasesListArrayAdapter(Phrases_SelectionList_Activity.this, phrasesPatientLanguage));

                    }else{
                        lv.setAdapter(new PhrasesListArrayAdapter(Phrases_SelectionList_Activity.this, phraseSpanish));
                    }


                }
            });

        }

    }

    private String getPhraseToDisplay(String phraseid) {
        if(actor.equalsIgnoreCase("patient")){
            for (int i = 0; i < phrasesPatientLanguage.size(); i++) {
                if(phrasesPatientLanguage.get(i).getId().equals(phraseid)){
                    return phrasesPatientLanguage.get(i).getPhrase();
                }
            }
        }else{
            for (int i = 0; i < phraseSpanish.size(); i++) {
                if(phraseSpanish.get(i).getId().equals(phraseid)){
                    return phraseSpanish.get(i).getPhrase();
                }
            }

        }
        return null;
    }

    private void getPhrasesTranslated() throws JSONException, UnsupportedEncodingException {
        phrasesPatientLanguage =new ArrayList<>(phrases_translated.length());
        for (int i = 0; i < phrases_translated.length(); i++) {
            JSONObject c = phrases_translated.getJSONObject(i);

            String lang_codification =(patientLanguage.equalsIgnoreCase("es") || patientLanguage.equalsIgnoreCase("fr"))? "iso-8859-1" : "utf-8";
            String name = java.net.URLDecoder.decode(c.getString(TAG_PHRASE_TRANSLATION), lang_codification);
            String id = c.getString(TAG_PHRASE);

            Phrase row = null;

            row = new Phrase();
            row.setPhrase(name);
            row.setId(id);
            phrasesPatientLanguage.add(row);

        }
    }

    private void getPhrasesSpanish() throws JSONException, UnsupportedEncodingException {
        phraseSpanish =new ArrayList<>(phrases.length());
        for (int i = 0; i < phrases.length(); i++) {
            JSONObject c = phrases.getJSONObject(i);

            String id = c.getString(TAG_PHRASEID);

            String name = java.net.URLDecoder.decode(c.getString(TAG_PHRASE), "iso-8859-1");

            Phrase row = null;

            row = new Phrase();
            row.setPhrase(name);
            row.setId(id);
            phraseSpanish.add(row);

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.phrases_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_quickTranslator:
                Intent translator = new Intent(this, QuickTranslator_Activity.class);
                translator.putExtra("patientLanguage", patientLanguage);
                translator.putExtra("actor", actor);
                startActivity(translator);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
