package com.randeep.baking.api;

import com.randeep.baking.bean.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Randeeppulp on 10/29/17.
 */

public interface RecipeApi {

    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipe();
}
