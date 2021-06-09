package com.example.next2me;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.next2me.utils.UserHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {


    private final int LOCATION_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        BottomNavigationView menu_nav = findViewById(R.id.menu_nav);
        menu_nav.setSelectedItemId(R.id.nav_profile);
        menu_nav.setOnNavigationItemSelectedListener(selectedListener);
        loadUserInfo();

    }

    private void loadUserInfo() {
        ImageView userImageTV = findViewById(R.id.imageProfile);
        TextView userNameTV = findViewById(R.id.nameProfile);

        userImageTV.setImageBitmap(UserHelper.getInstance().getProfilePic());
        userNameTV.setText(UserHelper.getInstance().getUserName());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            item -> {
                switch(item.getItemId()){
                    case R.id.nav_home:
                        Log.d("menu","home");
                        startActivity(new Intent(this, HomeActivity.class));
                        break;
                    case R.id.nav_map:
                        Log.d("menu","map");
                        if(ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                            startActivity(new Intent(this, MapsActivity.class));
                        }else{
                            requestLocationPermission();
                        }
                        break;
                    case R.id.nav_profile:
                        Log.d("menu","profile");
                        return true;
                }
                return false;
            };

    private void requestLocationPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE );
        }
    }
}