package com.valiantdots.prismi;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PrismiDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Prismi.db";
    PrismiDbHelper mdbHelper;


    public PrismiDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public PrismiDbHelper open() throws SQLException
    {
        mdbHelper.getReadableDatabase();
        return this;
    }

    public void close()
    {
        mdbHelper.close();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PaletteModel.PaletteTable.PALETTE_SCHEMA);
        db.execSQL(ColorModel.ColorTable.COLOR_SCHEMA);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        int upgradeTo = oldVersion + 1;
        while (upgradeTo <= newVersion)
        {
            switch (upgradeTo)
            {
                case 2:
                    /*upgrade*/
                    break;
            }
            upgradeTo++;
        }

    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
