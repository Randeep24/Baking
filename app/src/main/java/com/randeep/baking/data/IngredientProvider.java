package com.randeep.baking.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Randeeppulp on 11/11/17.
 */

public class IngredientProvider extends ContentProvider {

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private IngredientDbHelper ingredientDbHelper;

    static final int INGREDIENT = 100;


    public static UriMatcher buildUriMatcher(){

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = IngredientContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, IngredientContract.PATH_INGREDIENT, INGREDIENT);

        return matcher;
    }


    @Override
    public boolean onCreate() {

        ingredientDbHelper = new IngredientDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;

        switch (uriMatcher.match(uri)){
            case INGREDIENT:
                cursor = ingredientDbHelper.getReadableDatabase().query(
                        IngredientContract.IngredientEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );


        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase sqLiteDatabase = ingredientDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Uri returnUri;

        if (uriMatcher.match(uri) == INGREDIENT){
            long _id = sqLiteDatabase.insert(IngredientContract.IngredientEntry.TABLE_NAME,
                    null, contentValues);
            if (_id > 0) {
                returnUri = IngredientContract.IngredientEntry.buildIngredientUri(_id);
            } else {
                throw new android.database.SQLException("Failed to insert row into " + uri);
            }

        }else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase sqLiteDatabase = ingredientDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);

        int rowsDeleted;

        if ( null == selection ) selection = "1";

        switch (match){
            case INGREDIENT:
                rowsDeleted = sqLiteDatabase.delete(IngredientContract.IngredientEntry.TABLE_NAME,selection,selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        final SQLiteDatabase sqLiteDatabase = ingredientDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);

        int rowsUpdated;

        switch (match) {
            case INGREDIENT:
                rowsUpdated = sqLiteDatabase.update(IngredientContract.IngredientEntry.TABLE_NAME,
                        contentValues, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;

    }
}
