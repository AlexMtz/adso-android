package com.nahtredn.helpers;

import com.nahtredn.adso.R;

/**
 * Created by Me on 15/03/2018.
 */

public class ArrayStates {

    public int getArray(int pos){
        switch (pos){
            case 1:
                return R.array.aguascalientes;
            case 2:
                return R.array.baja_california;
            case 3:
                return R.array.baja_california_sur;
            case 4:
                return R.array.campeche;
            case 5:
                return R.array.chiapas;
            case 6:
                return R.array.chihuahua;
            case 7:
                return R.array.ciudad_de_mexico;
            case 8:
                return R.array.coahuila;
            case 9:
                return R.array.colima;
            case 10:
                return R.array.mexico;
            case 11:
                return R.array.durango;
            case 12:
                return R.array.guanajuato;
            case 13:
                return R.array.guerrero;
            case 14:
                return R.array.hidalgo;
            case 15:
                return R.array.jalisco;
            case 16:
                return R.array.michoacan;
            case 17:
                return R.array.morelos;
            case 18:
                return R.array.nayarit;
            case 19:
                return R.array.nuevo_leon;
            case 20:
                return R.array.oaxaca;
            case 21:
                return R.array.puebla;
            case 22:
                return R.array.queretaro;
            case 23:
                return R.array.quintana_roo;
            case 24:
                return R.array.san_luis_potosi;
            case 25:
                return R.array.sinaloa;
            case 26:
                return R.array.sonora;
            case 27:
                return R.array.tabasco;
            case 28:
                return R.array.tamaulipas;
            case 29:
                return R.array.tlaxcala;
            case 30:
                return R.array.veracruz;
            case 31:
                return R.array.yucatan;
            case 32:
                return R.array.zacatecas;
        }
        return -1;
    }
}
