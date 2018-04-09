package com.nahtredn.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nahtredn.adso.R;
import com.nahtredn.entities.StudyDone;

import java.util.List;

/**
 * Created by Me on 13/03/2018.
 */

public class StudyDoneAdapter extends ArrayAdapter<StudyDone> {

    public StudyDoneAdapter(Context context, List<StudyDone> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.list_study_done_item,
                    parent,
                    false);
        }
        TextView academicLevel = convertView.findViewById(R.id.academic_level_study_done);
        TextView institute = convertView.findViewById(R.id.institute_study_done);
        TextView endDate = convertView.findViewById(R.id.end_date_study_done);
        StudyDone studiesDone = getItem(position);
        academicLevel.setText(studiesDone.getAcademicLevel());
        institute.setText(studiesDone.getInstitute());
        endDate.setText(studiesDone.getEndDate());
        return convertView;
    }
}
