package com.randeep.baking.adapter;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.randeep.baking.R;
import com.randeep.baking.bean.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.randeep.baking.util.Constants.ICOMOON_FONT;

/**
 * Created by Randeeppulp on 10/22/17.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;
    private RecipeListItemClickListener recipeListItemClickListener;

    public interface RecipeListItemClickListener {
        void recipeListItemClick(int position);
    }

    public RecipeListAdapter(RecipeListItemClickListener recipeListItemClickListener) {

        this.recipeListItemClickListener = recipeListItemClickListener;
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.recipe_icon) TextView recipeIcon;
        @BindView(R.id.recipe_name) TextView heading;
        @BindView(R.id.servings) TextView serving;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            Typeface icomoonFont = Typeface.createFromAsset(itemView.getContext().getAssets(), ICOMOON_FONT);
            recipeIcon.setTypeface(icomoonFont);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recipeListItemClickListener.recipeListItemClick(getAdapterPosition());
        }
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {

        holder.heading.setText(recipeList.get(position).getRecipeName());
        holder.serving.setText("Servings: " + recipeList.get(position).getServings());
    }

    @Override
    public int getItemCount() {
        if (recipeList == null) return 0;
        return recipeList.size();
    }

    public void setRecipeList(ArrayList<Recipe> recipeList) {
        this.recipeList = recipeList;
        notifyDataSetChanged();
    }

}
