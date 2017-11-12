package com.randeep.baking.api;

import com.randeep.baking.idlingResource.SimpleIdlingResource;
import com.randeep.baking.bean.Recipe;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.randeep.baking.util.Constants.BASE_URL;
import static com.randeep.baking.util.Constants.CONNECTION_TIMEOUT;

/**
 * Created by Randeeppulp on 10/29/17.
 */

public class NetworkCall {

    private static final String LOG_TAG = NetworkCall.class.getSimpleName();
    private RecipeApi recipeApi;
    private UpdateRecipeListListener updateRecipeListListener;
    private SimpleIdlingResource idlingResource;

    public NetworkCall(UpdateRecipeListListener updateRecipeListListener, SimpleIdlingResource idlingResource){


        this.idlingResource = idlingResource;

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recipeApi = retrofit.create(RecipeApi.class);

        this.updateRecipeListListener = updateRecipeListListener;

        if (this.idlingResource != null) {
            this.idlingResource.setIdleState(false);
        }

        fetchRecipeList();
    }

    public interface UpdateRecipeListListener{
        void updateRecipeList(ArrayList<Recipe> recipeArrayList);
        void onFailure();
    }

    private void fetchRecipeList(){

        Call<ArrayList<Recipe>> recipeCall = recipeApi.getRecipe();

        recipeCall.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {

                ArrayList<Recipe> recipeArrayList = response.body();
                updateRecipeListListener.updateRecipeList(recipeArrayList);
                if (idlingResource != null) {
                    idlingResource.setIdleState(true);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                updateRecipeListListener.onFailure();
            }
        });
    }
}
