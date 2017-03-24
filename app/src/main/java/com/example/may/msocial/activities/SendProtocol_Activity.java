package com.example.may.msocial.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.may.msocial.R;
import com.example.may.msocial.models.JSONParser;
import com.example.may.msocial.models.MsRow;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SendProtocol_Activity extends Activity {
    private String actor;
    private String patientLanguage;
    private String problemID;
    private String problem;
    private MsRow problemTranslated;
    private String medicalfield;
    private String protocolAdded;
    private String protocolAddedTranslated;
    private String videoUrl;
    private Boolean implemented;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            protocolAdded = extras.getString("protocolAdded");
            protocolAddedTranslated = extras.getString("protocolAddedTranslated");
            videoUrl = extras.getString("videoUrl");
            actor = extras.getString("actor");
            patientLanguage = extras.getString("patientLanguage");
            medicalfield = extras.getString("medicalfield");
            problem = extras.getString("problemSelectedName");
            problemID = extras.getString("problemSelectedId");
            problemTranslated = (MsRow) getIntent().getSerializableExtra("problemTranslated");
            implemented = extras.getBoolean("implemented");
        }

        setContentView(R.layout.user_credentials_layout);
        addSendButtonListener();
    }

    private void addSendButtonListener() {
        Button sendBtn = (Button)findViewById(R.id.sendProtocolToServerBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendProtocol(v);
            }
        });
    }

    private void sendProtocol(View v) {
        String user = ((TextView)findViewById(R.id.userName)).getText().toString();
        String password = ((TextView)findViewById(R.id.password)).getText().toString();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user", user));
        params.add(new BasicNameValuePair("key", password));
        params.add(new BasicNameValuePair("protocol", protocolAdded));
        params.add(new BasicNameValuePair("problem_ID", problemID));
        params.add(new BasicNameValuePair("video", videoUrl));
        JSONParser jParser = new JSONParser();
        JSONObject json = jParser.makeHttpRequest("http://10.0.2.2:80/Services/protocol_proposed/login_and_protocol_insert.php", "POST", params);
        int success = 0;
        try {
            success = json.getInt("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (success == 1) {
            Toast.makeText(SendProtocol_Activity.this, "enviado", Toast.LENGTH_SHORT).show();
            nextActivity(v);

        }else{
            Toast.makeText(SendProtocol_Activity.this, "Login erróneo", Toast.LENGTH_SHORT).show();
        }
    }

    private void nextActivity(View v) {
        Intent protocols = new Intent(v.getContext(), Problem_Protocols_Activity.class);
        protocols.putExtra("protocolAdded", protocolAdded);
        protocols.putExtra("protocolAddedTranslated", protocolAddedTranslated);
        protocols.putExtra("videoUrl", videoUrl);
        protocols.putExtra("actor", actor);
        protocols.putExtra("patientLanguage", patientLanguage);
        protocols.putExtra("medicalfield", medicalfield);
        protocols.putExtra("problemSelectedName", problem);
        protocols.putExtra("problemSelectedId", problemID);
        protocols.putExtra("problemTranslated", problemTranslated);
        protocols.putExtra("implemented", implemented);
        startActivity(protocols);
    }
}
