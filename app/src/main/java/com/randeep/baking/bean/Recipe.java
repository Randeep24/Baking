package com.randeep.baking.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Randeeppulp on 10/26/17.
 */

public class Recipe implements Parcelable{

    @SerializedName("id")
    int recipeId;

    @SerializedName("name")
    String recipeName;

    @SerializedName("ingredients")
    List<Ingredients> ingredients = null;

    @SerializedName("steps")
    List<RecipeSteps> steps = null;

    @SerializedName("servings")
    int servings;

    protected Recipe(Parcel in) {
        recipeId = in.readInt();
        recipeName = in.readString();
        ingredients = in.createTypedArrayList(Ingredients.CREATOR);
        steps = in.createTypedArrayList(RecipeSteps.CREATOR);
        servings = in.readInt();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public int getRecipeId() {
        return recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public List<RecipeSteps> getSteps() {
        return steps;
    }

    public int getServings() {
        return servings;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(recipeId);
        parcel.writeString(recipeName);
        parcel.writeTypedList(ingredients);
        parcel.writeTypedList(steps);
        parcel.writeInt(servings);
    }
}
