package com.nahtredn.adso;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;

import com.nahtredn.utilities.Messenger;
import com.nahtredn.utilities.PreferencesProperties;
import com.nahtredn.utilities.RealmController;
import com.nahtredn.utilities.Validator;

public class RegistryActivity extends AppCompatActivity {

    private EditText inputUsername, inputPassword, inputRepeatPassword;
    private TextInputLayout layoutUsername, layoutPassword, layoutRepeatPassword;
    private CheckBox conditions;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);

        getSupportActionBar().setTitle("Crear cuenta");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        inputUsername = findViewById(R.id.input_username_registry);
        layoutUsername = findViewById(R.id.layout_input_username_registry);
        inputPassword = findViewById(R.id.input_password_registry);
        layoutPassword = findViewById(R.id.layout_input_password_registry);
        inputRepeatPassword = findViewById(R.id.input_password_repeat_registry);
        layoutRepeatPassword = findViewById(R.id.layout_input_password_repeat_registry);
        conditions = findViewById(R.id.checkbox_conditions);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);
            RegistryActivity.this.finish();
            return true;
        }

        return true;
    }

    public void registry(View view){

        if (!Validator.with(this).validateText(inputUsername, layoutUsername)){
            return;
        }

        if (!Validator.with(this).validateText(inputPassword, layoutPassword)){
            return;
        }

        if (!Validator.with(this).validateText(inputRepeatPassword, layoutRepeatPassword)){
            return;
        }

        if (!Validator.with(this).isValidUsername(inputUsername, layoutUsername)){
            return;
        }

        if (!inputPassword.getText().toString().trim().equals(inputRepeatPassword.getText().toString().trim())){
            Messenger.with(this).showMessage("Las contraseñas no coinciden");
            return;
        }

        if (!conditions.isChecked()){
            Messenger.with(this).showMessage("Es necesario aceptar los términos y condiciones para utilizar la app");
            return;
        }

        new Registry().execute();
    }

    class Registry extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegistryActivity.this, ProgressDialog.THEME_HOLO_DARK);
            pDialog.setMessage("Creando cuenta...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... f_url) {
            RealmController.with(RegistryActivity.this).save(PreferencesProperties.USERNAME.toString(), inputUsername.getText().toString().trim());
            RealmController.with(RegistryActivity.this).save(PreferencesProperties.PASSWORD.toString(), inputPassword.getText().toString().trim());
            RealmController.with(RegistryActivity.this).save(PreferencesProperties.IS_LOGGED.toString(), true);
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            Messenger.with(RegistryActivity.this).showMessage("Cuenta creada exitosamente");
            pDialog.dismiss();
            Intent intent = new Intent(RegistryActivity.this, MainActivity.class);
            RegistryActivity.this.startActivity(intent);
            RegistryActivity.this.finish();
        }
    }
}
