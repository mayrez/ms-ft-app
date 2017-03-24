package com.example.may.msocial.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.may.msocial.R;
import com.example.may.msocial.models.MsRow;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;


public class Add_Protocol_Activity extends Activity {
    private String actor;
    private String patientLanguage;
    private String problemID;
    private Language translateTo;
    private String problem;
    private MsRow problemTranslated;
    private String medicalfield;
    private Boolean implemented;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            actor = extras.getString("actor");
            patientLanguage = extras.getString("patientLanguage");
            medicalfield = extras.getString("medicalfield");
            problem = extras.getString("problemSelectedName");
            problemID = extras.getString("problemSelectedId");
            problemTranslated = (MsRow) getIntent().getSerializableExtra("problemTranslated");
            implemented = extras.getBoolean("implemented");
        }

        setContentView(R.layout.add_protocol_activity);
        settranslateTo();
    }

    private void settranslateTo() {
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

    public void onClickNextButton(View v){
        String protocolText = ((TextView)findViewById(R.id.protocoladded)).getText().toString();
        String videoUrl = ((TextView)findViewById(R.id.videoaddedUrl)).getText().toString();
        String protocolTranslated = translateProtocol(protocolText);
        Intent protocolList = new Intent(v.getContext(), Problem_Protocols_Activity.class);
        protocolList.putExtra("protocolAdded", protocolText);
        protocolList.putExtra("protocolAddedTranslated", protocolTranslated);
        protocolList.putExtra("actor", actor);
        protocolList.putExtra("patientLanguage", patientLanguage);
        protocolList.putExtra("medicalfield", medicalfield);
        protocolList.putExtra("problemSelectedName", problem);
        protocolList.putExtra("problemSelectedId", problemID);
        protocolList.putExtra("problemTranslated", problemTranslated);
        protocolList.putExtra("videoUrl", videoUrl);
        protocolList.putExtra("implemented", implemented);
        startActivity(protocolList);
    }


    public void onClickSendButton(View v){
        String protocolText = ((TextView)findViewById(R.id.protocoladded)).getText().toString();
        String videoUrl = ((TextView)findViewById(R.id.videoaddedUrl)).getText().toString();
        String protocolTranslated = translateProtocol(protocolText);
        Intent sendProtocol = new Intent(v.getContext(), SendProtocol_Activity.class);
        sendProtocol.putExtra("protocolAdded", protocolText);
        sendProtocol.putExtra("protocolAddedTranslated", protocolTranslated);
        sendProtocol.putExtra("videoUrl", videoUrl);
        sendProtocol.putExtra("actor", actor);
        sendProtocol.putExtra("patientLanguage", patientLanguage);
        sendProtocol.putExtra("medicalfield", medicalfield);
        sendProtocol.putExtra("problemSelectedName", problem);
        sendProtocol.putExtra("problemSelectedId", problemID);
        sendProtocol.putExtra("problemTranslated", problemTranslated);
        sendProtocol.putExtra("implemented", implemented);
        startActivity(sendProtocol);
    }

    private String translateProtocol(String protocolText) {
        Translate.setClientId("msocial-123456789");
        Translate.setClientSecret("M5Ah1/fWnBwr10Gd57rEdjy3qfu4Tc5rmzRvkOsmwrc=");

        String translatedText = null;
        try {
            translatedText = Translate.execute(protocolText, translateTo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return translatedText;
    }

}
