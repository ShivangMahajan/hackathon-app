package com.example.shivam.project1;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class fragmentSos extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragement_sos,container,false);
    }

    public void onSosclick(View view){
        SmsManager mySmsManager = SmsManager.getDefault();
        mySmsManager.sendTextMessage("6239027206",null, "Hello", null, null);
    }

}

