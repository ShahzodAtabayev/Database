package com.example.database.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.database.database.LocalStorage;
import com.example.database.R;


public class SplashFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Handler handler = new Handler(Looper.myLooper());
        handler.postDelayed(() -> {
            if (!LocalStorage.isLogin()) {
                NavHostFragment.findNavController(this).navigate(R.id.action_splashFragment_to_signInFragment);
            } else {
                requireActivity().finish();
                NavHostFragment.findNavController(this).navigate(R.id.action_splashFragment_to_mainActivity);
            }
        }, 2000);
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}