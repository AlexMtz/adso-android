package com.nahtredn.adso;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nahtredn.entities.CurrentStudy;
import com.nahtredn.helpers.CurrentStudyAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Me on 14/03/2018.
 */

public class CurrentStudyFragment extends Fragment {
    private ListView mCurrentStudyList;
    private CurrentStudyAdapter mCurrentStudyAdapter;

    public CurrentStudyFragment() {
        // Required empty public constructor
    }

    public static CurrentStudyFragment newInstance(/*parámetros*/) {
        CurrentStudyFragment fragment = new CurrentStudyFragment();
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
        View root = inflater.inflate(R.layout.fragment_current_study, container, false);

        // Instancia del ListView.
        mCurrentStudyList = (ListView) root.findViewById(R.id.current_study_list);

        // Inicializar el adaptador con la fuente de datos.
        mCurrentStudyAdapter = new CurrentStudyAdapter(getActivity(),
                getCurrentStudies());

        //Relacionando la lista con el adaptador
        mCurrentStudyList.setAdapter(mCurrentStudyAdapter);

        // Eventos
        mCurrentStudyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                CurrentStudy currentStudy = mCurrentStudyAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), CurrentStudyActivity.class);
                intent.putExtra("current_study_id", currentStudy.getId());
                getActivity().startActivity(intent);
            }
        });

        mCurrentStudyList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                Realm.init(getContext());
                final Realm realm = Realm.getDefaultInstance();

                CurrentStudy study = mCurrentStudyAdapter.getItem(position);
                CurrentStudy result = realm.where(CurrentStudy.class).equalTo("id", study.getId()).findFirst();

                // Get the study title to show it in toast message

                // All changes to data must happen in a transaction
                realm.beginTransaction();
                // remove single match
                result.deleteFromRealm();
                realm.commitTransaction();

                mCurrentStudyAdapter.notifyDataSetChanged();
                return false;
            }
        });

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCurrentStudyAdapter = new CurrentStudyAdapter(getActivity(),
                getCurrentStudies());
        mCurrentStudyList.setAdapter(mCurrentStudyAdapter);
    }

    private List<CurrentStudy> getCurrentStudies(){

        Realm.init(getContext());
        final Realm realm = Realm.getDefaultInstance();
        RealmQuery<CurrentStudy> query = realm.where(CurrentStudy.class);
        // Execute the query:
        RealmResults<CurrentStudy> result = query.findAll();
        return result;
    }
}
