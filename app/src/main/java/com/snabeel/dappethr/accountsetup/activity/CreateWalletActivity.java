package com.snabeel.dappethr.accountsetup.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.snabeel.dappethr.R;
import com.snabeel.dappethr.accountsetup.viewmodel.CreateWalletViewModel;
import com.snabeel.dappethr.databinding.ActivityCreateWalletBinding;

public class CreateWalletActivity extends AppCompatActivity {


    ActivityCreateWalletBinding binding;
    public CreateWalletViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateWalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(CreateWalletViewModel.class);

    }
}