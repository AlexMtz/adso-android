package com.nahtredn.adso;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.nahtredn.entities.Vacancy;
import com.nahtredn.utilities.Messenger;
import com.nahtredn.utilities.PDF;
import com.nahtredn.utilities.PreferencesProperties;
import com.nahtredn.utilities.RealmController;
import com.nahtredn.utilities.Validator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class VacancyActivity extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;
    TextView jobTitle, jobType, company, salary, hours, place, description,skills, knowledge, benefits, experience,
            email, phone;

    private String errorMesage = "";

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacancy);

        mInterstitialAd = newInterstitialAd();
        loadInterstitial();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        AdView mAdView = findViewById(R.id.adViewVacancy);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        jobTitle = findViewById(R.id.job_title_vacancy);
        jobType = findViewById(R.id.type_job_vacancy);
        company = findViewById(R.id.company_vacancy);
        salary = findViewById(R.id.salary_vacancy_activity);
        hours = findViewById(R.id.hours_vacancy);
        place = findViewById(R.id.place_vacancy);
        description = findViewById(R.id.description_vacancy);
        skills = findViewById(R.id.skills_vacancy);
        knowledge = findViewById(R.id.knowledgments_vacancy);
        benefits = findViewById(R.id.benefits_vacancy);
        experience = findViewById(R.id.experience_vacancy);
        email = findViewById(R.id.email_vacancy);
        phone = findViewById(R.id.phone_vacancy);

        String id = getIntent().getStringExtra("vacancy_id");
        if (!id.equals("")){
            loadData(RealmController.with().find(new Vacancy(), id));
        }
    }

    private void loadData(Vacancy vacancy){
        jobTitle.setText(vacancy.getJobTitle());
        jobType.setText(vacancy.getType());
        company.setText(vacancy.getCompany());
        salary.setText(vacancy.getSalary());
        hours.setText(vacancy.getWorkingDay() + " hrs p/semana");
        place.setText(vacancy.getLocation());
        description.setText(vacancy.getDescription());
        skills.setText(vacancy.getSkills());
        knowledge.setText(vacancy.getKnowledgments());
        benefits.setText(vacancy.getBenefits());
        experience.setText(vacancy.getExperience());
        email.setText(vacancy.getEmail());
        phone.setText(vacancy.getPhone());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacancy, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            this.finish();
            return true;
        }

        if (id == R.id.action_send) {
            if (!TextUtils.isEmpty(email.getText().toString()) && Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                //
                GeneratePdf task = new GeneratePdf(1);
                task.execute();
            }
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

    class GeneratePdf extends AsyncTask<String, String, String> {
        private int action;

        public GeneratePdf(int action) {
            this.action = action;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(VacancyActivity.this, ProgressDialog.THEME_HOLO_DARK);
            pDialog.setMessage("Generando solicitud...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... f_url) {
            try {
                errorMesage = PDF.with(getApplication()).generaSolicitud();
                if(!errorMesage.equals("")){
                    this.cancel(true);
                }
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            File f = new File(RealmController.with(getApplication()).find(PreferencesProperties.PATH_FILE.toString()));
            if (f.exists()){
                if (action == 1){
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + f.getAbsolutePath()));
                    intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{email.getText().toString()});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "SOLICITUD DE EMPLEO");
                    intent.putExtra(Intent.EXTRA_TEXT   , "solicitud de empleo digital, una oportunidad");
                    intent.setType("application/pdf");
                    startActivity(Intent.createChooser(intent, "Selecciona una app de emails..."));
                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            pDialog.dismiss();
            Messenger.with(VacancyActivity.this).showMessage(errorMesage);
        }
    }
}
