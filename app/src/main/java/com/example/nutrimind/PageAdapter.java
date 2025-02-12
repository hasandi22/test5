package com.example.nutrimind;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import Fragments.Meditation;
import Fragments.Tips;

public class PageAdapter extends FragmentStateAdapter {
    // Constructor: Accepts a FragmentActivity to create the adapter
    public PageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);  // Calls the superclass constructor with the activity
    }

    // Creates the fragment based on the position
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Returns a new fragment based on the current position (0 = Meditation, 1 = Tips)
        switch (position) {
            case 0:  // For the first position, return the Meditation fragment
                return new Meditation();
            case 1:  // For the second position, return the Tips fragment
                return new Tips();
            default:  // In case of an invalid position, return the Meditation fragment by default
                return new Meditation();
        }
    }

    // Returns the total number of pages (fragments) to display
    @Override
    public int getItemCount() {
        return 2;  // We have two fragments: Meditation and Tips
    }
}

