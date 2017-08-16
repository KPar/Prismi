package com.valiantdots.prismi;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ColorPickerActivity extends AppCompatActivity {
    PrismiDbHelper mDbHelper;
    SQLiteDatabase db;
    int paletteID;
    int colorID=-1;
    int HSLHueValue=0;
    double HSLSatValue=.10;
    double HSLLightValue=.20;
    int RGBRedValue=0;
    int RGBGreenValue=0;
    int RGBBlueValue=0;
    String currHexColor="#ffffff";
    String currRgbColor="(0,0,0)";
    String currHslColor="(0,0,0)";
    int colorTextId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        RecentTasksStyler.styleRecentTasksEntry(this);

        RelativeLayout dropShadow1 = (RelativeLayout) findViewById(R.id.dropShadow1);
        RelativeLayout dropShadow2 = (RelativeLayout) findViewById(R.id.dropShadow2);

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP,
                new int[] {ContextCompat.getColor(getApplicationContext(), R.color.real_transparent),ContextCompat.getColor(getApplicationContext(), R.color.grayShadow2),ContextCompat.getColor(getApplicationContext(), R.color.grayShadow)});
        gd.setCornerRadius(0f);
        dropShadow1.setBackground(gd);
        GradientDrawable gd2=new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {ContextCompat.getColor(getApplicationContext(), R.color.real_transparent),ContextCompat.getColor(getApplicationContext(), R.color.grayShadow2),ContextCompat.getColor(getApplicationContext(), R.color.grayShadow)});
        gd2.setCornerRadius(0f);
        dropShadow2.setBackground(gd2);

        SeekBar seekBarHSLH= (SeekBar) findViewById(R.id.seekbarHSLH);
        SeekBar seekBarHSLS= (SeekBar) findViewById(R.id.seekbarHSLS);
        SeekBar seekBarHSLL= (SeekBar) findViewById(R.id.seekbarHSLL);
        final TextView textHueValueI = (TextView) findViewById(R.id.hueValueI);
        final TextView textSatValueI = (TextView) findViewById(R.id.satValueI);
        final TextView textLightValueI = (TextView) findViewById(R.id.lightValueI);
        final RelativeLayout colorPrev = (RelativeLayout) findViewById(R.id.setterColorPrev);
        final TextView colorTextView = (TextView) findViewById(R.id.colorTextView);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if(getIntent().hasExtra("paletteID")){
                if(extras.getInt("paletteID")>=0){
                    paletteID=extras.getInt("paletteID");
                }
            }
            LinearLayout hslSaveLayout = (LinearLayout) findViewById(R.id.hslSave);
            LinearLayout hslEditSaveLayout = (LinearLayout) findViewById(R.id.hslEditSave);
            LinearLayout rgbSaveLayout = (LinearLayout) findViewById(R.id.rgbSave);
            LinearLayout rgbEditSaveLayout = (LinearLayout) findViewById(R.id.rgbEditSave);
            if(getIntent().hasExtra("colorID")){
                if(extras.getInt("colorID")>=0){
                    colorID=extras.getInt("colorID");
                    double rgbHSLArray[] =RGB(Integer.valueOf(extras.getString("hexColor").substring(1,3),16),
                            Integer.valueOf(extras.getString("hexColor").substring(3,5),16),
                            Integer.valueOf(extras.getString("hexColor").substring(5,7),16));

                    seekBarHSLH.setProgress((int) Math.round(rgbHSLArray[0]));
                    seekBarHSLS.setProgress((int) Math.round(rgbHSLArray[1]));
                    seekBarHSLL.setProgress((int) Math.round(rgbHSLArray[2]));
                    textHueValueI.setText(""+(int) Math.round(rgbHSLArray[0]));
                    textSatValueI.setText(""+(int) Math.round(rgbHSLArray[1])+"%");
                    textLightValueI.setText(""+(int) Math.round(rgbHSLArray[2])+"%");

                    hslEditSaveLayout.setVisibility(View.VISIBLE);
                    rgbEditSaveLayout.setVisibility(View.VISIBLE);
                    hslSaveLayout.setVisibility(View.GONE);
                    rgbSaveLayout.setVisibility(View.GONE);
                }
            }else{
                hslEditSaveLayout.setVisibility(View.GONE);
                rgbEditSaveLayout.setVisibility(View.GONE);
                hslSaveLayout.setVisibility(View.VISIBLE);
                rgbSaveLayout.setVisibility(View.VISIBLE);
            }
        }

        //setting default color on opening of activity
        HSLHueValue=seekBarHSLH.getProgress();
        HSLSatValue=(double) seekBarHSLS.getProgress() / 100;
        HSLLightValue=(double) seekBarHSLL.getProgress() / 100;

        int[] hslArray = HSL(HSLHueValue,HSLSatValue,HSLLightValue);

        currHexColor=String.format("#%02x%02x%02x", hslArray[0],hslArray[1],hslArray[2]);
        currRgbColor="("+ hslArray[0]+", "+ hslArray[1]+", "+ hslArray[2]+")";
        colorPrev.setBackgroundColor(Color.parseColor(currHexColor));
        refreshColorText();
        TextView hslPrev = (TextView) findViewById(R.id.text_hslPrev);
        TextView hexPrev = (TextView) findViewById(R.id.text_hexPrev);
        TextView rgbPrev = (TextView) findViewById(R.id.text_rgbPrev);
        TextView hexPrev2 = (TextView) findViewById(R.id.text_hexPrev2);

        //change the textview to either b/w based on color
        if ((hslArray[0]*0.299 + hslArray[1]*0.587 + hslArray[2]*0.114) > 186){
            colorTextView.setTextColor(Color.parseColor("#000000"));
            hexPrev.setTextColor(Color.parseColor("#000000"));
            hslPrev.setTextColor(Color.parseColor("#000000"));
            rgbPrev.setTextColor(Color.parseColor("#000000"));
            hexPrev2.setTextColor(Color.parseColor("#000000"));

        }
        else {
            colorTextView.setTextColor(Color.parseColor("#ffffff"));
            hexPrev.setTextColor(Color.parseColor("#ffffff"));
            hexPrev2.setTextColor(Color.parseColor("#ffffff"));
            hslPrev.setTextColor(Color.parseColor("#ffffff"));
            rgbPrev.setTextColor(Color.parseColor("#ffffff"));
        }



        SeekBar.OnSeekBarChangeListener seekBarListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (seekBar.getId()){
                    case R.id.seekbarHSLH:
                        textHueValueI.setText(""+progress);
                        HSLHueValue=progress;
                        break;
                    case R.id.seekbarHSLS:
                        textSatValueI.setText(""+progress+"%");
                        HSLSatValue=(double) progress / 100;
                        break;
                    case R.id.seekbarHSLL:
                        textLightValueI.setText(""+progress+"%");
                        HSLLightValue=(double) progress / 100;
                        break;
                }
                int[] hslArray = HSL(HSLHueValue,HSLSatValue,HSLLightValue);
                currHexColor=String.format("#%02x%02x%02x", hslArray[0],hslArray[1],hslArray[2]);
                currRgbColor="("+ hslArray[0]+", "+ hslArray[1]+", "+ hslArray[2]+")";
                colorPrev.setBackgroundColor(Color.parseColor(currHexColor));
                refreshColorText();
                TextView hslPrev = (TextView) findViewById(R.id.text_hslPrev);
                TextView hexPrev = (TextView) findViewById(R.id.text_hexPrev);
                TextView rgbPrev = (TextView) findViewById(R.id.text_rgbPrev);
                TextView hexPrev2 = (TextView) findViewById(R.id.text_hexPrev2);

                if ((hslArray[0]*0.299 + hslArray[1]*0.587 + hslArray[2]*0.114) > 186){
                    colorTextView.setTextColor(Color.parseColor("#000000"));
                    hexPrev.setTextColor(Color.parseColor("#000000"));
                    hslPrev.setTextColor(Color.parseColor("#000000"));
                    rgbPrev.setTextColor(Color.parseColor("#000000"));
                    hexPrev2.setTextColor(Color.parseColor("#000000"));

                }
                else {
                    colorTextView.setTextColor(Color.parseColor("#ffffff"));
                    hexPrev.setTextColor(Color.parseColor("#ffffff"));
                    hexPrev2.setTextColor(Color.parseColor("#ffffff"));
                    hslPrev.setTextColor(Color.parseColor("#ffffff"));
                    rgbPrev.setTextColor(Color.parseColor("#ffffff"));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };

        seekBarHSLH.setOnSeekBarChangeListener(seekBarListener);
        seekBarHSLS.setOnSeekBarChangeListener(seekBarListener);
        seekBarHSLL.setOnSeekBarChangeListener(seekBarListener);

        //RGB to HSL
        SeekBar seekBarRGBR= (SeekBar) findViewById(R.id.seekbarRGBR);
        SeekBar seekBarRGBG= (SeekBar) findViewById(R.id.seekbarRGBG);
        SeekBar seekBarRGBB= (SeekBar) findViewById(R.id.seekbarRGBB);
        final TextView textRedValueI = (TextView) findViewById(R.id.redValueI);
        final TextView textGreenValueI = (TextView) findViewById(R.id.greenValueI);
        final TextView textBlueValueI = (TextView) findViewById(R.id.blueValueI);

        SeekBar.OnSeekBarChangeListener seekBarListener2 = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (seekBar.getId()){
                    case R.id.seekbarRGBR:
                        textRedValueI.setText(""+progress);
                        RGBRedValue=progress;
                        break;
                    case R.id.seekbarRGBG:
                        textGreenValueI.setText(""+progress);
                        RGBGreenValue=progress;
                        break;
                    case R.id.seekbarRGBB:
                        textBlueValueI.setText(""+progress);
                        RGBBlueValue =progress;
                        break;
                }
                double[] rgbArray = RGB(RGBRedValue,RGBGreenValue, RGBBlueValue);
                currHexColor=String.format("#%02x%02x%02x", RGBRedValue, RGBGreenValue, RGBBlueValue);
                currHslColor="("+ rgbArray[0]+", "+ rgbArray[1]+", "+ rgbArray[2]+")";
                colorPrev.setBackgroundColor(Color.parseColor(currHexColor));
                refreshColorText();
                TextView hslPrev = (TextView) findViewById(R.id.text_hslPrev);
                TextView hexPrev = (TextView) findViewById(R.id.text_hexPrev);
                TextView rgbPrev = (TextView) findViewById(R.id.text_rgbPrev);
                TextView hexPrev2 = (TextView) findViewById(R.id.text_hexPrev2);

                if ((RGBRedValue*0.299 + RGBGreenValue*0.587 + RGBBlueValue*0.114) > 186){
                    colorTextView.setTextColor(Color.parseColor("#000000"));
                    hexPrev.setTextColor(Color.parseColor("#000000"));
                    hslPrev.setTextColor(Color.parseColor("#000000"));
                    rgbPrev.setTextColor(Color.parseColor("#000000"));
                    hexPrev2.setTextColor(Color.parseColor("#000000"));

                }
                else {
                    colorTextView.setTextColor(Color.parseColor("#ffffff"));
                    hexPrev.setTextColor(Color.parseColor("#ffffff"));
                    hexPrev2.setTextColor(Color.parseColor("#ffffff"));
                    hslPrev.setTextColor(Color.parseColor("#ffffff"));
                    rgbPrev.setTextColor(Color.parseColor("#ffffff"));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };

        seekBarRGBR.setOnSeekBarChangeListener(seekBarListener2);
        seekBarRGBG.setOnSeekBarChangeListener(seekBarListener2);
        seekBarRGBB.setOnSeekBarChangeListener(seekBarListener2);
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_color_setter, menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        int color = ContextCompat.getColor(getApplicationContext(),R.color.black);
        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP);

        for (int i = 0; i < toolbar.getChildCount(); i++) {
            final View v = toolbar.getChildAt(i);

            if (v instanceof ImageButton) {
                ((ImageButton) v).setColorFilter(colorFilter);
            }
        }//specific only hamburger or back button

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static int[] HSL(int Hue, double Saturation, double Lightness) {
        double c = (1 - Math.abs(2 * Lightness - 1)) * Saturation;
        double x = c * (1 - Math.abs(((double) Hue / 60) % 2 - 1));
        double m = Lightness - c / 2;

        double R,G,B;

        if(Hue < 60){
            R=c;
            G=x;
            B=0;
        }else if(Hue < 120){
            R=x;
            G=c;
            B=0;
        }else if(Hue < 180){
            R=0;
            G=c;
            B=x;
        }else if(Hue < 240){
            R=0;
            G=x;
            B=c;
        }else if(Hue < 300){
            R=x;
            G=0;
            B=c;
        }else {
            R=c;
            G=0;
            B=x;
        }

        int[] RGB = {(int) Math.round(((R + m) * 255)) , (int) Math.round(((G + m) * 255)) , (int) Math.round(((B + m) * 255))};
        return RGB;
    }

    public static double[] RGB(double red, double green, double blue) {
        double r = red/255;
        double g = green/255;
        double b = blue/255;
        double max= Math.max(r,g);
        int maxId;
        if(max==r){
            maxId=1;
        }else{
            maxId=2;
        }
        max=Math.max(max, b);
        if(max==b){
            maxId=3;
        }
        double min = Math.min(r,g);
        min = Math.min(min, b);
        double diff= max-min;

        double H,S,L;

        if (maxId==1){
            H=60*(((g-b)/diff));
            if(H<0){
                H+=360;
            }
        }else if (maxId==2){
            H=60*(((b-r)/diff)+2);
        }else{
            H=60*(((r-g)/diff)+4);
        }

        L=(max+min)/2;

        if(diff==0){
            S=0;
        }else{
            S=(diff/(1-Math.abs(2*L-1)));
        }

        double[] HSL = { Math.round(H), Math.round((S*100)*10.0)/10.0, Math.round((L*100)*10.0)/10.0};
        return HSL;
    }

    public void colorSave (View view){
        mDbHelper= new PrismiDbHelper(getApplicationContext());
        db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        switch (view.getId()){
            case R.id.hslSave:
            case R.id.rgbSave:
                values.put(ColorModel.ColorTable.COLOR, currHexColor);
                values.put(ColorModel.ColorTable.PALETTE_ID,paletteID);
                db.insert(ColorModel.ColorTable.TABLE_NAME, null, values);
                finish();
                break;
            case R.id.hslSave2:
            case R.id.rgbSave2:
                values.put(ColorModel.ColorTable.COLOR, currHexColor);
                db.update(ColorModel.ColorTable.TABLE_NAME,values,ColorModel.ColorTable._ID+"="+colorID,null);
                finish();
                break;
            case R.id.hslTrash:
            case R.id.rgbTrash:
                db.delete(ColorModel.ColorTable.TABLE_NAME,ColorModel.ColorTable._ID+"="+colorID,null);
                finish();
                break;
        }
    }

    //sets the correct text color format to display
    public void refreshColorText(){
        final TextView colorTextView = (TextView) findViewById(R.id.colorTextView);
        switch (colorTextId){
            case 1:
                colorTextView.setText(currHexColor);
                break;
            case 2:
                colorTextView.setText(currRgbColor);
                break;
            case 3:
                colorTextView.setText(currHslColor);
                break;
        }
    }

    //switch for color formats
    public void colorTextSwitch(View view){
        switch (view.getId()){
            case R.id.bttn_hexPrev:
                colorTextId = 1;
                refreshColorText();
                break;
            case R.id.bttn_rgbPrev:
                colorTextId = 2;
                refreshColorText();
                break;
            case R.id.bttn_hexPrev2:
                colorTextId = 1;
                refreshColorText();
                break;
            case R.id.bttn_hslPrev:
                colorTextId = 3;
                refreshColorText();
                break;
        }
    }

    public void conversionLayoutSwitch(View view){
        RelativeLayout rgbLayout = (RelativeLayout) findViewById(R.id.rgbLayout);
        RelativeLayout hslLayout = (RelativeLayout) findViewById(R.id.hslLayout);
        LinearLayout hexRgbPrev = (LinearLayout) findViewById(R.id.hex_rgbPrev);
        LinearLayout hexHslPrev = (LinearLayout) findViewById(R.id.hex_hslPrev);
        RelativeLayout colorPrev = (RelativeLayout) findViewById(R.id.setterColorPrev);
        SeekBar seekBarHSLH= (SeekBar) findViewById(R.id.seekbarHSLH);
        SeekBar seekBarHSLS= (SeekBar) findViewById(R.id.seekbarHSLS);
        SeekBar seekBarHSLL= (SeekBar) findViewById(R.id.seekbarHSLL);
        SeekBar seekBarRGBR= (SeekBar) findViewById(R.id.seekbarRGBR);
        SeekBar seekBarRGBG= (SeekBar) findViewById(R.id.seekbarRGBG);
        SeekBar seekBarRGBB= (SeekBar) findViewById(R.id.seekbarRGBB);
        switch (view.getId()){
            case R.id.bttn_hslC:
               if(rgbLayout.getVisibility()==View.VISIBLE) {
                   double[] rgbArray = RGB(RGBRedValue, RGBGreenValue, RGBBlueValue);
                   int[] rgbArray2 = {(int) Math.round(rgbArray[0]), (int) Math.round(rgbArray[1]),
                           (int) Math.round(rgbArray[2])};
                   currHexColor = String.format("#%02x%02x%02x", RGBRedValue, RGBGreenValue, RGBBlueValue);
                   currHslColor = "(" + rgbArray[0] + ", " + rgbArray[1] + ", " + rgbArray[2] + ")";
                   rgbLayout.setVisibility(View.GONE);
                   hslLayout.setVisibility(View.VISIBLE);
                   colorTextId = 1;
                   colorPrev.setBackgroundColor(Color.parseColor(currHexColor));
                   refreshColorText();
                   hexRgbPrev.setVisibility(View.VISIBLE);
                   hexHslPrev.setVisibility(View.GONE);
                   seekBarHSLH.setProgress(rgbArray2[0]);
                   seekBarHSLS.setProgress(rgbArray2[1]);
                   seekBarHSLL.setProgress(rgbArray2[2]);
                   break;
               }else{
                   break;
               }
            case R.id.bttn_rgbC:
                if(hslLayout.getVisibility()==View.VISIBLE) {
                    int[] hslArray = HSL(HSLHueValue, HSLSatValue, HSLLightValue);
                    currHexColor = String.format("#%02x%02x%02x", hslArray[0], hslArray[1], hslArray[2]);
                    currRgbColor = "(" + hslArray[0] + ", " + hslArray[1] + ", " + hslArray[2] + ")";
                    rgbLayout.setVisibility(View.VISIBLE);
                    hslLayout.setVisibility(View.GONE);
                    colorTextId=1;
                    colorPrev.setBackgroundColor(Color.parseColor(currHexColor));
                    refreshColorText();
                    hexRgbPrev.setVisibility(View.GONE);
                    hexHslPrev.setVisibility(View.VISIBLE);
                    seekBarRGBR.setProgress(hslArray[0]);
                    seekBarRGBG.setProgress(hslArray[1]);
                    seekBarRGBB.setProgress(hslArray[2]);
                    break;
                }else{
                    break;
                }
        }

    }
}
