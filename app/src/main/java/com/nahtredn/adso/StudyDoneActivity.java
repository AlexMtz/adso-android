package com.nahtredn.adso;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.nahtredn.entities.StudyDone;
import com.nahtredn.utilities.Messenger;
import com.nahtredn.utilities.RealmController;
import com.nahtredn.utilities.Validator;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class StudyDoneActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText inputCourse, inputInstitute, inputStartCourse, inputEndCourse;
    private TextInputLayout layoutCourse, layoutInstitute, layoutStartCourse, layoutEndCourse;

    private Spinner spinnerAcademicLevel, spinnerState, spinnerTitle;

    private boolean isStartDate;

    private InterstitialAd mInterstitialAd;

    private Calendar calendar = Calendar.getInstance();

    private int id;

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
        spinnerAcademicLevel = findViewById(R.id.spinner_academic_level_study_done);
        spinnerTitle = findViewById(R.id.spinner_title_study_done);

        reloadInterstitial();

        AdView mAdView = findViewById(R.id.adViewStudyDone);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        // Se identifica si se creará un nuevo objeto o se modificará otro
        this.id = getIntent().getIntExtra("study_done_id",-1);
        if (this.id != -1){
            loadData(RealmController.with().find(new StudyDone(), id));
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    private void loadData(StudyDone studyDone){
        inputCourse.setText(studyDone.getCourseName());
        inputInstitute.setText(studyDone.getInstitute());
        inputStartCourse.setText(studyDone.getStartDate());
        inputEndCourse.setText(studyDone.getEndDate());
        spinnerAcademicLevel.setSelection(studyDone.getAcademicLevelPos());
        spinnerState.setSelection(studyDone.getStatePos());
        spinnerTitle.setSelection(studyDone.getTitlePos());
    }

    public void onClicSaveStudyDone(View view){
        if (!Validator.with(this).validateText(inputCourse, layoutCourse)){
            return;
        }

        if (!Validator.with(this).validateText(inputInstitute, layoutInstitute)){
            return;
        }

        if (!Validator.with(this).validateText(inputStartCourse, layoutStartCourse)){
            return;
        }

        if (!Validator.with(this).validateText(inputEndCourse, layoutEndCourse)){
            return;
        }
        if (spinnerAcademicLevel.getSelectedItemPosition() == 0){
            Messenger.with(this.getApplication()).showMessage(getString(R.string.error_academic_level));
            return;
        }
        if (spinnerState.getSelectedItemPosition() == 0){
            Messenger.with(this.getApplication()).showMessage(getString(R.string.error_state_general));
            return;
        }
        if (spinnerTitle.getSelectedItemPosition() == 0){
            Messenger.with(this.getApplication()).showMessage(getString(R.string.error_title));
            return;
        }

        StudyDone studyDone = new StudyDone();
        studyDone.setId(this.id);
        studyDone.setCourseName(inputCourse.getText().toString().trim());
        studyDone.setInstitute(inputInstitute.getText().toString().trim());
        studyDone.setStartDate(inputStartCourse.getText().toString());
        studyDone.setEndDate(inputEndCourse.getText().toString().trim());
        studyDone.setAcademicLevel((String) spinnerAcademicLevel.getSelectedItem());
        studyDone.setAcademicLevelPos(spinnerAcademicLevel.getSelectedItemPosition());
        studyDone.setState((String) spinnerState.getSelectedItem());
        studyDone.setStatePos(spinnerState.getSelectedItemPosition());
        studyDone.setTitle((String) spinnerTitle.getSelectedItem());
        studyDone.setTitlePos(spinnerTitle.getSelectedItemPosition());

        if (RealmController.with().save(studyDone)){
            Messenger.with(this.getApplication()).showSuccessMessage();
            this.finish();
        } else {
            Messenger.with(this.getApplication()).showFailMessage();
        }

        showInterstitial();
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
            this.finish();
            return true;
        }

        if (id == R.id.action_delete){
            if (RealmController.with().delete(new StudyDone(), this.id)){
                Messenger.with(this).showMessage(R.string.success_delete);
                this.finish();
            } else {
                Messenger.with(this).showMessage(R.string.error_delete);
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
                reloadInterstitial();
            }
        });
        return interstitialAd;
    }

    private void showInterstitial() {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            reloadInterstitial();
        }
    }

    private void loadInterstitial() {
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

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());

        if (isStartDate){
            inputStartCourse.setText(dateFormat.format(calendar.getTime()));
        } else {
            inputEndCourse.setText(dateFormat.format(calendar.getTime()));
        }
    }
}
