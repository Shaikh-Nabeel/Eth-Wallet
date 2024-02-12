package com.snabeel.dappethr.accountsetup.viewmodel;

import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.snabeel.dappethr.Constant;
import com.snabeel.dappethr.MainActivity;
import com.snabeel.dappethr.MnemonicUtil;
import com.snabeel.dappethr.PreferenceManager;
import com.snabeel.dappethr.accountsetup.activity.ImportWallet;

import org.web3j.crypto.Bip32ECKeyPair;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.MnemonicUtils;

import java.util.ArrayList;

public class CreateWalletViewModel extends ViewModel {

    MutableLiveData<String> liveData = new MutableLiveData<>();
    public MutableLiveData<String> isPassCorrect = new MutableLiveData<>();
    public MutableLiveData<String> isMnemonicCorrect = new MutableLiveData<>();
    String mnemonic = null;



    public void setPassword(String password){
        PreferenceManager.setStringValue(Constant.PASSWORD, password);
        this.mnemonic = MnemonicUtil.generateMnemonicKey();
        isPassCorrect.setValue("password_set");
    }

    private void createAccount(String phrase){

        Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(MnemonicUtils.generateSeed(phrase, null));
        int[] derivationPath = {44 | Bip32ECKeyPair.HARDENED_BIT, 60 | Bip32ECKeyPair.HARDENED_BIT, 0 | Bip32ECKeyPair.HARDENED_BIT, 0,0};
        Bip32ECKeyPair derivedKeyPair = Bip32ECKeyPair.deriveKeyPair(masterKeypair, derivationPath);
        Credentials credentials = Credentials.create(derivedKeyPair);

        // Create list of private keys
        ArrayList<String> listOfAcc = new ArrayList<>();
        listOfAcc.add(credentials.getEcKeyPair().getPrivateKey().toString(16));

        PreferenceManager.setStringValue(Constant.LIST_OF_KEYS, listOfAcc.toString());
        PreferenceManager.setBoolValue(Constant.IS_WALLET_SETUP, true);
        PreferenceManager.setIntValue(Constant.CURRENT_ACCOUNT, 1);
        PreferenceManager.setIntValue(Constant.NO_OF_ACCOUNT, 1);
        PreferenceManager.setStringValue(Constant.CURRENT_NETWORK, Constant.ETHEREUM_MAIN_NET);
    }

    public void isMnemonicCorrect(String mnemonic){
        if(mnemonic.equals(this.mnemonic))
            isMnemonicCorrect.setValue("Correct");
        else
            isMnemonicCorrect.setValue("Please Enter correct secret phrase.");
    }

}