package com.example.getlocation;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateProfileActivity extends AppCompatActivity {

    private EditText addressEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private DatabaseHelper dbHelper;
    private int userId; // ID pengguna yang login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        dbHelper = new DatabaseHelper(this);
        addressEditText = findViewById(R.id.addressEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);

        // Mengambil ID pengguna dari sesi login
        // Contoh sederhana: disini kita anggap ID pengguna adalah 1
        userId = getIntent().getIntExtra("userId", -1);

        // Mengisi data profil pengguna saat ini
        loadUserProfile(userId);

        Button updateButton = findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mengambil nilai dari EditText
                String address = addressEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String email = emailEditText.getText().toString();

                // Membandingkan dengan nilai yang telah disimpan sebelumnya
                if (address.equals(savedAddress) && phone.equals(savedPhone) && email.equals(savedEmail)) {
                    // Tidak ada perubahan, tidak perlu memperbarui
                    Toast.makeText(UpdateProfileActivity.this, "Tidak ada perubahan yang dilakukan", Toast.LENGTH_SHORT).show();
                } else {
                    // Ada perubahan, perbarui profil
                    updateProfile(userId, address, phone, email);
                }
            }
        });

        // Tombol kembali
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kembali ke MenuActivity
                Intent intent = new Intent(UpdateProfileActivity.this, MenuActivity.class);
                startActivity(intent);
                finish(); // Menutup LoginActivity
            }
        });
    }

    private String savedAddress, savedPhone, savedEmail;

    private void loadUserProfile(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT address, phone, email FROM users WHERE id = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            int indexAddress = cursor.getColumnIndexOrThrow("address");
            int indexPhone = cursor.getColumnIndexOrThrow("phone");
            int indexEmail = cursor.getColumnIndexOrThrow("email");

            savedAddress = cursor.getString(indexAddress);
            savedPhone = cursor.getString(indexPhone);
            savedEmail = cursor.getString(indexEmail);

            addressEditText.setText(savedAddress);
            phoneEditText.setText(savedPhone);
            emailEditText.setText(savedEmail);
        }

        cursor.close();
    }


    private void updateProfile(int userId, String address, String phone, String email) {
        // Memperbarui data profil pengguna di database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("address", address);
        values.put("phone", phone);
        values.put("email", email);
        int rowsAffected = db.update("users", values, "id=?", new String[]{String.valueOf(userId)});
        if (rowsAffected > 0) {
            // Pembaruan berhasil
            Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show();
        } else {
            // Pembaruan gagal
            Toast.makeText(this, "Gagal memperbarui profil", Toast.LENGTH_SHORT).show();
        }
    }
}
