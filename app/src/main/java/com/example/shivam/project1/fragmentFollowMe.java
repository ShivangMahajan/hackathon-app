package com.example.shivam.project1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.LOCATION_SERVICE;

public class fragmentFollowMe extends Fragment {
    private static final int PERMISSIONS_REQUEST = 1;
    private Button saveBtn;
    private Button followContact;
    private TextView number;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private Boolean isUser = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragement_follow_me, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        saveBtn = getActivity().findViewById(R.id.savebtn);
        followContact = getActivity().findViewById(R.id.followcontact);
        number = getActivity().findViewById(R.id.contact);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!number.getText().toString().isEmpty()) {
                    isAUser();
                }
                else
                   number.setError("Cannot be empty");
            }
        });

        followContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String path = "users/" + user.getPhoneNumber() + "/access";
//                DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
//                if (ref.)
                getPhoneno();
            }
        });
        // Check GPS is enabled
        LocationManager lm = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(getContext(), "Please enable location services", Toast.LENGTH_SHORT).show();

        }

        // Check location permission is granted - if it is, start
        // the service, otherwise request the permission
        int permission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            startTrackerService();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }
    }

    private void isAUser() {
        if (number.getText().toString().equals(user.getPhoneNumber()))
        {
            Toast.makeText(getContext(), "You cannot add your own number", Toast.LENGTH_SHORT).show();
            return;
        }
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("users/" + number.getText().toString());
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String path = "users/" + user.getPhoneNumber() + "/contacts";
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
                    ref.push().setValue(number.getText().toString());
                    path = "users/" + number.getText().toString() + "/access";
                    ref = FirebaseDatabase.getInstance().getReference(path);
                    ref.push().setValue(user.getPhoneNumber());
                    Toast.makeText(getContext(), "Save successfully", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(getContext(), "No user exists with this number", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ds", databaseError.getMessage()); //Don't ignore errors!
            }
        };
        rootRef.addListenerForSingleValueEvent(eventListener);

    }

    private void getPhoneno() {
        final String path = "users/" + user.getPhoneNumber() + "/access";
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Log.v("ds", ds.getValue().toString());
                        String phno = ds.getValue().toString();
                        Intent intent = new Intent(getContext(), FollowContact.class);
                        intent.putExtra("phno", phno);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(getContext(), "You don't have access to see anyone's location", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ref.addListenerForSingleValueEvent(valueEventListener);
    }

    private void startTrackerService() {
        getActivity().startService(new Intent(getContext(), TrackerService.class));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Start the service when the permission is granted
            startTrackerService();
        } else {

        }
    }


}

