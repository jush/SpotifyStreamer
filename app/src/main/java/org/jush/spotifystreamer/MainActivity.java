package org.jush.spotifystreamer;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.List;
import java.util.concurrent.Executors;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.RetrofitError;
import retrofit.android.MainThreadExecutor;
import retrofit.client.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private SearchView searchView;
    private SpotifyApi spotifyApi;
    private ArtistListAdapter artistListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.result_list);
        artistListAdapter = new ArtistListAdapter();
        listView.setAdapter(artistListAdapter);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // Assumes current activity is the searchable activity
        searchView = (SearchView) findViewById(R.id.search);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        spotifyApi = new SpotifyApi(Executors.newSingleThreadExecutor(), new MainThreadExecutor());
        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    private void doMySearch(String query) {
        Timber.d("Performing '%s' query", query);
        SpotifyService service = spotifyApi.getService();
        service.searchArtists(query, new SpotifyCallback<ArtistsPager>() {
            @Override
            public void success(ArtistsPager artistsPager, Response response) {
                List<Artist> artists = artistsPager.artists.items;
                artistListAdapter.swapData(artists);
                for (int i = 0; i < artists.size(); i++) {
                    Artist artist = artists.get(i);
                    Timber.d("Found artist '%s'", artist.name);
                }
            }

            @Override
            public void failure(SpotifyError spotifyError) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        } else {
            super.onNewIntent(intent);
        }
    }
}
