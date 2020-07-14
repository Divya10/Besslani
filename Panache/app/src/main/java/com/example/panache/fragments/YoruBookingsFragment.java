package com.example.panache.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.panache.R;
import com.example.panache.MyBookingsAdapter;
import com.example.panache.MyBookingsData;


public class YoruBookingsFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_yoru_bookings, container, false);
        MyBookingsData[] myListData = new MyBookingsData[]{
                new MyBookingsData("Email", android.R.drawable.ic_dialog_email),
                new MyBookingsData("Info", android.R.drawable.ic_dialog_info),
                new MyBookingsData("Delete", android.R.drawable.ic_delete),
                new MyBookingsData("Dialer", android.R.drawable.ic_dialog_dialer),
                new MyBookingsData("Alert", android.R.drawable.ic_dialog_alert),
                new MyBookingsData("Map", android.R.drawable.ic_dialog_map),
                new MyBookingsData("Email", android.R.drawable.ic_dialog_email),
                new MyBookingsData("Info", android.R.drawable.ic_dialog_info),
                new MyBookingsData("Delete", android.R.drawable.ic_delete),
                new MyBookingsData("Dialer", android.R.drawable.ic_dialog_dialer),
                new MyBookingsData("Alert", android.R.drawable.ic_dialog_alert),
                new MyBookingsData("Map", android.R.drawable.ic_dialog_map),
        };
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        MyBookingsAdapter adapter = new MyBookingsAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }

}
