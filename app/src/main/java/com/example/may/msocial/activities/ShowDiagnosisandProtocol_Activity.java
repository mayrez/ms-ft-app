package com.example.may.msocial.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.may.msocial.R;
import com.example.may.msocial.models.MsRow;
import com.example.may.msocial.models.Protocol;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;


public class ShowDiagnosisandProtocol_Activity extends Activity implements TextToSpeech.OnInitListener {
    private String actor;
    private String patientLanguage;
    private String medicalfield;
    private TextToSpeech tts;
    private ArrayList<Protocol> protocolsSelected;
    private MsRow problemTranslated;
    private WebView webView;
    private String video;
    private String protocols;
    private String diagnosis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            actor = extras.getString("actor");
            medicalfield = extras.getString("medicalfield");
            patientLanguage = extras.getString("patientLanguage");
            protocolsSelected = (ArrayList<Protocol>)getIntent().getSerializableExtra("protocolsSelected");
            problemTranslated = (MsRow) getIntent().getSerializableExtra("problemTranslated");
        }

        setContentView(R.layout.diagnosis_protocol_activity);
        tts = new TextToSpeech(this, this);
        webView = (WebView)findViewById(R.id.videoView);
        addTitle();
        addProtocolList();
        addWebView(webView);
        addOnCLickListenButtonListener();
        addOnclickSendButtonListener();

    }

    private int setTextToSpeechLanguage() {

        switch (patientLanguage) {
            case "es":
                return tts.setLanguage(new Locale("es"));

            case "en":
                return tts.setLanguage(Locale.ENGLISH);

            case "sv":
                return tts.setLanguage(new Locale("sv"));

            case "zh":
                return tts.setLanguage(Locale.SIMPLIFIED_CHINESE);

            case "ar":
                return tts.setLanguage(new Locale("ar"));

            case "fr":
                return tts.setLanguage(Locale.FRENCH);

            case "de":
                return tts.setLanguage(Locale.GERMAN);

            case "ru":
                return tts.setLanguage(new Locale("ru"));

            default:
               return tts.setLanguage(new Locale("es"));
        }

    }

    private void addOnCLickListenButtonListener() {
        ImageButton listenBtn = (ImageButton)findViewById(R.id.listenbtn);
        listenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakOut(diagnosis+" "+protocols);
            }
        });
    }

    public String extractYoutubeId(String url) throws MalformedURLException {
        String query = new URL(url).getQuery();
        String[] param = query.split("&");
        String id = null;
        for (String row : param) {
            String[] param1 = row.split("=");
            if (param1[0].equals("v")) {
                id = param1[1];
            }
        }
        return id;
    }
    private void addOnclickSendButtonListener() {
        Button sendButton = (Button)findViewById(R.id.sendBtn);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String protocols = "";
                for (int i = 0; i < protocolsSelected.size(); i++) {
                    protocols += i+": "+protocolsSelected.get(i).getDescription()+"\n";
                }
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{((TextView)findViewById(R.id.email)).getText().toString()});
                i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.Diagnosis)+"--"+getResources().getString(R.string.Protocol));
                i.putExtra(Intent.EXTRA_TEXT   , getResources().getString(R.string.Diagnosis)+": "+problemTranslated.getTitle()+"\n"
                +getResources().getString(R.string.Protocol)+": \n"+protocols+"\n"+video);
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ShowDiagnosisandProtocol_Activity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addWebView(WebView webView) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        for (int i = 0; i < protocolsSelected.size() ; i++) {
            video = "https://www.youtube.com/embed/" + protocolsSelected.get(i).getVideo() + "?autoplay=1&vq=small";
            final Activity activity = this;
            webView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    activity.setProgress(progress * 1000);
                }
            });
            webView.setWebViewClient(new MyOwnWebViewClient());
            webView.loadUrl(video);
            webView.setWebViewClient(new MyOwnWebViewClient());

            break;
        }


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void addProtocolList() {
        protocols = "";
        LinearLayout ll = (LinearLayout)findViewById(R.id.diagnosisView);
        for (int i = 0; i < protocolsSelected.size(); i++) {
            TextView tv = new TextView(this);
            tv.setText((i + 1) + ". " + protocolsSelected.get(i).getDescription());
            protocols += protocolsSelected.get(i).getDescription()+ " ";
            tv.setTextSize(20);
            ll.addView(tv);
        }
    }

    private void addTitle() {
        TextView title = (TextView)findViewById(R.id.nombreProblema);
        diagnosis = problemTranslated.getTitle();
        title.setText(problemTranslated.getTitle());
    }
    private void speakOut(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
    @Override
    public void onInit(int status) {


        if (status == TextToSpeech.SUCCESS) {

            int result = setTextToSpeechLanguage();

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");

            } else {

            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        if(tts != null) {

            tts.stop();
            tts.shutdown();

        }
        super.onDestroy();
    }

    private class MyOwnWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            webView.setWebChromeClient(new WebChromeClient(){});
            view.loadUrl(url);
            return true;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.showdiagnosis_menu, menu);

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
