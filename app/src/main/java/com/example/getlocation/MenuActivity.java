package com.example.getlocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MenuActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;

    private int userId; // Menyimpan ID pengguna yang login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Mendapatkan ID pengguna yang login dari Intent
        userId = getIntent().getIntExtra("userId", -1);

        // Inisialisasi FusedLocationProviderClient
        fusedLocationProviderClient = new FusedLocationProviderClient(this);

        // Inisialisasi locationCallback
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update lokasi pada peta
                    updateLocationOnMap(location);
                }
            }
        };

        // Load peta asynchronusly
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Mulai memperbarui lokasi setiap perubahan
        startLocationUpdates();
    }

    // Memulai pembaruan lokasi
    private void startLocationUpdates() {
        // Cek izin lokasi
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Meminta izin jika tidak diberikan
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        // Mulai pembaruan lokasi
        fusedLocationProviderClient.requestLocationUpdates(createLocationRequest(),
                locationCallback, null);
    }

    // Membuat objek LocationRequest
    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000); // Interval pembaruan lokasi dalam milidetik
        locationRequest.setFastestInterval(5000); // Interval tercepat dalam milidetik
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // Prioritas akurasi tinggi
        return locationRequest;
    }

    // Memperbarui lokasi pada peta
    private void updateLocationOnMap(Location location) {
        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.clear(); // Hapus marker sebelumnya
        mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Lokasi Anda"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15)); // Zoom pada lokasi
    }

    // Metode untuk membuka UpdateProfileActivity
    public void openProfileActivity(View view) {
        Intent intent = new Intent(this, UpdateProfileActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    // Metode untuk membuka MapsActivity
    public void openMapsActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    // Metode yang dipanggil setelah izin diberikan atau ditolak
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin diberikan, mulai pembaruan lokasi
                startLocationUpdates();
            } else {
                // Izin ditolak, tampilkan pesan kepada pengguna
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

