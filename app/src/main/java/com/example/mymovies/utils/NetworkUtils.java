package com.example.mymovies.utils;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class NetworkUtils {

    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    private static final String BASE_URL_VIDEOS = "https://api.themoviedb.org/3/movie/%s/videos";
    private static final String BASE_URL_REVIEWS = "https://api.themoviedb.org/3/movie/%s/reviews";

    private static final String PARAMS_API_KEY = "api_key";
    private static final String PARAMS_LANGUAGE = "language";
    private static final String PARAMS_SORT_BY = "sort_by";
    private static final String PARAMS_PAGE = "page";
    private static final String PARAMS_MIN_VOTE_COUNT = "vote_count.gte";

    private static String API_KEY = "44cde46891c59f4f9134040d8a78e336";
    private static final String LANGUAGE_VALUE = "ru-RU";
    private static final String SORT_BY_POPULARITY = "popularity.desc";
    private static final String SORT_BY_RATING = "vote_average.desc";
    private static final String VOTE_COUNT = "700";

    public static final int POPULARITY = 0;
    public static final int RATING = 1;

    public static URL buildURL(int sortBy, int page) {
        URL url = null;
        String methodOfSort;

        if (sortBy == POPULARITY)
            methodOfSort = SORT_BY_POPULARITY;
        else
            methodOfSort= SORT_BY_RATING;
        Uri uri = Uri.parse(BASE_URL).buildUpon()
        .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE_VALUE)
                .appendQueryParameter(PARAMS_SORT_BY, methodOfSort)
                .appendQueryParameter(PARAMS_PAGE, Integer.toString(page))
                .appendQueryParameter(PARAMS_MIN_VOTE_COUNT, VOTE_COUNT)
                .build();
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildURLToVideos(int id) {
        Uri uri = Uri.parse(String.format(BASE_URL_VIDEOS, id)).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE_VALUE)
                .build();
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static URL buildURLToReviews(int movieId) {
        Uri uri = Uri.parse(String.format(BASE_URL_REVIEWS, movieId)).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .build();
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getJSONFromNetwork(int sortBy, int page) {
        JSONObject jsonObject = null;
        URL url = buildURL(sortBy, page);
        try {
            jsonObject = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject getJSONForVideos(int id) {
        JSONObject jsonObject = null;
        URL url = buildURLToVideos(id);
        try {
            jsonObject = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject getJSONForReviews(int id) {
        JSONObject jsonObject = null;
        URL url = buildURLToReviews(id);
        try {
            jsonObject = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static class JSONLoader extends AsyncTaskLoader<JSONObject> {

        private Bundle bundle;
        private OnStartLoadingListener onStartLoadingListener;

        public interface OnStartLoadingListener {
            void onStartLoading();
        }

        public void setOnStartLoadingListener(OnStartLoadingListener onStartLoadingListener) {
            this.onStartLoadingListener = onStartLoadingListener;
        }

        public JSONLoader(@NonNull Context context, Bundle bundle) {
            super(context);
            this.bundle = bundle;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if (onStartLoadingListener != null) {
                onStartLoadingListener.onStartLoading();
            }
            forceLoad();
        }

        @Nullable
        @Override
        public JSONObject loadInBackground() {
            Log.i("dbg", "JSONLoader started");
            if (bundle == null)
                return null;
            String urlAsString = bundle.getString("url");
            URL url = null;
            try {
                url = new URL(urlAsString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            JSONObject result = null;
            if (url == null) {
                return null;
            }
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder data = new StringBuilder();
                String line = reader.readLine();
                while (line != null) {
                    data.append(line);
                    line = reader.readLine();
                }
                result = new JSONObject(data.toString());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
            }
            Log.i("dbg", "jsonLoadTask over");
            return result;
        }
    }

    private static class JSONLoadTask extends AsyncTask<URL, Void, JSONObject> {
        protected JSONObject doInBackground(URL... urls) {
            Log.i("dbg", "jsonloadTask started");
            JSONObject result = null;
            if (urls == null || urls.length == 0) {
                return null;
            }
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) urls[0].openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder data = new StringBuilder();
                String line = reader.readLine();
                while (line != null) {
                    data.append(line);
                    line = reader.readLine();
                }
                result = new JSONObject(data.toString());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
            }
            Log.i("dbg", "jsonLoadTask over");
            return result;
        }
    }

}