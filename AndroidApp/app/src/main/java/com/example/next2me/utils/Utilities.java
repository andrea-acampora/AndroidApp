package com.example.next2me.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.next2me.R;

public class Utilities {
    public static void insertFragment(AppCompatActivity activity, Fragment fragment, String tag){
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    public static Bitmap byteToBitmap(byte[] b) {
        return (b == null || b.length == 0) ? null : BitmapFactory
                .decodeByteArray(b, 0, b.length);
    }


}
