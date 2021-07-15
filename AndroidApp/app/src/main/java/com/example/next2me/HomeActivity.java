package com.example.next2me;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.example.next2me.utils.ChatConstants;
import com.example.next2me.utils.DatabaseHelper;
import com.example.next2me.utils.LocationService;
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
    ProfileFragment profileFragment;
    LocationManager locationManager;
    private LatLng currentUserPos = new LatLng(44.0575500,12.5652800);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChatLogIn();
        setContentView(R.layout.activity_home);
        profileFragment = new ProfileFragment();
        menu_nav = findViewById(R.id.menu_nav);
        menu_nav.setSelectedItemId(R.id.nav_home);
        menu_nav.setOnNavigationItemSelectedListener(selectedListener);
        DatabaseHelper.getInstance().SendUserPositionToDB(new LatLng(44.0575500,12.5652800));



        Utilities.insertFragment(this, new DashboardFragment(), "FRAGMENT_TAG");

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                currentUserPos = new LatLng(location.getLatitude(), location.getLongitude());
                if(profileFragment.isPosSharingEnabled()){
                   DatabaseHelper.getInstance().SendUserPositionToDB(currentUserPos);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("pos", "status changed");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("pos", "provider enabled");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("pos", "provider disabled");
            }
        };

        try {
            locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 1, locationListener);
            }
        } catch (Exception e){
            e.printStackTrace();
        }


    }



    private void ChatLogIn() {
        if (CometChat.getLoggedInUser() == null) {
            CometChat.login(FirebaseAuth.getInstance().getCurrentUser().getUid(), ChatConstants.API_KEY, new CometChat.CallbackListener<com.cometchat.pro.models.User>() {

                @Override
                public void onSuccess(User user) {
                }

                @Override
                public void onError(CometChatException e) {
                }
            });
        }
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
                        Utilities.insertFragment(this, new MapsFragment(),"MAP FRAGMENT");
                        break;
                    case R.id.nav_profile:
                        menu_nav.getMenu().getItem(2).setChecked(true);
                        Utilities.insertFragment(this, profileFragment,"PROFILE FRAGMENT");
                        break;

                    case R.id.nav_chat:
                        menu_nav.getMenu().getItem(3).setChecked(true);
                        Utilities.insertFragment(this, new ChatListFragment(),"CHAT FRAGMENT");
                        break;

                    case R.id.nav_matches:
                        menu_nav.getMenu().getItem(4).setChecked(true);
                        Utilities.insertFragment(this, new MatchFragment(),"MATCH FRAGMENT");
                        break;
                }
                return false;
            };

    public LatLng getCurrentUserPos(){
        return currentUserPos;
    }

}