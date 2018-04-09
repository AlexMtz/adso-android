package com.nahtredn.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nahtredn.adso.R;
import com.nahtredn.adso.WorkExperienceActivity;
import com.nahtredn.adapters.WorkExperiencesAdapter;
import com.nahtredn.entities.WorkExperience;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Me on 08/04/2018.
 */

public class WorkExperienceFragment extends Fragment {

    private ListView list;
    private WorkExperiencesAdapter adapter;

    public WorkExperienceFragment() {
        // Required empty public constructor
    }

    public static WorkExperienceFragment newInstance(/*parámetros*/) {
        WorkExperienceFragment fragment = new WorkExperienceFragment();
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
        View root = inflater.inflate(R.layout.fragment_work_experience, container, false);

        // Instancia del ListView.
        list = (ListView) root.findViewById(R.id.work_experience_list);

        // Inicializar el adaptador con la fuente de datos.
        adapter = new WorkExperiencesAdapter(getActivity(),
                getExperiences());

        //Relacionando la lista con el adaptador
        list.setAdapter(adapter);

        // Eventos
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                WorkExperience workExperience = adapter.getItem(position);
                Intent intent = new Intent(getActivity(), WorkExperienceActivity.class);
                intent.putExtra("work_experience_id", workExperience.getId());
                getActivity().startActivity(intent);
            }
        });

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter = new WorkExperiencesAdapter(getActivity(),
                getExperiences());
        adapter.notifyDataSetChanged();
        list.setAdapter(adapter);
    }

    private List<WorkExperience> getExperiences(){
        Realm.init(getContext());

        final Realm realm = Realm.getDefaultInstance();
        RealmQuery<WorkExperience> query = realm.where(WorkExperience.class);
        // Execute the query:
        RealmResults<WorkExperience> result = query.findAll();
        return result;
    }
}
