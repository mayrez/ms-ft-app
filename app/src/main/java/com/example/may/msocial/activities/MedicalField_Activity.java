package com.example.may.msocial.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.may.msocial.R;


public class MedicalField_Activity extends FragmentActivity implements AdapterView.OnItemClickListener {
    private DrawerLayout mDrawer;
    private ListView mDrawerOptions;
    private String actor;
    private String listName;
    private String patientLanguage;
    private String[] tabs;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            actor = extras.getString("actor");
            tabs = extras.getStringArray("tabs");
            patientLanguage = extras.getString("patientLanguage");
            listName = extras.getString("listName");
        }
        if (actor.equalsIgnoreCase("patient"))
            setContentView(R.layout.medical_fields_activity);
        else if (actor.equalsIgnoreCase("professional"))
            setContentView(R.layout.areas_medicas_activity);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerOptions = (ListView) findViewById(R.id.left_drawer);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        mDrawerOptions.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, tabs));
        mDrawerOptions.setOnItemClickListener(this);
    }


    public void onClickButtonsMedicalField (View v){
        Intent symptomsList = new Intent(v.getContext(),SelectionList_Activity.class);
        symptomsList.putExtra("listName", listName);
        symptomsList.putExtra("patientLanguage", patientLanguage);
        switch(v.getId()) {
            case R.id.cardiologybtn:
                Toast.makeText(getBaseContext(), "Cardiología", Toast.LENGTH_SHORT).show();
                symptomsList.putExtra("medicalfield","cardiology");
                startActivity(symptomsList);
                break;
            case R.id.dermatologybtn:
                Toast.makeText(getBaseContext(),"Dermatología",Toast.LENGTH_SHORT).show();
                symptomsList.putExtra("medicalfield","dermatology");
                startActivity(symptomsList);
                break;
            case R.id.digestivebtn:
                Toast.makeText(getBaseContext(),"Digestivo",Toast.LENGTH_SHORT).show();
                symptomsList.putExtra("medicalfield","digestive");
                startActivity(symptomsList);
                break;
            case R.id.emergenciesbtn:
                Toast.makeText(getBaseContext(),"Urgencias",Toast.LENGTH_SHORT).show();
                symptomsList.putExtra("medicalfield","emergencies");
                startActivity(symptomsList);
                break;
            case R.id.earsbtn:
                Toast.makeText(getBaseContext(),"Oido",Toast.LENGTH_SHORT).show();
                symptomsList.putExtra("medicalfield","ear");
                startActivity(symptomsList);
                break;
            case R.id.familydctorbtn:
                Toast.makeText(getBaseContext(),"Medicina General",Toast.LENGTH_SHORT).show();
                symptomsList.putExtra("medicalfield","general");
                startActivity(symptomsList);
                break;
            case R.id.neurologybtn:
                symptomsList.putExtra("medicalfield","neurology");
                Toast.makeText(getBaseContext(),"Sistema nervioso",Toast.LENGTH_SHORT).show();
                startActivity(symptomsList);
                break;
            case R.id.gynecologybtn:
                Toast.makeText(getBaseContext(),"Ginecología",Toast.LENGTH_SHORT).show();
                symptomsList.putExtra("medicalfield","ginecology");
                startActivity(symptomsList);
                break;
            case R.id.oralhealthbtn:
                Toast.makeText(getBaseContext(),"Salud bucal",Toast.LENGTH_SHORT).show();
                symptomsList.putExtra("medicalfield","mouth");
                startActivity(symptomsList);
                break;
            case R.id.Pediatricsbtn:
                Toast.makeText(getBaseContext(),"Pediatría",Toast.LENGTH_SHORT).show();
                symptomsList.putExtra("medicalfield","pediatrics");
                startActivity(symptomsList);
                break;
            case R.id.obstetricsbtn:
                Toast.makeText(getBaseContext(),"Obstetricia",Toast.LENGTH_SHORT).show();
                symptomsList.putExtra("medicalfield","obstetrics");
                startActivity(symptomsList);
                break;
            case R.id.respiratorybtn:
                Toast.makeText(getBaseContext(),"Sistema respiratorio",Toast.LENGTH_SHORT).show();
                symptomsList.putExtra("medicalfield","respiratory");
                startActivity(symptomsList);
                break;
            case R.id.urogenitalsystembtn:
                Toast.makeText(getBaseContext(),"Urología",Toast.LENGTH_SHORT).show();
                symptomsList.putExtra("medicalfield","urology");
                startActivity(symptomsList);
                break;
            case R.id.traumatologybtn:
                Toast.makeText(getBaseContext(),"Traumatología",Toast.LENGTH_SHORT).show();
                symptomsList.putExtra("medicalfield","traumatology");
                startActivity(symptomsList);
                break;
            case R.id.mentalhealthbtn:
                Toast.makeText(getBaseContext(),"Salud mental",Toast.LENGTH_SHORT).show();
                symptomsList.putExtra("medicalfield","mental");
                startActivity(symptomsList);
                break;
            case R.id.visionbtn:
                Toast.makeText(getBaseContext(),"oftalmología",Toast.LENGTH_SHORT).show();
                symptomsList.putExtra("medicalfield","eyes");
                startActivity(symptomsList);
                break;
            default:
                Toast.makeText(getBaseContext(),"Error",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch(position){
            case 0:
                Intent main = new Intent(view.getContext(), MainMenu_Activity.class);
                main.putExtra("actor",actor);
                main.putExtra("patientLanguage",patientLanguage);
                startActivity(main);
                break;
            case 1:
                Intent translator = new Intent(this, QuickTranslator_Activity.class);
                translator.putExtra("patientLanguage", patientLanguage);
                translator.putExtra("actor", actor);
                startActivity(translator);
                Toast.makeText(this, tabs[position], Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, tabs[position], Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, tabs[position], Toast.LENGTH_SHORT).show();
        }


        mDrawer.closeDrawers();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (mDrawer.isDrawerOpen(mDrawerOptions)){
                    mDrawer.closeDrawers();
                }else{
                    mDrawer.openDrawer(mDrawerOptions);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


