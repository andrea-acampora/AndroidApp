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

import com.example.next2me.utils.UserHelper;
import com.example.next2me.utils.Utilities;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class GenderFragment extends Fragment {

    private ImageButton maleButton;
    private ImageButton femaleButton;
    private SwitchMaterial malePref;
    private SwitchMaterial femalePref;
    private Button btnContinua;

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
            activity.increaseProgressBar(20);
            maleButton = view.findViewById(R.id.buttonMale);
            femaleButton = view.findViewById(R.id.buttonFemale);

            maleButton.setOnClickListener(v -> {
                UserHelper.getInstance().setGender("M");
                femaleButton.setVisibility(View.INVISIBLE);
            });

            femaleButton.setOnClickListener(v -> {
                UserHelper.getInstance().setGender("F");
                maleButton.setVisibility(View.INVISIBLE);
            });

            malePref = view.findViewById(R.id.uomini);
            femalePref = view.findViewById(R.id.donne);

            btnContinua  = view.findViewById(R.id.buttonContinua);
            btnContinua.setOnClickListener((View.OnClickListener) v -> {
            if(getUserPreferences()!=null){
                UserHelper.getInstance().setPreferences(getUserPreferences());
                Utilities.insertFragment((AppCompatActivity) activity, new DescriptionFragment(), "DESCRIPTION_FRAGMENT");
            }
            });
        }

    }

    private String getUserPreferences(){
        if(femalePref.isChecked() && !malePref.isChecked()){
            return "F";
        }else if(!femalePref.isChecked() && malePref.isChecked()){
            return "M";
        }else if(femalePref.isChecked() && malePref.isChecked()){
            return "MF";
        }
        return null;
    }

}
