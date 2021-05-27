package com.example.capstone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;




public class MainActivity extends AppCompatActivity {

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navController = Navigation.findNavController(this, R.id.main_fragment);

        if (getSupportActionBar() != null)getSupportActionBar().hide();
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController.navigateUp();
        return true;
    }
}