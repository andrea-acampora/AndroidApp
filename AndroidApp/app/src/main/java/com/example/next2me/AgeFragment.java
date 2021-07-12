package com.example.next2me;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.next2me.utils.UserHelper;
import com.example.next2me.utils.Utilities;

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
            activity.increaseProgressBar(20);
            DatePicker date = view.findViewById(R.id.datePicker);
            Button btnContinua = view.findViewById(R.id.buttonContinua);
            btnContinua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String day;
                    String month;
                    String year;
                    if(date.getDayOfMonth() < 10){
                        day = "0" + date.getDayOfMonth();
                    }else{
                        day = String.valueOf(date.getDayOfMonth());
                    }

                    if(date.getMonth() < 10){
                        month = "0" + date.getDayOfMonth();
                    }else{
                        month = String.valueOf(date.getMonth());
                    }


                    year =  String.valueOf(date.getYear());
                    String birthdate = day + "/" + month + "/" + year;
                    Log.d("eta", "x = " + day);
                    Log.d("eta", "x = " + month);
                    Log.d("eta", "x = " + year);
                    UserHelper.getInstance().setBirthdate(birthdate);
                    Utilities.insertFragment((AppCompatActivity) activity, new GenderFragment(), "GENDER_FRAGMENT");
                }
            });
        }
    }
}
