package com.example.database.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.database.R;

public class AuthActivity extends AppCompatActivity {
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        loadView();

    }

    private void loadView() {
        navController = (NavController) Navigation.findNavController(this, R.id.nav_host_fragment);
    }

    @Override
    public void supportNavigateUpTo(@NonNull Intent intent) {
        if (navController != null) {
            navController.navigateUp();
        }
    }
}