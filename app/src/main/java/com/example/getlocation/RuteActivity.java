package com.example.getlocation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;

public class RuteActivity extends AppCompatActivity {

    private EditText etAsal, etTujuan;
    private Button btnJalur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rute);

        etAsal = findViewById(R.id.etAsal);
        etTujuan = findViewById(R.id.etTujuan);
        btnJalur = findViewById(R.id.btnOpenMaps);

        btnJalur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Asal = etAsal.getText().toString();
                String Tujuan = etTujuan.getText().toString();

                DisplayTrack(Asal, Tujuan);
            }
        });


    }

    private void DisplayTrack(String asal, String tujuan) {
        try {
            Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + asal + "/" + tujuan);

            Intent intent = new Intent(Intent.ACTION_VIEW,uri);

            intent.setPackage("com.google.android.apps.maps");

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        }catch (ActivityNotFoundException e){
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps");

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        }
    }
}