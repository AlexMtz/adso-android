package com.nahtredn.utilities;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.nahtredn.entities.Knowledge;
import com.nahtredn.entities.Reference;
import com.nahtredn.entities.WorkExperience;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Clase utilitaria para administrar las operaciones realizadas con la base de datos.
 */

public class RealmController {

    // Instancia de la clase
    private static RealmController instance;

    // Contexto para iniciar Realm
    private static Context context;

    // Constructor de la clase
    private RealmController(){
    }

    /**
     * Método que devuelve una instancia del Controller, si aún no existe, la crea.
     * @return una instancia del Controller.
     */
    public static RealmController with(){
        if (instance == null){
            instance = new RealmController();
        }
        return instance;
    }

    /**
     * Método que devuelve una instancia del Controller, si aún no existe, la crea.
     * @param application párametro que permite inicializar el Context del Controller
     * @return una instancia del Controller
     */
    public static RealmController with(Application application){
        if (instance == null){
            instance = new RealmController();
            context = application.getApplicationContext();
        }
        return instance;
    }

    /**
     * Método que devuelve una instancia del Controller, si aún no existe, la crea.
     * @param activity párametro que permite inicializar el Context del Controller
     * @return una instancia del Controller
     */
    public static RealmController with(Activity activity){
        if (instance == null){
            instance = new RealmController();
            context = activity.getApplicationContext();
        }
        return instance;
    }

    /**
     * Método que devuelve una instancia del Controller, si aún no existe, la crea.
     * @param fragment párametro que permite inicializar el Context del Controller
     * @return una instancia del Controller
     */
    public static RealmController with(Fragment fragment){
        if (instance == null){
            instance = new RealmController();
            context = fragment.getActivity().getApplicationContext();
        }
        return instance;
    }

    //******************************** FIND OPERATIONS ***********************************

    /**
     * Método que busca un registro de tipo Knowledge en la base de datos a partir de su identificador.
     * @param id corresponde al valor identificador del registro a buscar
     * @return  un objeto de tipo Knowledge
     */
    public Knowledge find(int id){
        if (id == -1){
            return null;
        }
        Knowledge result = null;
        Realm realm = io.realm.Realm.getDefaultInstance();
        try{
            result = realm.where(Knowledge.class).equalTo("id", id).findFirst();
        }finally {
            realm.close();
        }
        return result;
    }

    /**
     * Método que busca un registro de tipo WorkExperience en la base de datos a partir de su identificador.
     * @param id corresponde al valor identificador del registro a buscar
     * @return  un objeto de tipo WorkExperience
     */
    public WorkExperience find(WorkExperience workExperience ,int id){
        if (id == -1){
            return null;
        }
        WorkExperience result = null;
        Realm realm = io.realm.Realm.getDefaultInstance();
        try{
            result = realm.where(workExperience.getClass()).equalTo("id", id).findFirst();
        }finally {
            realm.close();
        }
        return result;
    }

    /**
     * Método que busca un registro de tipo Reference en la base de datos a partir de su identificador.
     * @param id corresponde al valor identificador del registro a buscar
     * @return  un objeto de tipo WorkExperience
     */
    public Reference find(Reference reference, int id){
        if (id == -1){
            return null;
        }
        Reference result = null;
        Realm realm = io.realm.Realm.getDefaultInstance();
        try{
            result = realm.where(reference.getClass()).equalTo("id", id).findFirst();
        }finally {
            realm.close();
        }
        return result;
    }

    // ********************* SAVE OPERATIONS ****************************

    /**
     * Método que guarda un objeto Knowledge en la base de datos.
     * @param tmpKnowledge corresponde al objeto que se va a guardar.
     * @return un valor booleano que indica si se pudo guardar o no el objeto.
     */
    public boolean save(Knowledge tmpKnowledge) {
        boolean result = true;
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            Number currentIdNum = realm.where(tmpKnowledge.getClass()).max("id");
            if (currentIdNum == null) {
                tmpKnowledge.setId(1);
            } else {
                int nextId = currentIdNum.intValue() + 1;
                tmpKnowledge.setId(nextId);
            }
            realm.copyToRealmOrUpdate(tmpKnowledge);
            realm.commitTransaction();
        } catch (Exception e) {
            realm.cancelTransaction();
            result = false;
        } finally {
            realm.close();
        }
        return result;
    }

    /**
     * Método que guarda un objeto WorkExperience en la base de datos.
     * @param workExperience corresponde al objeto que se va a guardar.
     * @return un valor booleano que indica si se pudo guardar o no el objeto.
     */
    public boolean save(WorkExperience workExperience) {
        boolean result = true;
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            if (workExperience.getId() == -1) {
                Number currentIdNum = realm.where(workExperience.getClass()).max("id");
                if (currentIdNum == null) {
                    workExperience.setId(1);
                } else {
                    int nextId = currentIdNum.intValue() + 1;
                    workExperience.setId(nextId);
                }
            }
            realm.copyToRealmOrUpdate(workExperience);
            realm.commitTransaction();
        } catch (Exception e) {
            realm.cancelTransaction();
            result = false;
        } finally {
            realm.close();
        }
        return result;
    }

    /**
     * Método que guarda un objeto Reference en la base de datos.
     * @param reference corresponde al objeto que se va a guardar.
     * @return un valor booleano que indica si se pudo guardar o no el objeto.
     */
    public boolean save(Reference reference) {
        boolean result = true;
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            if (reference.getId() == -1) {
                Number currentIdNum = realm.where(reference.getClass()).max("id");
                if (currentIdNum == null) {
                    reference.setId(1);
                } else {
                    int nextId = currentIdNum.intValue() + 1;
                    reference.setId(nextId);
                }
            }
            realm.copyToRealmOrUpdate(reference);
            realm.commitTransaction();
        } catch (Exception e) {
            realm.cancelTransaction();
            result = false;
        } finally {
            realm.close();
        }
        return result;
    }

    // ********************* DELETE OPERATIONS ****************************
    /**
     * Método que permite eliminar un objeto de tipo Knowledge de la base de datos.
     * @param knowledge corresponde a la tabla en la cual el objeto esta guardado
     * @param id corresponde al identificador del objeto a eliminar
     * @return un valor booleano que indica si se pudo eliminar o no
     */
    public boolean delete(Knowledge knowledge, int id){
        Realm realm = Realm.getDefaultInstance();
        Knowledge result = realm.where(knowledge.getClass()).equalTo("id", id).findFirst();
        realm.beginTransaction();
        try {
            result.deleteFromRealm();
            realm.commitTransaction();
            return true;
        } catch (NullPointerException npe){
            return false;
        }
    }

    /**
     * Método que permite eliminar un objeto de tipo WorkExperience de la base de datos.
     * @param workExperience corresponde a la tabla en la cual el objeto esta guardado
     * @param id corresponde al identificador del objeto a eliminar
     * @return un valor booleano que indica si se pudo eliminar o no
     */
    public boolean delete(WorkExperience workExperience, int id){
        Realm realm = Realm.getDefaultInstance();
        WorkExperience result = realm.where(workExperience.getClass()).equalTo("id", id).findFirst();
        realm.beginTransaction();
        try {
            result.deleteFromRealm();
            realm.commitTransaction();
            return true;
        } catch (NullPointerException npe){
            return false;
        }
    }

    /**
     * Método que permite eliminar un objeto de tipo Reference de la base de datos.
     * @param reference corresponde a la tabla en la cual el objeto esta guardado
     * @param id corresponde al identificador del objeto a eliminar
     * @return un valor booleano que indica si se pudo eliminar o no
     */
    public boolean delete(Reference reference, int id){
        Realm realm = Realm.getDefaultInstance();
        Reference result = realm.where(reference.getClass()).equalTo("id", id).findFirst();
        realm.beginTransaction();
        try {
            result.deleteFromRealm();
            realm.commitTransaction();
            return true;
        } catch (NullPointerException npe){
            return false;
        }
    }

    //*********************** FIND ALL OPERATIONS *******************************

    /**
     * Método que permite buscar todos los objetos de tipo Reference guardados.
     * @return una lista con los objetos encontrados
     */
    public List<Reference> findAllReferences(){
        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<Reference> query = realm.where(Reference.class);
        RealmResults<Reference> result = query.findAll();
        return result;
    }
}
