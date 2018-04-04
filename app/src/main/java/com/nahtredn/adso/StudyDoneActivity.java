package com.nahtredn.adso;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.nahtredn.entities.StudiesDone;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

public class StudyDoneActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    private EditText inputCourse, inputInstitute, inputStartCourse, inputEndCourse;
    private TextInputLayout layoutCourse, layoutInstitute, layoutStartCourse, layoutEndCourse;

    private Spinner spinnerAcademicLevel, spinnerState, spinnerTitle;

    private boolean isStartDate;

    private InterstitialAd mInterstitialAd;

    private Calendar calendar = Calendar.getInstance();

    private StudiesDone studyDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_done);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        inputCourse = findViewById(R.id.input_name_course_study_done);
        inputInstitute = findViewById(R.id.input_institute_study_done);
        inputStartCourse = findViewById(R.id.input_date_start_study_done);
        inputEndCourse = findViewById(R.id.input_date_end_study_done);

        layoutCourse = findViewById(R.id.layout_name_course_study_done);
        layoutInstitute = findViewById(R.id.layout_institute_study_done);
        layoutStartCourse = findViewById(R.id.layout_input_start_date_study_done);
        layoutEndCourse = findViewById(R.id.layout_input_end_date_study_done);

        spinnerState = findViewById(R.id.spinner_state_study_done);
        spinnerState.setOnItemSelectedListener(this);
        spinnerAcademicLevel = findViewById(R.id.spinner_academic_level_study_done);
        spinnerAcademicLevel.setOnItemSelectedListener(this);
        spinnerTitle = findViewById(R.id.spinner_title_study_done);
        spinnerTitle.setOnItemSelectedListener(this);

        // Create the InterstitialAd and set the adUnitId (defined in values/strings.xml).
        reloadInterstitial();

        AdView mAdView = findViewById(R.id.adViewStudyDone);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Realm.init(this);

        studyDone = new StudiesDone();
        StudiesDone studyDoneTmp = findStudy();
        if (studyDoneTmp != null){
            studyDone.setId(studyDoneTmp.getId());
            loadStudy(studyDoneTmp);
        }


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private StudiesDone findStudy(){
        int studyId = getIntent().getIntExtra("study_done_id",-1);
        if (studyId == -1){
            return null;
        }
        StudiesDone result = null;
        Realm realm = Realm.getDefaultInstance();
        try{
            result = realm.where(StudiesDone.class).equalTo("id", studyId).findFirst();
        }finally {
            realm.close();
        }
        return result;
    }

    private void deleteStudy(int id){
        Log.w("StudyDoneActivity", "Id " + studyDone.getId());
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        try{
            StudiesDone studyDoneTmp = studyDone;
            RealmResults result = realm.where(StudiesDone.class).findAll();
        }finally {
            realm.close();
        }
    }

    private void loadStudy(StudiesDone studyDone){
        inputCourse.setText(studyDone.getCourseName());
        inputInstitute.setText(studyDone.getInstitute());
        inputStartCourse.setText(studyDone.getStartDate());
        inputEndCourse.setText(studyDone.getEndDate());
        spinnerAcademicLevel.setSelection(studyDone.getAcademicLevelPos());
        spinnerState.setSelection(studyDone.getStatePos());
        spinnerTitle.setSelection(studyDone.getTitlePos());
    }

    public void onClicSaveStudyDone(View view){
        if (!validateText(inputCourse, layoutCourse)){
            return;
        } else {
            studyDone.setCourseName(inputCourse.getText().toString().trim());
        }
        if (!validateText(inputInstitute, layoutInstitute)){
            return;
        } else {
            studyDone.setInstitute(inputInstitute.getText().toString().trim());
        }
        if (!validateText(inputStartCourse, layoutStartCourse)){
            return;
        }
        if (!validateText(inputEndCourse, layoutEndCourse)){
            return;
        }
        if (spinnerAcademicLevel.getSelectedItemPosition() == 0){
            showMessage(getString(R.string.error_academic_level));
            return;
        }
        if (spinnerState.getSelectedItemPosition() == 0){
            showMessage(getString(R.string.error_state_general));
            return;
        }
        if (spinnerTitle.getSelectedItemPosition() == 0){
            showMessage(getString(R.string.error_title));
            return;
        }

        showInterstitial();
        saveStudyDone();
    }

    private void saveStudyDone() {
        Realm.init(getApplicationContext());
        final Realm realm = Realm.getDefaultInstance();

        try {
            realm.beginTransaction();
            Number currentIdNum = realm.where(StudiesDone.class).max("id");
            if (currentIdNum == null) {
                int nextId = 1;
                studyDone.setId(nextId);
            }
            realm.copyToRealmOrUpdate(studyDone);
            realm.commitTransaction();
            showMessage(getString(R.string.success_save));
            Log.w("StudyDoneActivity", "Succesfully study saved");
            this.finish();
        } catch (Exception e) {
            realm.cancelTransaction();
            showMessage(getString(R.string.error_save));
            Log.w("StudyDoneActivity", "Error: " + e.getMessage());
        } finally {
            realm.close();
        }
    }

    private void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private boolean validateText(EditText editText, TextInputLayout textInputLayout) {
        if (editText.getText().toString().trim().isEmpty()) {
            textInputLayout.setError(getString(R.string.error_field_required));
            requestFocus(editText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void onClicDateStudyDone(View view){
        calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(StudyDoneActivity.this,
                this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
        isStartDate = view.getId() == R.id.btn_date_start_study_done;
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
            Log.w("StudyDoneActivity", "Id " + studyDone.getId());
            this.finish();
            return true;
        }

        if (id == R.id.action_delete){
            Log.w("StudyDoneActivity", "Id " + studyDone.getId());
            if (studyDone.getId() == 0){
                this.finish();
            } else {
                deleteStudy(studyDone.getId());
                this.finish();
            }
            return true;
        }

        if (id == R.id.action_help){
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.commonAlertDialog));
            builder.setMessage(getString(R.string.help_study_done_activity))
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
                // Proceed to the next level.
                reloadInterstitial();
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
            reloadInterstitial();
        }
    }

    private void loadInterstitial() {
        // Disable the next level button and load the ad.
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void reloadInterstitial() {
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        //set birth date
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());

        if (isStartDate){
            inputStartCourse.setText(dateFormat.format(calendar.getTime()));
            studyDone.setStartDate(dateFormat.format(calendar.getTime()));
        } else {
            inputEndCourse.setText(dateFormat.format(calendar.getTime()));
            studyDone.setEndDate(dateFormat.format(calendar.getTime()));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        if (adapterView.getId() == R.id.spinner_academic_level_study_done){
            studyDone.setAcademicLevel((String) spinnerAcademicLevel.getSelectedItem());
            studyDone.setAcademicLevelPos(pos);
            return;
        }
        if (adapterView.getId() == R.id.spinner_state_study_done){
            studyDone.setState((String) spinnerState.getSelectedItem());
            studyDone.setStatePos(pos);
            return;
        }
        if (adapterView.getId() == R.id.spinner_title_study_done){
            studyDone.setTitle((String) spinnerTitle.getSelectedItem());
            studyDone.setTitlePos(pos);
            return;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
