package com.example.database.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.database.R;
import com.example.database.data.UserModel;
import com.example.database.database.LocalStorage;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NavController navController;
    AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolBar));
        loadView();
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        setTitle("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getParentActivityIntent() == null) {
                    Log.i("TAG", "You have forgotten to specify the parentActivityName in the AndroidManifest!");
                    onBackPressed();
                } else {
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void loadView() {
        navController = Navigation.findNavController(this, R.id.home_fragment);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        ImageButton btLogOut = (ImageButton) findViewById(R.id.buttonLogout);
        ImageButton btShowHistories = (ImageButton) findViewById(R.id.buttonShowHistory);
        btShowHistories.setOnClickListener(a -> {
            navController.navigate(R.id.action_profileFragment_to_historiesFragment);
        });
        btLogOut.setOnClickListener(a -> new AlertDialog.Builder(this)
                .setTitle("Are you sure")
                .setMessage("Are you sure you want to exit this app?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    LocalStorage.setLogin(false);
                    LocalStorage.setUser(new UserModel());
                    navController.navigate(R.id.action_profileFragment_to_authActivity);
                    finish();
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show());
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            switch (destination.getId()) {
                case R.id.homeFragment: {
                    showBottomNavigationView();
                    hideBackButton();
                    hideLogout();
                    hideHistory();
                    hideAddStudent();
                    break;
                }
                case R.id.calendarFragment: {
                    hideAddStudent();
                    hideLogout();
                    hideBottomNavigationView();
                    hideHistory();
                    showBackButton();
                    break;
                }
                case R.id.profileFragment: {
                    showLogout();
                    hideAddStudent();
                    showBottomNavigationView();
                    showHistory();
                    hideBackButton();
                    break;
                }
                case R.id.groupsFragment: {
                    hideLogout();
                    hideBottomNavigationView();
                    hideHistory();
                    hideAddStudent();
                    showBackButton();
                    break;
                }
                case R.id.studentsFragment: {
                    hideLogout();
                    hideBottomNavigationView();
                    hideHistory();
                    hideHistory();
                    showBackButton();
                    break;
                }
                case R.id.historiesFragment: {
                    hideBottomNavigationView();
                    hideAddStudent();
                    hideLogout();
                    hideHistory();
                    showBackButton();
                    break;
                }
                default: {
                    hideHistory();
                    hideLogout();
                    hideAddStudent();
                    hideBackButton();
                }
            }
        });
    }

    @Override
    public void supportNavigateUpTo(@NonNull Intent intent) {
        if (navController != null) {
            NavigationUI.navigateUp(navController, appBarConfiguration);
        }
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        if (navController != null) {
//            NavigationUI.navigateUp(navController, appBarConfiguration);
//        }
//        return super.onSupportNavigateUp();
//    }

    /*override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.home_fragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }*/


    void showLogout() {
        ImageButton logOut = (ImageButton) findViewById(R.id.buttonLogout);
        logOut.setVisibility(View.VISIBLE);
    }

    void hideLogout() {
        ImageButton logOut = (ImageButton) findViewById(R.id.buttonLogout);
        logOut.setVisibility(View.GONE);
    }

    void showAddStudent() {
        ImageButton logOut = (ImageButton) findViewById(R.id.buttonAddStudent);
        logOut.setVisibility(View.VISIBLE);
    }

    void hideAddStudent() {
        ImageButton logOut = (ImageButton) findViewById(R.id.buttonAddStudent);
        logOut.setVisibility(View.GONE);
    }

    void showHistory() {
        ImageButton logOut = (ImageButton) findViewById(R.id.buttonShowHistory);
        logOut.setVisibility(View.VISIBLE);
    }

    void hideHistory() {
        ImageButton logOut = (ImageButton) findViewById(R.id.buttonShowHistory);
        logOut.setVisibility(View.GONE);
    }

    void hideBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);
    }

    void showBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    private void showBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void hideBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }
}