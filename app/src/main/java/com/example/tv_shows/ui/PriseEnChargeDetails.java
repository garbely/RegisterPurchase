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
import com.example.tv_shows.db.entity.PriseEnCharge;
import com.example.tv_shows.util.OnAsyncEventListener;
import com.example.tv_shows.viewmodel.priseEnCharge.PriseEnChargeViewModel;

public class PriseEnChargeDetails extends AppCompatActivity {

    private static final String TAG = "PriseEnChargeDetails";

    // PriseEnCharge Entity & ViewModel
    private PriseEnCharge priseEnCharge;
    private PriseEnChargeViewModel viewModel;

    // 3 TextViews for all attributes
    private TextView tvDate;
    private TextView tvDatePeser;
    private TextView tvNombre;
    private TextView tvSorte;
    private TextView tvPoids;
    private TextView tvQualite;
    private TextView tvPrixKilo;
    private TextView tvReduction;
    private TextView tvPrixFinale;
    private TextView tvRemarque;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setContentView(R.layout.activity_charge_details);
        setTitle("PriseEnCharge Details");

        // Get the showname and the ID of the priseEnCharge chosen by the user
        String idEpisode = getIntent().getStringExtra("idPriseEnCharge");
        String showName = getIntent().getStringExtra("fromagiereName");

        // Associate TextViews with xml declarations
        initiateView();

        // Get PriseEnCharge Details & Create ViewModel
        PriseEnChargeViewModel.Factory factory = new PriseEnChargeViewModel.Factory(getApplication(), idEpisode, showName);
        viewModel = ViewModelProviders.of(this, factory).get(PriseEnChargeViewModel.class);
        viewModel.getPriseEnCharge().observe(this, episodeEntity -> {
            if (episodeEntity != null) {
                priseEnCharge = episodeEntity;
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
                intent = new Intent(PriseEnChargeDetails.this, PriseEnChargeModify.class);
                intent.putExtra("idPriseEnCharge", priseEnCharge.getId()); // give priseEnCharge ID to the PriseEnChargeModify activity
                break;

            // Delete Function
            case R.id.delete:
                viewModel.deletePriseEnCharge(priseEnCharge, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "PriseEnCharge Details: success");
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "PriseEnCharge Details: failure", e);
                        Toast toast = Toast.makeText(getApplicationContext(),"PriseEnCharge couldn't be deleted. Try Again.", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
                intent = new Intent(PriseEnChargeDetails.this, FromagerieDetails.class);
                break;
        }
        intent.putExtra("fromagerieName", priseEnCharge.getFromagerieName()); // give ShowName to the FromagerieDetails activity
        intent.setFlags(
                Intent.FLAG_ACTIVITY_NO_ANIMATION |
                        Intent.FLAG_ACTIVITY_NO_HISTORY
        );
        finish();
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    private void initiateView() {
        tvDate = findViewById(R.id.name);
        tvDatePeser = findViewById(R.id.date_peser);
        tvNombre = findViewById(R.id.nombre);
        tvSorte = findViewById(R.id.sorte);
        tvPoids = findViewById(R.id.poids);
        tvQualite = findViewById(R.id.qualite);
        tvPrixKilo = findViewById(R.id.prixKilo);
        tvReduction = findViewById(R.id.reduction);
        tvPrixFinale = findViewById(R.id.prixFinale);
        tvRemarque = findViewById(R.id.remarque);
    }

    private void updateContent() {
        if (priseEnCharge != null) {
            tvDate.setText(priseEnCharge.getDate());
            tvDatePeser.setText("Date psesés le: " + priseEnCharge.getDate_peser());
            tvNombre.setText("Nombre: " + priseEnCharge.getNombre());
            tvSorte.setText("Sorte: " + priseEnCharge.getSorte());
            tvPoids.setText("Poids: " + priseEnCharge.getPoids() + " kg");
            tvQualite.setText("Qualité: " + priseEnCharge.getQualite());
            tvPrixKilo.setText("Prix par Kilo: " + priseEnCharge.getPrixKilo() + " Fr.");
            tvReduction.setText("Réduction: " + priseEnCharge.getReduction() + " %");
            tvPrixFinale.setText("Prix Finale: " + priseEnCharge.getPrixFinale() + " Fr.");
            tvRemarque.setText("Remarques: " + priseEnCharge.getRemarques());

        }
    }
}
