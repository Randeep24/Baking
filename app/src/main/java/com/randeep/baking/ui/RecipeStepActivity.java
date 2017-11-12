package com.randeep.baking.ui;

import android.content.res.Configuration;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.randeep.baking.R;
import com.randeep.baking.bean.RecipeSteps;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.randeep.baking.util.Constants.RECIPE_STEP;
import static com.randeep.baking.util.Constants.RECIPE_STEPS;
import static com.randeep.baking.util.Constants.STEP_NUMBER;

public class RecipeStepActivity extends AppCompatActivity {


    ArrayList<RecipeSteps> recipeSteps;
    private int stepNumber;
    RecipeStepFragment recipeStepFragment;

    @BindView(R.id.previousActionButton) FloatingActionButton previousActionButton;
    @BindView(R.id.nextActionButton) FloatingActionButton nextActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_step);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        recipeSteps = bundle.getParcelableArrayList(RECIPE_STEPS);


        if(savedInstanceState == null) {
            stepNumber = getIntent().getIntExtra(STEP_NUMBER,0);
            callFragment(stepNumber);

        } else {
            stepNumber = savedInstanceState.getInt(STEP_NUMBER);
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ||
                recipeSteps.get(stepNumber).getVideoUrl().isEmpty()) {

            showNextPreviousButton(stepNumber);
        }

        previousActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (stepNumber >= 1){
                    stepNumber--;
                    callFragment(stepNumber);
                    showNextPreviousButton(stepNumber);
                }
            }
        });

        nextActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stepNumber <= recipeSteps.size()-1){
                    stepNumber++;
                    callFragment(stepNumber);
                    showNextPreviousButton(stepNumber);
                }
            }
        });

    }

    private void showNextPreviousButton(int stepNumber){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ||
                recipeSteps.get(stepNumber).getVideoUrl().isEmpty()) {
            if (stepNumber == 0) {
                previousActionButton.setVisibility(View.GONE);
                nextActionButton.setVisibility(View.VISIBLE);
            } else if (stepNumber == recipeSteps.size() - 1) {
                nextActionButton.setVisibility(View.GONE);
                previousActionButton.setVisibility(View.VISIBLE);
            } else {
                nextActionButton.setVisibility(View.VISIBLE);
                previousActionButton.setVisibility(View.VISIBLE);
            }
        }else {
            nextActionButton.setVisibility(View.GONE);
            previousActionButton.setVisibility(View.GONE);
        }
    }


    private void callFragment(int position){

        recipeStepFragment = new RecipeStepFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(RECIPE_STEP,recipeSteps.get(position));
        recipeStepFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_step_fragment, recipeStepFragment)
                .commitAllowingStateLoss();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(STEP_NUMBER,stepNumber);
    }


}
