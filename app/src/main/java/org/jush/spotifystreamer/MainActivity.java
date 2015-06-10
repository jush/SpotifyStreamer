package org.jush.spotifystreamer;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

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
    private ListView listView;
    private TextView messageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageView = (TextView) findViewById(R.id.message);
        listView = (ListView) findViewById(R.id.result_list);
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
                if (artistsPager == null || artistsPager.artists == null || artistsPager.artists
                        .items == null || artistsPager.artists.items
                        .isEmpty()) {
                    showNoResultsFound();
                } else {
                    // Make sure the list is visible in case it was hidden due to no results found
                    listView.setVisibility(View.VISIBLE);
                    List<Artist> artists = artistsPager.artists.items;
                    artistListAdapter.swapData(artists);
                }
            }

            @Override
            public void failure(SpotifyError spotifyError) {
                showErrorMessage();
            }

            @Override
            public void failure(RetrofitError error) {
                showErrorMessage();
            }
        });
    }

    private void showErrorMessage() {
        showMessage(R.string.query_error);
    }

    private void showNoResultsFound() {
        showMessage(R.string.no_artists_found);
    }

    private void showMessage(@StringRes int messageId) {
        messageView.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        messageView.setText(messageId);
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
