package com.nahtredn.adso;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.nahtredn.entities.Knowledge;
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

public class SkillDetailActivity extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;

    private EditText inputKnowledge;
    private TextInputLayout layoutInputKnowledge;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle(R.string.title_activity_skill_detail);
            actionBar.setSubtitle(R.string.subtitle_activity_knowledge_detail);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        inputKnowledge = findViewById(R.id.input_knowledge);
        layoutInputKnowledge = findViewById(R.id.layout_input_knowledge);

        // Se identifica si se creará un nuevo objeto o se modificará otro
        this.id = getIntent().getIntExtra("knowledge_id",-1);
        if (this.id != -1){
            loadData(RealmController.with().find(new Knowledge(), id));
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Create the InterstitialAd and set the adUnitId (defined in values/strings.xml).
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();

        AdView mAdView = findViewById(R.id.adViewSkillsDetails);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            this.finish();
            return true;
        }

        if (id == R.id.action_delete){
            if (RealmController.with().delete(new Knowledge(), this.id)){
                Messenger.with(this).showMessage(R.string.success_delete);
                this.finish();
            } else {
                Messenger.with(this).showMessage(R.string.error_delete);
            }
            return true;
        }

        if (id == R.id.action_help){
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.commonAlertDialog));
            builder.setMessage(getString(R.string.help_skills_activity))
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
            public void onAdLoaded() { }

            @Override
            public void onAdFailedToLoad(int errorCode) { }

            @Override
            public void onAdClosed() {
                // Proceed to the next level.
                goToNextLevel();
            }
        });
        return interstitialAd;
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            goToNextLevel();
        }
    }

    private void loadInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void goToNextLevel() {
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
    }

    private void loadData(Knowledge knowledge){
        inputKnowledge.setText(knowledge.getTitle());
    }

    public void onClicSaveKnowledge(View view){

        if (!Validator.with(this).validateText(inputKnowledge, layoutInputKnowledge)){
            return;
        }

        Knowledge knowledge = new Knowledge();
        knowledge.setId(this.id);
        knowledge.setTitle(inputKnowledge.getText().toString().trim());
        if (RealmController.with().save(knowledge)){
            Messenger.with(this.getApplication()).showSuccessMessage();
            this.finish();
        } else {
            Messenger.with(this.getApplication()).showFailMessage();
        }
        showInterstitial();
    }
}
