package com.randeep.baking.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.randeep.baking.data.IngredientContract.IngredientEntry;


/**
 * Created by Randeeppulp on 11/11/17.
 */

public class IngredientDbHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "ingredient.db";

    public IngredientDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String CREATE_INGREDIENT_TABLE =
                "CREATE TABLE " + IngredientEntry.TABLE_NAME + "(" +
                        IngredientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        IngredientEntry.QUANTITY + " REAL, " +
                        IngredientEntry.MEASURE + " TEXT, " +
                        IngredientEntry.INGREDIENT + " TEXT)";

        sqLiteDatabase.execSQL(CREATE_INGREDIENT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + IngredientEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
