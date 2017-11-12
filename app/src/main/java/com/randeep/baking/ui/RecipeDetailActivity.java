package com.randeep.baking.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.randeep.baking.R;
import com.randeep.baking.bean.Ingredients;
import com.randeep.baking.bean.Recipe;
import com.randeep.baking.bean.RecipeSteps;
import com.randeep.baking.data.IngredientContract;
import com.randeep.baking.util.Prefs;
import com.randeep.baking.widget.IngredientWidget;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.randeep.baking.util.Constants.RECIPE_DETAIL;
import static com.randeep.baking.util.Constants.RECIPE_NAME;
import static com.randeep.baking.util.Constants.RECIPE_STEP;
import static com.randeep.baking.util.Constants.RECIPE_STEPS;
import static com.randeep.baking.util.Constants.STEP_NUMBER;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailFragment.OnStepClickListener {

    private boolean twoPane;
    private ArrayList<Recipe> recipes;
    private String recipeName;

    @BindView(R.id.toolbar) Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        ButterKnife.bind(this);

        recipeName = getIntent().getStringExtra(RECIPE_NAME);
        Bundle bundle = getIntent().getExtras();
        recipes = bundle.getParcelableArrayList(RECIPE_DETAIL);

        ArrayList<RecipeSteps> recipeSteps = (ArrayList<RecipeSteps>) recipes.get(0).getSteps();

        toolbar.setTitle(recipeName);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.inflateMenu(R.menu.detail_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int id = item.getItemId();

                switch (id){
                    case R.id.add_widget:
                        addDataWidget();
                        break;

                }

                return true;
            }
        });


        if (findViewById(R.id.recipeStepFragment) != null) {
            twoPane = true;
            if (savedInstanceState == null) {
                RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipeDetailFragment, recipeDetailFragment)
                        .commitAllowingStateLoss();

                RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
                Bundle bundlefragment = new Bundle();
                bundlefragment.putParcelable(RECIPE_STEP, recipeSteps.get(0));
                recipeStepFragment.setArguments(bundlefragment);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipeStepFragment, recipeStepFragment)
                        .commitAllowingStateLoss();
            }

        } else {
            twoPane = false;
            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipeDetailFragment, recipeDetailFragment)
                    .commitAllowingStateLoss();
        }


    }

    @Override
    public void openStep(int stepNumber) {
        if (twoPane) {

            RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
            Bundle bundlefragment = new Bundle();
            bundlefragment.putParcelable(RECIPE_STEP, recipes.get(0).getSteps().get(stepNumber));
            recipeStepFragment.setArguments(bundlefragment);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipeStepFragment, recipeStepFragment)
                    .commitAllowingStateLoss();

        } else {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(RECIPE_STEPS, (ArrayList<? extends Parcelable>) recipes.get(0).getSteps());
            Intent intent = new Intent(this, RecipeStepActivity.class);
            intent.putExtra(RECIPE_NAME, recipeName);
            intent.putExtra(STEP_NUMBER, stepNumber);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }


    private void addDataWidget(){

        Prefs prefs = new Prefs(this);
        prefs.setRecipeName(recipeName);

        Cursor cursor = null;
        try {

            cursor = getContentResolver().query(IngredientContract.IngredientEntry.CONTENT_URI,
                    new String[]{IngredientContract.IngredientEntry._ID},
                    null,
                    null,
                    null);

            if (cursor != null && cursor.moveToFirst()){

                do {
                    getContentResolver().delete(IngredientContract.IngredientEntry.CONTENT_URI,
                            IngredientContract.IngredientEntry._ID + " = ?",
                            new String[]{cursor.getInt(0) + ""});
                }while (cursor.moveToNext());

                cursor.close();
            }


            ArrayList<Ingredients> ingredientsArrayList = (ArrayList<Ingredients>) recipes.get(0).getIngredients();


            for(Ingredients ingredients: ingredientsArrayList) {

                ContentValues contentValues = new ContentValues();
                contentValues.put(IngredientContract.IngredientEntry.QUANTITY, ingredients.getQuantity());
                contentValues.put(IngredientContract.IngredientEntry.MEASURE, ingredients.getMeasure());
                contentValues.put(IngredientContract.IngredientEntry.INGREDIENT, ingredients.getIngredient());

                getContentResolver().insert(IngredientContract.IngredientEntry.CONTENT_URI,contentValues);

            }

            int[] ids = AppWidgetManager.getInstance(getApplication())
                    .getAppWidgetIds(new ComponentName(getApplication(), IngredientWidget.class));
            IngredientWidget ingredientWidget = new IngredientWidget();
            ingredientWidget.onUpdate(this, AppWidgetManager.getInstance(this), ids);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            ComponentName thisWidget = new ComponentName(this, IngredientWidget.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ingredient_list);


        }catch (Exception e){

        }finally {
            if (cursor!= null)cursor.close();
        }

    }
}
