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
import android.provider.ContactsContract;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class MapsFragment extends Fragment {

    private GoogleMap mMap;

    private final int LOCATION_PERMISSION_CODE = 1;
    LocationManager locationManager;
    private LatLng currentUserPos = new LatLng(44.0575500, 12.5652800);
    private List<String> matches = new ArrayList<>();
    private HashMap<String, Marker> markers = new HashMap<>();


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            getAllMatches();
        }
    };

    /*
    1. guardo tutti i matches accepted di una persona
    2. per ogni id, aggiungo un listener (nel onCreate creo il marker)
    */

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

    private void getAllMatches(){

        DatabaseReference reference = DatabaseHelper.getInstance().getDb().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("MATCHES");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue().equals("accepted")){
                    String uid = snapshot.getKey();
                    addListenerToMatch(uid);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void addListenerToMatch(String uid){
        DatabaseReference reference = DatabaseHelper.getInstance().getDb().getReference("Users").child(uid);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getKey().equals("POS")){
                    Double lat = snapshot.child("lat").getValue(Double.class);
                    Double lng = snapshot.child("long").getValue(Double.class);
                    Log.d("match", "lat = " + lat);
                    Log.d("match", "long = " + lng);

                    MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(lat, lng));

                    Bitmap bitmap = createUserBitmap();
                    if (bitmap != null) {
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));

                        Marker marker = mMap.addMarker(markerOptions);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentUserPos));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUserPos, 12));
                        markers.put(uid, marker);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if(snapshot.getKey().equals("POS")){
                    Double lat = (Double) snapshot.child("lat").getValue();
                    Double lng = (Double) snapshot.child("long").getValue();
                    Marker marker = markers.get(uid);
                    if (marker != null) {
                        marker.setPosition(new LatLng(lat, lng));
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String id = snapshot.getKey();
                Marker marker = markers.get(id);
                if (marker != null) {
                    marker.remove();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

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
}

