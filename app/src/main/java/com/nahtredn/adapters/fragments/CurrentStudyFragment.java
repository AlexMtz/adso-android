package com.nahtredn.adapters.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nahtredn.adso.CurrentStudyActivity;
import com.nahtredn.adso.R;
import com.nahtredn.entities.CurrentStudy;
import com.nahtredn.adapters.CurrentStudyAdapter;
import com.nahtredn.utilities.RealmController;

/**
 * Created by Me on 14/03/2018.
 */

public class CurrentStudyFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView list;
    private CurrentStudyAdapter adapter;

    public CurrentStudyFragment() { }

    public static CurrentStudyFragment newInstance() {
        CurrentStudyFragment fragment = new CurrentStudyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_current_study, container, false);
        list = root.findViewById(R.id.current_study_list);
        adapter = new CurrentStudyAdapter(getActivity(),
                RealmController.with(this).findAllCurrentStudies());
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter = new CurrentStudyAdapter(getActivity(),
                RealmController.with(this).findAllCurrentStudies());
        list.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        CurrentStudy currentStudy = adapter.getItem(position);
        Intent intent = new Intent(getActivity(), CurrentStudyActivity.class);
        intent.putExtra("current_study_id", currentStudy.getId());
        getActivity().startActivity(intent);
    }
}
