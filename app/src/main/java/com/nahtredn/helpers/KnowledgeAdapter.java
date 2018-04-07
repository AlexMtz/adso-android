package com.nahtredn.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nahtredn.adso.R;
import com.nahtredn.entities.Knowledge;

import java.util.List;

/**
 * Created by Me on 07/04/2018.
 */

public class KnowledgeAdapter extends ArrayAdapter<Knowledge> {

    public KnowledgeAdapter(Context context, List<Knowledge> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // ¿Existe el view actual?
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.list_knowledge_item,
                    parent,
                    false);
        }

        // Referencias UI.
        TextView title = convertView.findViewById(R.id.knowledge_title);

        // Cliente actual.
        Knowledge knowledge = getItem(position);

        // Setup.
        title.setText(knowledge.getTitle());
        return convertView;
    }
}
