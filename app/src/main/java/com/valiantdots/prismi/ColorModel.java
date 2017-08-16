package com.valiantdots.prismi;

import android.provider.BaseColumns;

public final class ColorModel {
    ColorModel(){}
    public static final String COMMA=", ";
    public static abstract class ColorTable implements BaseColumns {
        public static final String TABLE_NAME="Color";
        public static final String _ID = BaseColumns._ID;
        public static final String PALETTE_ID ="paletteId";
        public static final String COLOR="color";



        public static final String COLOR_SCHEMA="CREATE TABLE "+TABLE_NAME+" ("+
                "_id INTEGER PRIMARY KEY"+COMMA+COLOR+" TEXT"+COMMA+ PALETTE_ID +" INTEGER"+");";


    }

}
