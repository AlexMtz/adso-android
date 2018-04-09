package com.nahtredn.adso;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.nahtredn.entities.Reference;
import com.nahtredn.helpers.ArrayStates;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ReferenceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private EditText inputName, inputJobTitle, inputPhone, inputNumber;

    private TextInputLayout layoutName, layoutJobTitle, layoutPhone, layoutNumber;

    private Spinner spinnerTypeMeet, spinnerState, spinnerMunicipality;

    private InterstitialAd mInterstitialAd;

    private int id = -1, posMunicipality = 0, posState = 0;
    private String state, municipality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        inputName = findViewById(R.id.input_name_reference);
        inputJobTitle = findViewById(R.id.input_job_title_reference);
        inputPhone = findViewById(R.id.input_phone_reference);
        inputNumber = findViewById(R.id.input_time_meeting);

        layoutName = findViewById(R.id.layout_input_name_reference);
        layoutJobTitle = findViewById(R.id.layout_input_job_title_reference);
        layoutPhone = findViewById(R.id.layout_input_phone_reference);
        layoutNumber = findViewById(R.id.layout_input_time_meeting);

        spinnerTypeMeet = findViewById(R.id.spinner_type_time_meeting);
        spinnerState = findViewById(R.id.spinner_states_reference);
        spinnerState.setOnItemSelectedListener(this);
        spinnerMunicipality = findViewById(R.id.spinner_municipality_reference);
        spinnerMunicipality.setOnItemSelectedListener(this);

        mInterstitialAd = newInterstitialAd();
        loadInterstitial();

        AdView mAdView = findViewById(R.id.adViewReference);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Se identifica si se creará un nuevo objeto o se modificará otro
        this.id = getIntent().getIntExtra("reference_id",-1);
        if (this.id != -1){
            loadData(RealmController.with().find(new Reference(), id));
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public void loadData(Reference reference){
        inputName.setText(reference.getName());
        inputJobTitle.setText(reference.getJobTitle());
        inputPhone.setText(reference.getPhone());
        inputNumber.setText(reference.getTimeMeet());
        state = reference.getState();
        posState = reference.getPosState();
        municipality = reference.getMunicipality();
        posMunicipality = reference.getPosMunicipality();
        spinnerTypeMeet.setSelection(reference.getPosTypeTimeMeet());
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
            if (this.id != -1){
                if (RealmController.with().delete(new Reference(), this.id)){
                    Messenger.with(this).showMessage(R.string.success_delete);
                    this.finish();
                } else {
                    Messenger.with(this).showMessage(R.string.error_delete);
                }
            } else {
                this.finish();
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

    public void onClicSaveReference(View view){
        if (!Validator.with(this).validateText(inputName, layoutName)){
            return;
        }

        if (!Validator.with(this).validateText(inputJobTitle, layoutJobTitle)){
            return;
        }

        if (!Validator.with(this).validateText(inputNumber, layoutNumber)){
            return;
        }

        if (spinnerTypeMeet.getSelectedItemPosition() == 0){
            Messenger.with(this).showMessage(R.string.error_type_meet);
            return;
        }

        if (!Validator.with(this).validateText(inputPhone, layoutPhone)){
            return;
        }

        if (posState == 0){
            Messenger.with(this).showMessage(R.string.error_state_general);
            return;
        }

        if (posMunicipality == 0){
            Messenger.with(this).showMessage(R.string.error_municipality_general);
            return;
        }

        Reference reference = new Reference();
        reference.setId(this.id);
        reference.setName(inputName.getText().toString().trim());
        reference.setJobTitle(inputJobTitle.getText().toString().trim());
        reference.setTimeMeet(inputNumber.getText().toString().trim());
        reference.setTypeTimeMeet((String) spinnerTypeMeet.getSelectedItem());
        reference.setPosTypeTimeMeet(spinnerTypeMeet.getSelectedItemPosition());
        reference.setState(state);
        reference.setPosState(posState);
        reference.setMunicipality(municipality);
        reference.setPosMunicipality(posMunicipality);
        reference.setPhone(inputPhone.getText().toString().trim());

        if (RealmController.with().save(reference)){
            Messenger.with(this).showSuccessMessage();
            this.finish();
        } else {
            Messenger.with(this).showFailMessage();
        }

        showInterstitial();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        if (pos != 0){
            if (adapterView.getId() == R.id.spinner_states_reference) {
                state = (String) adapterView.getItemAtPosition(pos);
                posState = pos;
                ArrayAdapter<CharSequence> adapterMunicipality = ArrayAdapter.createFromResource(this,
                        new ArrayStates().getArray(pos), android.R.layout.simple_spinner_item);
                adapterMunicipality.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerMunicipality.setAdapter(adapterMunicipality);
            }

            if (adapterView.getId() == R.id.spinner_municipality_reference){
                municipality = (String) adapterView.getItemAtPosition(pos);
                posMunicipality = pos;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
