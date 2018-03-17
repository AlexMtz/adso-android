package com.nahtredn.adso;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.nahtredn.entities.Documentation;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;

public class DocumentationActivity extends AppCompatActivity {

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    private EditText inputDriverLicense, inputCURP, inputRFC;
    private TextInputLayout layoutInputCURP;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentation);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        Realm.init(this);

        mInterstitialAd = newInterstitialAd();
        loadInterstitial();

        mAdView = findViewById(R.id.adViewDocumentation);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        reloadInterestitial();

        inputDriverLicense = findViewById(R.id.input_drive_license_documentation);
        layoutInputCURP = findViewById(R.id.layout_input_curp_documentation);
        inputCURP = findViewById(R.id.input_curp_documentation);
        inputRFC = findViewById(R.id.input_rfc_documentation);

        Documentation documentation = findDocumentation();
        if (documentation != null){
            loadDocumentation(documentation);
            id = documentation.getId();
        }else {
            id = -1;
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadDocumentation(Documentation documentation){
        inputDriverLicense.setText(documentation.getDriverLicense());
        inputCURP.setText(documentation.getCurp());
        inputRFC.setText(documentation.getRfc());
    }

    private Documentation findDocumentation(){
        Documentation documentation = null;
        Realm realm = Realm.getDefaultInstance();
        try{
            documentation = realm.where(Documentation.class).findFirst();
        }finally {
            realm.close();
        }
        return documentation;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            this.finish();
            return true;
        }

        if (id == R.id.action_help){
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.commonAlertDialog));
            builder.setMessage(getString(R.string.help_documentation_activity))
                    .setTitle(getString(R.string.help_title));
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

            }

            @Override
            public void onAdClosed() {
                reloadInterestitial();
            }
        });
        return interstitialAd;
    }

    public void onClicSaveDocumentationActivity(View view){
        showInterstitial();

        if (!validateCURP()) {
            return;
        }

        Documentation documentation = new Documentation();
        if (id != -1){
            documentation.setId(id);
        }
        documentation.setDriverLicense(inputDriverLicense.getText().toString());
        documentation.setCurp(inputCURP.getText().toString());
        documentation.setRfc(inputRFC.getText().toString());
        saveDocumentation(documentation);
    }

    private void saveDocumentation(Documentation tmpDocumentation){
        Realm.init(getApplicationContext());
        final Realm realm = Realm.getDefaultInstance();

        try {
            realm.beginTransaction();
            Number currentIdNum = realm.where(Documentation.class).max("id");
            if (currentIdNum == null) {
                int nextId = 1;
                tmpDocumentation.setId(nextId);
            }
            realm.copyToRealmOrUpdate(tmpDocumentation);
            realm.commitTransaction();
            showMessage(getString(R.string.success_save));
            // this.finish();
        } catch (Exception e) {
            realm.cancelTransaction();
            showMessage(getString(R.string.error_save));
            Log.w("GeneralActivity", "Error: " + e.getMessage());
        } finally {
            realm.close();
        }
    }

    private void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private boolean validateCURP(){
        String curp = inputCURP.getText().toString().trim();

        if (curp.isEmpty() || !isValidCURP(curp)) {
            layoutInputCURP.setError(getString(R.string.error_curp_documentation));
            requestFocus(inputCURP);
            return false;
        } else {
            layoutInputCURP.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private static boolean isValidCURP(String curp) {
        String regex =
                "[A-Z]{1}[AEIOU]{1}[A-Z]{2}[0-9]{2}" +
                        "(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])" +
                        "[HM]{1}" +
                        "(AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)" +
                        "[B-DF-HJ-NP-TV-Z]{3}" +
                        "[0-9A-Z]{1}[0-9]{1}$";
        Pattern patron = Pattern.compile(regex);
        return !TextUtils.isEmpty(curp) && patron.matcher(curp).matches();
    }

    private void showInterstitial() {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            reloadInterestitial();
        }
    }

    private void loadInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void reloadInterestitial() {
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
    }
}
