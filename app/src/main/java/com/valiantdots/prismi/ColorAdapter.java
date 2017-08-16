package com.valiantdots.prismi;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;

import java.util.HashMap;


public class ColorAdapter extends CursorAdapter {

    public ColorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }
    public void sendActivity(MainActivity a){
        activity=a;
    }
    MainActivity activity;
    int paletteId;
    HashMap firstColorMap;


    public void setPaletteId(int paletteId) {
        this.paletteId = paletteId;
    }

    public void setFirstColorMap(HashMap hm){
        this.firstColorMap=hm;
    }
    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {

        View rowlayout = LayoutInflater.from(context).inflate(R.layout.list_col_color, parent, false);
        return rowlayout;
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final int colorID = cursor.getInt(cursor.getColumnIndexOrThrow(ColorModel.ColorTable._ID));
        final String hexColor = cursor.getString(cursor.getColumnIndexOrThrow(ColorModel.ColorTable.COLOR));

        LinearLayout colorBox1 = (LinearLayout) view.findViewById(R.id.colorBox1);
        LinearLayout colorBox2 = (LinearLayout) view.findViewById(R.id.colorBox2);

        if((int)firstColorMap.get(paletteId)==colorID){
            colorBox1.setVisibility(View.VISIBLE);
            colorBox2.setVisibility(View.GONE);
        }else {
            colorBox1.setVisibility(View.GONE);
            colorBox2.setVisibility(View.VISIBLE);
        }
        colorBox1.setBackgroundColor(Color.parseColor(hexColor));
        colorBox2.setBackgroundColor(Color.parseColor(hexColor));

        view.setOnTouchListener(new View.OnTouchListener() {
            final Handler handler = new Handler();
            int stat=0;
            Runnable mLongPressed = new Runnable() {
                public void run() {
                    activity.colorPrevSwitch(1,hexColor);
                    stat=1;
                }
            };
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.postDelayed(mLongPressed, 300);
                }
                if((event.getAction() == MotionEvent.ACTION_UP)||(event.getAction() == MotionEvent.ACTION_CANCEL)) {
                    handler.removeCallbacks(mLongPressed);
                    if(stat==1) {
                        stat = 0;
                        activity.colorPrevSwitch(2,hexColor);
                    }else if((stat==0) && (event.getAction() == MotionEvent.ACTION_UP)){
                        Intent intent = new Intent(context, ColorPickerActivity.class);
                        intent.putExtra("colorID", colorID);
                        intent.putExtra("hexColor", hexColor);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
                return true;
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ColorPickerActivity.class);
                intent.putExtra("colorID", colorID);
                intent.putExtra("hexColor", hexColor);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }
}
