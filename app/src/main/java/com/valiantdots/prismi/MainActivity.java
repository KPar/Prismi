package com.valiantdots.prismi;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.lucasr.twowayview.TwoWayView;

import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {
    PrismiDbHelper mDbHelper;
    SQLiteDatabase db;
    PaletteAdapter paletteAdapter;
    ColorChartsAdapter colorChartsAdapter;
    NonScrollListView paletteList;
    TwoWayView colorChartsList;
    HashMap firstColorMap = new HashMap();
    int paletteID=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDbHelper= new PrismiDbHelper(getApplicationContext());
        db = mDbHelper.getWritableDatabase();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        toolbarTitle.setText(getString(R.string.app_name));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        RecentTasksStyler.styleRecentTasksEntry(this);

        RelativeLayout dropShadow1 = (RelativeLayout) findViewById(R.id.dropShadow1);
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP,
                new int[] {ContextCompat.getColor(getApplicationContext(), R.color.real_transparent),ContextCompat.getColor(getApplicationContext(), R.color.grayShadow2),ContextCompat.getColor(getApplicationContext(), R.color.grayShadow)});
        gd.setCornerRadius(0f);
        dropShadow1.setBackground(gd);

        Cursor cursor = db.rawQuery("SELECT * FROM "+PaletteModel.PaletteTable.TABLE_NAME,null);
        cursor.moveToFirst();
        paletteList = (NonScrollListView) findViewById(R.id.paletteList);
        paletteAdapter=new PaletteAdapter(getApplicationContext(),cursor);

        //obtains first color of each palette
        Cursor firstColor = db.rawQuery("SELECT *, MIN("+ ColorModel.ColorTable._ID+") FROM "+ ColorModel.ColorTable.TABLE_NAME+" GROUP BY "+ ColorModel.ColorTable.PALETTE_ID,null);
        firstColor.moveToFirst();
        for(int i=0;i<firstColor.getCount();i++){
            firstColorMap.put(firstColor.getInt(firstColor.getColumnIndex(ColorModel.ColorTable.PALETTE_ID)),firstColor.getInt(firstColor.getColumnIndex(ColorModel.ColorTable._ID)));
            firstColor.moveToNext();
        }
        paletteAdapter.setFirstColorMap(firstColorMap);
        paletteList.setAdapter(paletteAdapter);
        paletteAdapter.sendActivity(this);

        final CardView colorPrev = (CardView) findViewById(R.id.colorPrev);

        colorPrev.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if((event.getAction() == MotionEvent.ACTION_UP)) {
                    colorPrev.setVisibility(View.GONE);
                }
                return false;
            }
        });


        String[] colorCharts = {getResources().getString(R.string.materialChart),
                getResources().getString(R.string.flatChart) ,
                getResources().getString(R.string.webChart)};
        colorChartsList = (TwoWayView) findViewById(R.id.colorChartsList);
        colorChartsAdapter = new ColorChartsAdapter(getApplicationContext(),colorCharts);
        colorChartsList.setAdapter(colorChartsAdapter);
        colorChartsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ColorChartActivity.class);
                intent.putExtra("chartId",position);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onResume() {
        super.onResume();
        mDbHelper= new PrismiDbHelper(getApplicationContext());
        db = mDbHelper.getWritableDatabase();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

        Cursor cursor= db.rawQuery("SELECT * FROM "+PaletteModel.PaletteTable.TABLE_NAME,null);
        cursor.moveToFirst();
        Cursor firstColor = db.rawQuery("SELECT *, MIN("+ ColorModel.ColorTable._ID+") FROM "+ ColorModel.ColorTable.TABLE_NAME+" GROUP BY "+ ColorModel.ColorTable.PALETTE_ID,null);
        firstColor.moveToFirst();
        for(int i=0;i<firstColor.getCount();i++){
            firstColorMap.put(firstColor.getInt(firstColor.getColumnIndex(ColorModel.ColorTable.PALETTE_ID)),firstColor.getInt(firstColor.getColumnIndex(ColorModel.ColorTable._ID)));
            firstColor.moveToNext();
        }
        paletteAdapter.setFirstColorMap(firstColorMap);
        paletteAdapter.changeCursor(cursor);
        paletteAdapter.notifyDataSetChanged();
        db.close();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        LinearLayout aboutScreen =(LinearLayout) findViewById(R.id.aboutScreen);
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.action_info:
                if(aboutScreen.getVisibility()==View.GONE) {
                    aboutScreen.setVisibility(View.VISIBLE);
                }else{
                    aboutScreen.setVisibility(View.GONE);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //handles editPalette dialog clicks
    public void editPaletteDialog(View view){
        mDbHelper= new PrismiDbHelper(getApplicationContext());
        db = mDbHelper.getWritableDatabase();

        LinearLayout editPaletteLayout = (LinearLayout) findViewById(R.id.editPaletteLayout);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        EditText editTextPalette = (EditText) findViewById(R.id.editText_palette2);
        if(view!=null) {
            switch (view.getId()) {
                case R.id.editPaletteOk:
                    if (editTextPalette.getText().toString().trim().length() > 0) {
                        String text = editTextPalette.getText().toString().trim();
                        ContentValues values = new ContentValues();
                        values.put(PaletteModel.PaletteTable.PALETTE_NAME, text);
                        db.update(PaletteModel.PaletteTable.TABLE_NAME, values, PaletteModel.PaletteTable._ID+"="+paletteID, null);
                        Cursor cursor = db.rawQuery("SELECT * FROM " + PaletteModel.PaletteTable.TABLE_NAME, null);
                        cursor.moveToFirst();
                        paletteAdapter.changeCursor(cursor);
                        paletteAdapter.notifyDataSetChanged();
                        editTextPalette.setText("");
                    }
                    break;
                case R.id.editPaletteDelete:
                    db.delete(ColorModel.ColorTable.TABLE_NAME, ColorModel.ColorTable.PALETTE_ID+"="+paletteID,null);
                    db.delete(PaletteModel.PaletteTable.TABLE_NAME,PaletteModel.PaletteTable._ID+"="+paletteID,null);
                    Cursor cursor = db.rawQuery("SELECT * FROM " + PaletteModel.PaletteTable.TABLE_NAME, null);
                    cursor.moveToFirst();
                    paletteAdapter.changeCursor(cursor);
                    paletteAdapter.notifyDataSetChanged();
                    editTextPalette.setText("");
                    break;
            }
        }

        if(editPaletteLayout.getVisibility()==View.GONE){
            editPaletteLayout.setVisibility(View.VISIBLE);
            Cursor titleCursor = db.rawQuery("SELECT * FROM " + PaletteModel.PaletteTable.TABLE_NAME + " WHERE " + PaletteModel.PaletteTable._ID + "=" + paletteID, null);
            titleCursor.moveToFirst();
            String currTitle = titleCursor.getString(titleCursor.getColumnIndex(PaletteModel.PaletteTable.PALETTE_NAME));
            editTextPalette.setText(currTitle);
            editTextPalette.requestFocus();
            imm.showSoftInput(editTextPalette,0);
            editTextPalette.setSelection(currTitle.length());
        }else{
            if (imm != null) {
                imm.hideSoftInputFromWindow(editTextPalette.getWindowToken(), 0);
            }
            editPaletteLayout.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
        db.close();
    }

    //handles newPalette dialog clicks
    public void newPaletteDialog(View view){
        mDbHelper= new PrismiDbHelper(getApplicationContext());
        db = mDbHelper.getWritableDatabase();

        LinearLayout newPaletteLayout = (LinearLayout) findViewById(R.id.newPaletteLayout);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        EditText editTextPalette = (EditText) findViewById(R.id.editText_palette);

        switch (view.getId()){
            case R.id.newPaletteOk:
                if(editTextPalette.getText().toString().trim().length()>0){
                    String text=editTextPalette.getText().toString().trim();
                    ContentValues values = new ContentValues();
                    values.put(PaletteModel.PaletteTable.PALETTE_NAME,text);
                    db.insert(PaletteModel.PaletteTable.TABLE_NAME,null,values);
                    Cursor cursor= db.rawQuery("SELECT * FROM "+PaletteModel.PaletteTable.TABLE_NAME,null);
                    cursor.moveToFirst();
                    paletteAdapter.changeCursor(cursor);
                    paletteAdapter.notifyDataSetChanged();
                    editTextPalette.setText("");
                }
                break;
        }

        editTextPalette.setText("");
        if(newPaletteLayout.getVisibility()==View.GONE){
            newPaletteLayout.setVisibility(View.VISIBLE);
            editTextPalette.requestFocus();
            imm.showSoftInput(editTextPalette,0);
        }else{
            if (imm != null) {
                imm.hideSoftInputFromWindow(editTextPalette.getWindowToken(), 0);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
            newPaletteLayout.setVisibility(View.GONE);
        }
        db.close();
    }

    //handles clicks within list rows
    public void listClick (View v, int button){
        mDbHelper= new PrismiDbHelper(getApplicationContext());
        db = mDbHelper.getWritableDatabase();
        int position=0;
        Cursor cursor;
        switch (button){
            case 0:
                position= paletteList.getPositionForView (v);
                cursor= db.rawQuery("SELECT * FROM "+PaletteModel.PaletteTable.TABLE_NAME,null);
                cursor.moveToPosition(position);
                paletteID = cursor.getInt(cursor.getColumnIndexOrThrow(PaletteModel.PaletteTable._ID));
                editPaletteDialog(null);
                break;
            case 1:
                position= paletteList.getPositionForView (v);
                cursor= db.rawQuery("SELECT * FROM "+PaletteModel.PaletteTable.TABLE_NAME,null);
                cursor.moveToPosition(position);
                paletteID = cursor.getInt(cursor.getColumnIndexOrThrow(PaletteModel.PaletteTable._ID));
                Intent intent = new Intent(getApplicationContext(), ColorPickerActivity.class);
                intent.putExtra("paletteID", paletteID);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                break;
        }

        db.close();
    }

    //handles previewing colors
    public void colorPrevSwitch(int id,String color){
        CardView colorPrev = (CardView) findViewById(R.id.colorPrev);
        TextView colorLabel = (TextView) findViewById(R.id.colorLabel);

        switch (id){
            case 1:
                colorPrev.setVisibility(View.VISIBLE);
                colorLabel.setText(color);
                int colorHashless = (int)Long.parseLong(color.substring(1,7), 16);
                int r = (colorHashless >> 16) & 0xFF;
                int g = (colorHashless >> 8) & 0xFF;
                int b = (colorHashless >> 0) & 0xFF;
                if ((r*0.299 + g*0.587 + b*0.114) > 186){
                    colorLabel.setTextColor(Color.parseColor("#000000"));
                }
                else {
                    colorLabel.setTextColor(Color.parseColor("#ffffff"));
                }
                colorPrev.setCardBackgroundColor(Color.parseColor(color));
                break;
            case 2:
                colorPrev.setVisibility(View.GONE);
                break;
        }

    }


}
