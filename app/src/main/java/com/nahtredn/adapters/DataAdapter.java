package com.nahtredn.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahtredn.adso.R;
import com.nahtredn.entities.DataItem;

import java.util.List;

/**
 * Created by Me on 10/03/2018.
 */

public class DataAdapter extends ArrayAdapter<DataItem> {

    public DataAdapter(Context context, List<DataItem> objects) {
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
                    R.layout.list_data_item,
                    parent,
                    false);
        }

        // Referencias UI.
        TextView title = convertView.findViewById(R.id.title_data);
        TextView description = convertView.findViewById(R.id.brief_description_data);
        ImageView imageView = convertView.findViewById(R.id.image_data);

        // Cliente actual.
        DataItem dataItem = getItem(position);

        // Setup.
        title.setText(dataItem.getTitle());
        description.setText(dataItem.getDescription());
        imageView.setImageResource(dataItem.getIdImage());
        return convertView;
    }
}
