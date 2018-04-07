package com.nahtredn.utilities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.nahtredn.adso.R;

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

    /**
     * Método que realiza un Focus en un elemento específico.
     * @param view corresponde al elemento sobre el cual se hará el enfoque.
     */
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            act.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
