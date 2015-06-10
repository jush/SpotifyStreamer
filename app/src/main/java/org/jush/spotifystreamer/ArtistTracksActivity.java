package org.jush.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.android.MainThreadExecutor;
import retrofit.client.Response;

public class ArtistTracksActivity extends AppCompatActivity {

    private static final String ARTIST_ID_ARG = "ARTIST_ID_ARG";
    private static final String ARTIST_NAME_ARG = "ARTIST_NAME_ARG";

    private static final Map<String, Object> TOP_TRACK_QUERY_PARAMS = new HashMap<>();

    static {
        TOP_TRACK_QUERY_PARAMS.put("country", "FI");
    }

    private ListView listView;
    private TrackListAdapter trackListAdapter;
    private TextView messageView;

    public static Intent newStartIntent(Context context, ParcelableArtist artist) {
        return new Intent(context, ArtistTracksActivity.class).putExtra(ARTIST_ID_ARG, artist
                .getId())
                .putExtra(ARTIST_NAME_ARG, artist.getName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_tracks);

        String artistName = getIntent().getStringExtra(ARTIST_NAME_ARG);
        getSupportActionBar().setTitle(R.string.top_10_tracks);
        getSupportActionBar().setSubtitle(artistName);

        messageView = (TextView) findViewById(R.id.message);

        listView = (ListView) findViewById(R.id.tracks_list);

        trackListAdapter = new TrackListAdapter();
        listView.setAdapter(trackListAdapter);

        SpotifyApi spotifyApi = new SpotifyApi(Executors.newSingleThreadExecutor(), new
                MainThreadExecutor());
        String artistId = getIntent().getStringExtra(ARTIST_ID_ARG);
        spotifyApi.getService()
                .getArtistTopTrack(artistId, TOP_TRACK_QUERY_PARAMS, new Callback<Tracks>() {
                    @Override
                    public void success(Tracks tracks, Response response) {
                        if (tracks == null || tracks.tracks == null || tracks.tracks.isEmpty()) {
                            showNoTracksFound();
                        } else {
                            messageView.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                            trackListAdapter.swapData(tracks.tracks);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        showNoTracksFound();
                    }
                });
    }

    private void showNoTracksFound() {
        messageView.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        String artistName = getIntent().getStringExtra(ARTIST_NAME_ARG);
        messageView.setText(getString(R.string.no_tracks_found, artistName));

    }
}
