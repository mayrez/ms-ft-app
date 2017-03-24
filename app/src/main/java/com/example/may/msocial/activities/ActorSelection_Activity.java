package com.example.may.msocial.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.may.msocial.R;


public class ActorSelection_Activity extends Activity {
    private String patientLanguage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actor_selection_activity);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            patientLanguage = extras.getString("patientLanguage");
        }
    }

    public void onClickButtonsSelectPatientOrProfessional (View v){
        Intent mainMenu = new Intent(v.getContext(), MainMenu_Activity.class);
        mainMenu.putExtra("patientLanguage", patientLanguage);
        switch(v.getId()) {
            case R.id.patient:
                Toast.makeText(getBaseContext(), "Paciente", Toast.LENGTH_SHORT).show();
                mainMenu.putExtra("actor","patient");
                startActivity(mainMenu);
                break;
            case R.id.professional:
                Toast.makeText(getBaseContext(),"Sanitario",Toast.LENGTH_SHORT).show();
                mainMenu.putExtra("actor","professional");
                startActivity(mainMenu);
                break;
            default:
                Toast.makeText(getBaseContext(),"Error",Toast.LENGTH_SHORT).show();
        }

    }

}
