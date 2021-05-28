package com.example.capstone.activities.log;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.capstone.R;
import com.example.capstone.activities.log.adapter.LogAdapter;
import com.example.capstone.databinding.ActivityLogsBinding;
import com.example.capstone.model.Logs;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class LogsActivity extends AppCompatActivity {
    private ActivityLogsBinding mBinding;
    private LogsViewModel mLogsViewModel;
    private List<Logs> logsList;
    private LogAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityLogsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mLogsViewModel = ViewModelProviders.of(this).get(LogsViewModel.class);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        init();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    @Override
    protected void onStart() {
        // Hides the clear button and shows progress bar on start
        mBinding.clearLogsButton.hide();
        mBinding.progressBar.setVisibility(View.VISIBLE);
        super.onStart();
    }

    private void init() {
        // Creates a new array-list object on initialisation
        logsList = new ArrayList<>();

        // I've set the linear layout manager in the layout file
        // mBinding.logsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // get all the log list live data from the view model
        mLogsViewModel.getmLogsList().observe(this, logs -> {
            mBinding.progressBar.setVisibility(View.INVISIBLE);
            adapter = new LogAdapter(LogsActivity.this, logs);
            mBinding.logsRecyclerView.setAdapter(adapter);
            if (logs.isEmpty()) {
                mBinding.clearLogsButton.hide();
                findViewById(R.id.emptyListImage).setVisibility(View.VISIBLE);
            } else {
                mBinding.clearLogsButton.show();
                mBinding.clearLogsButton.setOnClickListener(v -> {
                    mLogsViewModel.clearLogs();
                    showSnackBar("Logs Cleared");
                });
                mBinding.emptyListImage.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * Snack-bar Method
     * Gets String As Input
     */
    private void showSnackBar(String message) {
        Snackbar.make(mBinding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
//        try {
//            Intent adIntent = new Intent().setClassName(this, "com.aravi.dot.ads.AdvertisementActivity");
//            startActivity(adIntent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.aravi.dot.ads.AdvertisementActivity");
//        if (launchIntent != null) {
//            startActivity(launchIntent);//null pointer check in case package name was not found
//        }
        finish();
    }
}