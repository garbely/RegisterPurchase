package com.example.tv_shows.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tv_shows.R;
import com.example.tv_shows.db.entity.PriseEnCharge;
import com.example.tv_shows.util.OnAsyncEventListener;
import com.example.tv_shows.viewmodel.priseEnCharge.PriseEnChargeViewModel;

public class EpisodeModify extends AppCompatActivity {

    private static final String TAG = "EpisodeModify";

    private String showName;

    // Initiate EditTexts to change the 3 attributes
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;

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
        setContentView(R.layout.activity_episode_modify);

        showName = getIntent().getStringExtra("showName"); // Attribute (foreign key) ShowName -> needed if new PriseEnCharge is created
        System.out.println("0: " + showName);
        String idEpisode = getIntent().getStringExtra("idEpisode"); // Attribute (primary key) episodeID -> needed if existing PriseEnCharge is updated

        initiateView(); // Associate TextViews with xml declarations & Add TextChangedListener

        if (idEpisode==null) {
            setTitle("Add PriseEnCharge");
            isEditMode = false;
        } else {
            setTitle("Edit PriseEnCharge");
            button.setText("Save Changes"); // Changing Text in the button if EditMode
            isEditMode = true;
        }

        // Get PriseEnCharge Details & Create ViewModel
        PriseEnChargeViewModel.Factory factory = new PriseEnChargeViewModel.Factory(getApplication(), idEpisode, showName);
        viewModel = ViewModelProviders.of(this, factory).get(PriseEnChargeViewModel.class);
        if (isEditMode) {
            viewModel.getPriseEnCharge().observe(this, episodeEntity -> {
                if (episodeEntity != null) {
                    priseEnCharge = episodeEntity;
                    editText1.setText(priseEnCharge.getName());
                    editText2.setText(priseEnCharge.getId());
                    editText2.setEnabled(false);
                    editText3.setText(String.valueOf(priseEnCharge.getLength()));
                }
            });
        }

        button.setOnClickListener(view -> {
            saveChanges(editText1.getText().toString(),
                    editText2.getText().toString(),
                    editText3.getText().toString()
            );
            onBackPressed();
        });
    }

    private void initiateView() {
        editText1 = (EditText) findViewById(R.id.date);
        editText2 = (EditText) findViewById(R.id.date_peser);
        editText3 = (EditText) findViewById(R.id.nombre);
        button = (Button) findViewById(R.id.save);

        editText1.addTextChangedListener(loginTextWatcher);
        editText2.addTextChangedListener(loginTextWatcher);
        editText3.addTextChangedListener(loginTextWatcher);
    }

    private void saveChanges(String name, String id, String length) {

        Intent intent = new Intent(EpisodeModify.this, ShowDetails.class);

        if (isEditMode) {
            priseEnCharge.setName(name);
            priseEnCharge.setId(id);
            priseEnCharge.setLength(length);
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
            intent.putExtra("showName", priseEnCharge.getShowName()); // give ShowName to next activity

        } else {
            PriseEnCharge newPriseEnCharge = new PriseEnCharge(id, name, length, showName, , , , , , , , );
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
            intent.putExtra("showName", showName);  // give ShowName to next activity
        }
        intent.setFlags(
                Intent.FLAG_ACTIVITY_NO_ANIMATION |
                        Intent.FLAG_ACTIVITY_NO_HISTORY
        );
        startActivity(intent);
    }

    // TextWatcher class, to enable the Button only if Textfields are not empty
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
    };
}