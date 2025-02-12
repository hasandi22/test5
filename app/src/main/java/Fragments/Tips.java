package Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nutrimind.HomeActivity;
import com.example.nutrimind.R;

public class Tips extends Fragment {

    // Constructor: Required empty public constructor for the Fragment
    public Tips() {
        // Required empty public constructor
    }

    // onCreateView method: Called when the fragment's view is created
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment (R.layout.fragment_tips)
        View view = inflater.inflate(R.layout.fragment_tips, container, false);

        // Find the back button in the layout by its ID
        Button btnBackToHome = view.findViewById(R.id.btnBackToHome);

        // Set a click listener for the back button
        btnBackToHome.setOnClickListener(v -> {
            // Create an Intent to open the HomeActivity
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);  // Start the HomeActivity when the button is clicked
        });

        // Return the inflated view
        return view;
    }
}
