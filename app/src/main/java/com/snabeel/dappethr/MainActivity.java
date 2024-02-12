package com.snabeel.dappethr;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.biometrics.BiometricPrompt;
import android.icu.text.SymbolTable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.snabeel.dappethr.databinding.ActivityMainBinding;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.Utils;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.StructType;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.websocket.WebSocketService;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.ConnectException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import com.google.android.material.snackbar.Snackbar;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java8.util.Optional;


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

//        password = PreferenceManager.getStringValue(Constant.PASSWORD);
//        showPasswordDialog();
        setupBouncyCastle();
        handler = new Handler();
        executors = Executors.newSingleThreadExecutor();
//        connectToWeb3();

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
//        new Handler().postDelayed(()->{
//            try {
////                listenToThisShit();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        },5000);

        SecretKey keyy = generateSecretKey();




//        binding.sendBtn.setOnClickListener(v-> {
//            executors.execute(this::addPassword);
//        });
    }


    private void addPassword(){

        try {
            DefaultGasProvider gasProvider = new DefaultGasProvider();

            String functionName = "addPassword";
        List<TypeReference<?>> outputParameters = Collections.emptyList();
        List<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Utf8String("Amazon"));
        inputParameters.add(new Utf8String("7007158611"));
        inputParameters.add(new Utf8String("Nabs34324"));

        Function function = new Function(
                functionName,
                inputParameters,
                outputParameters
        );

        String encodedFunction = FunctionEncoder.encode(function);

        // Create a raw transaction
        BigInteger nonce = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST)
                    .send()
                    .getTransactionCount();

        runOnUiThread(()->{
            System.out.println("'noncee ::' "+ nonce.toString());
        });

        BigInteger gasLimit = BigInteger.valueOf(200_0000); // Adjust this value based on your contract's gas consumption
        BigInteger gasPrice = gasProvider.getGasPrice().multiply(BigInteger.valueOf(2)); // Example: Double the default gas price

//        BigInteger gasPrice = gasProvider.getGasPrice();
//        BigInteger gasLimit = gasProvider.getGasLimit();

        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                gasLimit,
                "0xdaa92aE32234C076675a8BbB52396906D9444F58",
                encodedFunction
        );

        // Sign the raw transaction
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        // Send the signed transaction
        String transactionHash = null;

            transactionHash = web3j.ethSendRawTransaction(hexValue)
                    .send()
                    .getTransactionHash();

            String finalTransactionHash1 = transactionHash;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(finalTransactionHash1 +"     ==============");
                }
            });


        // Wait for the transaction to be mined
        Optional<TransactionReceipt> receipt;

            receipt = web3j.ethGetTransactionReceipt(transactionHash)
                    .send()
                    .getTransactionReceipt();

            if (receipt.isPresent() && receipt.get().isStatusOK()) {
                // Transaction successful
                System.out.println("Password added successfully!"+ receipt.get().getTransactionHash());
            } else {
                // Transaction failed
                System.out.println("Failed to add password.");
            }

            listenToThisShit();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private void listenToThisShit() throws IOException {

// Initialize Web3j with a WebSocketService
        WebSocketService webSocketService = new WebSocketService("wss://sepolia.infura.io/ws/v3/646a096d224e4589a34c0a18546a4ffe", true);
        webSocketService.connect();
        Web3j web3j2 = Web3j.build(webSocketService);

// Rest of your code...

// Inside your addPassword method
//        Event passwordAddedEvent = new Event(
//                "PasswordAdded",
//                Arrays.asList(
//                        new TypeReference<Bytes32>() {},  // bytes32 for appName
//                        new TypeReference<Bytes32>() {},  // bytes32 for emailOrMobile
//                        new TypeReference<Bytes32>() {}   // bytes32 for password
//                )
//        );

        EthFilter ethFilter = new EthFilter(
                DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST,
                "0xdaa92aE32234C076675a8BbB52396906D9444F58"  // Address of the smart contract
        );
//        ethFilter.addSingleTopic(EventEncoder.encode(passwordAddedEvent));
        Event event = new Event("PasswordAdded",
                Arrays.asList(new TypeReference<Address>() {},  // Address for indexed user
                        new TypeReference<org.web3j.abi.datatypes.Utf8String>() {},    // string for appName
                        new TypeReference<Utf8String>() {})    // string for emailOrMobile
        );
        ethFilter.addSingleTopic(EventEncoder.encode(event));

        // Handle the error
        // Add any additional error handling logic here
        Disposable subscription = web3j2.ethLogFlowable(ethFilter)
                .observeOn(Schedulers.io()) // Ensure UI updates on the main thread
                .subscribe(log -> {
                    // Handle the event log
//                            System.out.println("Received log: " + log);

//                            if (log.getData().equals("0x1")) {
//                            System.out.println("Password added successfully! " + log.getTransactionHash());
//                        } else {
//                            System.out.println("Failed to add password. Status: " + log.getData());
//                        }

//                            List<Type> params = FunctionReturnDecoder.decode(
//                                    log.getData(), event.getParameters());
                            List<Type> params = FunctionReturnDecoder.decode(log.getData(), Arrays.asList(
                                    new TypeReference<Type>() {},     // Address for indexed user
                                    new TypeReference<Type>() {},  // String for appName
                                    new TypeReference<Type>() {}   // String for emailOrMobile
                            ));

                            // Retrieve the decoded values
                            String indexedAddress = Keys.toChecksumAddress(params.get(0).getValue().toString());
                            String appName = params.get(1).getValue().toString();

                            System.out.println(indexedAddress +" ===> "+appName);

//                            Optional<TransactionReceipt> transactionReceipt = web3j.ethGetTransactionReceipt(log.getTransactionHash()).send().getTransactionReceipt();
//
//                            // Check if the transaction was successful
//                            if (transactionReceipt != null && transactionReceipt.get().isStatusOK()) {
//                                // Transaction was successful
//                                System.out.println("Password added successfully! " + log.getTransactionHash());
//                            } else {
//
//                                System.out.println("Failed to add password. Transaction status: " + (transactionReceipt != null ? transactionReceipt.get().getStatus() : "N/A"));
//                            }
                },
                        Throwable::printStackTrace
                );

        compositeDisposable.add(subscription);

    }

//    class Password{
//        public String appName;
//        public String emailOrMobile;
//        public String password;
//
//        public Password(){}
//
//    }

//    public static String hexToString(String hex) {
//        if (hex.startsWith("0x")) {
//            hex = hex.substring(2);
//        }
////        StringBuilder output = new StringBuilder("");
////        for (int i = 0; i < hex.length(); i += 2) {
////            String str = hex.substring(i, i + 2);
////            output.append((char) Integer.parseInt(str, 16));
////        }
//
//        byte[] bytes = new byte[hex.length() / 2];
//        for (int i = 0; i < hex.length(); i += 2) {
//            String str = hex.substring(i, i + 2);
//            bytes[i / 2] = (byte) Integer.parseInt(str, 16);
//        }
//
//        return new String(bytes, StandardCharsets.UTF_8);
////        return output.toString();
//    }

    public static String hexToString(String hex) {
        if (hex == null || hex.isEmpty() || hex.equals("0x")) {
            return "";
        }

        // Remove "0x" prefix if present
        if (hex.startsWith("0x")) {
            hex = hex.substring(2);
        }

        return new String(Numeric.hexStringToByteArray(hex)) ;
    }

        public void getAllPassswords() throws IOException {

//        List<Password> passwordList = new ArrayList<>();
            List<TypeReference> typee = Arrays.asList(new TypeReference<Address>() {},  // Address for indexed user
                    new TypeReference<Utf8String>() {},    // string for appName
                    new TypeReference<Utf8String>() {});
        Function myFunc = new Function("getAllPasswords", Collections.emptyList(), Arrays.asList(new TypeReference<Utf8String>() {},  // Address for indexed user
                new TypeReference<Utf8String>() {},    // string for appName
                new TypeReference<Utf8String>() {}));

        // Set up the function call
        String encodedFunction = FunctionEncoder.encode(myFunc);
        // Set up the transaction
        EthCall ethCall = null;
        try {
            ethCall = web3j.ethCall(
                    Transaction.createEthCallTransaction(
                            "0x0AbD2B345F2B06c8830b097851801C527Cf697E9", // Replace with your private key
                            "0xdaa92aE32234C076675a8BbB52396906D9444F58",
                            encodedFunction
                    ),
                    DefaultBlockParameterName.LATEST
            ).send();
        } catch (IOException e) {
            System.out.println("Eth call erorr:::::: ");e.printStackTrace();
        }

        if (ethCall != null && ethCall.hasError()) {
            // Handle the error
            System.out.println("Error calling getAllPasswords: " + ethCall.getError().getMessage());
            System.out.println("Error calling : " + ethCall.getError().getData());

        } else {

            int size = FunctionReturnDecoder.decode(
                    ethCall.getValue(),
                    myFunc.getOutputParameters()
            ).size();
            List<Type> someType = FunctionReturnDecoder.decode(
                    ethCall.getValue(),
                    myFunc.getOutputParameters()
            );
            Iterator<Type> it = someType.iterator();
            while(it.hasNext()){
                String ll = (String) it.next().getValue();
//                System.out.println(ll.size());
                System.out.println(ll.toString());
            }

            System.out.println("SIZE -======>>>"+ size);
//            System.out.println("VALUUUE -======>>>"+ a);



//            System.out.println("result  ===-->> "+result);
            // Iterate through the Utf8String array and convert each element to a Password instance
//            for (Type<StructType> passwordUtf8 : result) {
//                System.out.println(passwordUtf8.getValue().toString());
//                System.out.println(passwordUtf8.getTypeAsString());
//                System.out.println(passwordUtf8.getValue());
//                System.out.println("\n\n\n\n");
////                passwordList.add(new Password(passwordUtf8.getValue()));
//            }
        }

//        return passwordList;
    }

    private void getAllPasswords(){
        Function myFunc = new Function(
                "getAllPasswords",
                Collections.emptyList(),
                Arrays.asList(new TypeReference<DynamicArray<Utf8String>>() {})
        );

        String encodedFunction = FunctionEncoder.encode(myFunc);

        EthCall ethCall = null;
        try {
            ethCall = web3j.ethCall(
                    Transaction.createEthCallTransaction(
                            "0x0AbD2B345F2B06c8830b097851801C527Cf697E9", // Replace with your private key
                            "0xdaa92aE32234C076675a8BbB52396906D9444F58",
                            encodedFunction
                    ),
                    DefaultBlockParameterName.LATEST
            ).send();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (ethCall != null && ethCall.hasError()) {
            // Handle the error
            System.out.println("Error calling getAllPasswords: " + ethCall.getError().getMessage());
            System.out.println("Error calling : " + ethCall.getError().getData());

        } else {
            ArrayList<Type> result = (ArrayList<Type>) FunctionReturnDecoder.decode(
                    ethCall.getValue(),
                    myFunc.getOutputParameters()
            );

            Type t = FunctionReturnDecoder.decodeIndexedValue(ethCall.getValue(),new TypeReference<DynamicArray<Utf8String>>() {});

            System.out.println("FUCKKKKKKKK OFFF"+ t.getValue().toString() + t.getTypeAsString() + t.getValue().toString());

//            org.web3j.abi.TypeDecoder.decodeStaticStruct()

            final int[] i = {0};
            for (Type type : result) {
                // Convert DynamicBytes to String
//                String value = new String((byte[]) type.getValue(), StandardCharsets.UTF_8);

//                System.out.println(type.getTypeAsString()+"\nEVERYVALUES=>>"+value);
                DynamicArray<Utf8String> utf8array = (DynamicArray<Utf8String>) type;
                System.out.println("Stringgg ::  "+ utf8array.getValue().toString() + utf8array.getValue().size());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    utf8array.getValue().iterator().forEachRemaining(utf8String -> runOnUiThread(()->{

                       binding.activityyy.append(utf8String.getValue().toString());
                   }));
                }

//                BiometricPrompt bp = new BiometricPrompt.Builder(this);



//                runOnUiThread(()->{
//                    binding.activityyy.setText(value);
//                });
            }
        }


    }

    public SecretKey generateSecretKey() {
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        keyGenerator.init(256);
        return keyGenerator.generateKey();
    }

//    String contractABI = "[{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"address\",\"name\":\"user\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"appName\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"emailOrMobile\",\"type\":\"string\"}],\"name\":\"PasswordAdded\",\"type\":\"event\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"_appName\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"_emailOrMobile\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"_password\",\"type\":\"string\"}],\"name\":\"addPassword\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"getAllPasswords\",\"outputs\":[{\"components\":[{\"internalType\":\"string\",\"name\":\"appName\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"emailOrMobile\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"password\",\"type\":\"string\"}],\"internalType\":\"struct PasswordManager.Password[]\",\"name\":\"\",\"type\":\"tuple[]\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"index\",\"type\":\"uint256\"}],\"name\":\"getPasswordOwner\",\"outputs\":[{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"}],\"stateMutability\":\"view\",\"type\":\"function\"}]";
//
//    class PassCont extends Contract{
//
//
//        protected PassCont(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
//            super(contractAddress, web3j, credentials, gasPrice, gasLimit);
//        }
//
//
//    }
//
//    // Define a function to call the getAllPasswords() function
//    public void getAllPasswords() throws ExecutionException, InterruptedException {
//
//        PassCont contract = new PassCont("0xdaa92aE32234C076675a8BbB52396906D9444F58", web3j, credentials, DefaultGasProvider.GAS_PRICE , DefaultGasProvider.GAS_LIMIT);
//
//
//// Define a gas provider for the contract
//        ContractGasProvider gasProvider = new DefaultGasProvider();
//
//// Call the getAllPasswords() function and get the result
//        List<Type> result = contract.callFunction("getAllPasswords", gasProvider);
//
//// Cast the result to a list of Password structs
//        List<StructType> passwords = (List<StructType>) result.get(0).getValue();
//
//// Print the data of each Password struct
//        for (StructType password : passwords) {
//            System.out.println("App name: " + password.getValue1());
//            System.out.println("Email or mobile: " + password.getValue2());
//            System.out.println("Password: " + password.getValue3());
//            System.out.println();
//        }
//    }

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
        web3j = Web3j.build(new HttpService("https://"+NETWORK+".infura.io/v3/"+"646a096d224e4589a34c0a18546a4ffe"));
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
                    break;
                }
                credentials = Credentials.create("82a745336314ab7622e39232b6c2e01fc8ec3f505977c5f865aa9bcf9b1e1f8e");
//                executors.execute(this::getEthereumBalance);
                executors.execute(() -> {
                    try{
                        EthGetBalance ethGetBalance = web3j.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
                        // convert to ether
                        BigDecimal bigDecimal = Convert.fromWei(ethGetBalance.getBalance().toString(), Convert.Unit.ETHER);
                        BigDecimal roundedValue = bigDecimal.setScale(2, RoundingMode.HALF_DOWN);
                        handler.post(() -> ethereum_balance.setText(roundedValue.toString().concat(balanceSuffix)));

                        getAllPasswords();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
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