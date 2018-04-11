package com.nahtredn.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nahtredn.adso.R;
import com.nahtredn.entities.Vacancy;

import java.util.List;

/**
 * Created by Me on 10/03/2018.
 */

public class VacancyAdapter extends ArrayAdapter<Vacancy> {

    public VacancyAdapter(Context context, List<Vacancy> objects) {
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
                    R.layout.list_vacancy_item,
                    parent,
                    false);
        }

        // Referencias UI.
        TextView name = (TextView) convertView.findViewById(R.id.name_vacancy);
        TextView companyName = (TextView) convertView.findViewById(R.id.company_name_vacancy);
        TextView place = (TextView) convertView.findViewById(R.id.place_vacancy);
        TextView salary = (TextView) convertView.findViewById(R.id.salary_vacancy);

        // Cliente actual.
        Vacancy vacancy = getItem(position);

        // Setup.
        name.setText(vacancy.getJobTitle());
        companyName.setText(vacancy.getCompanyName());
        place.setText(vacancy.getPlace());
        salary.setText(vacancy.getSalary());

        return convertView;
    }
}
