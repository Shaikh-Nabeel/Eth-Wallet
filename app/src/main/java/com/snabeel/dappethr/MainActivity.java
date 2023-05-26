package com.snabeel.dappethr;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.snabeel.dappethr.databinding.ActivityMainBinding;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity {

    private TextView connection, ethereum_balance, address;

    ActivityMainBinding binding;
    private Handler handler;
    private Executor executors;
    private String password = null;
    private String balanceSuffix = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        connection = findViewById(R.id.connecting_tv);
        ethereum_balance = findViewById(R.id.ethereum_balance_tv);
        address = findViewById(R.id.address_tv);
        View view = findViewById(R.id.root_view);

        password = PreferenceManager.getStringValue(Constant.PASSWORD);
//        showPasswordDialog();
        setupBouncyCastle();
        handler = new Handler();
        executors = Executors.newSingleThreadExecutor();
        connectToWeb3();

        address.setOnClickListener(v->{
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("address", credentials.getAddress());
            clipboard.setPrimaryClip(clip);
            Snackbar snackbar = Snackbar.make(view, "Address copied to Clipboard ✔", Snackbar.LENGTH_SHORT);
            Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
            layout.setBackgroundColor(ContextCompat.getColor(this,R.color.background_color));
            TextView textView = layout.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(18f);
            snackbar.show();
        });


//        transfer_btn.setOnClickListener(v -> {
//            try {
//                transferEthereum();
//            } catch (TransactionException | IOException | InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
    }


/*    @SuppressLint("SetTextI18n")
    private void fetchAccount(String phrase) throws ExecutionException, InterruptedException {
        String password = null;// can keep it null
        String mnemonic = "still town vast illness pen resource jar cancel price divide arrange donate";
//        byte[] entropy = new byte[16];
//
//        SecureRandom secureRandom = new SecureRandom();
//        secureRandom.nextBytes(entropy);
//        String mnemonic = MnemonicUtils.generateMnemonic(entropy);
//
//        String mnemonic = "excess during only earth journey bring business crack monster embark idle soap";

        // Generate a BIP32 master keypair from the mnemonic phrase
        Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(MnemonicUtils.generateSeed(mnemonic, password));
//        // custom derivation path
        int[] derivationPath = {44 | Bip32ECKeyPair.HARDENED_BIT, 60 | Bip32ECKeyPair.HARDENED_BIT, 0 | Bip32ECKeyPair.HARDENED_BIT, 0,0};
//        // Derived the key using the derivation path
        Bip32ECKeyPair derivedKeyPair = Bip32ECKeyPair.deriveKeyPair(masterKeypair, derivationPath);

        // Load the wallet for the derived key
        credentials = Credentials.create(derivedKeyPair);
        binding.addressTv.setText(credentials.getAddress());
        EthGetBalance ethGetBalance = web3j.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
        // convert to ether
        BigDecimal bigDecimal = Convert.fromWei(ethGetBalance.getBalance().toString(), Convert.Unit.ETHER);
        BigDecimal roundedValue = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        binding.ethereumBalanceTv.setText(roundedValue.toString().concat(" ETH"));
        Log.e("Keys", "final "+credentials.getEcKeyPair().getPrivateKey().toString(16));

    }*/

    Web3j web3j;

    @SuppressLint("SetTextI18n")
    private void connectToWeb3() {

//        String NETWORK = PreferenceManager.getStringValue(Constant.CURRENT_NETWORK);
        String NETWORK = Constant.SEPOLIA;
        web3j = Web3j.build(new HttpService("https://"+NETWORK+".infura.io/v3/"+BuildConfig.API_KEY));
        try {
            Web3ClientVersion clientVersion = web3j.web3ClientVersion()
                    .sendAsync().get();
            if (!clientVersion.hasError()) {
//                Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show();
                switch (NETWORK){
                    case Constant.ETHEREUM_MAIN_NET: {
                        connection.setText("Ethereum mainnet");
                        balanceSuffix = " ETH";
                    }
                    break;
                    case Constant.GOERLI: {
                        connection.setText("Goerli");
                        balanceSuffix = " GETH";
                    }
                    break;
                    case Constant.SEPOLIA: {
                        connection.setText("Sepolia");
                        balanceSuffix = " SEPETH";
                    }
                }
                executors.execute(this::getEthereumBalance);
            } else {
                System.exit(0);
                Toast.makeText(this, clientVersion.getError().getMessage(), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

/*    private void transferEthereum() throws TransactionException, IOException, InterruptedException, ExecutionException {

        String amount = amount_tv.getText().toString();
        String addr = receiver_address.getText().toString();

        if (!amount.equals("") && !addr.equals("")) {

            executors.execute(() -> {
                Log.d("transactEtr", "Ready for Ethereum Transfer");
                BigInteger nonce = null;
                try {
                    nonce = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).send().getTransactionCount();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                BigInteger gasPrice = BigInteger.valueOf(30_000_000_000L);
                BigInteger gasLimit = BigInteger.valueOf(100_000);
                BigDecimal value = new BigDecimal("0.12");
                BigInteger valueWei = Convert.toWei(value, Convert.Unit.ETHER).toBigInteger();

                RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce,gasPrice,gasLimit,addr,valueWei);
                Log.d("transactEtr", "Ethereum transfer successful");
                byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
                Log.d("transactEtr", "signing transaction message fetched");
                String hexValue = Numeric.toHexString(signedMessage);
                EthSendTransaction ethSendTransaction = null;
                try {
                    ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Log.d("transactEtr", "Signed transaction to send ethereum network, Transaction hash : "+ethSendTransaction.getTransactionHash());

            });

//            TransactionReceipt transactionReceipt = Transfer.sendFunds(web3j, credentials, addr, BigDecimal.valueOf(Long.parseLong(amount)), Convert.Unit.ETHER)
//                    .sendAsync().get();

            // Get hash of transaction

//            String hash = transactionReceipt.getTransactionHash();
//            transaction_hash.setText("Transaction Hash : " + hash);
        }
    }*/

    Credentials credentials = null;

    @SuppressLint("SetTextI18n")
    private void getEthereumBalance() {
        try {
            int currentAcc = PreferenceManager.getIntValue(Constant.CURRENT_ACCOUNT);
            binding.accountName.setText("Account "+currentAcc);
            String listOfAcc = PreferenceManager.getStringValue(Constant.LIST_OF_KEYS);
            listOfAcc = listOfAcc.substring(1, listOfAcc.length() - 1);
            String[] arr = listOfAcc.split(", ");

            ArrayList<String> listOfKeys = new ArrayList<>(Arrays.asList(arr));
            credentials = Credentials.create(listOfKeys.get(currentAcc-1));
            String addressStr = credentials.getAddress();
            addressStr = addressStr.substring(0,4) +"..."+ addressStr.substring(addressStr.length()-3);
            address.setText(addressStr);
            // get balance in wei
            EthGetBalance ethGetBalance = web3j.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
            // convert to ether
            BigDecimal bigDecimal = Convert.fromWei(ethGetBalance.getBalance().toString(), Convert.Unit.ETHER);
            BigDecimal roundedValue = bigDecimal.setScale(2,RoundingMode.HALF_DOWN);
            handler.post(()-> ethereum_balance.setText(roundedValue.toString().concat(balanceSuffix)));

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void showPasswordDialog() {
        Dialog startDialog = new Dialog(this);
        startDialog.setContentView(R.layout.password_dialog);
        startDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        startDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TextView button = startDialog.findViewById(R.id.submit_btn);
        EditText passw = startDialog.findViewById(R.id.password_et);
        startDialog.setCancelable(false);
        startDialog.setCanceledOnTouchOutside(false);

        button.setOnClickListener(v -> {
            String password = passw.getText().toString();
            if (!password.equals("") && password.length() > 6 && password.equals(this.password)) {
                startDialog.dismiss();
            }else{
                passw.setError("Wrong password");
            }
        });
        startDialog.show();
    }

    private void setupBouncyCastle() {
        final Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
        if (provider == null) {
            return;
        }
        if (provider.getClass().equals(BouncyCastleProvider.class)) {
            return;
        }
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }

//    public void createWallet(String password) {
//
//        executors.execute(() -> {
//            try {
//                String walletDirectory = getFilesDir().getAbsolutePath(); //wallet path
//                String walletName = WalletUtils.generateNewWalletFile(password, new File(walletDirectory));
//
//                handler.post(()->{
//                    PreferenceManager.setStringValue(WALLET_NAME, walletName);
//                    PreferenceManager.setStringValue(PASSWORD, password);
//                    connectToWeb3();
//                });
//
//            } catch (Exception e) {
//                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//                e.printStackTrace();
//            }
//        });
//    }

/*
    //not in use, take lot of time
    public void previousTransaction() throws IOException {
        String myAddress = "0x69fb2a80542721682bfe8daa8fee847cddd1a267";

// Get the latest block number
        executors.execute(()->{
            BigInteger latestBlock = null;
            try {
                latestBlock = web3j.ethBlockNumber().send().getBlockNumber();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


// Loop through the blocks
         for (BigInteger i = latestBlock; i.compareTo(BigInteger.ZERO) > 0; i = i.subtract(BigInteger.ONE)) {
                // Get the block by number
                EthBlock.Block block = null;
                try {
                    block = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(i), true).send().getBlock();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // Get the transactions in the block
                List<EthBlock.TransactionResult> transactions = block.getTransactions();
                // Loop through the transactions
                Log.d("kys", "transaction size list = "+transactions.size());
                for (EthBlock.TransactionResult txResult : transactions) {
                    // Get the transaction object
                    Transaction tx = (Transaction) txResult.get();
                    // Check if the transaction is related to your account
                    if (myAddress.equalsIgnoreCase(tx.getFrom()) || myAddress.equalsIgnoreCase(tx.getTo())) {
                        // Print the transaction details
                        System.out.println("Block: " + i);
                        System.out.println("Hash: " + tx.getHash());
                        System.out.println("From: " + tx.getFrom());
                        System.out.println("To: " + tx.getTo());
                        System.out.println("" + tx.getNonce().toString());
                        System.out.println("" + tx.getGas());
                        System.out.println("" + tx.getGasPrice());
                        System.out.println("Value: " + tx.getValue());
                        try {
                            System.out.println("Status: " + web3j.ethGetTransactionReceipt(tx.getHash()).send().getTransactionReceipt().get().isStatusOK());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println();
                    }
                }
            }
        });

    }*/
}