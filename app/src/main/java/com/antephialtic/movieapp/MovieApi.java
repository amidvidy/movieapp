package com.antephialtic.movieapp;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amidvidy on 12/20/15.
 */
public class MovieApi {

    private static final Uri kImageAPIEndpoint = Uri.parse("http://image.tmdb.org/t/p/w185/");

    public static class Movie {
        public Movie(long id, String title, String imageURL) {
            this.id = id;
            this.title = title;
            this.imageURL = kImageAPIEndpoint.buildUpon().appendPath(imageURL.substring(1, imageURL.length())).build().toString();
        }

        public long getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getImageURL() {
            return imageURL;
        }

        private final long id;
        private final String title;
        private final String imageURL;
    }

    public static List<Movie> getMoviesFromJSONResponse(String jsonResponse) throws JSONException {
        ArrayList<Movie> out = new ArrayList<Movie>();

        JSONObject resp = new JSONObject(jsonResponse);

        JSONArray movies = resp.getJSONArray("results");

        for (int i = 0; i < movies.length(); ++i) {
            JSONObject movie = movies.getJSONObject(i);

            out.add(new Movie(
                    movie.getLong("id"),
                    movie.getString("title"),
                    movie.getString("poster_path")
            ));
        }

        return out;
    }

}
