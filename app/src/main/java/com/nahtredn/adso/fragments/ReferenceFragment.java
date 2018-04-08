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

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Me on 08/04/2018.
 */

public class ReferenceFragment extends Fragment {

    private ListView list;
    private ReferenceAdapter adapter;

    public ReferenceFragment() {
        // Required empty public constructor
    }

    public static ReferenceFragment newInstance(/*parámetros*/) {
        ReferenceFragment fragment = new ReferenceFragment();
        // Setup parámetros
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // if (getArguments() != null) {
        // Gets parámetros
        // }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_references, container, false);

        // Instancia del ListView.
        list = (ListView) root.findViewById(R.id.reference_list);

        // Inicializar el adaptador con la fuente de datos.
        adapter = new ReferenceAdapter(getActivity(),
                getReferences());

        //Relacionando la lista con el adaptador
        list.setAdapter(adapter);

        // Eventos
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Reference workExperience = adapter.getItem(position);
                Intent intent = new Intent(getActivity(), ReferenceActivity.class);
                intent.putExtra("reference_id", workExperience.getId());
                getActivity().startActivity(intent);
            }
        });

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter = new ReferenceAdapter(getActivity(),
                getReferences());
        adapter.notifyDataSetChanged();
        list.setAdapter(adapter);
    }

    private List<Reference> getReferences(){
        Realm.init(getContext());

        final Realm realm = Realm.getDefaultInstance();
        RealmQuery<Reference> query = realm.where(Reference.class);
        // Execute the query:
        RealmResults<Reference> result = query.findAll();
        return result;
    }
}
