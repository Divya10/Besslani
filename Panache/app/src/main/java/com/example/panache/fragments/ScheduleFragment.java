package com.example.panache.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.panache.R;

import java.util.Calendar;


public class ScheduleFragment extends Fragment {
    View view;
    Button btnDatePicker, btnInTimePicker,btnOutTimePicker;
    EditText txtDate, txtInTime,txtOutTime;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_schedule, container, false);
        btnDatePicker = (Button) view.findViewById(R.id.btn_date);
        btnInTimePicker = (Button) view.findViewById(R.id.btn_in_time);
        btnOutTimePicker = (Button) view.findViewById(R.id.btn_out_time);
        txtDate = (EditText) view.findViewById(R.id.in_date);
        txtInTime = (EditText) view.findViewById(R.id.in_time);
        txtOutTime = (EditText) view.findViewById(R.id.out_time);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                    txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }


        });
        btnInTimePicker.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txtInTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        }));
        btnOutTimePicker.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txtOutTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        }));
        return view;

    }

}