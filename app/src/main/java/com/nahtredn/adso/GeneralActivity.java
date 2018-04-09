package com.nahtredn.adso;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.nahtredn.entities.Address;
import com.nahtredn.entities.General;
import com.nahtredn.helpers.ArrayStates;
import com.nahtredn.helpers.RadioButtons;
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
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class GeneralActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    private EditText inputName, inputLastName, inputMiddleName, inputBirthDate, inputAge, inputPhone,
            inputEmail, inputStreet, inputColony, inputZipCode;
    private TextInputLayout layoutInputName, layoutInputLastName,
            layoutInputBirthDate, layoutInputPhone, layoutInputEmail;

    private RadioGroup radioGroupGenre, radioGroupLivinWith, radioGroupCivilStatus;

    private InterstitialAd mInterstitialAd;
    private Spinner spnStates, spnMunicipality;

    private Calendar calendar = Calendar.getInstance();

    private String state, municipality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        inputName = findViewById(R.id.input_name_general);
        inputLastName = findViewById(R.id.input_last_name_general);
        inputMiddleName = findViewById(R.id.input_middle_name_general);
        inputBirthDate = findViewById(R.id.input_birth_date_general);
        inputAge = findViewById(R.id.input_age_general);
        inputPhone = findViewById(R.id.input_phone_general);
        inputEmail = findViewById(R.id.input_email_general);
        inputStreet = findViewById(R.id.input_street_general);
        inputColony = findViewById(R.id.input_colony_general);
        inputZipCode = findViewById(R.id.input_zip_code_general);

        layoutInputName = findViewById(R.id.layout_input_name_general);
        layoutInputLastName = findViewById(R.id.layout_input_last_name_general);
        layoutInputBirthDate = findViewById(R.id.layout_input_birth_date_general);
        layoutInputPhone = findViewById(R.id.layout_input_phone_general);
        layoutInputEmail = findViewById(R.id.layout_input_email_general);

        radioGroupGenre = findViewById(R.id.radio_group_genre_general);
        radioGroupLivinWith = findViewById(R.id.radio_group_living_with_general);
        radioGroupCivilStatus = findViewById(R.id.radio_group_civil_status_general);

        reloadInterstitial();

        AdView mAdView = findViewById(R.id.adViewGeneral);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        spnStates = findViewById(R.id.spinner_states_activity_general);
        spnStates.setOnItemSelectedListener(this);
        spnMunicipality = findViewById(R.id.spinner_municipality_activity_general);
        spnMunicipality.setOnItemSelectedListener(this);

        General general = RealmController.with(this).find(new General());
        if (general != null){
            loadData(general);
            state = general.getAddress().getState();
            municipality = general.getAddress().getMunicipality();
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void loadData(General general){
        inputName.setText(general.getName());
        inputLastName.setText(general.getLastName());
        inputMiddleName.setText(general.getMiddleName());
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        inputBirthDate.setText(dateFormat.format(general.getBirthDate()));
        inputAge.setText("" + general.getAge());
        inputPhone.setText(general.getPhone());
        inputEmail.setText(general.getEmail());
        inputStreet.setText(general.getAddress().getStreet());
        inputColony.setText(general.getAddress().getColony());
        inputZipCode.setText(general.getAddress().getZipCode());
        RadioButtons radioButtons = new RadioButtons();
        RadioButton genre = findViewById(radioButtons.getIdRadioButtonBy(general.getGenre()));
        genre.setChecked(true);
        RadioButton livingWith = findViewById(radioButtons.getIdRadioButtonBy(general.getLivingWith()));
        livingWith.setChecked(true);
        RadioButton civilStatus = findViewById(radioButtons.getIdRadioButtonBy(general.getCivilStatus()));
        civilStatus.setChecked(true);
    }

    public void onClicBirthDate(View view){
        calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(GeneralActivity.this,
                this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public void onClicSaveGeneralActivity(View view) throws ParseException {

        if (!Validator.with(this).validateText(inputName, layoutInputName)) {
            return;
        }

        if (!Validator.with(this).validateText(inputLastName, layoutInputLastName)) {
            return;
        }

        if (!Validator.with(this).validateText(inputBirthDate, layoutInputBirthDate)) {
            return;
        }

        if (!Validator.with(this).validateText(inputPhone, layoutInputPhone)) {
            return;
        }

        if (!Validator.with(this).validateEmail(inputEmail, layoutInputEmail)) {
            return;
        }

        if (radioGroupGenre.getCheckedRadioButtonId() == -1){
            Messenger.with(this).showMessage(getString(R.string.error_genre_field));
            return;
        }

        if (radioGroupLivinWith.getCheckedRadioButtonId() == -1){
            Messenger.with(this).showMessage(getString(R.string.error_livin_with));
            return;
        }

        if (radioGroupCivilStatus.getCheckedRadioButtonId() == -1){
            Messenger.with(this).showMessage(getString(R.string.error_civil_status));
            return;
        }

        if (state == null){
            Messenger.with(this).showMessage(getString(R.string.error_state_general));
            return;
        }

        if (municipality == null){
            Messenger.with(this).showMessage(getString(R.string.error_municipality_general));
            return;
        }

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        General general = new General();
        general.setName(inputName.getText().toString().trim());
        general.setLastName(inputLastName.getText().toString().trim());
        general.setMiddleName(inputMiddleName.getText().toString().trim());
        general.setBirthDate(dateFormat.parse(inputBirthDate.getText().toString()));
        general.setAge(Integer.parseInt(inputAge.getText().toString()));
        general.setGenre(((RadioButton) findViewById(radioGroupGenre.getCheckedRadioButtonId())).getText().toString());
        general.setLivingWith(((RadioButton) findViewById(radioGroupLivinWith.getCheckedRadioButtonId())).getText().toString());
        general.setCivilStatus(((RadioButton) findViewById(radioGroupCivilStatus.getCheckedRadioButtonId())).getText().toString());
        general.setPhone(inputPhone.getText().toString().trim());
        general.setEmail(inputEmail.getText().toString().trim());

        Address address = new Address();
        address.setStreet(inputStreet.getText().toString());
        address.setColony(inputColony.getText().toString());
        address.setZipCode(inputZipCode.getText().toString());
        address.setState(state);
        address.setMunicipality(municipality);
        general.setAddress(address);

        if (RealmController.with().save(general)){
            Messenger.with(this.getApplication()).showSuccessMessage();
            this.finish();
        } else {
            Messenger.with(this.getApplication()).showFailMessage();
        }

        showInterstitial();
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
            builder.setMessage(getString(R.string.help_general_activity))
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
            public void onAdLoaded() {}

            @Override
            public void onAdFailedToLoad(int errorCode) {}

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
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        if (pos != 0){
            if (adapterView.getId() == R.id.spinner_states_activity_general) {
                state = (String) spnStates.getSelectedItem();
                ArrayAdapter<CharSequence> adapterMunicipality = ArrayAdapter.createFromResource(this,
                        new ArrayStates().getArray(pos), android.R.layout.simple_spinner_item);
                adapterMunicipality.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnMunicipality.setAdapter(adapterMunicipality);
            }

            if (adapterView.getId() == R.id.spinner_municipality_activity_general){
                municipality = (String) spnMunicipality.getSelectedItem();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        //set birth date
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        inputBirthDate.setText(dateFormat.format(calendar.getTime()));

        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < calendar.get(Calendar.DAY_OF_YEAR)){
            age--;
        }
        inputAge.setText(age + "");
    }
}