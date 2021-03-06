package com.nahtredn.utilities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.nahtredn.adso.R;

import java.util.regex.Pattern;

/**
 * Clase utilitaria para validar texto.
 */

public class Validator {

    // instancia de la clase
    private static Validator instance;
    // Contexto en el que se realizará la validación
    private static Context context;
    // Activity que manda invocar el validador
    private static Activity act;

    /**
     * Método constructor de la clase.
     * @param activity corresponde a la Actividad que invoca el validador
     */
    public Validator(Activity activity){
        context = activity.getApplicationContext();
        act = activity;
    }

    /**
     * Método que devuelve una instancia del Validador, si aún no existe, la crea.
     * @param fragment corresponde a un fragmento que manda invocar el validador.
     * @return una instancia del Validador.
     */
    public static Validator with(Fragment fragment){
        if (instance == null){
            instance = new Validator(fragment.getActivity());
        }
        return instance;
    }

    /**
     * Método que devuelve una instancia del Validador, si aún no existe, la crea.
     * @param activity corresponde a la Actividad que manda invocar el validador.
     * @return una instancia del Validador.
     */
    public static Validator with(Activity activity){
        if (instance == null){
            instance = new Validator(activity);
        }
        return instance;
    }

    /**
     * Método que valida a un EditText para que éste no se encuentre vacio. Si se encuentra vacio
     * agregará un mensaje de error al Layout que lo acompaña.
     * @param editText corresponde al EditText que se validará
     * @param textInputLayout corresponde al Layout en el cual se agregará el mensaje de error en caso de
     *                        ser necesario.
     * @return un valor booleano indicando si el EditText está vacio o no.
     */
    public boolean validateText(EditText editText, TextInputLayout textInputLayout) {
        if (editText.getText().toString().trim().isEmpty()) {
            textInputLayout.setError(context.getString(R.string.error_field_required));
            requestFocus(editText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public boolean validateCURP(EditText inputCURP, TextInputLayout layoutInputCURP){
        String curp = inputCURP.getText().toString().trim();

        if (curp.isEmpty() || !isValidCURP(curp)) {
            layoutInputCURP.setError(context.getString(R.string.error_curp_documentation));
            requestFocus(inputCURP);
            return false;
        } else {
            layoutInputCURP.setErrorEnabled(false);
        }
        return true;
    }

    private boolean isValidCURP(String curp) {
        String regex =
                "[A-Z]{1}[AEIOU]{1}[A-Z]{2}[0-9]{2}" +
                        "(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])" +
                        "[HM]{1}" +
                        "(AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)" +
                        "[B-DF-HJ-NP-TV-Z]{3}" +
                        "[0-9A-Z]{1}[0-9]{1}$";
        Pattern patron = Pattern.compile(regex);
        return !TextUtils.isEmpty(curp) && patron.matcher(curp).matches();
    }

    /**
     * Método que realiza un Focus en un elemento específico.
     * @param view corresponde al elemento sobre el cual se hará el enfoque.
     */
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            act.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public boolean validateEmail(EditText inputEmail, TextInputLayout layoutInputEmail) {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            layoutInputEmail.setError(context.getString(R.string.error_email_activity_general));
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

    public void setErrorMessage(EditText editText, TextInputLayout textInputLayout, String error){
        textInputLayout.setError(error);
        requestFocus(editText);
    }

    public boolean isValidUsername(EditText editText, TextInputLayout textInputLayout){
        if (editText.getText().toString().contains(" ")){
            setErrorMessage(editText, textInputLayout, "Debe ser sin espacios");
            return false;
        } else {
            return true;
        }
    }

    public boolean validateText(TextView editText){
        return !editText.getText().toString().trim().isEmpty();
    }
}
