package com.example.tv_shows.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tv_shows.R;
import com.example.tv_shows.db.entity.Episode;
import com.example.tv_shows.db.entity.Show;
import com.example.tv_shows.util.OnAsyncEventListener;
import com.example.tv_shows.viewmodel.episode.EpisodeListViewModel;
import com.example.tv_shows.viewmodel.show.ShowViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ShowDetails extends AppCompatActivity {

    private static final String TAG = "ShowDetails";

    // Show Entity & ViewModel
    private Show show;
    private ShowViewModel vmShow;

    // TextViews for all information about the Show
    private TextView tvShowname;
    private TextView tvNumberEpisodes;
    private TextView tvDescription;

    // Listview for episode information (associated to EpisodeList ViewModel)
    private ListView listview;
    private List<Episode> episodeList;
    private EpisodeListViewModel vmEpisodeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setContentView(R.layout.activity_show_details);
        setTitle("Show Details");

        // Get the showname of the show chosen by the user
        String showName = getIntent().getStringExtra("showName");

        // Associate TextViews with xml declarations
        initiateView();

        // Get Show Details & Create ViewModels including List of Episodes
        ShowViewModel.Factory showFac = new ShowViewModel.Factory(getApplication(), showName);
        vmShow = ViewModelProviders.of(this, showFac).get(ShowViewModel.class);
        vmShow.getShow().observe(this, showEntity -> {
            if (showEntity != null) {
                show = showEntity;
                updateContent();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);

        MenuItem item1 = menu.findItem(R.id.action_settings);
        item1.setVisible(false);
        this.invalidateOptionsMenu();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = getIntent();

        switch (item.getItemId()) {

            // Insert Episode to Show
            case R.id.add:
                intent = new Intent(ShowDetails.this, EpisodeModify.class);
                intent.putExtra("showName", show.getName());
                break;

            // Function "Update Show"
            case R.id.edit:
                intent = new Intent(ShowDetails.this, ShowModify.class);
                intent.putExtra("showName", show.getName());
                break;

            // Function "Delete Show"
            case R.id.delete:
                vmShow.deleteShow(show, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "Show Details: success");
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "Show Details: failure", e);
                        Toast toast = Toast.makeText(getApplicationContext(),"Show couldn't be deleted. Try Again.", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
                intent = new Intent(ShowDetails.this, MainActivity.class);  // go back to mainpage (List of shows)
                break;
        }
        intent.setFlags(
                Intent.FLAG_ACTIVITY_NO_ANIMATION |
                        Intent.FLAG_ACTIVITY_NO_HISTORY
        );
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    private void initiateView() {
        tvShowname = findViewById(R.id.name);
        tvNumberEpisodes = findViewById(R.id.episodes);
        tvDescription = findViewById(R.id.description);
        listview = findViewById(R.id.listview);
    }

    private void updateContent() {
        if (show != null) {
            createEpisodeList();
            tvShowname.setText(show.getName());
            tvDescription.setText(show.getDescription());
        }
    }

    private void createEpisodeList() {
        episodeList = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        EpisodeListViewModel.Factory episodesFac = new EpisodeListViewModel.Factory(getApplication(), show.getName());
        vmEpisodeList = ViewModelProviders.of(this, episodesFac).get(EpisodeListViewModel.class);
        vmEpisodeList.getEpisodes().observe(this, episodeEntities -> {
            if (episodeEntities != null) {
                episodeList = episodeEntities;
                episodeList.sort(Comparator.comparingInt(Episode::getNumber));
                adapter.clear();
                adapter.addAll(episodeList);
                setListViewHeightBasedOnChildren(listview); // To stretch the listView dynamically, so it's not only showing the first object in the listview
                tvNumberEpisodes.setText(episodeList.size() + " episodes"); // show the amount of saved episodes by show (dynamic)
            }
        });
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), EpisodeDetails.class);

                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                Intent.FLAG_ACTIVITY_NO_HISTORY
                );

                intent.putExtra("idEpisode", episodeList.get(position).getId()); // give episode id parameter so next activity knows the desired episode
                intent.putExtra("showName", show.getName()); // give episode id parameter so next activity knows the desired episode
                startActivity(intent);
            }
        });
    }

    // Method to list all list items instead of only the first item of the list

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
