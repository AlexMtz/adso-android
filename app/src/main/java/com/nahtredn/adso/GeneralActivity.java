package com.nahtredn.adso;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.nahtredn.entities.Address;
import com.nahtredn.entities.General;
import com.nahtredn.helpers.ArrayStates;
import com.nahtredn.helpers.RadioButtons;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class GeneralActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    private EditText inputName, inputLastName, inputMiddleName, inputBirthDate, inputAge, inputPhone,
            inputEmail, inputStreet, inputColony, inputZipCode;
    private TextInputLayout layoutInputName, layoutInputLastName, layoutInputMiddleName,
            layoutInputBirthDate, layoutInputAge, layoutInputPhone, layoutInputEmail,
            layoutInputStreet, layoutInputColony, layoutInputZipCode;

    private RadioGroup radioGroupGenre, radioGroupLivinWith, radioGroupCivilStatus;

    private InterstitialAd mInterstitialAd;
    private AdView mAdView;
    private Spinner spnStates, spnMunicipality;

    private Calendar calendar = Calendar.getInstance();

    private General tmpGeneral;
    private General general;
    private Address address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        Realm.init(this);

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
        layoutInputMiddleName = findViewById(R.id.layout_input_middle_name_general);
        layoutInputBirthDate = findViewById(R.id.layout_input_birth_date_general);
        layoutInputAge = findViewById(R.id.layout_input_age_general);
        layoutInputPhone = findViewById(R.id.layout_input_phone_general);
        layoutInputEmail = findViewById(R.id.layout_input_email_general);
        layoutInputStreet = findViewById(R.id.layout_input_street_general);
        layoutInputColony = findViewById(R.id.layout_input_colony_general);
        layoutInputZipCode = findViewById(R.id.layout_input_zip_code_general);

        radioGroupGenre = findViewById(R.id.radio_group_genre_general);
        radioGroupLivinWith = findViewById(R.id.radio_group_living_with_general);
        radioGroupCivilStatus = findViewById(R.id.radio_group_civil_status_general);

        reloadInterstitial();

        mAdView = findViewById(R.id.adViewGeneral);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        spnStates = findViewById(R.id.spinner_states_activity_general);
        spnStates.setOnItemSelectedListener(this);
        spnMunicipality = findViewById(R.id.spinner_municipality_activity_general);
        spnMunicipality.setOnItemSelectedListener(this);

        loadGeneral();
        general = new General();
        address = new Address();
        if (tmpGeneral != null){
            loadData(tmpGeneral);
            general.setId(tmpGeneral.getId());
            general.setBirthDate(tmpGeneral.getBirthDate());
            general.setGenre(tmpGeneral.getGenre());
            general.setLivingWith(tmpGeneral.getLivingWith());
            general.setCivilStatus(tmpGeneral.getCivilStatus());
            address.setState(tmpGeneral.getAddress().getState());
            address.setMunicipality(tmpGeneral.getAddress().getMunicipality());
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

    public void onClicSaveGeneralActivity(View view){

        if (!validateText(inputName, layoutInputName)) {
            return;
        }

        if (!validateText(inputLastName, layoutInputLastName)) {
            return;
        }

        if (!validateText(inputBirthDate, layoutInputBirthDate)) {
            return;
        }

        if (!validateText(inputPhone, layoutInputPhone)) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (radioGroupGenre.getCheckedRadioButtonId() == -1){
            showMessage(getString(R.string.error_genre_field));
            return;
        }

        if (radioGroupLivinWith.getCheckedRadioButtonId() == -1){
            showMessage(getString(R.string.error_livin_with));
            return;
        }

        if (radioGroupCivilStatus.getCheckedRadioButtonId() == -1){
            showMessage(getString(R.string.error_civil_status));
            return;
        }

        if (address.getState() == null){
            showMessage(getString(R.string.error_state_general));
            return;
        }

        if (address.getMunicipality() == null){
            showMessage(getString(R.string.error_municipality_general));
            return;
        }

        general.setName(inputName.getText().toString());
        general.setLastName(inputLastName.getText().toString());
        general.setMiddleName(inputMiddleName.getText().toString());
        general.setPhone(inputPhone.getText().toString());
        general.setEmail(inputEmail.getText().toString());
        address.setStreet(inputStreet.getText().toString());
        address.setColony(inputColony.getText().toString());
        address.setZipCode(inputZipCode.getText().toString());
        general.setAddress(address);
        saveGeneral(general);

        showInterstitial();
    }

    private void saveGeneral(final General tmpGeneral) {
        Realm.init(getApplicationContext());
        final Realm realm = Realm.getDefaultInstance();

        try {
            realm.beginTransaction();
            Number currentIdNum = realm.where(General.class).max("id");
            if (currentIdNum == null) {
                int nextId = 1;
                tmpGeneral.setId(nextId);
            }
            realm.copyToRealmOrUpdate(tmpGeneral);
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

    private void loadGeneral(){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                tmpGeneral = realm.where(General.class).findFirst();;
            }
        });
    }

    public void onClicRadioButtonGenreGeneral(View view){
        RadioButton radioButton = (RadioButton) view;
        general.setGenre(radioButton.getText().toString());
    }

    public void onClicRadioButtonLivingWithGeneral(View view){
        RadioButton radioButton = (RadioButton) view;
        general.setLivingWith(radioButton.getText().toString());
    }

    public void onClicRadioButtonCivilStatusGeneral(View view){
        RadioButton radioButton = (RadioButton) view;
        general.setCivilStatus(radioButton.getText().toString());
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
        if (pos == 0){
            return;
        }
        if (adapterView.getId() == R.id.spinner_states_activity_general) {
            String state = (String) adapterView.getItemAtPosition(pos);
            ArrayAdapter<CharSequence> adapterMunicipality = ArrayAdapter.createFromResource(this,
                    new ArrayStates().getArray(pos), android.R.layout.simple_spinner_item);
            adapterMunicipality.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnMunicipality.setAdapter(adapterMunicipality);
            address.setState(state);
        }

        if (adapterView.getId() == R.id.spinner_municipality_activity_general){
            String municipality = (String) adapterView.getItemAtPosition(pos);
            address.setMunicipality(municipality);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            layoutInputEmail.setError(getString(R.string.error_email_activity_general));
            requestFocus(inputEmail);
            return false;
        } else {
            layoutInputEmail.setErrorEnabled(false);
        }
        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        //set birth date
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        inputBirthDate.setText(dateFormat.format(calendar.getTime()));
        general.setBirthDate(calendar.getTime());

        //set age
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < calendar.get(Calendar.DAY_OF_YEAR)){
            age--;
        }
        inputAge.setText(age + "");
        general.setAge(age);
    }
}