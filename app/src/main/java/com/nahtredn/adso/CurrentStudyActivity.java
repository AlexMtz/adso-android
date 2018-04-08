package com.nahtredn.adso;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.nahtredn.entities.CurrentStudy;
import com.nahtredn.utilities.Messenger;
import com.nahtredn.utilities.RealmController;
import com.nahtredn.utilities.Validator;

import android.app.TimePickerDialog;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class CurrentStudyActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private EditText inputCourseName, inputInstitute, startScheduleTime, endScheduleTime;
    private TextInputLayout layoutInputCourseName, layoutInputInstitute;
    private CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    private Spinner spinnerAcademicLevel, spinnerGrade, spinnerModalityGrade;

    private int id;

    private InterstitialAd mInterstitialAd;

    private boolean isStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_study);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        mInterstitialAd = newInterstitialAd();
        loadInterstitial();

        AdView mAdView = findViewById(R.id.adViewCurrentStudy);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        inputCourseName = findViewById(R.id.input_name_course_current_study);
        layoutInputCourseName = findViewById(R.id.layout_input_name_course_current_study);
        inputInstitute = findViewById(R.id.input_institute_current_study);
        layoutInputInstitute = findViewById(R.id.layout_input_institute_current_study);
        startScheduleTime = findViewById(R.id.input_time_start_current_study);
        endScheduleTime = findViewById(R.id.input_time_end_current_study);

        spinnerAcademicLevel = findViewById(R.id.spinner_academic_level_current_study);
        spinnerGrade = findViewById(R.id.spinner_grade_current_study);
        spinnerModalityGrade = findViewById(R.id.spinner_modality_current_study);

        monday = findViewById(R.id.checkbox_monday_current_study);
        tuesday = findViewById(R.id.checkbox_tuesday_current_study);
        wednesday = findViewById(R.id.checkbox_wenesday_current_study);
        thursday = findViewById(R.id.checkbox_thursday_current_study);
        friday = findViewById(R.id.checkbox_friday_current_study);
        saturday = findViewById(R.id.checkbox_saturday_current_study);
        sunday = findViewById(R.id.checkbox_sunday_current_study);

        this.id = getIntent().getIntExtra("current_study_id",-1);
        if (this.id != -1){
            loadData(RealmController.with().find(new CurrentStudy(), id));
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    private void loadData(CurrentStudy currentStudy){
        inputCourseName.setText(currentStudy.getCourseName());
        inputInstitute.setText(currentStudy.getInstitute());
        spinnerAcademicLevel.setSelection(currentStudy.getPositionAcademicLevel());
        spinnerGrade.setSelection(currentStudy.getPositionGrade());
        spinnerModalityGrade.setSelection(currentStudy.getPositionModality());
        checkDays(currentStudy.getDays());
        startScheduleTime.setText(currentStudy.getStartTime());
        endScheduleTime.setText(currentStudy.getEndTime());
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
            if (RealmController.with().delete(new CurrentStudy(), this.id)){
                Messenger.with(this).showMessage(R.string.success_delete);
                this.finish();
            } else {
                Messenger.with(this).showMessage(R.string.error_delete);
            }
            return true;
        }

        if (id == R.id.action_help){
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.commonAlertDialog));
            builder.setMessage(getString(R.string.help_current_study_activity))
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
                goToNextLevel();
            }
        });
        return interstitialAd;
    }

    private void showInterstitial() {
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

    public void onClicTime(View view){
        isStartTime = view.getId() == R.id.btn_date_start_current_study;
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(CurrentStudyActivity.this, this, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Selecciona la hora");
        mTimePicker.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
        StringBuffer time = new StringBuffer("");
        if (selectedHour < 10) {
            time.append("0");
        }
        time.append(selectedHour).append(":");
        if (selectedMinute < 10){
            time.append("0");
        }
        time.append(selectedMinute);
        if (isStartTime)
            startScheduleTime.setText(time);
        else
            endScheduleTime.setText(time);
    }

    public void saveCurrentStudy(View view){
        if (!Validator.with(this).validateText(inputCourseName, layoutInputCourseName)){
            return;
        }

        if (!Validator.with(this).validateText(inputInstitute, layoutInputInstitute)){
            return;
        }

        if (spinnerAcademicLevel.getSelectedItemPosition() == 0){
            Messenger.with(this).showMessage(getString(R.string.error_academic_level));
            return;
        }

        if (spinnerGrade.getSelectedItemPosition() == 0){
            Messenger.with(this).showMessage(getString(R.string.error_grade));
            return;
        }

        if (spinnerModalityGrade.getSelectedItemPosition() == 0){
            Messenger.with(this).showMessage(getString(R.string.error_modality));
            return;
        }

        CurrentStudy currentStudy = new CurrentStudy();
        currentStudy.setId(this.id);
        currentStudy.setCourseName(inputCourseName.getText().toString().trim());
        currentStudy.setInstitute(inputInstitute.getText().toString().trim());
        currentStudy.setAcademicLevel((String) spinnerAcademicLevel.getSelectedItem());
        currentStudy.setPositionAcademicLevel(spinnerAcademicLevel.getSelectedItemPosition());
        currentStudy.setGrade((String) spinnerGrade.getSelectedItem());
        currentStudy.setPositionGrade(spinnerGrade.getSelectedItemPosition());
        currentStudy.setModality((String) spinnerModalityGrade.getSelectedItem());
        currentStudy.setPositionModality(spinnerModalityGrade.getSelectedItemPosition());
        currentStudy.setDays(getDays());
        currentStudy.setStartTime(startScheduleTime.getText().toString());
        currentStudy.setEndTime(endScheduleTime.getText().toString());
        if (RealmController.with().save(currentStudy)){
            Messenger.with(this.getApplication()).showSuccessMessage();
            this.finish();
        } else {
            Messenger.with(this.getApplication()).showFailMessage();
        }
        showInterstitial();
    }

    private String getDays(){
        StringBuilder days = new StringBuilder("");
        if (monday.isChecked()){
            days.append("Lun-");
        }
        if (tuesday.isChecked()){
            days.append("Mar-");
        }
        if (wednesday.isChecked()){
            days.append("Mie-");
        }
        if (thursday.isChecked()){
            days.append("Jue-");
        }
        if (friday.isChecked()){
            days.append("Vie-");
        }
        if (saturday.isChecked()){
            days.append("Sab-");
        }
        if (sunday.isChecked()){
            days.append("Dom");
        }
        return days.toString();
    }

    private void checkDays(String days){
        if (days.contains("Lun")){
            monday.setChecked(true);
        }
        if (days.contains("Mar")){
            tuesday.setChecked(true);
        }
        if (days.contains("Mie")){
            wednesday.setChecked(true);
        }
        if (days.contains("Jue")){
            thursday.setChecked(true);
        }
        if (days.contains("Vie")){
            friday.setChecked(true);
        }
        if (days.contains("Sab")){
            saturday.setChecked(true);
        }
        if (days.contains("Dom")){
            sunday.setChecked(true);
        }
    }
}
