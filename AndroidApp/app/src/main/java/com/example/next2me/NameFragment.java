package com.example.next2me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.next2me.utils.UserHelper;
import com.example.next2me.utils.Utilities;
import com.google.android.material.textfield.TextInputEditText;

public class NameFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_name, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RegistrationActivity activity = (RegistrationActivity) getActivity();
        if(activity != null){
            activity.increaseProgressBar(20);
            Button btnContinua = view.findViewById(R.id.buttonContinua);
            TextInputEditText name = view.findViewById(R.id.name);
            btnContinua.setOnClickListener((View.OnClickListener) v -> {
                if(name.getText().length() > 0){
                    UserHelper.getInstance().setName(name.getText().toString());
                    Utilities.insertFragment((AppCompatActivity) activity, new AgeFragment(), "AGE_FRAGMENT");
                }
            });
        }
    }
}