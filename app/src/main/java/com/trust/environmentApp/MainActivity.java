package com.trust.environmentApp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.trust.environment.TrustEnv;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    Button btnTry;
    Button btnUrl;
    TextView tvUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTry = findViewById(R.id.btn_try);
        btnUrl = findViewById(R.id.btn_url);
        tvUrl = findViewById(R.id.txt_url);

        btnUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvUrl.setText(TrustEnv.getUrlEnvironment());
            }
        });
        btnTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrustEnv t = new TrustEnv.Builder(MainActivity.this)
                        .withDevURL("https://www.google.cl/dev")
                        .withProdURL("https://www.google.cl/prod")
                        .withStgURL("https://www.google.cl/stg")
                        .withPinCode("1233")
                        .withTrustId("f4ig0n4g49g4g-4h")
                        .build();

                t.showEnvironmentModal();
            }
        });



    }
}