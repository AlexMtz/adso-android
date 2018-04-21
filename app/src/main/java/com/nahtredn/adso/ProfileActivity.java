package com.nahtredn.adso;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.nahtredn.helpers.RadioButtons;
import com.nahtredn.utilities.Messenger;
import com.nahtredn.utilities.PreferencesProperties;
import com.nahtredn.utilities.RealmController;
import com.nahtredn.utilities.Validator;

public class ProfileActivity extends AppCompatActivity {

    private EditText inputUsername, inputCurrentPassword, inputNewPassword, inputNewPasswordRepeat;
    private TextInputLayout layoutCurrentPassword, layoutNewPassword, layoutNewPasswordRepeat;

    // private Spinner spinnerUpdateVacancies, spinnerDeleteVacancies;
    private RadioGroup backgroundDocument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null){
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
            setSupportActionBar(toolbar);
        }

        inputUsername = findViewById(R.id.input_username_profile);
        inputCurrentPassword = findViewById(R.id.input_current_password_profile);
        layoutCurrentPassword = findViewById(R.id.layout_input_current_password_profile);
        inputNewPassword = findViewById(R.id.input_new_password_profile);
        layoutNewPassword = findViewById(R.id.layout_input_new_password_profile);
        inputNewPasswordRepeat = findViewById(R.id.input_password_repeat_profile);
        layoutNewPasswordRepeat = findViewById(R.id.layout_input_password_repeat_profile);

        // spinnerUpdateVacancies = findViewById(R.id.spinner_update_vacancies_profile);
        // spinnerDeleteVacancies = findViewById(R.id.spinner_delete_vacancies_profile);

        backgroundDocument = findViewById(R.id.radio_group_background_color_profile);

        loadData();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public void loadData(){
        inputUsername.setText(RealmController.with(this).find(PreferencesProperties.USERNAME.toString()));
        // spinnerUpdateVacancies.setSelection(RealmController.with(this).findInt(PreferencesProperties.UPDATE_VACANCIES_OPTION.toString()));
        // spinnerDeleteVacancies.setSelection(RealmController.with(this).findInt(PreferencesProperties.DELETE_VACANCIES_OPTION.toString()));
        String colorOption = RealmController.with(this).find(PreferencesProperties.BACKGROUND_DOCUMENT.toString());
        if (colorOption != null && !colorOption.equals("")){
            RadioButtons radioButtons = new RadioButtons();
            RadioButton backgroundDocument = findViewById(radioButtons.getIdRadioButtonBy(colorOption));
            backgroundDocument.setChecked(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
            builder.setMessage(getString(R.string.help_profile_activity))
                    .setTitle(getString(R.string.help_title));
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void savePreferences(View view) {

        if (!inputCurrentPassword.getText().toString().trim().isEmpty()) {
            if (!Validator.with(this).validateText(inputNewPassword, layoutNewPassword)) {
                return;
            }

            if (!Validator.with(this).validateText(inputNewPasswordRepeat, layoutNewPasswordRepeat)) {
                return;
            }

            if (!RealmController.with(this).find(PreferencesProperties.PASSWORD.toString()).equalsIgnoreCase(inputCurrentPassword.getText().toString().trim())) {
                Validator.with(this).setErrorMessage(inputCurrentPassword, layoutCurrentPassword, "Contraseña incorrecta");
                return;
            }

            if (!inputNewPassword.getText().toString().trim().equalsIgnoreCase(inputNewPasswordRepeat.getText().toString().trim())) {
                Validator.with(this).setErrorMessage(inputNewPassword, layoutNewPassword, "La contraseña no coincide");
                Validator.with(this).setErrorMessage(inputNewPasswordRepeat, layoutNewPasswordRepeat, "La contraseña no coincide");
                return;
            }

            RealmController.with(this).save(PreferencesProperties.PASSWORD.toString(), inputNewPassword.getText().toString().trim());
            Messenger.with(this).showMessage("Contraseña actualizada");
        }

        if (backgroundDocument.getCheckedRadioButtonId() == -1) {
            Messenger.with(this).showMessage("Por favor selecciona un color de fondo");
            return;
        }

        // RealmController.with(this).save(PreferencesProperties.UPDATE_VACANCIES.toString(), (String) spinnerUpdateVacancies.getSelectedItem());
        // RealmController.with(this).save(PreferencesProperties.UPDATE_VACANCIES_OPTION.toString(), spinnerUpdateVacancies.getSelectedItemPosition());
        // RealmController.with(this).save(PreferencesProperties.DELETE_VACANCIES.toString(), (String) spinnerDeleteVacancies.getSelectedItem());
        // RealmController.with(this).save(PreferencesProperties.DELETE_VACANCIES_OPTION.toString(), spinnerDeleteVacancies.getSelectedItemPosition());
        RealmController.with(this).save(PreferencesProperties.BACKGROUND_DOCUMENT.toString(), ((RadioButton) findViewById(backgroundDocument.getCheckedRadioButtonId())).getText().toString());
        Messenger.with(this).showSuccessMessage();
        this.finish();
    }

}
