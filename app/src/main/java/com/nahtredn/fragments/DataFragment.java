package com.nahtredn.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nahtredn.adso.CurrentStudiesActivity;
import com.nahtredn.adso.DocumentationActivity;
import com.nahtredn.adso.GeneralActivity;
import com.nahtredn.adso.KnowledgeActivity;
import com.nahtredn.adso.PhotoActivity;
import com.nahtredn.adso.R;
import com.nahtredn.adso.ReferencesActivity;
import com.nahtredn.adso.StudiesDoneActivity;
import com.nahtredn.adso.WorkExperiencesActivity;
import com.nahtredn.entities.DataItem;
import com.nahtredn.adapters.DataAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Me on 10/03/2018.
 */

public class DataFragment extends Fragment {
    private ListView mDataList;
    private DataAdapter mDataAdapter;

    public DataFragment() {
        // Required empty public constructor
    }

    public static DataFragment newInstance(/*parámetros*/) {
        DataFragment fragment = new DataFragment();
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
        View root = inflater.inflate(R.layout.fragment_data, container, false);

        // Instancia del ListView.
        mDataList = (ListView) root.findViewById(R.id.data_list);

        // Inicializar el adaptador con la fuente de datos.
        mDataAdapter = new DataAdapter(getActivity(),
                getData());

        //Relacionando la lista con el adaptador
        mDataList.setAdapter(mDataAdapter);

        // Eventos
        mDataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                DataItem dataItem = mDataAdapter.getItem(position);
                Intent intent = null;
                switch (position){
                    case 0:
                        intent = new Intent(getActivity(),PhotoActivity.class);
                        break;
                    case 1:
                        intent = new Intent(getActivity(),GeneralActivity.class);
                        break;
                    case 2:
                        intent = new Intent(getActivity(),DocumentationActivity.class);
                        break;
                    case 3:
                        intent = new Intent(getActivity(),StudiesDoneActivity.class);
                        break;
                    case 4:
                        intent = new Intent(getActivity(), CurrentStudiesActivity.class);
                        break;
                    case 5:
                        intent = new Intent(getActivity(), KnowledgeActivity.class);
                        break;
                    case 6:
                        intent = new Intent(getActivity(), WorkExperiencesActivity.class);
                        break;
                    case 7:
                        intent = new Intent(getActivity(), ReferencesActivity.class);
                        break;
                }

                if (intent != null) {
                    getActivity().startActivity(intent);
                }
                /*Client client = mVacancyAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), ClientDetailActivity.class);
                intent.putExtra("client_id", client.getId());
                getActivity().startActivity(intent);*/
            }
        });

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mDataAdapter = new DataAdapter(getActivity(),
                getData());
        mDataList.setAdapter(mDataAdapter);
    }

    private List<DataItem> getData(){
        ArrayList<DataItem> dataItems = new ArrayList<>();
        dataItems.add(new DataItem("Foto","Es la imagen con la que los reclutadores te podrán identificar.", R.drawable.ic_camera));
        dataItems.add(new DataItem("Generales","Tu nombre, apellidos, edad y domicnilio serán necesarios", R.drawable.ic_identify));
        dataItems.add(new DataItem("Documentación","CURP, RFC y quizás licencia de conducir no pueden faltar", R.drawable.ic_folder));
        dataItems.add(new DataItem("Estudios Realizados","Platicanos que tanto te has preparado", R.drawable.ic_diploma));
        dataItems.add(new DataItem("Estudios Actuales","Platicanos si aún te estás preparando", R.drawable.ic_backpack));
        dataItems.add(new DataItem("Conocimientos y Habilidades","Todo lo que te gusta y has aprendido a hacer", R.drawable.ic_idea));
        dataItems.add(new DataItem("Experiencia Laboral","¿Antes has trabajado?", R.drawable.ic_job));
        dataItems.add(new DataItem("Referencias","¿A quién podemos preguntar por tí?", R.drawable.ic_reference));
        return dataItems;
    }
}
