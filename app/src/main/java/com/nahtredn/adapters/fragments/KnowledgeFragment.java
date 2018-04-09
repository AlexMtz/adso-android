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
import com.nahtredn.adso.SkillDetailActivity;
import com.nahtredn.entities.Knowledge;
import com.nahtredn.adapters.KnowledgeAdapter;
import com.nahtredn.utilities.RealmController;

/**
 * Created by Me on 07/04/2018.
 */

public class KnowledgeFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView list;
    private KnowledgeAdapter adapter;

    public KnowledgeFragment() { }

    public static KnowledgeFragment newInstance() {
        KnowledgeFragment fragment = new KnowledgeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_knowledge, container, false);
        list = root.findViewById(R.id.knowledge_list);
        adapter = new KnowledgeAdapter(getActivity(),
                RealmController.with(this).findAllKnowledges());
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter = new KnowledgeAdapter(getActivity(),
                RealmController.with(this).findAllKnowledges());
        adapter.notifyDataSetChanged();
        list.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Knowledge knowledge = adapter.getItem(position);
        Intent intent = new Intent(getActivity(), SkillDetailActivity.class);
        intent.putExtra("knowledge_id", knowledge.getId());
        getActivity().startActivity(intent);
    }
}
