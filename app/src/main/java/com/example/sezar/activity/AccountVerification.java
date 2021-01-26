package com.example.sezar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sezar.R;


public class AccountVerification extends AppCompatActivity {
    private String verificationCode;
    private long safetyCounter;
    private boolean timerCheck;
    private EditText editText_verification_code;
    private Button button_verify;
    private TextView textView_counter;
    private String counter;
    private boolean controlVerificationCodeEmpty, controlVerificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        timerCheck = false;
        editText_verification_code = findViewById(R.id.editText_verification_code);
        button_verify = findViewById(R.id.button_verify);
        textView_counter = findViewById(R.id.textView_verification_code_timer);
        controlVerificationCodeEmpty = false;
        controlVerificationCode = false;
        //final Animation button_animation = AnimationUtils.loadAnimation(this, R.anim.animation_pulse);
        //  final DBCreateAccountHelper myDb = new DBCreateAccountHelper(this);
        counter();
        button_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                setHideSoftKeyboard(editText_verification_code);
                checkVerification();
                checkEmpty();
            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void counter() {
        new CountDownTimer(120000, 1000) {
            public void onTick(long millisUntilFinished) {
                safetyCounter = millisUntilFinished / 1000;
                textView_counter.setText(getString(R.string.verification_counter).concat(Long.toString(safetyCounter)));

            }

            public void onFinish() {
                if (timerCheck) {
                    finish();
                } else {
                    Toast.makeText(AccountVerification.this, getString(R.string.verification_timer_up), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AccountVerification.this, LoginScreen.class));
                    finish();
                }
            }
        }.start();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timerCheck = true;
    }

    private void setHideSoftKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void checkVerification() {
        Bundle extras = getIntent().getExtras();
        verificationCode = editText_verification_code.getText().toString().trim();
        if (editText_verification_code.getText().length() != 0) {
            if (verificationCode.equals(extras.get("Verification Code").toString().trim())) {
                controlVerificationCodeEmpty = true;
                controlVerificationCode = true;

            } else {
                controlVerificationCodeEmpty = true;
                controlVerificationCode = false;
            }
        } else {
            controlVerificationCode = false;
            controlVerificationCodeEmpty = false;
        }
    }

    private void checkEmpty() {
        if (controlVerificationCode) {
            CreateAccount.insertDataBase();
            Toast.makeText(AccountVerification.this, getString(R.string.verification_success), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AccountVerification.this, LoginScreen.class));
        } else if (!controlVerificationCodeEmpty) {
            Toast.makeText(AccountVerification.this, getString(R.string.verification_empty), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AccountVerification.this, getString(R.string.verification_invalid), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(AccountVerification.this, LoginScreen.class));
    }
}
