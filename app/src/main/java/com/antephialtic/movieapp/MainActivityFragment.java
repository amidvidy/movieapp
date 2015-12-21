package com.antephialtic.movieapp;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final Uri kMovieAPIEndpoint = Uri.parse("http://api.themoviedb.org/3/");
    private static final String kMovieAPIKey = "8dbe5693fe6e40be0f45e5528a954dfb";
    private final String LOG_TAG = getClass().getSimpleName();

    public MainActivityFragment() {
    }

    GridView _movieView;
    MovieImageAdapter _movieAdapter;

    public void loadMovies() {
        new MovieImageLoader().execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        _movieView = (GridView) rootView.findViewById(R.id.movie_grid);

        _movieAdapter = new MovieImageAdapter(this.getContext());

        _movieView.setAdapter(_movieAdapter);
        _movieView.setOnItemClickListener(this);

        loadMovies();

        return rootView;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public class MovieImageAdapter extends ArrayAdapter<MovieApi.Movie> {
        public MovieImageAdapter(Context context) {
            super(context, 0, new ArrayList<MovieApi.Movie>());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MovieApi.Movie movie = getItem(position);

            ImageView imageView = (convertView != null) ? ((ImageView) convertView) : new ImageView(getContext());

            Log.i(LOG_TAG, "displaying movie: " + movie.getTitle());

            Picasso.with(getContext()).load(movie.getImageURL()).into(imageView);

            return imageView;

        }
    }

    public class MovieImageLoader extends AsyncTask<Void, Void, List<MovieApi.Movie>> {
        @Override
        protected void onPostExecute(List<MovieApi.Movie> movies) {
            Log.i(LOG_TAG, "Got " + movies.size() + " movies");

            _movieAdapter.clear();
            _movieAdapter.addAll(movies);
        }

        @Override
        protected List<MovieApi.Movie> doInBackground(Void... params) {

            Uri popularMoviesEndpoint = kMovieAPIEndpoint.buildUpon()
                    .appendPath("movie")
                    .appendPath("popular")
                    .appendQueryParameter("api_key", kMovieAPIKey)
                    .build();

            try {

                URL url = new URL(popularMoviesEndpoint.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = r.readLine()) != null) {
                    sb.append(line + "\n");
                }

                final String apiResponse = sb.toString();

                Log.i(LOG_TAG, sb.toString());

                return MovieApi.getMoviesFromJSONResponse(apiResponse);

            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Failed to parse URL", e);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Failed to connect", e);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Failed to parse", e);
            }

            return null;
        }
    }
}
