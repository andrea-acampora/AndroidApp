package com.example.androidapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

public class AgeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_age, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RegistrationActivity activity = (RegistrationActivity) getActivity();
        if(activity != null){
            DatePicker date = view.findViewById(R.id.datePicker);
            Button btnContinua = view.findViewById(R.id.buttonContinua);
            btnContinua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.getUser().setBirthdate(date.toString());
                    Utilities.insertFragment((AppCompatActivity) activity, new AgeFragment(), "AGE_FRAGMENT");
                }
            });
        }

    }
}
