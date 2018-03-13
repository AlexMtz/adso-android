package com.nahtredn.adso;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.nahtredn.entities.DataItem;
import com.nahtredn.entities.StudiesDone;
import com.nahtredn.helpers.StudyDoneAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Me on 13/03/2018.
 */

public class StudyDoneFragment extends Fragment {
    private ListView mStudyDoneList;
    private StudyDoneAdapter mStudyDoneAdapter;

    public StudyDoneFragment() {
        // Required empty public constructor
    }

    public static StudyDoneFragment newInstance(/*parámetros*/) {
        StudyDoneFragment fragment = new StudyDoneFragment();
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
        View root = inflater.inflate(R.layout.fragment_study_done, container, false);

        // Instancia del ListView.
        mStudyDoneList = (ListView) root.findViewById(R.id.study_done_list);

        // Inicializar el adaptador con la fuente de datos.
        mStudyDoneAdapter = new StudyDoneAdapter(getActivity(),
                getStudiesDone());

        //Relacionando la lista con el adaptador
        mStudyDoneList.setAdapter(mStudyDoneAdapter);

        // Eventos
        mStudyDoneList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                StudiesDone studiesDone = mStudyDoneAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), StudyDoneActivity.class);
                getActivity().startActivity(intent);
            }
        });

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mStudyDoneAdapter = new StudyDoneAdapter(getActivity(),
                getStudiesDone());
        mStudyDoneList.setAdapter(mStudyDoneAdapter);
    }

    private List<StudiesDone> getStudiesDone(){
        ArrayList<StudiesDone> studiesDones = new ArrayList<>();
        StudiesDone studyDone = new StudiesDone();
        studyDone.setAcademicLevel("Preparatoria");
        studyDone.setInstitute("Colegio de Bachilleres");
        studyDone.setEndDate("06/06/2017");
        studiesDones.add(studyDone);
        studiesDones.add(studyDone);
        studiesDones.add(studyDone);
        studiesDones.add(studyDone);
        /*Realm.init(getContext());
        final Realm realm = Realm.getDefaultInstance();
        RealmQuery<Client> query = realm.where(Client.class);
        // Execute the query:
        RealmResults<Client> result = query.findAll();
        return result;*/
        return studiesDones;
    }
}
