package com.stoefelz.justdoit;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

//Time Picker Dialog for adding Task
public class Time_Picker extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog time_picker_dialog =
                new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener)getActivity(), hour, minute, true);
        return time_picker_dialog;
    }

}
