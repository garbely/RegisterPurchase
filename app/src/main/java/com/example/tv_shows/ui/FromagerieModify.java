package com.example.tv_shows.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tv_shows.R;
import com.example.tv_shows.db.entity.Fromagerie;
import com.example.tv_shows.util.OnAsyncEventListener;
import com.example.tv_shows.viewmodel.fromagerie.FromagerieViewModel;

public class FromagerieModify extends AppCompatActivity {

    private static final String TAG = "FromagerieModify";

    private String fromagerieName;
    private boolean isEditMode;

    // Initiate EditTexts to change the 2 attributes
    private EditText editText1;
    private EditText editText2;

    // Button to save changed attribute/s
    private Button button;

    // Fromagerie Entity & ViewModel
    private FromagerieViewModel viewModel;
    private Fromagerie fromagerie;

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
        setContentView(R.layout.activity_fromagerie_modify);

        fromagerieName = getIntent().getStringExtra("fromagerieName"); // Attribute (primary key) ShowName

        initiateView(); // Associate TextViews with xml declarations & Add TextChangedListener

        // Define if EditMode or not (Add mode)
        if (fromagerieName.equals("")) {
            setTitle("Add Fromagerie");
            isEditMode = false;
        } else {
            setTitle("Edit Fromagerie");
            button.setText("Save Changes");
            isEditMode = true;
        }

        // Get Fromagerie Details & Create ViewModel
        FromagerieViewModel.Factory factory = new FromagerieViewModel.Factory(getApplication(), fromagerieName);
        viewModel = ViewModelProviders.of(this, factory).get(FromagerieViewModel.class);

        if (isEditMode) {
            viewModel.getFromagerie().observe(this, showEntity -> {
                if (showEntity != null) {
                    fromagerie = showEntity;
                    editText1.setText(fromagerie.getName() + " (Not editable)"); // Inform user that Name is not editable -> Primary Key
                    editText1.setEnabled(false);
                    editText2.setText(fromagerie.getLocation());
                }
            });
        }

        button.setOnClickListener(view -> {
            saveChanges(editText1.getText().toString(),
                    editText2.getText().toString()
            );
            onBackPressed();
        });
    }

    private void initiateView() {
        editText1 = (EditText) findViewById(R.id.name);
        editText2 = (EditText) findViewById(R.id.location);
        button = (Button) findViewById(R.id.save);

        editText1.addTextChangedListener(loginTextWatcher);
        editText2.addTextChangedListener(loginTextWatcher);
    }

    private void saveChanges(String name, String location) {

        Intent intent;

        if (isEditMode) {
                String substringFromagerieName = name.substring(0, name.length() - 15); // Delete part of the String -> (Not editable)
                fromagerie.setName(substringFromagerieName);
                fromagerie.setLocation((location));
                System.out.println(fromagerie.getName());
                viewModel.updateFromagerie(fromagerie, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "update Fromagerie: success");
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "update Fromagerie: failure", e);
                        Toast toast = Toast.makeText(getApplicationContext(), "Fromagerie couldn't be updated. Try Again.", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
            intent = new Intent(FromagerieModify.this, FromagerieDetails.class);
            intent.putExtra("fromagerieName", fromagerie.getName());
        } else {
            Fromagerie newFromagerie = new Fromagerie(name, location);
            viewModel.createFromagerie(newFromagerie, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "create Fromagerie: success");
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d(TAG, "create Fromagerie: failure", e);
                    Toast toast = Toast.makeText(getApplicationContext(),"Fromagerie is already in the list. Try Again.", Toast.LENGTH_LONG);
                    toast.show(); // Because showname is primary key, name cannot exist twice
                }
            });
            intent = new Intent(FromagerieModify.this, MainActivity.class);
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
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String nameInput = editText1.getText().toString().trim();

            button.setEnabled(!nameInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };
}