package com.nahtredn.adso.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nahtredn.adso.R;
import com.nahtredn.adso.ReferenceActivity;
import com.nahtredn.adso.adapters.ReferenceAdapter;
import com.nahtredn.entities.Reference;
import com.nahtredn.utilities.RealmController;

/**
 * Clase Fragment que permite crear una lista de referencias y llenar un adapter para ser mostradas
 * en la vista. Implementa el evento OnItemClicListener para manejar el clic de los elementos de
 * la lista de referencias.
 */
public class ReferenceFragment extends Fragment implements AdapterView.OnItemClickListener{
    // Lista de referencias
    private ListView list;
    // Datos utilizados en la lista de referencias
    private ReferenceAdapter adapter;

    /**
     * Constructor de la clase
     */
    public ReferenceFragment() {
    }

    /**
     * Método que devuelve una instancia del Fragment, si aún no existe, la crea.
     * @return una instancia del Fragment.
     */
    public static ReferenceFragment newInstance() {
        ReferenceFragment fragment = new ReferenceFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_references, container, false);
        list = root.findViewById(R.id.reference_list);
        adapter = new ReferenceAdapter(getActivity(),
                RealmController.with(this).findAllReferences());
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter = new ReferenceAdapter(getActivity(),
                RealmController.with(this).findAllReferences());
        adapter.notifyDataSetChanged();
        list.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Reference workExperience = adapter.getItem(position);
        Intent intent = new Intent(getActivity(), ReferenceActivity.class);
        intent.putExtra("reference_id", workExperience.getId());
        getActivity().startActivity(intent);
    }
}
