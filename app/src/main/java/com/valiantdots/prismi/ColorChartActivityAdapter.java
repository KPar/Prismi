package com.valiantdots.prismi;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

/**
 * Created by Ken on 6/25/2017.
 */

public class ColorChartActivityAdapter extends ArrayAdapter<String> {
    Context context;
    String[] values;
    public ColorChartActivityAdapter(Context context, String[] values){
        super(context, R.layout.list_col_color_chart, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_col_color_chart, parent, false);
        LinearLayout colorBox = (LinearLayout) rowView.findViewById(R.id.colorBox);

        String s = values[position];
        colorBox.setBackgroundColor(Color.parseColor(s));
        return rowView;
    }
}
