package com.example.mymovies.data;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mymovies.data.FavoriteMovie;
import com.example.mymovies.data.Movie;
import com.example.mymovies.data.MovieDatabase;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainViewModel extends AndroidViewModel {
    private static MovieDatabase database;
    private LiveData<List<Movie>> movies;
    private LiveData<List<FavoriteMovie>> favoriteMovies;

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i("dbg", "ViewModel cleared");
    }

    public MainViewModel(@NonNull Application application) {
        super(application);
        Log.i("dbg", "MainViewModelCalled");
        database = MovieDatabase.getInstance(getApplication());
        movies = database.movieDao().getAllMovies();
        favoriteMovies = database.movieDao().getAllFavoriteMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public Movie getMovieById(int id) {
        GetMovieTask getMovieTask = new GetMovieTask();
        try {
            Movie movie = getMovieTask.execute(id).get();
            return movie;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertMovie(Movie movie) {
        new InsertTask().execute(movie);
    }

    public void deleteMovie(Movie movie) {
        new DeleteMovieTask().execute(movie);
    }

    public void deleteAllMovies() {
        new DeleteAllMoviesTask().execute();
    }

    private static class GetMovieTask extends AsyncTask<Integer, Void, Movie> {
        @Override
        protected Movie doInBackground(Integer... integers) {
            if (integers != null && integers.length > 0)
                return database.movieDao().getMovieById(integers[0]);
            return null;
        }
    }

    private static class InsertTask extends AsyncTask<Movie, Void, Void> {
        @Override
        protected Void doInBackground(Movie... movies) {
            if (movies != null && movies.length > 0)
                database.movieDao().insertMovie(movies[0]);
            return null;
        }
    }

    private static class DeleteMovieTask extends AsyncTask<Movie, Void, Void> {
        @Override
        protected Void doInBackground(Movie... movies) {
            if (movies != null && movies.length > 0)
                database.movieDao().deleteMovie(movies[0]);
            return null;
        }
    }

    private static class DeleteAllMoviesTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            if (database != null)
                database.movieDao().deleteAllMovies();
            return null;
        }
    }

    //FavoriteMovies methods
    public LiveData<List<FavoriteMovie>> getFavoriteMovies() {
        return favoriteMovies;
    }

    public void insertFavoriteMovie(FavoriteMovie movie) {
        new InsertFavoriteMovieTask().execute(movie);
    }

    public void deleteFavoriteMovie(FavoriteMovie movie) {
        new DeleteFavoriteMovieTask().execute(movie);
    }

    public FavoriteMovie getFavoriteMovieById(int id) {
        try {
            return new GetFavoriteMovieTask().execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    //FavoriteMovie background tasks
    private static class InsertFavoriteMovieTask extends AsyncTask<FavoriteMovie, Void, Void> {
        @Override
        protected Void doInBackground(FavoriteMovie... movies) {
            if (movies != null && movies.length > 0)
                database.movieDao().insertFavoriteMovie(movies[0]);
            return null;
        }
    }

    private static class DeleteFavoriteMovieTask extends AsyncTask<FavoriteMovie, Void, Void> {
        @Override
        protected Void doInBackground(FavoriteMovie... movies) {
            if (movies != null && movies.length > 0)
                database.movieDao().deleteFavoriteMovie(movies[0]);
            return null;
        }
    }

    private static class GetFavoriteMovieTask extends AsyncTask<Integer, Void, FavoriteMovie> {
        @Override
        protected FavoriteMovie doInBackground(Integer... Id) {
            if (Id != null && Id.length > 0) {
                return database.movieDao().getFavoriteMovieById(Id[0]);
            }
            return null;
        }
    }
}