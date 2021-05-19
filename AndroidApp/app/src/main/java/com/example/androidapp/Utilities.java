package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Utilities {
    static void insertFragment(AppCompatActivity activity, Fragment fragment, String tag){
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }


}
