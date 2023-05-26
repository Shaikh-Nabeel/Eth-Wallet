package com.snabeel.dappethr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.snabeel.dappethr.databinding.ActivityCreateImportWalletBinding;

public class CreateImportWallet extends AppCompatActivity {

    ActivityCreateImportWalletBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateImportWalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.importWallet.setOnClickListener(v->{
            startActivity(new Intent(CreateImportWallet.this, ImportWallet.class));
        });

    }
}