package com.valiantdots.prismi;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.lucasr.twowayview.TwoWayView;

import java.util.HashMap;


public class PaletteAdapter extends CursorAdapter {
    PrismiDbHelper mDbHelper;
    SQLiteDatabase db;
    MainActivity activity;
    HashMap firstColorMap;

    public PaletteAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    public void sendActivity(MainActivity a){
        activity=a;
    }

    public void setFirstColorMap(HashMap hm){
        this.firstColorMap=hm;
    }

    @Override
    public View newView(final Context context, Cursor cursor, final ViewGroup parent) {
        mDbHelper=new PrismiDbHelper(context);
        db=mDbHelper.getWritableDatabase();
        View rowlayout = LayoutInflater.from(context).inflate(R.layout.list_row_palette, parent, false);
        ImageView addNewColor = (ImageView) rowlayout.findViewById(R.id.addNewColor);
        TextView paletteTitle = (TextView) rowlayout.findViewById(R.id.paletteTitle);
        paletteTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.listClick(v,0);
            }
        });
        addNewColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.listClick(v,1);
            }
        });
        db.close();
        return rowlayout;
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    TwoWayView colorList;
    int paletteID;
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        mDbHelper= new PrismiDbHelper(context);
        db = mDbHelper.getWritableDatabase();
        final TextView paletteLabel = (TextView) view.findViewById(R.id.paletteTitle);
        String paletteName = cursor.getString(cursor.getColumnIndexOrThrow(PaletteModel.PaletteTable.PALETTE_NAME));
        colorList = (TwoWayView) view.findViewById(R.id.colorList);

        Typeface font = Typeface.createFromAsset(context.getAssets(),
                "fonts/SourceSansPro-Semibold.otf");
        paletteLabel.setTypeface(font);
        paletteLabel.setText(paletteName);

        paletteID = cursor.getInt(cursor.getColumnIndexOrThrow(PaletteModel.PaletteTable._ID));
        Cursor colorCursor = db.rawQuery("SELECT * FROM " + ColorModel.ColorTable.TABLE_NAME + " WHERE "
                + ColorModel.ColorTable.PALETTE_ID + "=" + paletteID,null);
        colorCursor.moveToFirst();
        ColorAdapter colorAdapter = new ColorAdapter(context,colorCursor);
        colorAdapter.setPaletteId(paletteID);
        colorAdapter.sendActivity(activity);
        colorAdapter.setFirstColorMap(firstColorMap);
        colorList.setAdapter(colorAdapter);
        colorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor colorCursor3 = db.rawQuery("SELECT * FROM " + ColorModel.ColorTable.TABLE_NAME + " WHERE "
                        + ColorModel.ColorTable.PALETTE_ID + "=" + paletteID,null);
                colorCursor3.moveToPosition(position);
                int colorID = colorCursor3.getInt(colorCursor3.getColumnIndexOrThrow(ColorModel.ColorTable._ID));
                String hexColor = colorCursor3.getString(colorCursor3.getColumnIndexOrThrow(ColorModel.ColorTable.COLOR));
                Intent intent = new Intent(context, ColorPickerActivity.class);
                intent.putExtra("colorID", colorID);
                intent.putExtra("hexColor", hexColor);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        db.close();
    }

}
