package com.antephialtic.movieapp;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

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

    public static List<Movie> getMoviesFromJSONResponse(String jsonResponse) throws JSONException {
        ArrayList<Movie> out = new ArrayList<Movie>();

        JSONObject resp = new JSONObject(jsonResponse);

        JSONArray movies = resp.getJSONArray("results");

        for (int i = 0; i < movies.length(); ++i) {
            JSONObject movie = movies.getJSONObject(i);

            out.add(new Movie(
                    movie.getLong("id"),
                    movie.getString("original_title"),
                    movie.getString("poster_path"),
                    movie.getString("overview"),
                    movie.getString("release_date"),
                    movie.getDouble("vote_average")
            ));
        }

        return out;
    }

    public static class Movie implements Parcelable {
        public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
            @Override
            public Movie createFromParcel(Parcel source) {
                return new Movie(source);
            }

            @Override
            public Movie[] newArray(int size) {
                return new Movie[size];
            }
        };
        private final long id;
        private final String title;
        private final String imageURL;
        private final String plotSynopsis;
        private final String releaseDate;
        private final double userRating;

        public Movie(long id, String title, String imageURL, String plotSynopsis, String releaseDate, double userRating) {
            this.id = id;
            this.title = title;
            this.imageURL = kImageAPIEndpoint.buildUpon()
                    .appendPath(imageURL.substring(1, imageURL.length())).build().toString();
            this.plotSynopsis = plotSynopsis;
            this.releaseDate = releaseDate;
            this.userRating = userRating;
        }

        public Movie(Parcel parcel) {
            this.id = parcel.readLong();
            this.title = parcel.readString();
            this.imageURL = parcel.readString();
            this.plotSynopsis = parcel.readString();
            this.releaseDate = parcel.readString();
            this.userRating = parcel.readDouble();
        }

        public String getPlotSynopsis() {
            return plotSynopsis;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public double getUserRating() {
            return userRating;
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(id);
            dest.writeString(title);
            dest.writeString(imageURL);
            dest.writeString(plotSynopsis);
            dest.writeString(releaseDate);
            dest.writeDouble(userRating);
        }
    }

}
