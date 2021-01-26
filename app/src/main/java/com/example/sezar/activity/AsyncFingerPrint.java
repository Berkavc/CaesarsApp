package com.example.sezar.activity;

import android.Manifest;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.sezar.R;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import static androidx.biometric.BiometricConstants.ERROR_NEGATIVE_BUTTON;
import static androidx.core.content.ContextCompat.getSystemService;
import static android.content.Context.FINGERPRINT_SERVICE;
import static android.content.Context.KEYGUARD_SERVICE;

@RequiresApi(api = Build.VERSION_CODES.M)
class AsyncFingerPrint extends AsyncTask<Void, Void, Void> {


    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    //BiometricPrompt biometricPrompt;
    Activity mActivity;
    FragmentActivity mFragmentActivity;
    //Fragment mFragment;
    //static String fingerPrintKey;


    AsyncFingerPrint(Activity activity, FragmentActivity fragmentActivity) {
        super();
        mActivity = activity;
        mFragmentActivity = fragmentActivity;
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();


    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected Void doInBackground(Void... strings) {
        Looper.prepare();
        try {
            // fingerPrintCheck();
            if (LoginScreen.isFingerPrintSuccess()) {
                if (LoginScreen.isControlPhone()) {
                    Toast.makeText(mActivity, (R.string.login_screen_successfull), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mActivity, (R.string.login_screen_invalid_phone_number), Toast.LENGTH_LONG).show();
                }

            } else if (LoginScreen.isFingerPrintError()) {
                Toast.makeText(mActivity, "Error!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mActivity, "Fail!", Toast.LENGTH_LONG).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        Looper.loop();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (!isCancelled()) {

        }


    }


}
