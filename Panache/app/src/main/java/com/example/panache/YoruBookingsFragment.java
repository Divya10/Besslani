package com.example.panache;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


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
        mybookingsdata[] myListData = new mybookingsdata[]{
                new mybookingsdata("Email", android.R.drawable.ic_dialog_email),
                new mybookingsdata("Info", android.R.drawable.ic_dialog_info),
                new mybookingsdata("Delete", android.R.drawable.ic_delete),
                new mybookingsdata("Dialer", android.R.drawable.ic_dialog_dialer),
                new mybookingsdata("Alert", android.R.drawable.ic_dialog_alert),
                new mybookingsdata("Map", android.R.drawable.ic_dialog_map),
                new mybookingsdata("Email", android.R.drawable.ic_dialog_email),
                new mybookingsdata("Info", android.R.drawable.ic_dialog_info),
                new mybookingsdata("Delete", android.R.drawable.ic_delete),
                new mybookingsdata("Dialer", android.R.drawable.ic_dialog_dialer),
                new mybookingsdata("Alert", android.R.drawable.ic_dialog_alert),
                new mybookingsdata("Map", android.R.drawable.ic_dialog_map),
        };
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        myBookingsAdapter adapter = new myBookingsAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }

}
