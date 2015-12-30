package com.antephialtic.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    public MovieDetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        MovieApi.Movie movie = (MovieApi.Movie) getActivity().getIntent().getParcelableExtra(Intent.ACTION_ATTACH_DATA);

        TextView movieTitleText = (TextView) rootView.findViewById(R.id.movie_detail_title);
        movieTitleText.setText(movie.getTitle());

        ImageView moviePoster = (ImageView) rootView.findViewById(R.id.movie_detail_poster);
        Picasso.with(getContext()).load(movie.getImageURL()).into(moviePoster);

        TextView moviePlot = (TextView) rootView.findViewById(R.id.movie_detail_synopsis);
        moviePlot.setText(movie.getPlotSynopsis());

        TextView movieRating = (TextView) rootView.findViewById(R.id.movie_detail_rating);
        movieRating.setText(Double.toString(movie.getUserRating()));

        TextView movieReleaseDate = (TextView) rootView.findViewById(R.id.movie_detail_release_date);
        movieReleaseDate.setText(movie.getReleaseDate());

        return rootView;
    }
}
