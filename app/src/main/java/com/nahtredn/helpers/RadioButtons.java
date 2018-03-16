package com.nahtredn.helpers;

import com.nahtredn.adso.R;

/**
 * Created by Me on 15/03/2018.
 */

public class RadioButtons {

    public int getIdRadioButtonBy(String text){
        switch (text){
            case "Hombre":
                return R.id.radio_man_general;
            case "Mujer":
                return R.id.radio_woman_general;
            case "Padres":
                return R.id.radio_button_fhater_general;
            case "Familiares":
                return R.id.radio_button_family_general;
            case "Parientes":
                return R.id.radio_button_parents_general;
            case "Solo(a)":
                return R.id.radio_button_alone_general;
            case "Soltero(a)":
                return R.id.radio_button_single_general;
            case "Casado(a)":
                return R.id.radio_button_married_general;
            case "Divorciado(a)":
                return R.id.radio_button_divorced_general;
            case "Viudo(a)":
                return R.id.radio_button_widower_general;
            case "Unión libre":
                return R.id.radio_button_free_union_general;
        }
        return -1;
    }
}
