package com.snabeel.dappethr.accountsetup.activity;

import com.snabeel.dappethr.MnemonicUtil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.snabeel.dappethr.Constant;
import com.snabeel.dappethr.MainActivity;
import com.snabeel.dappethr.MnemonicUtil;
import com.snabeel.dappethr.PreferenceManager;
import com.snabeel.dappethr.databinding.ActivityImportWalletBinding;

import org.web3j.crypto.Bip32ECKeyPair;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.MnemonicUtils;

import java.util.ArrayList;

public class ImportWallet extends AppCompatActivity {

    ActivityImportWalletBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImportWalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.importWallet.setOnClickListener(v->{
            String phrase = binding.secretPhrase.getText().toString().trim();
            String password = binding.password.getText().toString().trim();
            String confirmPass = binding.passwordConfirm.getText().toString().trim();

            if(password.length() < 8){
                Toast.makeText(ImportWallet.this, "Set password of atleast 8 characters",Toast.LENGTH_SHORT)
                        .show();
            }else if(password.length() == confirmPass.length()){
                if(MnemonicUtil.verifyRecoveryPhrase(phrase)) {
                    fetchAccount(phrase);
                    PreferenceManager.setStringValue(Constant.PASSWORD, password);
                    PreferenceManager.setStringValue(Constant.CURRENT_NETWORK, Constant.ETHEREUM_MAIN_NET);
                    PreferenceManager.setBoolValue(Constant.IS_WALLET_SETUP, true);
                    startActivity(new Intent(ImportWallet.this, MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(ImportWallet.this, "Please fill correct secret phrase",Toast.LENGTH_SHORT)
                            .show();
                }
            }

        });
        
    }

    private void fetchAccount(String phrase){

        // Generate a BIP32 master keypair from the mnemonic phrase
        Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(MnemonicUtils.generateSeed(phrase, null));
        // custom derivation path
        int[] derivationPath = {44 | Bip32ECKeyPair.HARDENED_BIT, 60 | Bip32ECKeyPair.HARDENED_BIT, 0 | Bip32ECKeyPair.HARDENED_BIT, 0,0};
        // Derived the key using the derivation path
        Bip32ECKeyPair derivedKeyPair = Bip32ECKeyPair.deriveKeyPair(masterKeypair, derivationPath);
        // Load the wallet for the derived key
        Credentials credentials = Credentials.create(derivedKeyPair);

        // Create list of private keys
        ArrayList<String> listOfAcc = new ArrayList<>();
        listOfAcc.add(credentials.getEcKeyPair().getPrivateKey().toString(16));

        PreferenceManager.setStringValue(Constant.LIST_OF_KEYS, listOfAcc.toString());
        PreferenceManager.setIntValue(Constant.CURRENT_ACCOUNT, 1);
        PreferenceManager.setIntValue(Constant.NO_OF_ACCOUNT, 1);

    }

}