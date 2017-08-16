package com.valiantdots.prismi;

import android.provider.BaseColumns;

public final class PaletteModel {
    PaletteModel(){}
    public static final String COMMA=", ";
    public static abstract class PaletteTable implements BaseColumns {
        public static final String TABLE_NAME="Palette";
        public static final String _ID = BaseColumns._ID;
        public static final String PALETTE_NAME ="paletteName";

        public static final String PALETTE_SCHEMA="CREATE TABLE "+TABLE_NAME+" ("+
                "_id INTEGER PRIMARY KEY"+COMMA+ PALETTE_NAME +" TEXT"+");";

        public static final String INSERT_ROW ="INSERT INTO "+TABLE_NAME+" ("+PALETTE_NAME+") VALUES ("+"'Blue Oasis'"+");";
        public static final String INSERT_ROW2 ="INSERT INTO "+TABLE_NAME+" ("+PALETTE_NAME+") VALUES ("+"'Sahara Winds'"+");";
        public static final String INSERT_ROW3 ="INSERT INTO "+TABLE_NAME+" ("+PALETTE_NAME+") VALUES ("+"'Pink Dilemna'"+");";
        public static final String INSERT_ROW4 ="INSERT INTO "+TABLE_NAME+" ("+PALETTE_NAME+") VALUES ("+"'Oranges For All'"+");";
        public static final String INSERT_ROW5 ="INSERT INTO "+TABLE_NAME+" ("+PALETTE_NAME+") VALUES ("+"'Sky Palette'"+");";
        public static final String INSERT_ROW6 ="INSERT INTO "+TABLE_NAME+" ("+PALETTE_NAME+") VALUES ("+"'Valley Pines'"+");";
        public static final String INSERT_ROW7 ="INSERT INTO "+TABLE_NAME+" ("+PALETTE_NAME+") VALUES ("+"'Harsh Winds'"+");";



    }

}
