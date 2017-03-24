package com.example.may.msocial.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.may.msocial.models.ProblemsSorting;
import com.example.may.msocial.R;
import com.example.may.msocial.arrayAdapters.ShowSelected_ArrayAdapter;
import com.example.may.msocial.models.JSONParser;
import com.example.may.msocial.models.MsRow;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Symptoms_Problems_Activity extends Activity {
    private String actor;
    private String listName;
    private String medicalfield;
    private String patientLanguage;
    private ArrayList<MsRow> symptoms;
    private List<MsRow> rows;
    private List<MsRow> problemsRows;
    private ArrayList<MsRow> problemsTranslated;
    private ProgressDialog pDialog;
    private Boolean implemented = true;
    private JSONParser jParser = new JSONParser();
    private Button OkBtn;
    private RadioGroup rg;

    private static String url_problems = "http://10.0.2.2:80/Services/problems/get_problems.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PROBLEMS_TRANSLATED = "problem_translations";
    private static final String TAG_ID_PROBLEMTRANSLATED = "problem";
    private static final String TAG_PROBLEMID = "problem_ID";
    private static final String TAG_NAMETRANSLATED = "problem_translation";
    private static final String TAG_PROBLEMNAME = "problemName";
    private static final String TAG_NQUERIES = "nqueries";
    private static final String PROBLEMS = "problems";
    private JSONArray problems = null;
    private JSONArray problems_transl = null;
    private ArrayList<MsRow> notRepeated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            actor = extras.getString("actor");
            listName = extras.getString("listName");
            medicalfield = extras.getString("medicalfield");
            patientLanguage = extras.getString("patientLanguage");
            symptoms = (ArrayList<MsRow>) getIntent().getSerializableExtra("symptoms");
            implemented = extras.getBoolean("implemented");
        }

        setContentView(R.layout.symptoms_problems_activity);
        new LoadProblems().execute();
        LayoutInflater inflater = getLayoutInflater();
        RelativeLayout listFooterView = (RelativeLayout)inflater.inflate(
                R.layout.symptom_problem_buttons, null);
        ListView symptomslv = (ListView)findViewById(R.id.symptomsList);
        symptomslv.addFooterView(listFooterView);
        symptomslv.setAdapter(new ShowSelected_ArrayAdapter(this, symptoms));
    }

    class LoadProblems extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            super.onPreExecute();
            pDialog = new ProgressDialog(Symptoms_Problems_Activity.this);
            pDialog.setMessage("Cargando posibles dolencias.Por favor, espere...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {
            if(implemented) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                for (int i = 0; i < symptoms.size(); i++) {
                    params.add(new BasicNameValuePair("symptoms[]", symptoms.get(i).getSubtitle()));
                }
                params.add(new BasicNameValuePair("lang", patientLanguage));

                JSONObject json = jParser.makeHttpRequest(url_problems, "GET", params);

                Log.d("Problems: ", json.toString());

                try {

                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {

                        problems = json.getJSONArray(PROBLEMS);
                        problems_transl = json.getJSONArray(TAG_PROBLEMS_TRANSLATED);
                        rows = new ArrayList<MsRow>(problems.length());

                        for (int i = 0; i < problems.length(); i++) {
                            JSONObject c = problems.getJSONObject(i);

                            String id = c.getString(TAG_PROBLEMID);
                            int nqueries = c.getInt(TAG_NQUERIES);
                            String name = java.net.URLDecoder.decode(c.getString(TAG_PROBLEMNAME), "iso-8859-1");
                            MsRow row = null;

                            row = new MsRow();
                            row.setTitle(name);
                            row.setSubtitle(id);
                            row.setNqueries(nqueries);
                            rows.add(row);

                        }
                    } else {
                        setProblemsForNotFound();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }else{
                setProblemsForNotFound();

            }
            return null;
        }


        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

            runOnUiThread(new Runnable() {
                public void run() {
                    problemsRows = rows;
                    deleteRepeated();
                    addRadioButtons();
                    addListenerOnButton();
                    getTransLatedProblems();
                }
            });

        }

    }

    private void setProblemsForNotFound() {
        rows = new ArrayList<MsRow>(30);
        MsRow row = null;
        for (int i = 0; i < 31; i++) {
            row = new MsRow();
            row.setUrl("http://10.0.2.2/img/img.png");
            row.setTitle("Problema" + i);
            row.setSubtitle(Integer.toString(i));
            row.setNqueries(i);
            rows.add(row);
        }
        implemented = false;
    }

    private void deleteRepeated() {
        notRepeated = new ArrayList<MsRow>(rows.size());
        boolean equal;
        notRepeated.add(rows.get(rows.size()-1));

            int count = 1;
            for (int i = 0; i < rows.size() - 1; i++) {
                equal = false;
                for (int j = i + 1; j < rows.size(); j++) {

                    if (rows.get(i).getTitle().equals(rows.get(j).getTitle())) {
                        equal = true;
                        break;
                    }
                }
                if (!equal) notRepeated.add(rows.get(i));
            }

            Collections.sort(rows, new ProblemsSorting());

    }

    private void addRadioButtons() {
        RadioGroup rg = (RadioGroup)findViewById(R.id.radioProblemList);
        int size = (notRepeated.size() > 10 )? 10 : notRepeated.size();
        for (int i = 0; i < size; i++) {
            RadioButton rbutton = new RadioButton(Symptoms_Problems_Activity.this);
            rbutton.setTextSize(16);
            rbutton.setId(Integer.parseInt(notRepeated.get(i).getSubtitle()));
            rbutton.setText(notRepeated.get(i).getTitle());
            rg.addView(rbutton);
        }
    }

    private void getTransLatedProblems() {
        if(implemented) {
            problemsTranslated = new ArrayList<MsRow>(problems_transl.length());
            for (int i = 0; i < problems_transl.length(); i++) {
                try {
                    MsRow row = new MsRow();
                    JSONObject translation = problems_transl.getJSONObject(i);
                    String id = translation.getString(TAG_ID_PROBLEMTRANSLATED);
                    String lang = translation.getString("lang");
                    String lang_code = (lang.equalsIgnoreCase("fr") || lang.equalsIgnoreCase("es"))?"iso-8859-1":"utf-8";
                    String name = java.net.URLDecoder.decode(translation.getString(TAG_NAMETRANSLATED), lang_code);
                    row.setTitle(name);
                    row.setSubtitle(id);
                    problemsTranslated.add(row);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }else{
            problemsTranslated = new ArrayList<MsRow>(31);
            for (int i = 0; i < 31; i++) {
                MsRow row = new MsRow();
                row.setTitle(getResources().getString(R.string.Problems) + i);
                row.setSubtitle(Integer.toString(i));
                problemsTranslated.add(row);
            }
        }
    }

    public void addListenerOnButton() {

        rg = (RadioGroup) findViewById(R.id.radioProblemList);
        OkBtn = (Button) findViewById(R.id.problemsSelectedBtn);

        OkBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int selectedId = rg.getCheckedRadioButtonId();
                if(selectedId != -1) {
                    RadioButton radioButton = (RadioButton) findViewById(selectedId);
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    int queries = searchProblemNqueries(radioButton.getId());
                    StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append(queries + 1);
                    String nqueries = sb.toString();
                    params.add(new BasicNameValuePair("nqueries", nqueries));
                    params.add(new BasicNameValuePair("problem_ID", Integer.toString(radioButton.getId())));
                    jParser.makeHttpRequest("http://10.0.2.2:80/Services/problems/alter_nqueries_from_problem_selected.php", "POST", params);
                    Toast.makeText(Symptoms_Problems_Activity.this,
                            radioButton.getText(), Toast.LENGTH_SHORT).show();
                    Intent problem_protocols = new Intent(v.getContext(), Problem_Protocols_Activity.class);
                    problem_protocols.putExtra("patientLanguage", patientLanguage);
                    problem_protocols.putExtra("listName", listName);
                    problem_protocols.putExtra("actor", actor);
                    problem_protocols.putExtra("medicalfield", medicalfield);
                    problem_protocols.putExtra("problemSelectedName",radioButton.getText());
                    problem_protocols.putExtra("problemSelectedId",Integer.toString(radioButton.getId()));
                    problem_protocols.putExtra("problemTranslated", searchTranslation(radioButton.getId()));
                    problem_protocols.putExtra("implemented", implemented);
                    startActivity(problem_protocols);
                }else{
                    Toast.makeText(Symptoms_Problems_Activity.this,"Seleccione un diagnóstico", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private int searchProblemNqueries(int id) {

            for (int i = 0; i < rows.size(); i++) {
                if(rows.get(i).getSubtitle().equals(Integer.toString(id))) return rows.get(i).getNqueries();
            }
            return 0;

    }

    private MsRow searchTranslation(int problemSelectet) {
        for (int i = 0; i < problemsTranslated.size(); i++) {
            if(problemsTranslated.get(i).getSubtitle().equals(Integer.toString(problemSelectet))) return problemsTranslated.get(i);
        }
        return null;
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_problems, menu);

        return true;
    }
}