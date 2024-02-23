package com.example.getlocation;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    private int userId; // Menyimpan ID pengguna yang login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Mendapatkan ID pengguna yang login dari Intent
        userId = getIntent().getIntExtra("userId", -1);
    }

    public void openProfileActivity(View view) {
        Intent intent = new Intent(this, UpdateProfileActivity.class);
        intent.putExtra("userId", userId); // Mengirim ID pengguna yang login ke UpdateProfileActivity
        startActivity(intent);
    }

    public void openMapsActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

}
