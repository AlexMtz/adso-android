package com.nahtredn.utilities;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;

import com.nahtredn.entities.CurrentStudy;
import com.nahtredn.entities.Documentation;
import com.nahtredn.entities.General;
import com.nahtredn.entities.Knowledge;
import com.nahtredn.entities.Reference;
import com.nahtredn.entities.StudyDone;
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
     * @param contexto párametro que permite inicializar el Context del Controller
     * @return una instancia del Controller
     */
    public static RealmController with(Context contexto){
        if (instance == null){
            instance = new RealmController();
            context = contexto;
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
     * @param knowledge corresponde a la clase en la cual se reliazará la búsqueda
     * @param id corresponde al valor identificador del registro a buscar
     * @return  un objeto de tipo Knowledge
     */
    public Knowledge find(Knowledge knowledge, int id){
        Knowledge result = null;
        Realm realm = Realm.getDefaultInstance();
        try{
            result = realm.where(knowledge.getClass()).equalTo("id", id).findFirst();
        }finally {
            realm.close();
        }
        return result;
    }

    /**
     * Método que busca un registro de tipo WorkExperience en la base de datos a partir de su identificador.
     * @param workExperience corresponde a la clase en la cual se reliazará la búsqueda
     * @param id corresponde al valor identificador del registro a buscar
     * @return  un objeto de tipo WorkExperience
     */
    public WorkExperience find(WorkExperience workExperience ,int id){
        WorkExperience result = null;
        Realm realm = Realm.getDefaultInstance();
        try{
            result = realm.where(workExperience.getClass()).equalTo("id", id).findFirst();
        }finally {
            realm.close();
        }
        return result;
    }

    /**
     * Método que busca un registro de tipo Reference en la base de datos a partir de su identificador.
     * @param reference corresponde a la clase en la cual se reliazará la búsqueda
     * @param id corresponde al valor identificador del registro a buscar
     * @return  un objeto de tipo Reference
     */
    public Reference find(Reference reference, int id){
        Reference result = null;
        Realm realm = Realm.getDefaultInstance();
        try{
            result = realm.where(reference.getClass()).equalTo("id", id).findFirst();
        }finally {
            realm.close();
        }
        return result;
    }

    /**
     * Método que busca un registro de tipo CurrentStudy en la base de datos a partir de su identificador.
     * @param currentStudy corresponde a la clase en la cual se reliazará la búsqueda
     * @param id corresponde al valor identificador del registro a buscar
     * @return  un objeto de tipo StudyDone
     */
    public CurrentStudy find(CurrentStudy currentStudy, int id){
        CurrentStudy result = null;
        Realm realm = Realm.getDefaultInstance();
        try{
            result = realm.where(currentStudy.getClass()).equalTo("id", id).findFirst();
        }finally {
            realm.close();
        }
        return result;
    }

    /**
     * Método que busca un registro de tipo StudyDone en la base de datos a partir de su identificador.
     * @param studyDone corresponde a la clase en la cual se reliazará la búsqueda
     * @param id corresponde al valor identificador del registro a buscar
     * @return  un objeto de tipo CurrentStudy
     */
    public StudyDone find(StudyDone studyDone, int id){
        StudyDone result = null;
        Realm realm = Realm.getDefaultInstance();
        try{
            result = realm.where(studyDone.getClass()).equalTo("id", id).findFirst();
        }finally {
            realm.close();
        }
        return result;
    }

    /**
     * Método que busca un registro de tipo Documentation en la base de datos a partir de su identificador.
     * @param documentation corresponde a la clase en la cual se reliazará la búsqueda
     * @return un objeto de tipo Documentation
     */
    public Documentation find(Documentation documentation){
        final Documentation[] result = {null};
        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                result[0] = realm.where(Documentation.class).findFirst();;
            }
        });
        return result[0];
    }

    /**
     * Método que busca un registro de tipo Documentation en la base de datos a partir de su identificador.
     * @param general corresponde a la clase en la cual se reliazará la búsqueda
     * @return un objeto de tipo General
     */
    public General find(General general){
        final General[] result = {null};
        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                result[0] = realm.where(General.class).findFirst();;
            }
        });
        return result[0];
    }

    public String find(String property){
        SharedPreferences prefs = context.getSharedPreferences("J2WPreferences",Context.MODE_PRIVATE);
        return prefs.getString(property, "");
    }

    // ********************* SAVE OPERATIONS ****************************

    /**
     * Método que guarda una preferencia en el sistema.
     * @param property corresponde a la propiedad que se guardará.
     * @param value corresponde al valor de la propiedad que se guardará
     */
    public void save(String property, String value){
        SharedPreferences prefs = context.getSharedPreferences("J2WPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(property, value);
        editor.apply();
        editor.clear();
    }

    /**
     * Método que guarda un objeto Knowledge en la base de datos.
     * @param knowledge corresponde al objeto que se va a guardar.
     * @return un valor booleano que indica si se pudo guardar o no el objeto.
     */
    public boolean save(Knowledge knowledge) {
        boolean result = true;
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            if (knowledge.getId() == -1) {
                Number currentIdNum = realm.where(knowledge.getClass()).max("id");
                if (currentIdNum == null) {
                    knowledge.setId(1);
                } else {
                    int nextId = currentIdNum.intValue() + 1;
                    knowledge.setId(nextId);
                }
            }
            realm.copyToRealmOrUpdate(knowledge);
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

    /**
     * Método que guarda un objeto CurrentStudy en la base de datos.
     * @param currentStudy corresponde al objeto que se va a guardar.
     * @return un valor booleano que indica si se pudo guardar o no el objeto.
     */
    public boolean save(CurrentStudy currentStudy) {
        boolean result = true;
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            if (currentStudy.getId() == -1) {
                Number currentIdNum = realm.where(currentStudy.getClass()).max("id");
                if (currentIdNum == null) {
                    currentStudy.setId(1);
                } else {
                    int nextId = currentIdNum.intValue() + 1;
                    currentStudy.setId(nextId);
                }
            }
            realm.copyToRealmOrUpdate(currentStudy);
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
     * Método que guarda un objeto StudyDone en la base de datos.
     * @param studyDone corresponde al objeto que se va a guardar.
     * @return un valor booleano que indica si se pudo guardar o no el objeto.
     */
    public boolean save(StudyDone studyDone) {
        boolean result = true;
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            if (studyDone.getId() == -1) {
                Number currentIdNum = realm.where(studyDone.getClass()).max("id");
                if (currentIdNum == null) {
                    studyDone.setId(1);
                } else {
                    int nextId = currentIdNum.intValue() + 1;
                    studyDone.setId(nextId);
                }
            }
            realm.copyToRealmOrUpdate(studyDone);
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
     * Método que guarda un objeto Documentation en la base de datos.
     * @param documentation corresponde al objeto que se va a guardar.
     * @return un valor booleano que indica si se pudo guardar o no el objeto.
     */
    public boolean save(Documentation documentation) {
        boolean result = true;
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            if (documentation.getId() == -1) {
                Number currentIdNum = realm.where(documentation.getClass()).max("id");
                if (currentIdNum == null) {
                    documentation.setId(1);
                } else {
                    int nextId = currentIdNum.intValue() + 1;
                    documentation.setId(nextId);
                }
            }
            realm.copyToRealmOrUpdate(documentation);
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
     * Método que guarda un objeto General en la base de datos.
     * @param general corresponde al objeto que se va a guardar.
     * @return un valor booleano que indica si se pudo guardar o no el objeto.
     */
    public boolean save(General general) {
        boolean result = true;
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            if (general.getId() == -1) {
                Number currentIdNum = realm.where(general.getClass()).max("id");
                if (currentIdNum == null) {
                    general.setId(1);
                } else {
                    int nextId = currentIdNum.intValue() + 1;
                    general.setId(nextId);
                }
            }
            realm.copyToRealmOrUpdate(general);
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
     * Método que permite eliminar un objeto de tipo CurrentStudy de la base de datos.
     * @param currentStudy corresponde a la tabla en la cual el objeto esta guardado
     * @param id corresponde al identificador del objeto a eliminar
     * @return un valor booleano que indica si se pudo eliminar o no
     */
    public boolean delete(CurrentStudy currentStudy, int id){
        Realm realm = Realm.getDefaultInstance();
        CurrentStudy result = realm.where(currentStudy.getClass()).equalTo("id", id).findFirst();
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
     * Método que permite eliminar un objeto de tipo StudyDone de la base de datos.
     * @param studyDone corresponde a la tabla en la cual el objeto esta guardado
     * @param id corresponde al identificador del objeto a eliminar
     * @return un valor booleano que indica si se pudo eliminar o no
     */
    public boolean delete(StudyDone studyDone, int id){
        Realm realm = Realm.getDefaultInstance();
        StudyDone result = realm.where(studyDone.getClass()).equalTo("id", id).findFirst();
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
     * Método que permite eliminar un objeto de tipo Documentation de la base de datos.
     * @param documentation corresponde a la tabla en la cual el objeto esta guardado
     * @param id corresponde al identificador del objeto a eliminar
     * @return un valor booleano que indica si se pudo eliminar o no
     */
    public boolean delete(Documentation documentation, int id){
        Realm realm = Realm.getDefaultInstance();
        Documentation result = realm.where(documentation.getClass()).equalTo("id", id).findFirst();
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

    /**
     * Método que permite buscar todos los objetos de tipo Knowledge guardados.
     * @return una lista con los objetos encontrados
     */
    public List<Knowledge> findAllKnowledges(){
        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<Knowledge> query = realm.where(Knowledge.class);
        RealmResults<Knowledge> result = query.findAll();
        return result;
    }

    /**
     * Método que permite buscar todos los objetos de tipo Knowledge guardados.
     * @return una lista con los objetos encontrados
     */
    public List<CurrentStudy> findAllCurrentStudies(){
        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<CurrentStudy> query = realm.where(CurrentStudy.class);
        RealmResults<CurrentStudy> result = query.findAll();
        return result;
    }

    /**
     * Método que permite buscar todos los objetos de tipo StudyDone guardados.
     * @return una lista con los objetos encontrados
     */
    public List<StudyDone> findAllStudiesDone(){
        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<StudyDone> query = realm.where(StudyDone.class);
        RealmResults<StudyDone> result = query.findAll();
        return result;
    }
}
