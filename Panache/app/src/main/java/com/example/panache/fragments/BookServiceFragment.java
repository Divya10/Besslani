package com.example.panache.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.panache.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BookServiceFragment extends Fragment {

    View view;
    BottomNavigationView navView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_book, container, false);
        navView = view.findViewById(R.id.navigation);
        Fragment fragment = new BookNowFragment();
        loadFragment(fragment);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        return view;


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {         Fragment fragment;
            //toolbar.setTitle("Employee Login");
            if (item.getItemId() == R.id.navigation_schedule) {//toolbar.setTitle("Customer Login");
                fragment = new ScheduleFragment();
                loadFragment(fragment);
                return true;
            }
            fragment = new BookNowFragment();
            loadFragment(fragment);
            return true;
        }
    };


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}

