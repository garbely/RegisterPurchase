package com.example.tv_shows.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tv_shows.R;
import com.example.tv_shows.db.entity.PriseEnCharge;
import com.example.tv_shows.util.OnAsyncEventListener;
import com.example.tv_shows.viewmodel.priseEnCharge.PriseEnChargeViewModel;

public class PriseEnChargeModify extends AppCompatActivity {

    private static final String TAG = "PriseEnChargeModify";

    private String fromagerieName;

    // Initiate EditTexts to change the 3 attributes
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private EditText editText5;
    private EditText editText6;
    private EditText editText7;
    private EditText editText8;
    private EditText editText9;
    private EditText editText10;

    // Button to save changed attribute/s
    private Button button;

    // Boolean that distinguishes between Add and Update Function
    private boolean isEditMode;

    // PriseEnCharge Entity & ViewModel
    private PriseEnChargeViewModel viewModel;
    private PriseEnCharge priseEnCharge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setContentView(R.layout.activity_priseEnCharge_modify);

        fromagerieName = getIntent().getStringExtra("fromagerieName"); // Attribute (foreign key) ShowName -> needed if new PriseEnCharge is created
        System.out.println("0: " + fromagerieName);
        String idPriseEnCharge = getIntent().getStringExtra("idPriseEnCharge"); // Attribute (primary key) episodeID -> needed if existing PriseEnCharge is updated

        initiateView(); // Associate TextViews with xml declarations & Add TextChangedListener

        if (idPriseEnCharge==null) {
            setTitle("Add PriseEnCharge");
            isEditMode = false;
        } else {
            setTitle("Edit PriseEnCharge");
            button.setText("Save Changes"); // Changing Text in the button if EditMode
            isEditMode = true;
        }

        // Get PriseEnCharge Details & Create ViewModel
        PriseEnChargeViewModel.Factory factory = new PriseEnChargeViewModel.Factory(getApplication(), idPriseEnCharge, fromagerieName);
        viewModel = ViewModelProviders.of(this, factory).get(PriseEnChargeViewModel.class);
        if (isEditMode) {
            viewModel.getPriseEnCharge().observe(this, episodeEntity -> {
                if (episodeEntity != null) {
                    priseEnCharge = episodeEntity;
                    editText1.setText(priseEnCharge.getDate());
                    editText2.setText(priseEnCharge.getDate_peser());
                    editText3.setText(priseEnCharge.getNombre());
                    editText4.setText(priseEnCharge.getSorte());
                    editText5.setText(priseEnCharge.getPoids());
                    editText6.setText(priseEnCharge.getQualite());
                    editText7.setText(priseEnCharge.getPrixKilo());
                    editText8.setText(priseEnCharge.getReduction());
                    editText9.setText(priseEnCharge.getPrixFinale());
                    editText10.setText(priseEnCharge.getRemarques());
                }
            });
        }

        button.setOnClickListener(view -> {
            saveChanges(editText1.getText().toString(),
                    editText2.getText().toString(),
                    editText3.getText().toString(),
                    editText4.getText().toString(),
                    editText5.getText().toString(),
                    editText6.getText().toString(),
                    editText7.getText().toString(),
                    editText8.getText().toString(),
                    editText9.getText().toString(),
                    editText10.getText().toString()
            );
            onBackPressed();
        });
    }

    private void initiateView() {
        editText1 = (EditText) findViewById(R.id.name);
        editText2 = (EditText) findViewById(R.id.date_peser);
        editText3 = (EditText) findViewById(R.id.nombre);
        editText4 = (EditText) findViewById(R.id.sorte);
        editText5 = (EditText) findViewById(R.id.poids);
        editText6 = (EditText) findViewById(R.id.qualite);
        editText7 = (EditText) findViewById(R.id.prixKilo);
        editText8 = (EditText) findViewById(R.id.reduction);
        editText9 = (EditText) findViewById(R.id.prixFinale);
        editText10 = (EditText) findViewById(R.id.remarque);
        button = (Button) findViewById(R.id.save);
/*
        editText1.addTextChangedListener(loginTextWatcher);
        editText2.addTextChangedListener(loginTextWatcher);
        editText3.addTextChangedListener(loginTextWatcher);*/
    }

    private void saveChanges(String date, String date_peser,
                             String nombre, String sorte, String poids,
                             String qualite, String prixKilo, String prixFinale,
                             String reduction, String remarques) {

        Intent intent = new Intent(PriseEnChargeModify.this, FromagerieDetails.class);

        if (isEditMode) {
            priseEnCharge.setDate(date);
            priseEnCharge.setDate_peser(date_peser);
            priseEnCharge.setNombre(nombre);
            priseEnCharge.setSorte(sorte);
            priseEnCharge.setPoids(poids);
            priseEnCharge.setQualite(qualite);
            priseEnCharge.setPrixKilo(prixKilo);
            priseEnCharge.setReduction(reduction);
            priseEnCharge.setPrixFinale(prixFinale);
            priseEnCharge.setRemarques(remarques);

            viewModel.updatePriseEnCharge(priseEnCharge, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "update PriseEnCharge: success");
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d(TAG, "update PriseEnCharge: failure", e);
                    Toast toast = Toast.makeText(getApplicationContext(), "PriseEnCharge couldn't be updated. Try Again.", Toast.LENGTH_LONG);
                    toast.show();
                }
            });
            intent.putExtra("fromagerieName", priseEnCharge.getFromagerieName()); // give ShowName to next activity

        } else {
            PriseEnCharge newPriseEnCharge = new PriseEnCharge(date, date_peser, nombre, sorte,
                    poids, qualite, prixKilo, prixFinale, reduction, remarques, fromagerieName);

            viewModel.createPriseEnCharge(newPriseEnCharge, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "create PriseEnCharge: success");
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d(TAG, "create PriseEnCharge: failure", e);
                    Toast toast = Toast.makeText(getApplicationContext(), "PriseEnCharge couldn't be inserted. Try Again.", Toast.LENGTH_LONG);
                    toast.show();
                }
            });
            intent.putExtra("fromagerieName", fromagerieName);  // give ShowName to next activity
        }
        intent.setFlags(
                Intent.FLAG_ACTIVITY_NO_ANIMATION |
                        Intent.FLAG_ACTIVITY_NO_HISTORY
        );
        startActivity(intent);
    }

    // TextWatcher class, to enable the Button only if Textfields are not empty
    /*
    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String nameInput = editText1.getText().toString().trim();
            String numberInput = editText2.getText().toString().trim();
            String lenghtInput = editText3.getText().toString().trim();

            button.setEnabled(!nameInput.isEmpty() && !numberInput.isEmpty() && !lenghtInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };*/
}