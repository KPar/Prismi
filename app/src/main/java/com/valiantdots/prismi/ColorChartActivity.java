package com.valiantdots.prismi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ColorChartActivity extends AppCompatActivity {
    PrismiDbHelper mDbHelper;
    SQLiteDatabase db;
    ColorChartActivityAdapter colorChartActivityAdapter;
    GridView colorChartList;
    int chartId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_chart);

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

        final String[] materialChart = {"#FFEBEE","#FFCDD2","#EF9A9A","#E57373","#EF5350","#F44336","#E53935",
                "#D32F2F","#C62828","#B71C1C","#FCE4EC","#F8BBD0","#F48FB1","#F06292","#EC407A"
                ,"#E91E63","#D81B60","#C2185B","#AD1457","#880E4F","#F3E5F5","#E1BEE7","#CE93D8"
                ,"#BA68C8","#AB47BC","#9C27B0","#8E24AA","#7B1FA2","#6A1B9A","#4A148C","#EDE7F6"
                ,"#D1C4E9","#B39DDB","#9575CD","#7E57C2","#673AB7","#5E35B1","#512DA8","#4527A0"
                ,"#311B92","#E8EAF6","#C5CAE9","#9FA8DA","#7986CB","#5C6BC0","#3F51B5","#3949AB"
                ,"#303F9F","#283593","#1A237E","#E3F2FD","#BBDEFB","#90CAF9","#64B5F6","#42A5F5","#2196F3"
                ,"#1E88E5","#1976D2","#1565C0","#0D47A1","#E1F5FE","#B3E5FC","#81D4FA","#4FC3F7"
                ,"#29B6F6","#03A9F4","#039BE5","#0288D1","#0277BD","#01579B","#E0F7FA","#B2EBF2"
                ,"#80DEEA","#4DD0E1","#26C6DA","#00BCD4","#00ACC1","#0097A7","#00838F","#006064"
                ,"#E0F2F1","#B2DFDB","#80CBC4","#4DB6AC","#26A69A","#009688","#00897B","#00796B"
                ,"#00695C","#004D40","#E8F5E9","#C8E6C9","#A5D6A7","#81C784","#66BB6A","#4CAF50"
                ,"#43A047","#388E3C","#2E7D32","#1B5E20","#F1F8E9","#DCEDC8","#C5E1A5","#AED581"
                ,"#9CCC65","#8BC34A","#7CB342","#689F38","#558B2F","#33691E","#F9FBE7","#F0F4C3"
                ,"#E6EE9C","#DCE775","#D4E157","#CDDC39","#C0CA33","#AFB42B","#9E9D24","#827717"
                ,"#FFFDE7","#FFF9C4","#FFF59D","#FFF176","#FFEE58","#FFEB3B","#FDD835","#FBC02D"
                ,"#F9A825","#F57F17","#FFF8E1","#FFECB3","#FFE082","#FFD54F","#FFCA28","#FFC107"
                ,"#FFB300","#FFA000","#FF8F00","#FF6F00","#FFF3E0","#FFE0B2","#FFCC80","#FFB74D"
                ,"#FFA726","#FF9800","#FB8C00","#F57C00","#EF6C00","#E65100","#FBE9E7","#FFCCBC"
                ,"#FFAB91","#FF8A65","#FF7043","#FF5722","#F4511E","#E64A19","#D84315","#BF360C"
                ,"#EFEBE9","#D7CCC8","#BCAAA4","#A1887F","#8D6E63","#795548","#6D4C41","#5D4037"
                ,"#4E342E","#3E2723","#FAFAFA","#F5F5F5","#EEEEEE","#E0E0E0","#BDBDBD","#9E9E9E"
                ,"#757575","#616161","#424242","#212121","#ECEFF1","#CFD8DC","#B0BEC5","#90A4AE"
                ,"#78909C","#607D8B","#546E7A","#455A64","#37474F","#263238","#000000","#FFFFFF"
        };
        final String[] flatChart = {"#F9EBEA","#F2D7D5","#E6B0AA","#D98880","#CD6155","#C0392B"
                ,"#A93226","#922B21","#7B241C","#641E16","#FDEDEC","#FADBD8","#F5B7B1","#F1948A"
                ,"#EC7063","#E74C3C","#CB4335","#B03A2E","#943126","#78281F","#F5EEF8","#EBDEF0"
                ,"#D7BDE2","#C39BD3","#AF7AC5","#9B59B6","#884EA0","#76448A","#633974","#512E5F"
                ,"#F4ECF7","#E8DAEF","#D2B4DE","#BB8FCE","#A569BD","#8E44AD","#7D3C98","#6C3483"
                ,"#5B2C6F","#4A235A","#EAF2F8","#D4E6F1","#A9CCE3","#7FB3D5","#5499C7","#2980B9"
                ,"#2471A3","#1F618D","#1A5276","#154360","#EBF5FB","#D6EAF8","#AED6F1","#85C1E9"
                ,"#5DADE2","#3498DB","#2E86C1","#2874A6","#21618C","#1B4F72","#E8F8F5","#D1F2EB"
                ,"#A3E4D7","#76D7C4","#48C9B0","#1ABC9C","#17A589","#148F77","#117864","#0E6251"
                ,"#E8F6F3","#D0ECE7","#A2D9CE","#73C6B6","#45B39D","#16A085","#138D75","#117A65"
                ,"#0E6655","#0B5345","#E9F7EF","#D4EFDF","#A9DFBF","#7DCEA0","#52BE80","#27AE60"
                ,"#229954","#1E8449","#196F3D","#145A32","#EAFAF1","#D5F5E3","#ABEBC6","#82E0AA"
                ,"#58D68D","#2ECC71","#28B463","#239B56","#1D8348","#186A3B","#FEF9E7","#FCF3CF"
                ,"#F9E79F","#F7DC6F","#F4D03F","#F1C40F","#D4AC0D","#B7950B","#9A7D0A","#7D6608"
                ,"#FEF5E7","#FDEBD0","#FAD7A0","#F8C471","#F5B041","#F39C12","#D68910","#B9770E"
                ,"#9C640C","#7E5109","#FDF2E9","#FAE5D3","#F5CBA7","#F0B27A","#EB984E","#E67E22"
                ,"#CA6F1E","#AF601A","#935116","#784212","#FBEEE6","#F6DDCC","#EDBB99","#E59866"
                ,"#DC7633","#D35400","#BA4A00","#A04000","#873600","#6E2C00","#FDFEFE","#FBFCFC"
                ,"#F7F9F9","#F4F6F7","#F0F3F4","#ECF0F1","#D0D3D4","#B3B6B7","#979A9A","#7B7D7D"
                ,"#F8F9F9","#F2F3F4","#E5E7E9","#D7DBDD","#CACFD2","#BDC3C7","#A6ACAF","#909497"
                ,"#797D7F","#626567","#F4F6F6","#EAEDED","#D5DBDB","#BFC9CA","#AAB7B8","#95A5A6"
                ,"#839192","#717D7E","#5F6A6A","#4D5656","#F2F4F4","#E5E8E8","#CCD1D1","#B2BABB"
                ,"#99A3A4","#7F8C8D","#707B7C","#616A6B","#515A5A","#424949","#EBEDEF","#D6DBDF"
                ,"#AEB6BF","#85929E","#5D6D7E","#34495E","#2E4053","#283747","#212F3C","#1B2631"
                ,"#EAECEE","#D5D8DC","#ABB2B9","#808B96","#566573","#2C3E50","#273746","#212F3D"
                ,"#212F3C","#17202A"
        };
        final String[] webChart = {"#CCFFFF","#CCFFCC","#CCFF99","#CCFF66","#CCFF33","#CCFF00"
                ,"#FFFFFF","#FFFFCC","#FFFF99","#FFFF66","#FFFF33","#FFFF00","#CCCCFF","#CCCCCC"
                ,"#CCCC99","#CCCC66","#CCCC33","#CCCC00","#FFCCFF","#FFCCCC","#FFCC99","#FFCC66"
                ,"#FFCC33","#FFCC00","#CC99FF","#CC99CC","#CC9999","#CC9966","#CC9933","#CC9900"
                ,"#FF99FF","#FF99CC","#FF9999","#FF9966","#FF9933","#FF9900","#CC66FF","#CC66CC"
                ,"#CC6699","#CC6666","#CC6633","#CC6600","#FF66FF","#FF66CC","#FF6699","#FF6666"
                ,"#FF6633","#FF6600","#CC33FF","#CC33CC","#CC3399","#CC3366","#CC3333","#CC3300"
                ,"#FF33FF","#FF33CC","#FF3399","#FF3366","#FF3333","#FF3300","#CC00FF","#CC00CC"
                ,"#CC0099","#CC0066","#CC0033","#CC0000","#FF00FF","#FF00CC","#FF0099","#FF0066"
                ,"#FF0033","#FF0000","#6600FF","#6600CC","#660099","#660066","#660033","#660000"
                ,"#9900FF","#9900CC","#990099","#990066","#990033","#990000","#6633FF","#6633CC"
                ,"#660099","#660066","#660033","#660000","#9933FF","#9933CC","#993399","#993366"
                ,"#993333","#993300","#6666FF","#6666CC","#666699","#666666","#666633","#666600"
                ,"#9966FF","#9966CC","#996699","#996666","#996633","#996600","#6699FF","#6699CC"
                ,"#669999","#669966","#669933","#669900","#9999FF","#9999CC","#999999","#999966"
                ,"#999933","#999900","#66CCFF","#66CCCC","#66CC99","#66CC66","#66CC33","#66CC00"
                ,"#99CCFF","#99CCCC","#99CC99","#99CC66","#99CC33","#99CC00","#66FFFF","#66FFCC"
                ,"#66FF99","#66FF66","#66FF33","#66FF00","#99FFFF","#99FFCC","#99FF99","#99FF66"
                ,"#99FF33","#99FF00","#99FFFF","#99FFCC","#99FF99","#99FF66","#99FF33","#99FF00"
                ,"#00FFFF","#00FFCC","#00FF99","#00FF66","#00FF33","#00FF00","#33FFFF","#33FFCC"
                ,"#33FF99","#33FF66","#33FF33","#33FF00","#00CCFF","#00CCCC","#00CC99","#00CC66"
                ,"#00CC33","#00CC00","#33CCFF","#33CCCC","#33CC99","#33CC66","#33CC33","#33CC00"
                ,"#0099FF","#0099CC","#009999","#009966","#009933","#009900","#3399FF","#3399CC"
                ,"#339999","#339966","#339933","#339900","#0066FF","#0066CC","#006699","#006666"
                ,"#006633","#006600","#3366FF","#3366CC","#336699","#336666","#336633","#336600"
                ,"#0033FF","#0033CC","#003399","#003366","#003333","#003300","#3333FF","#3333CC"
                ,"#333399","#333366","#333333","#333300","#0000FF","#0000CC","#000099","#000066"
                ,"#000033","#000000","#3300FF","#3300CC","#330099","#330066","#330033","#330000"
        };
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (getIntent().hasExtra("chartId")) {
                chartId = extras.getInt("chartId");
            }
        }
        colorChartList= (GridView) findViewById(R.id.colorChartList);
        switch (chartId){
            case 0:
                colorChartActivityAdapter = new ColorChartActivityAdapter(getApplicationContext(),materialChart);
                break;
            case 1:
                colorChartActivityAdapter = new ColorChartActivityAdapter(getApplicationContext(),flatChart);
                break;
            case 2:
                colorChartActivityAdapter = new ColorChartActivityAdapter(getApplicationContext(),webChart);
                break;
        }
        colorChartList.setAdapter(colorChartActivityAdapter);
        final RelativeLayout colorPrev = (RelativeLayout) findViewById(R.id.colorPrev);
        colorChartList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView hexText = (TextView) findViewById(R.id.hexColor);
                TextView hslText = (TextView) findViewById(R.id.HSLColor);
                TextView rgbText = (TextView) findViewById(R.id.RGBColor);
                TextView closeBttn = (TextView) findViewById(R.id.closeBttn);
                int colorHashless=0;
                switch (chartId){
                    case 0:
                        colorPrev.setBackgroundColor(Color.parseColor(materialChart[position]));
                        hexText.setText(materialChart[position]);
                        colorHashless= (int)Long.parseLong(materialChart[position].substring(1,7), 16);
                        break;
                    case 1:
                        colorPrev.setBackgroundColor(Color.parseColor(flatChart[position]));
                        hexText.setText(flatChart[position]);
                        colorHashless= (int)Long.parseLong(flatChart[position].substring(1,7), 16);
                        break;
                    case 2:
                        colorPrev.setBackgroundColor(Color.parseColor(webChart[position]));
                        hexText.setText(webChart[position]);
                        colorHashless= (int)Long.parseLong(webChart[position].substring(1,7), 16);
                        break;
                }
                colorPrev.setVisibility(View.VISIBLE);

                int r = (colorHashless >> 16) & 0xFF;
                int g = (colorHashless >> 8) & 0xFF;
                int b = (colorHashless >> 0) & 0xFF;
                rgbText.setText("RGB ("+r+", "+g+", "+b+")");

                ColorPickerActivity conversionMethods = new ColorPickerActivity();
                double[] rgbArray = conversionMethods.RGB(r, g, b);
                hslText.setText("HSL (" + rgbArray[0] + ", " + rgbArray[1] + ", " + rgbArray[2] + ")");

                if ((r*0.299 + g*0.587 + b*0.114) > 186){
                    hexText.setTextColor(Color.parseColor("#000000"));
                    hslText.setTextColor(Color.parseColor("#000000"));
                    rgbText.setTextColor(Color.parseColor("#000000"));
                    closeBttn.setTextColor(Color.parseColor("#000000"));

                }
                else {
                    hexText.setTextColor(Color.parseColor("#ffffff"));
                    hslText.setTextColor(Color.parseColor("#ffffff"));
                    rgbText.setTextColor(Color.parseColor("#ffffff"));
                    closeBttn.setTextColor(Color.parseColor("#ffffff"));

                }
            }
        });
        //drop shadow
        RelativeLayout dropShadow1 = (RelativeLayout) findViewById(R.id.dropShadow1);
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP,
                new int[] {ContextCompat.getColor(getApplicationContext(), R.color.real_transparent),
                        ContextCompat.getColor(getApplicationContext(), R.color.grayShadow2),
                        ContextCompat.getColor(getApplicationContext(), R.color.grayShadow)});
        gd.setCornerRadius(0f);
        dropShadow1.setBackground(gd);
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_color_chart, menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        int color = ContextCompat.getColor(getApplicationContext(),R.color.black);//Color.parseColor("#FFFF");
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

    public void closePrev(View view){
        RelativeLayout colorPrev = (RelativeLayout) findViewById(R.id.colorPrev);
        colorPrev.setVisibility(View.GONE);
    }
}
