package com.example.shivam.project1;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class fragmentSafetyAudit extends Fragment {
    private TextView mTextView;
    public static final String TAG = "TAG";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    public fragmentSafetyAudit()
    {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragement_safety_audit,container,false);


        final RatingBar secureRating = view.findViewById(R.id.securityRating);
        final RatingBar lightRating = view.findViewById(R.id.lightRating);
        final RatingBar transportRating = view.findViewById(R.id.walkRating);
        final RatingBar walkPath = view.findViewById(R.id.walkRating);
        Button submit = view.findViewById(R.id.ratebutton);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getUid();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float s =secureRating.getRating();
                float l = lightRating.getRating();
                float t = transportRating.getRating();
                float w = walkPath.getRating();
                //add user to database
                DocumentReference docRef = fStore.collection("rating").document(userID);
                Map<String,Object> user = new HashMap<>();
                user.put("security",s);
                user.put("lights",l);
                user.put("transport",t);
                user.put("walkpath" , w);

                //add user to database
                docRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: User Profile Created." + userID);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Failed to Create User " + e.toString());
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTextView = (TextView) getActivity().findViewById(R.id.pincode);
        Bundle bundle = getArguments();
        if(bundle != null)
        {
            String postalcode = bundle.getString("pincode");
            Log.d("bundle",postalcode);
            mTextView.setText(postalcode);
        }
    }
}