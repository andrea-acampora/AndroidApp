package com.example.next2me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.next2me.utils.Utilities;

public class GenderFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gender, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RegistrationActivity activity = (RegistrationActivity) getActivity();
        if(activity != null){
            ImageButton uomo = view.findViewById(R.id.buttonMale);
            ImageButton donna = view.findViewById(R.id.buttonFemale);

            Button btnContinua = view.findViewById(R.id.buttonContinua);
            btnContinua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((RegistrationActivity) getActivity()).getUser().setGender("male");
                    ((RegistrationActivity) getActivity()).getUser().setPreferences("F");
                    Utilities.insertFragment((AppCompatActivity) activity, new DescriptionFragment(), "DESCRIPTION_FRAGMENT");
                }
            });
        }

    }
}
