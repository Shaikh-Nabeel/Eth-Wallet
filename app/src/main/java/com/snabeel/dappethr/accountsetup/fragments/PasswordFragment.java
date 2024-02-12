package com.snabeel.dappethr.accountsetup.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snabeel.dappethr.R;
import com.snabeel.dappethr.accountsetup.activity.CreateWalletActivity;
import com.snabeel.dappethr.accountsetup.viewmodel.CreateWalletViewModel;
import com.snabeel.dappethr.databinding.FragmentPasswordBinding;

public class PasswordFragment extends Fragment {


    FragmentPasswordBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if(binding == null){
            binding = FragmentPasswordBinding.inflate(inflater,container,false);

            ((CreateWalletActivity) requireActivity()).viewModel.isPassCorrect.observe(getViewLifecycleOwner(), s -> {
                if(s.equals("password_set")){
                    
                }
            });

            binding.importWalletLL.setOnClickListener(v->{
                String pass = binding.password.getText().toString();
                String passConfirm = binding.passwordConfirm.getText().toString();

                if(pass.length() > 7 && pass.equals(passConfirm)){
//                    ((CreateWalletViewModel) )
                    ((CreateWalletActivity) requireActivity()).viewModel.setPassword(pass);
                }
            });
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}