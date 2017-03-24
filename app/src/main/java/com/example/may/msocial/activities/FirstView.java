package com.example.may.msocial.activities;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.example.may.msocial.R;


public class FirstView extends Activity {


    private  Locale myLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_language_selector_activity);
    }

    public void onClickButtonsSelectLanguage (View v){
        Intent actorSelection = new Intent(v.getContext(), ActorSelection_Activity.class);
        switch(v.getId()) {
            case R.id.spanishbtn:
               setLocale("es");
                actorSelection.putExtra("patientLanguage", "es");
                Toast.makeText(getBaseContext(),"Idioma español seleccionado",Toast.LENGTH_SHORT).show();
                break;
            case R.id.englishbtn:
                setLocale("en");
                actorSelection.putExtra("patientLanguage", "en");
                Toast.makeText(getBaseContext(),"Idioma inglés seleccionado",Toast.LENGTH_SHORT).show();
                break;
            case R.id.germanbtn:
                setLocale("de");
                actorSelection.putExtra("patientLanguage", "de");
                Toast.makeText(getBaseContext(),"Idioma alemán seleccionado",Toast.LENGTH_SHORT).show();
                break;
            case R.id.frenchbtn:
                setLocale("fr");
                actorSelection.putExtra("patientLanguage", "fr");
                Toast.makeText(getBaseContext(),"Idioma francés seleccionado",Toast.LENGTH_SHORT).show();
                break;
            case R.id.russianbtn:
                setLocale("ru");
                actorSelection.putExtra("patientLanguage", "ru");
                Toast.makeText(getBaseContext(),"Idioma ruso seleccionado",Toast.LENGTH_SHORT).show();
                break;
            case R.id.swedenbtn:
                setLocale("sv");
                actorSelection.putExtra("patientLanguage", "sv");
                Toast.makeText(getBaseContext(),"Idioma sueco seleccionado",Toast.LENGTH_SHORT).show();
                break;
            case R.id.chinabtn:
                setLocale("zh");
                actorSelection.putExtra("patientLanguage", "zh");
                Toast.makeText(getBaseContext(),"Idioma chino seleccionado",Toast.LENGTH_SHORT).show();
                break;
            case R.id.moroccobtn:
                setLocale("ar");
                actorSelection.putExtra("patientLanguage", "ar");
                Toast.makeText(getBaseContext(),"Idioma árabe seleccionado",Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getBaseContext(),"Error",Toast.LENGTH_SHORT).show();
        }
        startActivity(actorSelection);
    }

    private void setLocale(String lang) {
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, FirstView.class);
        startActivity(refresh);
    }



}
