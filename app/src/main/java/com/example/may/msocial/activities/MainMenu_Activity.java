package com.example.may.msocial.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.may.msocial.R;



public class MainMenu_Activity extends Activity {
    private String actor;
    private String[] tabs;
    private String patientLanguage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            actor = extras.getString("actor");
            patientLanguage = extras.getString("patientLanguage");
        }
        if(actor.equalsIgnoreCase("patient")) {
            setContentView(R.layout.main_menu__activity);
            tabs = new String[]{"",getResources().getString(R.string.QuickTranslator), getResources().getString(R.string.settings), getResources().getString(R.string.help)};
        }
        else if(actor.equalsIgnoreCase("professional")) {
            setContentView(R.layout.principal_activity);
            tabs = new String[]{"","Traductor", "Ajustes", "Ayuda"};

        }
    }


    public void onClickButtonsMainMenu (View v){
        tabs[0] = (actor.equalsIgnoreCase("patient"))?getResources().getString(R.string.main):"Principal";
        switch(v.getId()) {
            case R.id.medicalFieldbtn:
                Intent medicalField = new Intent(v.getContext(), MedicalField_Activity.class);
                Toast.makeText(getBaseContext(), "Áreas Médicas", Toast.LENGTH_SHORT).show();
                medicalField.putExtra("actor",actor);
                medicalField.putExtra("patientLanguage", patientLanguage);
                medicalField.putExtra("listName","symptoms");
                medicalField.putExtra("tabs",tabs);
                startActivity(medicalField);
                break;
            case R.id.phrasesbtn:
                Intent phrases = new Intent(v.getContext(), Phrases_SelectionList_Activity.class);
                Toast.makeText(getBaseContext(),"Expresiones",Toast.LENGTH_SHORT).show();
                phrases.putExtra("actor",actor);
                phrases.putExtra("patientLanguage", patientLanguage);
                phrases.putExtra("section", "phrases");
                startActivity(phrases);
                break;
            case R.id.admattersbtn:
                Toast.makeText(getBaseContext(),"Temas Administrativos",Toast.LENGTH_SHORT).show();
                break;
            case R.id.quicktransbtn:
                Intent quickTranslator = new Intent(v.getContext(), QuickTranslator_Activity.class);
                Toast.makeText(getBaseContext(),"Traductor Rápido",Toast.LENGTH_SHORT).show();
                quickTranslator.putExtra("actor",actor);
                quickTranslator.putExtra("patientLanguage", patientLanguage);
                quickTranslator.putExtra("tabs",tabs);
                startActivity(quickTranslator);
                break;

            default:
                Toast.makeText(getBaseContext(),"Error",Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (actor){
            case "patient":
                getMenuInflater().inflate(R.menu.main, menu);
                break;
            default:
                getMenuInflater().inflate(R.menu.principal_menu, menu);

        }
        return true;
    }
}
