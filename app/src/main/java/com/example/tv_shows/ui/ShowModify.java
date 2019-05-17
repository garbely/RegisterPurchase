package com.example.tv_shows.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tv_shows.R;
import com.example.tv_shows.db.entity.Show;
import com.example.tv_shows.util.OnAsyncEventListener;
import com.example.tv_shows.viewmodel.show.ShowViewModel;

public class ShowModify extends AppCompatActivity {

    private static final String TAG = "ShowModify";

    private String showName;
    private boolean isEditMode;

    // Initiate EditTexts to change the 2 attributes
    private EditText editText1;
    private EditText editText2;

    // Button to save changed attribute/s
    private Button button;

    // Show Entity & ViewModel
    private ShowViewModel viewModel;
    private Show show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setContentView(R.layout.activity_show_modify);

        showName = getIntent().getStringExtra("showName"); // Attribute (primary key) ShowName

        initiateView(); // Associate TextViews with xml declarations & Add TextChangedListener

        // Define if EditMode or not (Add mode)
        if (showName.equals("")) {
            setTitle("Add Show");
            isEditMode = false;
        } else {
            setTitle("Edit Show");
            button.setText("Save Changes");
            isEditMode = true;
        }

        // Get Show Details & Create ViewModel
        ShowViewModel.Factory factory = new ShowViewModel.Factory(getApplication(), showName);
        viewModel = ViewModelProviders.of(this, factory).get(ShowViewModel.class);

        if (isEditMode) {
            viewModel.getShow().observe(this, showEntity -> {
                if (showEntity != null) {
                    show = showEntity;
                    editText1.setText(show.getName() + " (Not editable)"); // Inform user that Name is not editable -> Primary Key
                    editText1.setEnabled(false);
                    editText2.setText(String.valueOf(show.getDescription()));
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
        editText2 = (EditText) findViewById(R.id.description);
        button = (Button) findViewById(R.id.save);

        editText1.addTextChangedListener(loginTextWatcher);
        editText2.addTextChangedListener(loginTextWatcher);
    }

    private void saveChanges(String name, String description) {

        Intent intent;

        if (isEditMode) {
                String substringShowName = name.substring(0, name.length() - 15); // Delete part of the String -> (Not editable)
                show.setName(substringShowName);
                System.out.println(show.getName());
                show.setDescription(description);
                viewModel.updateShow(show, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "update Show: success");
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "update Show: failure", e);
                        Toast toast = Toast.makeText(getApplicationContext(), "Show couldn't be updated. Try Again.", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
            intent = new Intent(ShowModify.this, ShowDetails.class);
            intent.putExtra("showName", show.getName());
        } else {
            Show newShow = new Show(name, description);
            viewModel.createShow(newShow, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "create Show: success");
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d(TAG, "create Show: failure", e);
                    Toast toast = Toast.makeText(getApplicationContext(),"Show is already in the list. Try Again.", Toast.LENGTH_LONG);
                    toast.show(); // Because showname is primary key, name cannot exist twice
                }
            });
            intent = new Intent(ShowModify.this, MainActivity.class);
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
            String descInput = editText2.getText().toString().trim();

            button.setEnabled(!nameInput.isEmpty() && !descInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };
}