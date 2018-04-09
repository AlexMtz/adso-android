package com.nahtredn.adapters.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nahtredn.adso.R;
import com.nahtredn.adso.StudyDoneActivity;
import com.nahtredn.adapters.StudyDoneAdapter;
import com.nahtredn.entities.StudyDone;
import com.nahtredn.utilities.RealmController;

public class StudyDoneFragment extends Fragment implements AdapterView.OnItemClickListener{
    private ListView list;
    private StudyDoneAdapter adapter;

    public StudyDoneFragment() { }

    public static StudyDoneFragment newInstance() {
        StudyDoneFragment fragment = new StudyDoneFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_study_done, container, false);
        list = root.findViewById(R.id.study_done_list);
        adapter = new StudyDoneAdapter(getActivity(),
                RealmController.with(this).findAllStudiesDone());
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter = new StudyDoneAdapter(getActivity(),
                RealmController.with(this).findAllStudiesDone());
        adapter.notifyDataSetChanged();
        list.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        StudyDone studyDone = adapter.getItem(position);
        Intent intent = new Intent(getActivity(), StudyDoneActivity.class);
        intent.putExtra("study_done_id", studyDone.getId());
        getActivity().startActivity(intent);
    }
}
