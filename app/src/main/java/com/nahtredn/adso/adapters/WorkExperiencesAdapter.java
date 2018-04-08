package com.nahtredn.adso.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nahtredn.adso.R;
import com.nahtredn.entities.WorkExperience;

import java.util.List;

/**
 * Created by Me on 08/04/2018.
 */

public class WorkExperiencesAdapter extends ArrayAdapter<WorkExperience> {

    public WorkExperiencesAdapter(Context context, List<WorkExperience> objects) {
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
                    R.layout.list_work_experience_item,
                    parent,
                    false);
        }

        // Referencias UI.
        TextView jobTitle = convertView.findViewById(R.id.job_title_work_experiences);
        TextView experienceType = convertView.findViewById(R.id.experience_type_work_experiences);
        TextView institute = convertView.findViewById(R.id.institute_work_experiences);

        // Cliente actual.
        WorkExperience workExperience = getItem(position);

        // Setup.
        jobTitle.setText(workExperience.getJobTitle());
        experienceType.setText(workExperience.getTypeExperience());
        institute.setText(workExperience.getInstitute());
        return convertView;
    }
}

