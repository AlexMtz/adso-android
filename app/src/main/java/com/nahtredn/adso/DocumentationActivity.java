package com.nahtredn.adso;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.nahtredn.entities.Documentation;
import com.nahtredn.utilities.Messenger;
import com.nahtredn.utilities.RealmController;
import com.nahtredn.utilities.Validator;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class DocumentationActivity extends AppCompatActivity {

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

        mInterstitialAd = newInterstitialAd();
        loadInterstitial();

        AdView mAdView = findViewById(R.id.adViewDocumentation);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        reloadInterestitial();

        inputDriverLicense = findViewById(R.id.input_drive_license_documentation);
        layoutInputCURP = findViewById(R.id.layout_input_curp_documentation);
        inputCURP = findViewById(R.id.input_curp_documentation);
        inputRFC = findViewById(R.id.input_rfc_documentation);

        // Se identifica si se creará un nuevo objeto o se modificará otro
        Documentation documentation = RealmController.with().find(new Documentation());
        if (documentation != null){
            loadData(documentation);
            this.id = documentation.getId();
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadData(Documentation documentation){
        inputDriverLicense.setText(documentation.getDriverLicense());
        inputCURP.setText(documentation.getCurp());
        inputRFC.setText(documentation.getRfc());
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

        if (!Validator.with(this).validateCURP(inputCURP, layoutInputCURP)) {
            return;
        }

        Documentation documentation = new Documentation();
        documentation.setId(id);
        documentation.setDriverLicense(inputDriverLicense.getText().toString());
        documentation.setCurp(inputCURP.getText().toString());
        documentation.setRfc(inputRFC.getText().toString());

        if (RealmController.with().save(documentation)){
            Messenger.with(this.getApplication()).showSuccessMessage();
            this.finish();
        } else {
            Messenger.with(this.getApplication()).showFailMessage();
        }

        showInterstitial();
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
