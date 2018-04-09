package com.nahtredn.adso.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.nahtredn.adso.R;
import com.nahtredn.entities.Address;
import com.nahtredn.entities.Vacancy;
import com.nahtredn.adso.adapters.VacancyAdapter;
import com.nahtredn.utilities.Messenger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Me on 10/03/2018.
 */

public class VacancyFragment extends Fragment {
    private ListView mVacancyList;
    private VacancyAdapter mVacancyAdapter;

    public VacancyFragment() {
        // Required empty public constructor
    }

    public static VacancyFragment newInstance(/*parámetros*/) {
        VacancyFragment fragment = new VacancyFragment();
        // Setup parámetros
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Gets parámetros
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_vacancy, container, false);

        // Instancia del ListView.
        mVacancyList = (ListView) root.findViewById(R.id.vacancy_list);

        // Inicializar el adaptador con la fuente de datos.
        mVacancyAdapter = new VacancyAdapter(getActivity(),
                getVacancies());

        //Relacionando la lista con el adaptador
        mVacancyList.setAdapter(mVacancyAdapter);

        // Eventos
        mVacancyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Messenger.with(getActivity()).showMessage("Vacancy pressed");
            }
        });

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mVacancyAdapter = new VacancyAdapter(getActivity(),
                getVacancies());
        mVacancyList.setAdapter(mVacancyAdapter);
    }

    private List<Vacancy> getVacancies(){
        ArrayList<Vacancy> vacancies = new ArrayList<>();
        Vacancy vacancy = new Vacancy("Gerente","FEMSA","$$");
        Address address = new Address("Guadalupe","Zacatecas");
        Vacancy v2 =  new Vacancy("Empleado de mostrador","WalMart","$$$");
        vacancy.setAddress(address);
        v2.setAddress(address);
        vacancies.add(vacancy);
        vacancies.add(vacancy);
        vacancies.add(vacancy);
        vacancies.add(v2);
        vacancies.add(v2);
        vacancies.add(v2);
        vacancies.add(v2);
        return vacancies;
    }
}