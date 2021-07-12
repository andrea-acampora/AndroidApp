package com.example.next2me;

import androidx.annotation.DrawableRes;
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
import android.graphics.PorterDuff;
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
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.Context.LOCATION_SERVICE;

public class MapsFragment extends Fragment {

    private GoogleMap mMap;

    private final int LOCATION_PERMISSION_CODE = 1;
    LocationManager locationManager;
    private List<String> matches = new ArrayList<>();
    private HashMap<String, Marker> idMarkers = new HashMap<>();
    private HashMap<Marker, String> markersId = new HashMap<>();
    private Bitmap imageBitmap;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            getAllMatches();

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    String uid = markersId.get(marker);

                    return false;
                }
            });
        }
    };

    private void setBitmap(Bitmap bitmap){
        this.imageBitmap = Bitmap.createBitmap(bitmap);
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

                    StorageReference imageRef = DatabaseHelper.getInstance().getStorageRef().child("ProfilePictures").child(uid + ".jpg");
                    imageRef.getBytes(1024*1024)
                            .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    Bitmap result = null;
                                    try {
                                        result = Bitmap.createBitmap(dp(70), dp(85), Bitmap.Config.ARGB_8888);
                                        result.eraseColor(Color.TRANSPARENT);
                                        Canvas canvas = new Canvas(result);
                                        Drawable drawable;
                                        drawable = ContextCompat.getDrawable(getActivity(), R.drawable.livepin);

                                        drawable.setBounds(0, 0, dp(72), dp(88));
                                        drawable.draw(canvas);

                                        Paint roundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                                        RectF bitmapRect = new RectF();
                                        canvas.save();
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        if (bitmap != null) {
                                            BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                                            Matrix matrix = new Matrix();
                                            float scale = dp(55) / (float) bitmap.getWidth();
                                            matrix.postTranslate(dp(6), dp(6));
                                            matrix.postScale(scale, scale);
                                            roundPaint.setShader(shader);
                                            shader.setLocalMatrix(matrix);
                                            bitmapRect.set(dp(6), dp(6), dp(60 + 6), dp(60 + 6));
                                            canvas.drawRoundRect(bitmapRect, dp(28), dp(28), roundPaint);
                                        }
                                        canvas.restore();
                                        try {
                                            canvas.setBitmap(null);
                                        } catch (Exception e) {
                                        }
                                    } catch (Throwable t) {
                                        t.printStackTrace();
                                    }
                                    Marker marker = mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(lat, lng))
                                            .icon(BitmapDescriptorFactory.fromBitmap(result)));
                                    idMarkers.put(uid, marker);
                                    markersId.put(marker, uid);
                                }
                            });

                    DatabaseReference reference = DatabaseHelper.getInstance().getDb().getReference("Users").child(uid).child("POS");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Double lat = (Double) snapshot.child("lat").getValue();
                            Double lng = (Double) snapshot.child("long").getValue();
                            LatLng currentUserPos = new LatLng(lat, lng);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentUserPos));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUserPos, 12));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if(snapshot.getKey().equals("POS")){
                    Double lat = (Double) snapshot.child("lat").getValue();
                    Double lng = (Double) snapshot.child("long").getValue();
                    Marker marker = idMarkers.get(uid);
                    if (marker != null) {
                        marker.setPosition(new LatLng(lat, lng));
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String id = snapshot.getKey();
                Marker marker = idMarkers.get(uid);
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

    public int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(getResources().getDisplayMetrics().density * value);
    }
}

