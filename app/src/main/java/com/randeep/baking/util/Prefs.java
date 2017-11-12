package com.randeep.baking.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Randeeppulp on 11/11/17.
 */

public class Prefs {

    SharedPreferences sharedPreferences;

    public Prefs(Context context){

        sharedPreferences = context.getSharedPreferences("BAKING_PREF", Context.MODE_PRIVATE);

    }

    public void setRecipeName(String recipeName){
        sharedPreferences.edit().putString("RECIPE_NAME",recipeName).apply();
    }

    public String getRecipeName(){
        return sharedPreferences.getString("RECIPE_NAME","");
    }

//    public void setRecipeIngredient(String ingredient){
//        sharedPreferences.edit().("RECIPE_INGREDIENT", ingredient).apply();
//    }
//
//    public String getRecipeIngredient(){
//        return sharedPreferences.getString("RECIPE_INGREDIENT","");
//    }
}
