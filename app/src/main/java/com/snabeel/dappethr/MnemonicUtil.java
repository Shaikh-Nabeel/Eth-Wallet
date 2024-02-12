package com.snabeel.dappethr;

import org.web3j.crypto.MnemonicUtils;

import java.security.SecureRandom;

public class MnemonicUtil {

    public static boolean verifyRecoveryPhrase(String recoveryPhrase) {

        String[] words = recoveryPhrase.split(" ");

        // Check if the number of words is valid (should be 12, 15, 18, 21, or 24)
        if (words.length != 12 && words.length != 15 && words.length != 18 && words.length != 21 && words.length != 24) {
            return false;
        }

        // Verify that each word is a valid BIP-39 word
        for (String word : words) {
            if (!org.web3j.crypto.MnemonicUtils.getWords().contains(word)) {
                return false;
            }
        }

        byte[] entropy = org.web3j.crypto.MnemonicUtils.generateEntropy(recoveryPhrase);
        String generatedWords = org.web3j.crypto.MnemonicUtils.generateMnemonic(entropy);

        return recoveryPhrase.contentEquals(generatedWords);
    }

    public static String generateMnemonicKey(){
        byte[] entropy = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(entropy);
        return MnemonicUtils.generateMnemonic(entropy);
    }

}
