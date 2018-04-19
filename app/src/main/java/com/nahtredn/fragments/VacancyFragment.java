package com.nahtredn.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nahtredn.adso.R;
import com.nahtredn.adso.VacancyActivity;
import com.nahtredn.entities.Vacancy;
import com.nahtredn.adapters.VacancyAdapter;
import com.nahtredn.utilities.RealmController;

public class VacancyFragment extends Fragment implements AdapterView.OnItemClickListener{
    private ListView list;
    private VacancyAdapter adapter;

    public VacancyFragment() {
    }

    public static VacancyFragment newInstance() {
        VacancyFragment fragment = new VacancyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_vacancy, container, false);
        list = root.findViewById(R.id.vacancy_list);
        adapter = new VacancyAdapter(getActivity(),
                RealmController.with(this).findAllVacancies());
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter = new VacancyAdapter(getActivity(),
                RealmController.with(this).findAllVacancies());
        adapter.notifyDataSetChanged();
        list.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Vacancy vacancy = adapter.getItem(position);
        Intent intent = new Intent(getActivity(), VacancyActivity.class);
        intent.putExtra("vacancy_id", vacancy.getId());
        getActivity().startActivity(intent);
    }
}
