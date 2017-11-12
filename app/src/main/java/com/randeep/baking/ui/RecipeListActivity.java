package com.randeep.baking.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.randeep.baking.IdlingResource.SimpleIdlingResource;
import com.randeep.baking.R;
import com.randeep.baking.adapter.RecipeListAdapter;
import com.randeep.baking.api.NetworkCall;
import com.randeep.baking.bean.Recipe;
import com.randeep.baking.util.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.randeep.baking.util.Constants.RECIPE_DETAIL;
import static com.randeep.baking.util.Constants.RECIPE_LIST;
import static com.randeep.baking.util.Constants.RECIPE_NAME;

public class RecipeListActivity extends AppCompatActivity implements
        RecipeListAdapter.RecipeListItemClickListener, NetworkCall.UpdateRecipeListListener {

    private RecipeListAdapter recipeListAdapter;
    private ArrayList<Recipe> recipeList;
    private GridLayoutManager gridLayoutManager;

    private SimpleIdlingResource idlingResource;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recipeList)
    RecyclerView recipeRecyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.errorMessage)
    TextView errorMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        setSupportActionBar(toolbar);

        if (Utility.isSW600dp(this) && getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager = new GridLayoutManager(this, 3);
        } else if (Utility.isSW600dp(this) || getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager = new GridLayoutManager(this, 2);
        } else {
            gridLayoutManager = new GridLayoutManager(this, 1);
        }


        getIdlingResource();

        recipeListAdapter = new RecipeListAdapter(this);
        recipeRecyclerView.setLayoutManager(gridLayoutManager);
        recipeRecyclerView.setAdapter(recipeListAdapter);

        if (savedInstanceState == null) {
            progressBar.setVisibility(View.VISIBLE);
            new NetworkCall(this, idlingResource);
        } else {
            progressBar.setVisibility(View.GONE);
            recipeRecyclerView.setVisibility(View.VISIBLE);

            recipeList = savedInstanceState.getParcelableArrayList(RECIPE_LIST);
            recipeListAdapter.setRecipeList(recipeList);
        }


    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new SimpleIdlingResource();
        }
        return idlingResource;
    }

    @Override
    public void recipeListItemClick(int position) {

        Bundle selectedRecipeBundle = new Bundle();
        ArrayList<Recipe> selectedRecipe = new ArrayList<>();
        selectedRecipe.add(recipeList.get(position));
        selectedRecipeBundle.putParcelableArrayList(RECIPE_DETAIL, selectedRecipe);

        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RECIPE_NAME, recipeList.get(position).getRecipeName());
        intent.putExtras(selectedRecipeBundle);
        startActivity(intent);

    }

    @Override
    public void updateRecipeList(ArrayList<Recipe> recipeArrayList) {
        progressBar.setVisibility(View.GONE);
        recipeRecyclerView.setVisibility(View.VISIBLE);
        recipeList = recipeArrayList;
        recipeListAdapter.setRecipeList(recipeArrayList);
    }

    @Override
    public void onFailure() {
        progressBar.setVisibility(View.GONE);
        recipeRecyclerView.setVisibility(View.GONE);
        errorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (Utility.isSW600dp(this) && newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager.setSpanCount(3);
        } else if (Utility.isSW600dp(this) || newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager.setSpanCount(2);
        } else {
            gridLayoutManager.setSpanCount(1);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putParcelableArrayList(RECIPE_LIST, recipeList);

        super.onSaveInstanceState(outState);
    }
}
