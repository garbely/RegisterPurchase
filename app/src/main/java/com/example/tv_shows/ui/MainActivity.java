package com.example.tv_shows.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.tv_shows.R;
import com.example.tv_shows.db.entity.Fromagerie;
import com.example.tv_shows.viewmodel.fromagerie.FromagerieListViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Required Showlist parameters associated to ViewModel
    private List<Fromagerie> fromagerieList;
    private ListView listview;
    private FromagerieListViewModel viewModel;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Fromageries");

        // Create List by ViewModel
        listview = findViewById(R.id.listview);

        fromagerieList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), FromagerieDetails.class);

                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NO_ANIMATION
                );
                intent.putExtra("fromagerieName", fromagerieList.get(position).getName());
                startActivity(intent);
            }
        });

        FromagerieListViewModel.Factory factory = new FromagerieListViewModel.Factory(getApplication());
        viewModel = ViewModelProviders.of(this, factory).get(FromagerieListViewModel.class);
        viewModel.getFromageries().observe(this, showEntities -> {
            if (showEntities != null) {
                fromagerieList = showEntities;
                Collections.sort(fromagerieList);
                adapter.clear();
                adapter.addAll(fromagerieList);
                setListViewHeightBasedOnChildren(listview);
            }
        });
        listview.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);

        // Hide unused Action Bar icons

        MenuItem item1 = menu.findItem(R.id.delete);
        item1.setVisible(false);
        this.invalidateOptionsMenu();

        MenuItem item2 = menu.findItem(R.id.edit);
        item2.setVisible(false);
        this.invalidateOptionsMenu();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = getIntent();

        switch (item.getItemId()) {

            // Insert Fromagerie Function
            case R.id.add:
                intent = new Intent(MainActivity.this, FromagerieModify.class);
                intent.putExtra("fromagerieName", ""); // need to give a default String value "" that the FromagerieModify activity understands it is AddMode, not EditMode
                break;

            // Settings
            case R.id.action_settings:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                break;
        }
        intent.setFlags(
                Intent.FLAG_ACTIVITY_NO_ANIMATION |
                        Intent.FLAG_ACTIVITY_NO_HISTORY
        );
        startActivity(intent);
        return super.onOptionsItemSelected(item);
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