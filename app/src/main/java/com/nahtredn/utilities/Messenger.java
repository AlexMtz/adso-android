package com.nahtredn.utilities;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.nahtredn.adso.R;

/**
 * Clase utilitaria para mostrar mensajes.
 * Actualmente solo los muestra a través de un Toast.
 */

public class Messenger {
    // Instancia de la clase
    private static Messenger instance;
    // Contexto en el que se mostrará el Toast con el mensaje
    private static Context context;

    /**
     * Método constructor de la clase
     * @param contexto corresponde al contexto en el cual se mostrarán los mensajes.
     */
    public Messenger(Context contexto){
        context = contexto;
    }

    /**
     * Método que devuelve una instancia del Mensajero, si aún no existe, la crea.
     * @param application corresponde a la aplicación que lo manda ejecutar
     * @return una instancia del Mensajero
     */
    public static Messenger with(Application application){
        if (instance == null){
            instance = new Messenger(application.getApplicationContext());
        }
        return instance;
    }

    /**
     * Método que devuelve una instancia del Mensajero, si aún no existe, la crea.
     * @param activity corresponde al activity que lo manda ejecuutar.
     * @return una instancia del Mensajero
     */
    public static Messenger with(Activity activity){
        if (instance == null){
            instance = new Messenger(activity.getApplication().getApplicationContext());
        }
        return instance;
    }

    /**
     * Método que muestra un mensaje a partir de un String.
     * @param message corresponde al mensaje que se desea mostrar
     */
    public void showMessage(String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Método que muestra un mensaje a partir de un recurso ubicado en R.string
     * @param resource corresponde al id del recurso que se desea mostrar;
     */
    public void showMessage(int resource){
        Toast.makeText(context, context.getString(resource), Toast.LENGTH_LONG).show();
    }

    /**
     * Método que muestra un mensaje de éxito al guardar cambios.
     */
    public void showSuccessMessage(){
        Toast.makeText(context, context.getString(R.string.success_save), Toast.LENGTH_LONG).show();
    }

    /**
     * Método que muestra un mensaje de error al guardar cambios.
     */
    public void showFailMessage(){
        Toast.makeText(context, context.getString(R.string.error_save), Toast.LENGTH_LONG).show();
    }
}
