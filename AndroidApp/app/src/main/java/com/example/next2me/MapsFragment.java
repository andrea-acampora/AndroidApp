package com.example.next2me;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.next2me.data.User;
import com.example.next2me.utils.DatabaseHelper;
import com.example.next2me.utils.Utilities;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class MapsFragment extends Fragment {

    private GoogleMap mMap;
    private MarkerOptions userMarkerOptions;
    private Marker userMarker;
    private final int LOCATION_PERMISSION_CODE = 1;
    LocationManager locationManager;
    private LatLng currentUserPos = new LatLng(44.0575500,12.5652800);
    private List<String> matches = new ArrayList<>();


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            drawUsersOnMap();

            getLocation();
        }


    };



    private Bitmap createUserBitmap() {

        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(dp(62), dp(76), Bitmap.Config.ARGB_8888);
            result.eraseColor(Color.TRANSPARENT);
            Canvas canvas = new Canvas(result);
            Drawable drawable;
            drawable = ContextCompat.getDrawable(getActivity(), R.drawable.livepin);

            drawable.setBounds(0, 0, dp(62), dp(76));
            drawable.draw(canvas);

            Paint roundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            RectF bitmapRect = new RectF();
            canvas.save();

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar);

            //Bitmap bitmap = BitmapFactory.decodeFile(DatabaseHelper.getInstance().getStorageRef().child("ProfilePictures/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg").getPath()); /*generate bitmap here if your image comes from any url*/
//            Log.d("map", DatabaseHelper.getInstance().getStorageRef().child("ProfilePictures/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg").getDownloadUrl().getResult().toString());
            if (bitmap != null) {
                BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Matrix matrix = new Matrix();
                float scale = dp(52) / (float) bitmap.getWidth();
                matrix.postTranslate(dp(5), dp(5));
                matrix.postScale(scale, scale);
                roundPaint.setShader(shader);
                shader.setLocalMatrix(matrix);
                bitmapRect.set(dp(5), dp(5), dp(52 + 5), dp(52 + 5));
                canvas.drawRoundRect(bitmapRect, dp(26), dp(26), roundPaint);
            }
            canvas.restore();
            try {
                canvas.setBitmap(null);
            } catch (Exception e) {
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return result;
    }

    public int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(getResources().getDisplayMetrics().density * value);
    }

    private void getLocation() {
        try {
            locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
            } else {
                if(userMarkerOptions==null) {

                    userMarkerOptions = new MarkerOptions().position(currentUserPos);
                    Bitmap bitmap = createUserBitmap();
                    if (bitmap != null) {

                        userMarkerOptions.title("Ketan Ramani");
                        userMarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                        userMarker = mMap.addMarker(userMarkerOptions);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentUserPos));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUserPos, 13));
                    }
                }

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, location -> {
                    currentUserPos = new LatLng(location.getLatitude(), location.getLongitude());
                    if(userMarkerOptions==null){
                        Log.d("map", "entrato 2");

                        /*userMarkerOptions = new MarkerOptions().position(currentUserPos);
                        Bitmap bitmap = createUserBitmap();
                        if (bitmap != null) {
                            Log.d("map", "entrato 3");

                            userMarkerOptions.title("Ketan Ramani");
                            userMarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                            userMarker = mMap.addMarker(userMarkerOptions);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentUserPos));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUserPos, 13));
                        }*/
                    }else{
                        Log.d("map", "entrato 4");

                        userMarker.setPosition(currentUserPos);
                    }
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUserPos, 13));
                    DatabaseHelper.getInstance().SendUserPositionToDB(currentUserPos);
                });

                userMarker.setPosition(currentUserPos);

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }


    private void drawUsersOnMap() {

        DatabaseHelper.getInstance().getDb().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("MATCHES").equalTo("accepted").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> matches = new ArrayList<>();
                for(DataSnapshot user : snapshot.getChildren()){
                    if(user.getValue().toString().equals("accepted")){
                        matches.add(user.getKey());
                    }
                }
                addToMatches(matches);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addToMatches(List<String> matches){
        this.matches = matches; 
    }
}