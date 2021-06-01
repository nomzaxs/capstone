package com.example.capstone.activities.log;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.capstone.R;
import com.example.capstone.activities.log.adapter.LogAdapter;
import com.example.capstone.databinding.ActivityLogsBinding;
import com.example.capstone.model.Logs;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class LogsActivity extends AppCompatActivity implements ViewModelStoreOwner {
    private ViewModelProvider.AndroidViewModelFactory viewModelFactory;
    private ViewModelStore viewModelStore = new ViewModelStore();
    private ActivityLogsBinding mBinding;
    private LogsViewModel mLogsViewModel;
    private List<Logs> logsList;
    private LogAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityLogsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        if(viewModelFactory == null){
            viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance((getApplication()));
        }
        mLogsViewModel = new ViewModelProvider(this, viewModelFactory).get(LogsViewModel.class);
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
        // 지우기 버튼을 숨기고 시작 시 진행 표시줄을 표시합니다.
        mBinding.clearLogsButton.hide();
        mBinding.progressBar.setVisibility(View.VISIBLE);
        super.onStart();
    }

    private void init() {
        // 초기화 시 새로운 array-list object 생성
        logsList = new ArrayList<>();

        // view model 에서 모든 로그 목록 실시간 데이터 가져오기
        mLogsViewModel.getmLogsList().observe(this, logs -> {
            mBinding.progressBar.setVisibility(View.INVISIBLE);
            adapter = new LogAdapter(LogsActivity.this, logs);
            mBinding.logsRecyclerView.setAdapter(adapter);

            // 로그가 없을 시 로그 삭제 버튼이 표시되지 않는다.
            if (logs.isEmpty()) {
                mBinding.clearLogsButton.hide();
                findViewById(R.id.emptyListImage).setVisibility(View.VISIBLE);
            } else {
                mBinding.clearLogsButton.show();
                mBinding.clearLogsButton.setOnClickListener(v -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LogsActivity.this);
                    builder.setTitle(R.string.clear_dialog_title);
                    builder.setMessage(R.string.clear_dialog_description);
                    builder.setPositiveButton(R.string.clear_dialog_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mLogsViewModel.clearLogs();
                            showSnackBar("로그 삭제됨");
                        }
                    });
                    builder.setNegativeButton(R.string.clear_dialog_no, null);
                    builder.create().show();
                });
                mBinding.emptyListImage.setVisibility(View.INVISIBLE);
            }
        });
    }

    // Snack-bar 코드
    private void showSnackBar(String message) {
        Snackbar.make(mBinding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}