package com.example.next2me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.next2me.utils.UserHelper;
import com.example.next2me.utils.Utilities;
import com.google.android.material.textfield.TextInputEditText;

public class DescriptionFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_description, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RegistrationActivity activity = (RegistrationActivity) getActivity();
        if(activity != null){
            activity.increaseProgressBar(20);
            Button btnContinua = view.findViewById(R.id.buttonContinua);
            EditText description = view.findViewById(R.id.description);
            btnContinua.setOnClickListener((View.OnClickListener) v -> {
                if(description.getText().length() > 0){
                    UserHelper.getInstance().setDescription(description.getText().toString());
                    Utilities.insertFragment((AppCompatActivity) activity, new PhotoFragment(), "PHOTO_FRAGMENT");
                }
            });
        }
    }
}
