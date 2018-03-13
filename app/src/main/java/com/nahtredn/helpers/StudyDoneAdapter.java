package com.nahtredn.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nahtredn.adso.R;
import com.nahtredn.entities.DataItem;
import com.nahtredn.entities.StudiesDone;

import java.util.List;

/**
 * Created by Me on 13/03/2018.
 */

public class StudyDoneAdapter extends ArrayAdapter<StudiesDone> {

    public StudyDoneAdapter(Context context, List<StudiesDone> objects) {
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
                    R.layout.list_study_done_item,
                    parent,
                    false);
        }

        // Referencias UI.
        TextView academicLevel = convertView.findViewById(R.id.academic_level_study_done);
        TextView institute = convertView.findViewById(R.id.institute_study_done);
        TextView endDate = convertView.findViewById(R.id.end_date_study_done);

        // Cliente actual.
        StudiesDone studiesDone = getItem(position);

        // Setup.
        academicLevel.setText(studiesDone.getAcademicLevel());
        institute.setText(studiesDone.getInstitute());
        endDate.setText(studiesDone.getEndDate());
        return convertView;
    }
}
