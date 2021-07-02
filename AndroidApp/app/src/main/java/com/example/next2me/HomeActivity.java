package com.example.next2me;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.next2me.data.User;
import com.example.next2me.utils.DatabaseHelper;
import com.example.next2me.utils.Utilities;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private final int LOCATION_PERMISSION_CODE = 1;
    BottomNavigationView menu_nav;
    LocationManager locationManager;


    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            DatabaseHelper.getInstance().SendUserPositionToDB(new LatLng(latitude, longitude));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        menu_nav = findViewById(R.id.menu_nav);
        menu_nav.setSelectedItemId(R.id.nav_home);
        menu_nav.setOnNavigationItemSelectedListener(selectedListener);
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
        Utilities.insertFragment(this, new DashboardFragment(), "FRAGMENT_TAG");


    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            item -> {
                switch(item.getItemId()){
                    case R.id.nav_home:
                        menu_nav.getMenu().getItem(0).setChecked(true);
                        Utilities.insertFragment(this, new DashboardFragment(),"DASHBOARD FRAGMENT");
                        break;
                    case R.id.nav_map:
                        menu_nav.getMenu().getItem(1).setChecked(true);
                        if(ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                            Utilities.insertFragment(this, new MapsFragment(),"MAP FRAGMENT");
                        }else{
                            requestLocationPermission();
                        }
                        break;
                    case R.id.nav_profile:
                        menu_nav.getMenu().getItem(2).setChecked(true);
                        Utilities.insertFragment(this, new ProfileFragment(),"PROFILE FRAGMENT");
                        break;
                    case R.id.nav_matches:
                        menu_nav.getMenu().getItem(3).setChecked(true);
                        Utilities.insertFragment(this, new MatchFragment(),"MATCH FRAGMENT");
                        break;
                }
                return false;
            };

    private void requestLocationPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE );
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Utilities.insertFragment(this, new MapsFragment(),"MAP FRAGMENT");
            } else{
                Log.d("pos","Permission denied");
            }
        }
    }

   /* public void updateUI(){
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference userTable = DatabaseHelper.getInstance().getDb().getReference("Users");
        userTable.child(uid).child("INFORMATIONS").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                StorageReference imageRef = DatabaseHelper.getInstance().getStorageRef().child("ProfilePictures/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg");
                final long ONE_MEGABYTE = 1024 * 1024;
                imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        ((ImageView)findViewById(R.id.profilePicture)).setImageBitmap(Utilities.byteToBitmap(bytes));

                    }
                });

                ((TextView)findViewById(R.id.name)).setText(user.getName());
                ((TextView)findViewById(R.id.surname)).setText(user.getSurname());
                ((TextView)findViewById(R.id.birthdate)).setText(user.getBirthdate());
                ((TextView)findViewById(R.id.gender)).setText(user.getGender());
                ((TextView)findViewById(R.id.preferences)).setText(user.getPreferences());
                ((TextView)findViewById(R.id.mail)).setText(user.getEmail());
                ((TextView)findViewById(R.id.description)).setText(user.getDescription());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("db","Error while reading data");
            }
        });


    }*/





}