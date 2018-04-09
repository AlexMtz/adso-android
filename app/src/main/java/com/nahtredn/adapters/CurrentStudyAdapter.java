package com.nahtredn.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nahtredn.adso.R;
import com.nahtredn.entities.CurrentStudy;

import java.util.List;


public class CurrentStudyAdapter extends ArrayAdapter<CurrentStudy> {

    public CurrentStudyAdapter(Context context, List<CurrentStudy> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.list_current_study_item,
                    parent,
                    false);
        }

        TextView courseName = convertView.findViewById(R.id.course_name_current_study);
        TextView schedule = convertView.findViewById(R.id.schedule_current_study);
        TextView timeSchedule = convertView.findViewById(R.id.time_schedule_current_study);
        TextView institute = convertView.findViewById(R.id.institute_current_study);

        CurrentStudy currentStudy = getItem(position);

        courseName.setText(currentStudy.getCourseName());
        schedule.setText(currentStudy.getDays());
        timeSchedule.setText(currentStudy.getSchedule());
        institute.setText(currentStudy.getInstitute());
        return convertView;
    }
}
