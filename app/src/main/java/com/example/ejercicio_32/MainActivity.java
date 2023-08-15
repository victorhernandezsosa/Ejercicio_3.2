package com.example.ejercicio_32;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    Button btnrecy,btncard,btnnoti;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btncard = findViewById(R.id.btncard);
        btnrecy = findViewById(R.id.btnrecy);
        btnnoti = findViewById(R.id.btnnoti);

        WebView webView = findViewById(R.id.webView);
        String iframeHtml = "<iframe width=\"400\" height=\"700\" src=\"https://www.youtube.com/embed/YEXQR3GSkvQ\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";

        webView.loadData(iframeHtml, "text/html", "utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        btnnoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Notificacion.class);
                startActivity(intent);
                finish();
            }
        });

        btnrecy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Recycler.class);
                startActivity(intent);
                finish();
            }
        });

        btncard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Card.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
