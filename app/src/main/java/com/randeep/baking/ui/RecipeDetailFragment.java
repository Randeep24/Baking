package com.randeep.baking.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.randeep.baking.R;
import com.randeep.baking.adapter.RecipeDetailAdapter;
import com.randeep.baking.bean.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.randeep.baking.util.Constants.RECIPE_DETAIL;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailFragment extends Fragment implements RecipeDetailAdapter.RecipeStepClickListener {


    @BindView(R.id.recipe_detail) RecyclerView recipeRecyclerView;

    private RecipeDetailAdapter recipeDetailAdapter;
    private Recipe recipe;

    private OnStepClickListener onStepClickListener;

    public RecipeDetailFragment() {


    }


    public interface OnStepClickListener{
        void openStep(int stepNumber);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onStepClickListener = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Bundle bundle = getActivity().getIntent().getExtras();
        ArrayList<Recipe> recipes = bundle.getParcelableArrayList(RECIPE_DETAIL);
        recipe = recipes.get(0);

        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, rootView);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);

        recipeDetailAdapter = new RecipeDetailAdapter(recipe, this);

        recipeRecyclerView.setLayoutManager(linearLayoutManager);
        recipeRecyclerView.setAdapter(recipeDetailAdapter);

        return rootView;
    }

    @Override
    public void recipeStepClick(int position) {

        onStepClickListener.openStep(position);

    }
}
