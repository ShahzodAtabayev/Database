package com.example.database.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.example.database.database.LocalStorage;
import com.example.database.R;
import com.example.database.data.UserModel;
import com.example.database.base.BaseFragment;
import com.example.database.databinding.FragmentSignInBinding;

import java.util.List;


public class SignInFragment extends BaseFragment {
    public SignInFragment() {
    }

    FragmentSignInBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btSignUp.setOnClickListener(a -> NavHostFragment.findNavController(this).navigate(R.id.action_signInFragment_to_signUpFragment));
        binding.btSignIn.setOnClickListener(a -> login()
        );
    }

    private void login() {
        if (binding.inputLogin.getText().toString().length() == 0) {
            makeToast("enter login");
            return;
        }
        if (binding.inputPassword.getText().toString().length() < 6) {
            makeToast("Password length must be 6 character.");
            return;
        }
        runOnWorkerThread(() -> {
            List<UserModel> ls = db.getAllUsers();
            runOnUIThread(() -> {
                boolean isFound = false;
                for (int i = 0; i < ls.size(); i++) {
                    if (ls.get(i).getLogin().equals(binding.inputLogin.getText().toString()) &&
                            ls.get(i).getPassword().equals(binding.inputPassword.getText().toString())) {
                        LocalStorage.setLogin(true);
                        LocalStorage.setUser(ls.get(i));
                        NavHostFragment.findNavController(this).navigate(R.id.action_signInFragment_to_mainActivity);
                        requireActivity().finish();
                        return;
                    }
                }
                if (!isFound) {
                    makeToast("Not found");
                }
            });
        });
    }
}
