package com.nahtredn.adso;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.nahtredn.entities.Vacancy;
import com.nahtredn.utilities.RealmController;
import com.nahtredn.utilities.Validator;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class VacancyActivity extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;
    TextView jobTitle, jobType, company, salary, hours, place, description,skills, knowledge, benefits, experience,
            email, phone;

    private int id;

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

        this.id = getIntent().getIntExtra("vacancy_id",-1);
        if (this.id != -1){
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
            }
            return true;
        }

        if (id == R.id.action_call){
            if (Validator.with(this).validateText(phone)){
                if (Patterns.PHONE.matcher(phone.getText().toString()).matches()){
                    // make a call
                }
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
}
