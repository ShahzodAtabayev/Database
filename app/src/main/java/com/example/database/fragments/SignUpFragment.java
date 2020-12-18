package com.example.database.fragments;

import android.os.Bundle;
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
import com.example.database.databinding.FragmentSignUpBinding;

import java.util.List;
import java.util.Objects;


public class SignUpFragment extends BaseFragment {
    private FragmentSignUpBinding binding;

    public SignUpFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btSignUp.setOnClickListener(a -> register());
    }

    private void register() {
        if (Objects.requireNonNull(binding.inputName.getText()).toString().length() == 0) {
            makeToast("enter full name");
            return;
        }
        if (Objects.requireNonNull(binding.inputLogin.getText()).toString().length() == 0) {
            makeToast("enter login");
            return;
        }
        if (Objects.requireNonNull(binding.inputPassword.getText()).toString().length() < 6) {
            makeToast("Password length must be 6 character.");
            return;
        }
        UserModel userModel = new UserModel();
        userModel.setName(binding.inputName.getText().toString());
        userModel.setLogin(binding.inputLogin.getText().toString());
        userModel.setPassword(binding.inputPassword.getText().toString());
        userModel.setImage(" ");
        userModel.setStudentCount(0);
        runOnWorkerThread(() -> {
            db.addUser(userModel);
            runOnUIThread(() -> {
                List<UserModel> users = db.getAllUsers();
                LocalStorage.setUser(users.get(users.size() - 1));
                LocalStorage.setLogin(true);
                NavHostFragment.findNavController(this).navigate(R.id.action_signUpFragment_to_mainActivity);
                requireActivity().finish();
            });
        });
    }
}
