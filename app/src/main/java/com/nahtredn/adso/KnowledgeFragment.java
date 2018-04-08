package com.nahtredn.adso;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nahtredn.entities.Knowledge;
import com.nahtredn.helpers.KnowledgeAdapter;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Me on 07/04/2018.
 */

public class KnowledgeFragment extends Fragment {
    private ListView mKnowledgeList;
    private KnowledgeAdapter mKnowledgeAdapter;

    public KnowledgeFragment() {
        // Required empty public constructor
    }

    public static KnowledgeFragment newInstance(/*parámetros*/) {
        KnowledgeFragment fragment = new KnowledgeFragment();
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
        View root = inflater.inflate(R.layout.fragment_knowledge, container, false);

        // Instancia del ListView.
        mKnowledgeList = (ListView) root.findViewById(R.id.knowledge_list);

        // Inicializar el adaptador con la fuente de datos.
        mKnowledgeAdapter = new KnowledgeAdapter(getActivity(),
                getKnowledges());

        //Relacionando la lista con el adaptador
        mKnowledgeList.setAdapter(mKnowledgeAdapter);

        // Eventos
        mKnowledgeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Knowledge knowledge = mKnowledgeAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), SkillDetailActivity.class);
                intent.putExtra("knowledge_id", knowledge.getId());
                getActivity().startActivity(intent);
            }
        });

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mKnowledgeAdapter = new KnowledgeAdapter(getActivity(),
                getKnowledges());
        mKnowledgeAdapter.notifyDataSetChanged();
        mKnowledgeList.setAdapter(mKnowledgeAdapter);
    }

    private List<Knowledge> getKnowledges(){
        Realm.init(getContext());

        final Realm realm = Realm.getDefaultInstance();
        RealmQuery<Knowledge> query = realm.where(Knowledge.class);
        // Execute the query:
        RealmResults<Knowledge> result = query.findAll();
        return result;
    }
}
