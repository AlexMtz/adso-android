package com.nahtredn.utilities;

import com.nahtredn.entities.Knowledge;

import io.realm.Realm;

/**
 * Clase utilitaria para administrar las operaciones realizadas con la base de datos.
 */

public class RealmController {

    // Instancia de la clase
    private static RealmController instance;

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
}
