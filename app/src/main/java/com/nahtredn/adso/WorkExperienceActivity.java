package com.nahtredn.adso;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.nahtredn.entities.WorkExperience;
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
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;

/**
 * Clase que maneja el ciclo de vida del Activity WorkExperienceActivity, extiende de AppCompactActivity e implementa los métodos
 * necesarios para manejar los eventos de un Spinner y un DatePicker.
 */
public class WorkExperienceActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    // Variables que representan los inputs del activity
    private EditText inputEnterprise, inputJobTitle, inputStartDate, inputEndDate;

    // Variables que representan los layouts o etiquetas de los inputs, utilizadas para inficar errores.
    private TextInputLayout layoutEnterprise, layoutJobTitle, layoutStartDate, layoutEndDate;

    // Variable que maneja el spinner de selección del Activity
    private Spinner spinnerTypeExperience;

    // Variable que permite indicar si un objeto es nuevo o no
    private int id = -1;

    // Variable que permite cargar un anuncio publicitario de tipo Interstitial
    private InterstitialAd mInterstitialAd;

    // Variable que permite obtener la fecha actual
    private Calendar calendar;

    // Variable que permite identificar desde que boton, en cuestión de fechas, se hizo clic.
    private boolean isStartDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_experience);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        mInterstitialAd = newInterstitialAd();
        loadInterstitial();

        AdView mAdView = findViewById(R.id.adViewWorkExperience);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        inputEnterprise = findViewById(R.id.input_institute_or_enterprise);
        inputJobTitle = findViewById(R.id.input_job_title);
        inputStartDate = findViewById(R.id.input_date_start_work_experience);
        inputEndDate = findViewById(R.id.input_date_end_work_experience);

        layoutEnterprise = findViewById(R.id.layout_institute_or_enterprise);
        layoutJobTitle = findViewById(R.id.layout_job_title);
        layoutStartDate = findViewById(R.id.layout_input_start_date_work_experience);
        layoutEndDate = findViewById(R.id.layout_input_end_date_work_experience);

        spinnerTypeExperience = findViewById(R.id.spinner_experience_option);

        // Se identifica si se creará un nuevo objeto o se modificará otro
        this.id = getIntent().getIntExtra("work_experience_id",-1);
        if (this.id != -1){
            loadData(RealmController.with().find(new WorkExperience(), id));
        }

        // Se oculta el teclado al iniciar el Activity
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    /**
     * Método que permite cargar los datos de un objeto WorkExperience existente para posteriormente
     * actualizarlo.
     * @param workExperience corresponde al objeto del cual se cargará la información
     */
    private void loadData(WorkExperience workExperience){
        inputEnterprise.setText(workExperience.getInstitute());
        inputJobTitle.setText(workExperience.getJobTitle());
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        inputStartDate.setText(dateFormat.format(workExperience.getStartJob()));
        inputEndDate.setText(dateFormat.format(workExperience.getEndJob()));
        spinnerTypeExperience.setSelection(workExperience.getPosTypeExperience());
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
                if (RealmController.with().delete(new WorkExperience(), this.id)){
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

    /**
     * Método que crea un nuevo anuncio publicitario de tipo Interstitial
     * @return el anuncio Interstitial
     */
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
                reloadInterstitial();
            }
        });
        return interstitialAd;
    }

    /**
     * Método que mustra el anuncio previamente cargado
     */
    private void showInterstitial() {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            reloadInterstitial();
        }
    }

    /**
     * Método que realiza la solicitud del anuncio a Google y lo carga en el objeto del anuncio
     * previamente cargado.
     */
    private void loadInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        mInterstitialAd.loadAd(adRequest);
    }

    /**
     * Método que permite recargar el objeto del anuncio con un nuevo anuncio.
     */
    private void reloadInterstitial() {
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
    }

    /**
     * Método que permite mostrar el calendario al hacer clic en el botón de la fecha
     * @param view corresponde al botón del cual se generó el clic
     */
    public void onClicDateWork(View view){
        calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(WorkExperienceActivity.this,
                this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
        isStartDate = view.getId() == R.id.btn_date_start_work_experience;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());

        if (isStartDate){
            inputStartDate.setText(dateFormat.format(calendar.getTime()));
        } else {
            inputEndDate.setText(dateFormat.format(calendar.getTime()));
        }
    }

    /**
     * Método que permite validar las entradas del usuario y posteriormente guardar los datos en la
     * base de datos
     * @param view corresponde al objeto del cual se generó el clic
     * @throws ParseException excepción que se generá al tratar de convertir una fecha inválida
     */
    public void onClicSaveWorkExperience(View view) throws ParseException {

        if (spinnerTypeExperience.getSelectedItemPosition() == 0){
            Messenger.with(this).showMessage(R.string.error_type_experience);
            return;
        }

        if (!Validator.with(this).validateText(inputEnterprise, layoutEnterprise)){
            return;
        }

        if (!Validator.with(this).validateText(inputJobTitle, layoutJobTitle)){
            return;
        }

        if (!Validator.with(this).validateText(inputStartDate, layoutStartDate)) {
            return;
        }

        if (!Validator.with(this).validateText(inputEndDate, layoutEndDate)){
            return;
        }

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());

        WorkExperience workExperience = new WorkExperience();
        workExperience.setId(this.id);
        workExperience.setTypeExperience((String) spinnerTypeExperience.getSelectedItem());
        workExperience.setPosTypeExperience(spinnerTypeExperience.getSelectedItemPosition());
        workExperience.setJobTitle(inputJobTitle.getText().toString().trim());
        workExperience.setInstitute(inputEnterprise.getText().toString().trim());
        workExperience.setStartJob(dateFormat.parse(inputStartDate.getText().toString()));
        workExperience.setEndJob(dateFormat.parse(inputEndDate.getText().toString()));

        if (RealmController.with().save(workExperience)){
            Messenger.with(this).showSuccessMessage();
            this.finish();
        } else {
            Messenger.with(this).showFailMessage();
        }

        showInterstitial();
    }
}
