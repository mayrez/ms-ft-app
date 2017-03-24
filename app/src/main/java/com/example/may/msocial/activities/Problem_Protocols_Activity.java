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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.may.msocial.R;
import com.example.may.msocial.arrayAdapters.ProtocolsListArrayAdapter;
import com.example.may.msocial.models.JSONParser;
import com.example.may.msocial.models.MsRow;
import com.example.may.msocial.models.Protocol;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class Problem_Protocols_Activity extends Activity {

    private String problem;
    private String problemId;
    private MsRow problemTranslated;
    private String actor;
    private String medicalfield;
    private String patientLanguage;
    private List<Protocol> rows;
    private ArrayList<Protocol> protocolsTranslated;
    private ProgressDialog pDialog;
    private Boolean implemented = true;
    private JSONParser jParser = new JSONParser();
    private Button OkBtn;

    private static String url_protocols = "http://10.0.2.2:80/Services/protocols/get_protocols.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PROTOCOLS_TRANSLATED = "protocol_translations";
    private static final String TAG_ID_PROTOCOLTRANSLATED = "protocol";
    private static final String TAG_PROTOCOLID = "protocol_ID";
    private static final String TAG_NAMETRANSLATED = "protocol_translation";
    private static final String TAG_PROTOCOLNAME = "protocolName";
    private static final String TAG_VIDEO = "video";
    private static final String PROTOCOLS = "protocols";
    private JSONArray protocols = null;
    private JSONArray protocols_transl = null;
    private String protocolAdded;
    private String protocolAddedTranslated;
    private String videoAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            actor = extras.getString("actor");
            medicalfield = extras.getString("medicalfield");
            patientLanguage = extras.getString("patientLanguage");
            problem = extras.getString("problemSelectedName");
            problemId = extras.getString("problemSelectedId");
            problemTranslated = (MsRow) getIntent().getSerializableExtra("problemTranslated");
            protocolAdded = extras.getString("protocolAdded");
            protocolAddedTranslated = extras.getString("protocolAddedTranslated");
            videoAdded = extras.getString("videoUrl");
            implemented = extras.getBoolean("implemented");
        }

        setContentView(R.layout.problem_protocols_activity);
        addTitle();
        addListenerOnOkButton();
        addListenerOnAddProtocolButton();
        new LoadProtocols().execute();
    }

    private void addListenerOnAddProtocolButton() {
        Button addProtocolBtn = (Button)findViewById(R.id.addProtocolsBtn);
        addProtocolBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addProtocol = new Intent(v.getContext(), Add_Protocol_Activity.class);
                addProtocol.putExtra("problemID",problemId);
                addProtocol.putExtra("patientLanguage", patientLanguage);
                addProtocol.putExtra("actor", actor);
                addProtocol.putExtra("medicalfield", medicalfield);
                addProtocol.putExtra("problemSelectedName", problem);
                addProtocol.putExtra("problemSelectedId", problemId);
                addProtocol.putExtra("problemTranslated", problemTranslated);
                addProtocol.putExtra("implemented", implemented);
                startActivity(addProtocol);
            }
        });
    }

    private void addTitle() {
        View header = getLayoutInflater().inflate(R.layout.header, null);
        TextView  title = (TextView) header.findViewById(R.id.titleList);
        title.setText(problem);
        title.setTextSize(20);
        ListView listView = (ListView)findViewById(R.id.ProtocolsListView);
        listView.addHeaderView(header);
    }

    private void addFooter() {
        LayoutInflater inflater = getLayoutInflater();
        RelativeLayout listFooterView = ( RelativeLayout)inflater.inflate(
                R.layout.footerbuttons, null);
        ListView list = (ListView)findViewById(R.id.ProtocolsListView);
        list.addFooterView(listFooterView);
    }

    class LoadProtocols extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            super.onPreExecute();
            pDialog = new ProgressDialog(Problem_Protocols_Activity.this);
            pDialog.setMessage("Cargando protocolos.Por favor, espere...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {
            if (implemented){
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("problem", problemId));
                params.add(new BasicNameValuePair("lang", patientLanguage));

                JSONObject json = jParser.makeHttpRequest(url_protocols, "GET", params);

                Log.d("Problems: ", json.toString());

                try {

                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {

                        protocols = json.getJSONArray(PROTOCOLS);
                        protocols_transl = json.getJSONArray(TAG_PROTOCOLS_TRANSLATED);
                        rows = new ArrayList<Protocol>(protocols.length());

                        for (int i = 0; i < protocols.length(); i++) {
                            JSONObject c = protocols.getJSONObject(i);
                            Protocol row = new Protocol();
                            row.setProtocolId(c.getString(TAG_PROTOCOLID));
                            row.setDescription(java.net.URLDecoder.decode(c.getString(TAG_PROTOCOLNAME), "iso-8859-1"));
                            rows.add(row);

                        }
                        if (protocolAdded != null) {
                            addNewProtocol();

                        }
                    } else {
                        setProtocolsForNotFound();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
        }else{
               setProtocolsForNotFound();
            }
            return null;
        }


        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

            runOnUiThread(new Runnable() {
                public void run() {
                    ListView protocollv = (ListView)findViewById(R.id.ProtocolsListView);
                    addOnItemClickListener(protocollv);
                    protocollv.setAdapter(new ProtocolsListArrayAdapter(Problem_Protocols_Activity.this, rows));
                    getTransLatedProblems();
                }
            });

        }

    }

    private void setProtocolsForNotFound() {
        rows = new ArrayList<Protocol>(15);

        for (int i = 0; i < 8; i++) {
            Protocol row = new Protocol();
            row.setDescription("Protocolo" + i);
            row.setVideo("vpgeGqMjlrc");
            rows.add(row);
        }
        if (protocolAdded != null) {
            Protocol row = new Protocol();
            row.setDescription(protocolAdded);
            rows.add(row);
        }

        implemented = false;
    }

    private void addNewProtocol() {
        List<Protocol> rows_prev = new ArrayList<Protocol>(rows.size());
        rows_prev = rows;
        rows = new ArrayList<>(rows_prev.size()+1);
        for (int i = 0; i < rows_prev.size() ; i++) {
            Protocol row = new Protocol();
            row.setDescription(rows_prev.get(i).getDescription());
            row.setProtocolId(rows_prev.get(i).getProtocolId());
            rows.add(row);
        }
        Protocol row = new Protocol();
        row.setDescription(protocolAdded);
        rows.add(row);
    }

    private void addOnItemClickListener(ListView protocollv) {
        protocollv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        CheckBox checkBox = (CheckBox)view.findViewById(R.id.checkBoxProtocolList);
                        rows.get(position-1).setChecked(!checkBox.isChecked());
                        checkBox.setChecked(!checkBox.isChecked());
                        Toast.makeText(Problem_Protocols_Activity.this, rows.get(position-1).getDescription(), Toast.LENGTH_SHORT).show();
            }

        });
    }


    private void getTransLatedProblems() {
        if(implemented) {
            protocolsTranslated = new ArrayList<Protocol>(protocols_transl.length());
            for (int i = 0; i < protocols_transl.length(); i++) {
                try {
                    Protocol row = new Protocol();
                    JSONObject translation = protocols_transl.getJSONObject(i);
                    String id = translation.getString(TAG_ID_PROTOCOLTRANSLATED);
                    String lang = translation.getString("lang");
                    String lang_code = (lang.equalsIgnoreCase("fr") || lang.equalsIgnoreCase("es"))?"iso-8859-1":"utf-8";
                    String name = java.net.URLDecoder.decode(translation.getString(TAG_NAMETRANSLATED), lang_code);
                    row.setDescription(name);
                    row.setProtocolId(id);
                    row.setVideo(java.net.URLDecoder.decode(translation.getString(TAG_VIDEO), "utf-8"));
                    protocolsTranslated.add(row);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            if(protocolAddedTranslated != null){
                addProtocolTranslated();
            }
        }else{
            protocolsTranslated = new ArrayList<Protocol>(35);
            for (int i = 0; i < 30; i++) {
                Protocol row = new Protocol();
                row.setDescription(getResources().getString(R.string.Problems) + i);
                row.setProtocolId(patientLanguage + i);
                protocolsTranslated.add(row);
            }
            if(protocolAddedTranslated != null){
                Protocol row = new Protocol();
                row.setDescription(protocolAddedTranslated);
                row.setVideo(videoAdded);
                protocolsTranslated.add(row);

            }
        }
    }

    private void addProtocolTranslated() {
        List<Protocol> prevList = new ArrayList<Protocol>(protocolsTranslated.size());
        prevList = protocolsTranslated;
        protocolsTranslated = new ArrayList<Protocol>(prevList.size());
        for (int i = 0; i < prevList.size() ; i++) {
            Protocol row = new Protocol();
            row.setProtocolId(prevList.get(i).getProtocolId());
            row.setDescription(prevList.get(i).getDescription());
            row.setVideo(prevList.get(i).getVideo());
            protocolsTranslated.add(row);
        }
        Protocol row = new Protocol();
        row.setDescription(protocolAddedTranslated);
        row.setVideo(videoAdded);
        protocolsTranslated.add(row);
    }

    public void addListenerOnOkButton() {


        OkBtn = (Button) findViewById(R.id.protocolsOkBtn);

        OkBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ListView lv = (ListView) findViewById(R.id.ProtocolsListView);
                Protocol selectedRow;

                        ArrayList<Protocol> protocolsSelected = new ArrayList<Protocol>(lv.getAdapter().getCount());
                        for (int i = 0; i <rows.size(); i++) {
                            if (rows.get(i).isChecked()) {
                                selectedRow = new Protocol();
                                selectedRow.setDescription(protocolsTranslated.get(i).getDescription());
                                selectedRow.setProtocolId(protocolsTranslated.get(i).getProtocolId());
                                selectedRow.setProtocolId(protocolsTranslated.get(i).getVideo());
                                protocolsSelected.add(selectedRow);

                            }
                        }

                        Intent diagnosis = new Intent(v.getContext(), ShowDiagnosisandProtocol_Activity.class);
                        diagnosis.putExtra("protocolsSelected", protocolsSelected);
                        diagnosis.putExtra("patientLanguage", patientLanguage);
                        diagnosis.putExtra("actor", actor);
                        diagnosis.putExtra("medicalfield", medicalfield);
                        diagnosis.putExtra("problemTranslated", problemTranslated);
                        startActivity(diagnosis);

                    }


        });
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_protocols, menu);

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
