package com.rj.verificationcodeinputlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.rj.library.VerificationCodeInputLayout;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VerificationCodeInputLayout verificationCodeInputLayout = findViewById(R.id.verificationCodeInputLayout);
        verificationCodeInputLayout.setOnInputCompleteListener(new VerificationCodeInputLayout.onInputCompleteListener() {
            @Override
            public void onInputComplete(String content) {
                Toast.makeText(MainActivity.this, "content="+content, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
