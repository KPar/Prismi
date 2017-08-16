package com.valiantdots.prismi;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Ken on 6/25/2017.
 */

public class ColorChartsAdapter extends ArrayAdapter<String> {
    Context context;

    public ColorChartsAdapter(Context context, String[] values){
        super(context, R.layout.list_col_color_charts, values);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_col_color_charts, parent, false);
        LinearLayout box0 = (LinearLayout) rowView.findViewById(R.id.box0);
        LinearLayout box1 = (LinearLayout) rowView.findViewById(R.id.box1);
        LinearLayout box2 = (LinearLayout) rowView.findViewById(R.id.box2);
        LinearLayout box3 = (LinearLayout) rowView.findViewById(R.id.box3);
        LinearLayout box4 = (LinearLayout) rowView.findViewById(R.id.box4);
        LinearLayout box5 = (LinearLayout) rowView.findViewById(R.id.box5);
        LinearLayout box6 = (LinearLayout) rowView.findViewById(R.id.box6);
        LinearLayout box7 = (LinearLayout) rowView.findViewById(R.id.box7);
        LinearLayout box8 = (LinearLayout) rowView.findViewById(R.id.box8);
        TextView chartName = (TextView) rowView.findViewById(R.id.chartName);
        Typeface font = Typeface.createFromAsset(context.getAssets(),
                "fonts/SourceSansPro-Semibold.otf");
        chartName.setTypeface(font);
        switch (position){
            case 0:
                box0.setBackgroundColor(Color.parseColor("#43A047"));
                box1.setBackgroundColor(Color.parseColor("#388E3C"));
                box2.setBackgroundColor(Color.parseColor("#2E7D32"));
                box3.setBackgroundColor(Color.parseColor("#DCEDC8"));
                box4.setBackgroundColor(Color.parseColor("#C5E1A5"));
                box5.setBackgroundColor(Color.parseColor("#AED581"));
                box6.setBackgroundColor(Color.parseColor("#7CB342"));
                box7.setBackgroundColor(Color.parseColor("#689F38"));
                box8.setBackgroundColor(Color.parseColor("#558B2F"));
                chartName.setText("Material");
                break;
            case 1:
                box0.setBackgroundColor(Color.parseColor("#2980B9"));
                box1.setBackgroundColor(Color.parseColor("#2471A3"));
                box2.setBackgroundColor(Color.parseColor("#1F618D"));
                box3.setBackgroundColor(Color.parseColor("#EBF5FB"));
                box4.setBackgroundColor(Color.parseColor("#D6EAF8"));
                box5.setBackgroundColor(Color.parseColor("#AED6F1"));
                box6.setBackgroundColor(Color.parseColor("#3498D8"));
                box7.setBackgroundColor(Color.parseColor("#2E86C1"));
                box8.setBackgroundColor(Color.parseColor("#2874A6"));
                chartName.setText("Flat");

                break;
            case 2:
                box0.setBackgroundColor(Color.parseColor("#990099"));
                box1.setBackgroundColor(Color.parseColor("#990066"));
                box2.setBackgroundColor(Color.parseColor("#990033"));
                box3.setBackgroundColor(Color.parseColor("#6633CC"));
                box4.setBackgroundColor(Color.parseColor("#663399"));
                box5.setBackgroundColor(Color.parseColor("#663366"));
                box6.setBackgroundColor(Color.parseColor("#9933FF"));
                box7.setBackgroundColor(Color.parseColor("#9933CC"));
                box8.setBackgroundColor(Color.parseColor("#993399"));
                chartName.setText("Web");

                break;
        }
        return rowView;
    }
}
