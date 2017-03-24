package com.example.may.msocial.activities;


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.may.msocial.R;
import com.example.may.msocial.arrayAdapters.SymptomsListArrayAdapter;
import com.example.may.msocial.models.JSONParser;
import com.example.may.msocial.models.MsRow;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class SelectionList_Activity extends ListActivity {
    private String actor;
    private String listName;
    private String medicalfield;
    private String patientLanguage;
    private List<MsRow> rows;
    private ArrayList<MsRow> selectedSymptoms;
    private ArrayList<MsRow>symptomsEs;
    private ProgressDialog pDialog;
    private Boolean implemented = true;
    private JSONParser jParser = new JSONParser();

    private static String symptoms_url = "http://10.0.2.2:80/Services/symtoms/get_symptoms.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_SYMPTOMS = "symptom_translations";
    private static final String TAG_ID = "symptom";
    private static final String TAG_SYMPTOMID = "symptom_ID";
    private static final String TAG_IMG = "img";
    private static final String TAG_NAME = "symptom_translation";
    private static final String TAG_SYMPTOMNAME = "symptomName";
    private static final String SYMPTOMS = "symptoms";
    private JSONArray symptoms = null;
    private JSONArray symptoms_es = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            actor = extras.getString("actor");
            listName = extras.getString("listName");
            medicalfield = extras.getString("medicalfield");
            patientLanguage = extras.getString("patientLanguage");
        }
        setContentView(R.layout.listview);

        View header = getLayoutInflater().inflate(R.layout.header, null);

        TextView  title = (TextView) header.findViewById(R.id.titleList);
        switch(listName){
            case "alergies":
                title.setText( this.getString(R.string.selectHistory));
                break;
            case "symptoms":
                title.setText( this.getString(R.string.selectSymptoms));
                break;
            case "history":
                title.setText( this.getString(R.string.selectAllergies));
                break;
            default:
        }

        ListView listView = getListView();
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.addHeaderView(header);


        switch(listName){
            case "alergies":
                break;
            case "symptoms":
                new LoadSymptoms().execute();
                break;
            case "history":
                break;
            default:
                new LoadSymptoms().execute();
        }

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                CheckBox checkBox = (CheckBox)view.findViewById(R.id.checkBoxList);
                rows.get(position-1).setChecked(!checkBox.isChecked());
                Toast.makeText(SelectionList_Activity.this, rows.get(position-1).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }




    public void onClickOkSelectedListButton(View v){
        ListView lv = getListView();
        MsRow selectedRow;
        String[] symptoms_IDs = new String[lv.getAdapter().getCount()];
        SparseBooleanArray checkedItemPositions = getListView().getCheckedItemPositions();

        int checkedCount = checkedItemPositions.size();

        if (checkedCount > 0)
        {
            selectedSymptoms = new ArrayList<MsRow>(lv.getAdapter().getCount());
            for (int i = 0; i < lv.getAdapter().getCount(); i++) {
                if(lv.isItemChecked(i)){
                    selectedRow = new MsRow();
                    selectedRow.setTitle(symptomsEs.get(i-1).getTitle());
                    selectedRow.setSubtitle(symptomsEs.get(i-1).getSubtitle());
                    selectedSymptoms.add(selectedRow);
                }
            }

            Intent symptoms_problems = new Intent(v.getContext(),Symptoms_Problems_Activity.class);
            symptoms_problems.putExtra("symptoms", selectedSymptoms);
            symptoms_problems.putExtra("patientLanguage",patientLanguage);
            symptoms_problems.putExtra("listName",listName);
            symptoms_problems.putExtra("actor",actor);
            symptoms_problems.putExtra("medicalfield",medicalfield);
            symptoms_problems.putExtra("implemented",implemented);
            startActivity(symptoms_problems);
        }
    }
    class LoadSymptoms extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            super.onPreExecute();
            pDialog = new ProgressDialog(SelectionList_Activity.this);
            pDialog.setMessage(getResources().getString(R.string.LoadingSymptoms));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("pid", medicalfield));
            params.add(new BasicNameValuePair("lang",patientLanguage));

            JSONObject json = jParser.makeHttpRequest(symptoms_url, "GET", params);

            Log.d("Symptoms: ", json.toString());

            try {

                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {

                    symptoms = json.getJSONArray(TAG_SYMPTOMS);
                    symptoms_es = json.getJSONArray(SYMPTOMS);
                    rows = new ArrayList<MsRow>(symptoms.length());

                    for (int i = 0; i < symptoms.length(); i++) {
                        JSONObject c = symptoms.getJSONObject(i);

                        String id = c.getString(TAG_ID);

                        String lang_codification;
                        lang_codification =(patientLanguage.equalsIgnoreCase("es") || patientLanguage.equalsIgnoreCase("fr"))? "iso-8859-1" : "utf-8";
                        String name = java.net.URLDecoder.decode(c.getString(TAG_NAME), lang_codification);
                        String img = java.net.URLDecoder.decode(c.getString(TAG_IMG), lang_codification);

                        MsRow row = null;

                        row = new MsRow();
                        row.setUrl(img);
                        row.setTitle(name);
                        row.setSubtitle(id);
                        rows.add(row);

                    }
                } else {
                    rows = new ArrayList<MsRow>(30);
                    MsRow row = null;
                    for (int i = 1; i < 31; i++)
                    {
                        row = new MsRow();
                        row.setUrl("http://10.0.2.2/img/img.png");
                        row.setTitle(getResources().getString(R.string.Symptoms)+ i);
                        row.setSubtitle(Integer.toString(i));
                        rows.add(row);
                    }
                    implemented = false;
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

                setListAdapter(new SymptomsListArrayAdapter(SelectionList_Activity.this, rows));
                getTransLatedSymptoms();

                }
            });

        }

    }

    private void getTransLatedSymptoms() {
        if(implemented) {
            symptomsEs = new ArrayList<MsRow>(symptoms_es.length());
            for (int i = 0; i < symptoms_es.length(); i++) {
                try {
                    MsRow row = new MsRow();
                    JSONObject c = symptoms_es.getJSONObject(i);
                    String id = c.getString(TAG_SYMPTOMID);
                    String name =java.net.URLDecoder.decode(c.getString(TAG_SYMPTOMNAME), "iso-8859-1");
                    row.setTitle(name);
                    row.setSubtitle(id);
                    symptomsEs.add(row);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }else{
            symptomsEs = new ArrayList<MsRow>(31);
            for (int i = 0; i < 31; i++) {
                MsRow row = new MsRow();
                row.setTitle("Síntoma " + i);
                row.setSubtitle(Integer.toString(i));
                symptomsEs.add(row);
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.symptoms_selection_menu, menu);

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

