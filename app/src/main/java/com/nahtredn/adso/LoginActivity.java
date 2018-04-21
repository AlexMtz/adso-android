package com.nahtredn.adso;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.nahtredn.utilities.Messenger;
import com.nahtredn.utilities.PreferencesProperties;
import com.nahtredn.utilities.RealmController;
import com.nahtredn.utilities.Validator;

public class LoginActivity extends AppCompatActivity {

    EditText inputUsername, inputPassword;
    TextInputLayout layoutUsername, layoutPassword;
    String username, password;
    boolean canLogin;
    private ProgressDialog pDialog;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        inputUsername = findViewById(R.id.input_username_login);
        layoutUsername = findViewById(R.id.layout_input_username_login);
        inputPassword = findViewById(R.id.input_password_login);
        layoutPassword = findViewById(R.id.layout_input_password_login);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public void singIn(View view){

        if (view.getId() == R.id.btn_sing_in){
            if (!Validator.with(this).validateText(inputUsername, layoutUsername)){
                return;
            }

            if (!Validator.with(this).validateText(inputPassword, layoutPassword)){
                return;
            }

            username = inputUsername.getText().toString().trim();
            password = inputPassword.getText().toString().trim();

            new LogIn().execute();
        }

        if (view.getId() == R.id.link_signup){
            Intent intent = new Intent(getApplicationContext(), RegistryActivity.class);
            startActivity(intent);
        }

        if (view.getId() == R.id.link_restart){

        }
    }

    class LogIn extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this, ProgressDialog.THEME_HOLO_DARK);
            pDialog.setMessage("Iniciando sesión...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... f_url) {
            canLogin = true;

            if (RealmController.with(LoginActivity.this).find(PreferencesProperties.USERNAME.toString()) == null) {
                canLogin = false;
                message = "Aún no se ha creado una cuenta";
                return null;
            }

            if (!RealmController.with(LoginActivity.this).find(PreferencesProperties.USERNAME.toString()).equals(inputUsername.getText().toString().trim())) {
                canLogin = false;
                message = "Usuario incorrecto";
            }

            if (!RealmController.with(LoginActivity.this).find(PreferencesProperties.PASSWORD.toString()).equals(inputPassword.getText().toString().trim())){
                canLogin = false;
                message = "Contraseña incorrecta";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            if (!canLogin) {
                Messenger.with(LoginActivity.this).showMessage(message);
                pDialog.dismiss();
                return;
            }

            RealmController.with(LoginActivity.this).save(PreferencesProperties.IS_LOGGED.toString(), true);
            pDialog.dismiss();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplication().startActivity(intent);
            LoginActivity.this.finish();
        }
    }
}
