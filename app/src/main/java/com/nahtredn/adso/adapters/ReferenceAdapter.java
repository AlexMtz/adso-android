package com.nahtredn.adso.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nahtredn.adso.R;
import com.nahtredn.entities.Reference;

import java.util.List;

public class ReferenceAdapter extends ArrayAdapter<Reference> {

    public ReferenceAdapter(Context context, List<Reference> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.list_item_reference,
                    parent,
                    false);
        }

        TextView name = convertView.findViewById(R.id.name_reference);
        TextView jobTitle = convertView.findViewById(R.id.job_title_reference);
        TextView timeToMeet = convertView.findViewById(R.id.time_to_meet);

        Reference reference = getItem(position);

        name.setText(reference.getName());
        jobTitle.setText(reference.getJobTitle());
        timeToMeet.setText(reference.getTimeToMeet() + " de conocerlo");
        return convertView;
    }
}