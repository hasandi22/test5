package com.example.nutrimind;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class StressRelief extends AppCompatActivity {


    // Declare variables for the TabLayout and ViewPager
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    // Declare a PageAdapter to manage the fragments
    private PageAdapter pageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stress_relief);  // Set the layout for this activity

        // Initialize the TabLayout and ViewPager from the layout
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.pageView);

        // Create a PageAdapter to link ViewPager with the fragments
        pageAdapter = new PageAdapter(this);

        // Set the adapter for the ViewPager
        viewPager.setAdapter(pageAdapter);

        // Set an OnTabSelectedListener for the TabLayout to sync it with the ViewPager
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // When a tab is selected, set the current item in the ViewPager
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // You can handle logic for tab unselection here if needed
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // You can handle logic for tab reselection here if needed
            }
        });

        // Register a callback to sync ViewPager's position with the TabLayout
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                // Sync the tab position with the ViewPager's current page
                tabLayout.getTabAt(position);
            }
        });
    }
}