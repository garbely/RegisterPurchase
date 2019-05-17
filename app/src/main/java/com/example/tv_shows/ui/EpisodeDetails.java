package com.example.tv_shows.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tv_shows.R;
import com.example.tv_shows.db.entity.Episode;
import com.example.tv_shows.util.OnAsyncEventListener;
import com.example.tv_shows.viewmodel.episode.EpisodeViewModel;

public class EpisodeDetails extends AppCompatActivity {

    private static final String TAG = "EpisodeDetails";

    // Episode Entity & ViewModel
    private Episode episode;
    private EpisodeViewModel viewModel;

    // 3 TextViews for all attributes
    private TextView tvEpisodeName;
    private TextView tvEpisodeNumber;
    private TextView tvLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setContentView(R.layout.activity_episode_details);
        setTitle("Episode Details");

        // Get the showname and the ID of the episode chosen by the user
        String idEpisode = getIntent().getStringExtra("idEpisode");
        String showName = getIntent().getStringExtra("showName");

        // Associate TextViews with xml declarations
        initiateView();

        // Get Episode Details & Create ViewModel
        EpisodeViewModel.Factory factory = new EpisodeViewModel.Factory(getApplication(), idEpisode, showName);
        viewModel = ViewModelProviders.of(this, factory).get(EpisodeViewModel.class);
        viewModel.getEpisode().observe(this, episodeEntity -> {
            if (episodeEntity != null) {
                episode = episodeEntity;
                updateContent();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);

        // Hide "Add" & "Settings icons in action bar

        MenuItem item1 = menu.findItem(R.id.add);
        item1.setVisible(false);
        this.invalidateOptionsMenu();

        MenuItem item2 = menu.findItem(R.id.action_settings);
        item2.setVisible(false);
        this.invalidateOptionsMenu();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = getIntent();

        switch(item.getItemId()){

            // Update Function
            case R.id.edit:
                intent = new Intent(EpisodeDetails.this, EpisodeModify.class);
                intent.putExtra("idEpisode", episode.getId()); // give episode ID to the EpisodeModify activity
                break;

            // Delete Function
            case R.id.delete:
                viewModel.deleteEpisode(episode, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "Episode Details: success");
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "Episode Details: failure", e);
                        Toast toast = Toast.makeText(getApplicationContext(),"Episode couldn't be deleted. Try Again.", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
                intent = new Intent(EpisodeDetails.this, ShowDetails.class);
                break;
        }
        intent.putExtra("showName", episode.getShowName()); // give ShowName to the ShowDetails activity
        intent.setFlags(
                Intent.FLAG_ACTIVITY_NO_ANIMATION |
                        Intent.FLAG_ACTIVITY_NO_HISTORY
        );
        finish();
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    private void initiateView() {
        tvEpisodeName = findViewById(R.id.name);
        tvEpisodeNumber = findViewById(R.id.number);
        tvLength = findViewById(R.id.length);
    }

    private void updateContent() {
        if (episode != null) {
            tvEpisodeName.setText(episode.getName());
            tvEpisodeNumber.setText("Episode Number: #" + episode.getId());
            tvLength.setText("Length: " + episode.getLength() + " min");
        }
    }
}
