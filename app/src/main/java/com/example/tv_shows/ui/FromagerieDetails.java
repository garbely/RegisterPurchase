package com.example.tv_shows.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tv_shows.R;
import com.example.tv_shows.db.entity.Fromagerie;
import com.example.tv_shows.db.entity.PriseEnCharge;
import com.example.tv_shows.util.OnAsyncEventListener;
import com.example.tv_shows.viewmodel.priseEnCharge.PriseEnChargeListViewModel;
import com.example.tv_shows.viewmodel.fromagerie.FromagerieViewModel;

import java.util.ArrayList;
import java.util.List;

public class FromagerieDetails extends AppCompatActivity {

    private static final String TAG = "FromagerieDetails";

    // Fromagerie Entity & ViewModel
    private Fromagerie fromagerie;
    private FromagerieViewModel vmShow;

    // TextViews for all information about the Fromagerie
    private TextView tvFromageriename;
    private TextView tvNumberPrisEnCharges;
    private TextView tvLocation;

    // Listview for prisEnCharges information (associated to PrisEnChargesList ViewModel)
    private ListView listview;
    private List<PriseEnCharge> priseEnChargeList;
    private PriseEnChargeListViewModel vmPrisEnChargesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ActionBar bar = getSupportActionBar();
        if(bar!=null){
            TextView tv = new TextView(getApplicationContext());
            ViewGroup.LayoutParams lp = new RelativeLayout.LayoutParams(
                    android.app.ActionBar.LayoutParams.MATCH_PARENT, // Width of TextView
                    android.app.ActionBar.LayoutParams.WRAP_CONTENT); // Height of TextView
            tv.setLayoutParams(lp);
            tv.setText(bar.getTitle());
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            bar.setCustomView(tv);
        }

        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setContentView(R.layout.activity_fromagerie_details);
        setTitle("Fromagerie Details");

        // Get the fromagerieName of the fromagerie chosen by the user
        String fromagerieName = getIntent().getStringExtra("fromagerieName");

        // Associate TextViews with xml declarations
        initiateView();

        // Get Fromagerie Details & Create ViewModels including List of Episodes
        FromagerieViewModel.Factory showFac = new FromagerieViewModel.Factory(getApplication(), fromagerieName);
        vmShow = ViewModelProviders.of(this, showFac).get(FromagerieViewModel.class);
        vmShow.getFromagerie().observe(this, showEntity -> {
            if (showEntity != null) {
                fromagerie = showEntity;
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

            // Insert PriseEnCharge to Fromagerie
            case R.id.add:
                intent = new Intent(FromagerieDetails.this, PriseEnChargeModify.class);
                intent.putExtra("fromagerieName", fromagerie.getName());
                break;

            // Function "Update Fromagerie"
            case R.id.edit:
                intent = new Intent(FromagerieDetails.this, FromagerieModify.class);
                intent.putExtra("fromagerieName", fromagerie.getName());
                break;

            // Function "Delete Fromagerie"
            case R.id.delete:
                vmShow.deleteFromagerie(fromagerie, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "Fromagerie Details: success");
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "Fromagerie Details: failure", e);
                        Toast toast = Toast.makeText(getApplicationContext(),"Fromagerie couldn't be deleted. Try Again.", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
                intent = new Intent(FromagerieDetails.this, MainActivity.class);  // go back to mainpage (List of shows)
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
        tvFromageriename = findViewById(R.id.name);
        tvNumberPrisEnCharges = findViewById(R.id.episodes);
        tvLocation = findViewById(R.id.location);
        listview = findViewById(R.id.listview);
    }

    private void updateContent() {
        if (fromagerie != null) {
            createPrisEnCHargeList();
            tvFromageriename.setText(fromagerie.getName());
            tvLocation.setText(fromagerie.getLocation());
        }
    }

    private void createPrisEnCHargeList() {
        priseEnChargeList = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                /// Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text size 25 dip for ListView each item
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);

                // Return the view
                return view;
            }
        };

        PriseEnChargeListViewModel.Factory episodesFac = new PriseEnChargeListViewModel.Factory(getApplication(), fromagerie.getName());
        vmPrisEnChargesList = ViewModelProviders.of(this, episodesFac).get(PriseEnChargeListViewModel.class);
        vmPrisEnChargesList.getPriseEnCharges().observe(this, episodeEntities -> {
            if (episodeEntities != null) {
                priseEnChargeList = episodeEntities;
                //priseEnChargeList.sort(Comparator.comparingInt(PriseEnCharge::getNumber));
                adapter.clear();
                adapter.addAll(priseEnChargeList);
                setListViewHeightBasedOnChildren(listview); // To stretch the listView dynamically, so it's not only showing the first object in the listview
                tvNumberPrisEnCharges.setText(priseEnChargeList.size() + " entrées"); // fromagerie the amount of saved episodes by fromagerie (dynamic)
            }
        });
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), PriseEnChargeDetails.class);

                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                Intent.FLAG_ACTIVITY_NO_HISTORY
                );

                intent.putExtra("idPriseEnCharge", priseEnChargeList.get(position).getId()); // give episode id parameter so next activity knows the desired episode
                intent.putExtra("fromagiereName", fromagerie.getName()); // give episode id parameter so next activity knows the desired episode
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
