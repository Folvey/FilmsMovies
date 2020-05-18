package com.example.mymovies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovies.R;
import com.example.mymovies.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private OnPosterClickListener onPosterClickListener;
    private OnReachEndListener onReachEndListener;

    public MovieAdapter() {
        movies = new ArrayList<>();
    }

    public interface OnPosterClickListener {
        void onPosterClick(int position);
    }

    public interface OnReachEndListener {
        void onReachEnd();
    }

    public void setOnPosterClickListener(OnPosterClickListener onPosterClickListener) {
        this.onPosterClickListener = onPosterClickListener;
    }

    public void setOnReachEndListener(OnReachEndListener onReachEndListener) {
        this.onReachEndListener = onReachEndListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
//        Log.i("dbg", "onBindViewHolder started");
        if (movies.size() >= 20 && position == movies.size() - 4  && onReachEndListener != null) {
            onReachEndListener.onReachEnd();
        }
        Movie movie = movies.get(position);
        Picasso.get().load(movie.getPosterPath()).into(holder.imageViewSmallPoster);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewSmallPoster;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageViewSmallPoster = itemView.findViewById(R.id.imageViewSmallPoster);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPosterClickListener != null) {
                        onPosterClickListener.onPosterClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void addMovies(ArrayList<Movie> movies) {
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public void clear() {
        this.movies.clear();
        notifyDataSetChanged();
    }

}
