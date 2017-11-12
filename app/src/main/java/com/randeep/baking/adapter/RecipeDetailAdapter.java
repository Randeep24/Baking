package com.randeep.baking.adapter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.randeep.baking.R;
import com.randeep.baking.bean.Ingredients;
import com.randeep.baking.bean.Recipe;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Randeeppulp on 10/23/17.
 */

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int INGREDIENT_VIEW_HOLDER = 0;
    private static final int STEPS_VIEW_HOLDER = 1;
    private RecipeStepClickListener mRecipeStepClickListener;
    private Recipe recipe;

    public RecipeDetailAdapter(Recipe recipe, RecipeStepClickListener recipeStepClickListener) {
        this.recipe = recipe;
        mRecipeStepClickListener = recipeStepClickListener;
    }

    public interface RecipeStepClickListener{
        void recipeStepClick(int position);
    }

    class RecipeStepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.step_list_text)
        TextView stepTextView;
        public RecipeStepsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mRecipeStepClickListener.recipeStepClick(getAdapterPosition()-1);
        }
    }


    class RecipeIngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredient_list_text) TextView ingredientTextView;
        public RecipeIngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    @Override
    public int getItemCount() {
        return recipe.getSteps().size() + 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == INGREDIENT_VIEW_HOLDER) {
            View view = layoutInflater.inflate(R.layout.ingredient_item, parent, false);
            viewHolder = new RecipeIngredientViewHolder(view);
        } else {
            View view = layoutInflater.inflate(R.layout.step_list_item, parent, false);
            viewHolder = new RecipeStepsViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0){

            StringBuilder stringBuilder = new StringBuilder(holder.itemView.getContext()
                    .getString(R.string.ingredient_heading));

            for (Ingredients ingredientList: recipe.getIngredients()){
                stringBuilder.append("\u2022 "+ingredientList.getIngredient()
                        + " ( "+ ingredientList.getQuantity()
                        + ingredientList.getMeasure()+" ) \n");
            }

            ((RecipeIngredientViewHolder)holder).ingredientTextView.setText(stringBuilder.toString());

        }else {

            ((RecipeStepsViewHolder)holder).stepTextView.setText(position+". "
                    +recipe.getSteps().get(position-1).getShortDescription());
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0)
            return INGREDIENT_VIEW_HOLDER;
        else
            return STEPS_VIEW_HOLDER;

    }
}
