package com.atrio.quesapp.storeData;

import android.content.Context;
import android.content.SharedPreferences;

import com.atrio.quesapp.model.ListData;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Admin on 30-06-2017.
 */

public class CorrectList {

    public static final String PREFS_NAME = "PRODUCT_APP";
    public static final String FAVORITES = "Product_Favorite";
    public static  final String ALLQUESTION = "AllQuestionStatus";

    public CorrectList() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, List<ListData> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.apply();
    }

    public void saveAllQuestion(Context context, List<ListData> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(ALLQUESTION, jsonFavorites);

        editor.apply();
    }

    public void addFavorite(Context context, ListData product) {
        List<ListData> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<ListData>();
        favorites.add(product);
        saveFavorites(context, favorites);
    }
    public void addAllQuestion(Context context, ListData product) {
        List<ListData> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<ListData>();
        favorites.add(product);
        saveAllQuestion(context, favorites);
    }

    public void removeFavorite(Context context) {

        SharedPreferences settings;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        settings.edit().clear().apply();

    }

    public ArrayList<ListData> getFavorites(Context context) {
        SharedPreferences settings;
        List<ListData> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            ListData[] favoriteItems = gson.fromJson(jsonFavorites,
                    ListData[].class);
            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<ListData>(favorites);
        } else
            return null;

        return (ArrayList<ListData>) favorites;
    }

    public ArrayList<ListData> getAllQuestion(Context context) {
        SharedPreferences settings;
        List<ListData> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(ALLQUESTION)) {
            String jsonFavorites = settings.getString(ALLQUESTION, null);
            Gson gson = new Gson();
            ListData[] favoriteItems = gson.fromJson(jsonFavorites,
                    ListData[].class);
            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<ListData>(favorites);
        } else
            return null;

        return (ArrayList<ListData>) favorites;
    }


}
