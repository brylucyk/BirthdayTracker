package com.example.blucyk.birthdaytracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SetReminder#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetReminder extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static EditText birthdayHaver;
    private static int _year;
    private static int _month;
    private static int _day;
    private static View view;
    Button chooseDate;

    private static DBAdapter db;

    public SetReminder() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment set_reminder.
     */
    // TODO: Rename and change types and number of parameters
    public static SetReminder newInstance(String param1, String param2) {
        SetReminder fragment = new SetReminder();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /*
     * Copies the existing database file.
     *
     * @author  Lianne Wong
     * @param   InputStream inputStream, OutputStream outputStream
     * @return  void
     */
    public void CopyDB(InputStream inputStream, OutputStream outputStream) throws IOException {
        //Copy one byte at a time
        byte [] buffer = new byte[1024];
        int length;
        while((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer,0,length);
        }
        inputStream.close();
        outputStream.close();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // load the sessions into the array list
        db = new DBAdapter(getActivity().getApplicationContext());

        // get the existing database file or from the assets folder if it doesn't exist
        try {
            String destPath = "data/data/"+getActivity().getPackageName()+"/databases";
            File f = new File(destPath);
            if(!f.exists()){
                f.mkdirs();
                CopyDB(getActivity().getBaseContext().getAssets().open("mydb"),
                        new FileOutputStream(destPath + "/BirthdayTrackerDB"));
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_set_reminder, container, false);

        birthdayHaver = (EditText) view.findViewById(R.id.editTextBirthdayHaver);
        TextView linkHelp = (TextView) view.findViewById(R.id.linkHelp);

        linkHelp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://support.google.com/calendar/answer/37113?co=GENIE.Platform%3DAndroid&hl=en"));
                startActivity(viewIntent);
            }
        });

        chooseDate = (Button) view.findViewById(R.id.btnChooseDate);

        chooseDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                showDatePickerDialog(view);
            }
        });

        Button setCake = (Button) view.findViewById(R.id.btnCake);
        setCake.setEnabled(false);
        setCake.setOnClickListener(new View.OnClickListener() {
            Spinner cakeNumber = (Spinner) view.findViewById(R.id.spinnerCakeNumber);
            Spinner cakeUnits = (Spinner) view.findViewById(R.id.spinnerCakeUnits);

            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View arg0) {
                setOtherReminders("Cake",
                        Integer.parseInt(cakeNumber.getSelectedItem().toString()),
                        cakeUnits.getSelectedItem().toString());
            }
        });
        Button setGift = (Button) view.findViewById(R.id.btnGift);
        setGift.setEnabled(false);
        setGift.setOnClickListener(new View.OnClickListener() {
            Spinner giftNumber = (Spinner) view.findViewById(R.id.spinnerGiftNumber);
            Spinner giftUnits = (Spinner) view.findViewById(R.id.spinnerGiftUnits);

            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View arg0) {
                setOtherReminders("Gift",
                        Integer.parseInt(giftNumber.getSelectedItem().toString()),
                        giftUnits.getSelectedItem().toString());
            }
        });
        Button setCard = (Button) view.findViewById(R.id.btnCard);
        setCard.setEnabled(false);
        setCard.setOnClickListener(new View.OnClickListener() {
            Spinner cardNumber = (Spinner) view.findViewById(R.id.spinnerCardNumber);
            Spinner cardUnits = (Spinner) view.findViewById(R.id.spinnerCardUnits);

            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View arg0) {
                setOtherReminders("Card",
                        Integer.parseInt(cardNumber.getSelectedItem().toString()),
                        cardUnits.getSelectedItem().toString());
            }
        });
        Button setOther = (Button) view.findViewById(R.id.btnSetOther);
        setOther.setEnabled(false);
        setOther.setOnClickListener(new View.OnClickListener() {
            EditText other = (EditText) view.findViewById(R.id.editTextOther);
            Spinner otherNumber = (Spinner) view.findViewById(R.id.spinnerOtherNumber);
            Spinner otherUnits = (Spinner) view.findViewById(R.id.spinnerOtherUnits);

            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View arg0) {
                setOtherReminders(other.getText().toString(),
                        Integer.parseInt(otherNumber.getSelectedItem().toString()),
                        otherUnits.getSelectedItem().toString());
            }
        });

        return view;
    }

    public static String getBirthdayName() {
        return birthdayHaver.getText().toString();
    }

    // date picker
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker dpView, int year, int month, int day) {
            // insert birthday into database
            db.open();
            db.insertBirthday(getBirthdayName(), month, day);
            db.close();

            // save new date
            _year = year;
            _month = month;
            _day = day;

            // enable buttons
            Button setCake = (Button) view.findViewById(R.id.btnCake);
            setCake.setEnabled(true);
            Button setGift = (Button) view.findViewById(R.id.btnGift);
            setGift.setEnabled(true);
            Button setCard = (Button) view.findViewById(R.id.btnCard);
            setCard.setEnabled(true);
            Button setOther = (Button) view.findViewById(R.id.btnSetOther);
            setOther.setEnabled(true);

            // create calendar notification
            String title = getBirthdayName() + "'s Birthday";

            Calendar beginTime = Calendar.getInstance();
            beginTime.set(year, month, day, 00, 00);

            if (Build.VERSION.SDK_INT >= 14) {
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setData(CalendarContract.Events.CONTENT_URI);
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,beginTime.getTimeInMillis());
                intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
                intent.putExtra(CalendarContract.Events.TITLE, title);
                intent.putExtra(CalendarContract.Events.ALLOWED_REMINDERS,
                        CalendarContract.Reminders.METHOD_ALERT);
                intent.putExtra(CalendarContract.Events.RRULE, "FREQ=YEARLY");
                intent.putExtra(CalendarContract.Events.DURATION, "+P24H");
                intent.putExtra(CalendarContract.Events.AVAILABILITY,
                        CalendarContract.Events.AVAILABILITY_FREE);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra("beginTime", beginTime.getTimeInMillis());
                intent.putExtra("allDay", true);
                intent.putExtra("rrule", "FREQ=YEARLY");
                intent.putExtra("endTime", beginTime.getTimeInMillis()+60*60*1000);
                intent.putExtra("title", title);
                startActivity(intent);
            }
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setOtherReminders(String reminderType, int numberPrior, String units) {

        String title = "Get " + reminderType + " for " + getBirthdayName();

        String birthday = _year + "-";
        if(_month < 10) {
            birthday += "0" + _month;
        }
        else {
            birthday += _month;
        }
        birthday += "-";
        if(_day < 10) {
            birthday += "0" + _day;
        }
        else {
            birthday += _day;
        }

        LocalDate date = LocalDate.parse(birthday);
        Log.i("bday", String.valueOf(date));

        int reminderDay = 0;
        int reminderMonth = 0;

        if(units.equals("days prior")) {
            LocalDate newDate = date.minusDays(numberPrior);
            reminderMonth = newDate.getMonthValue();
            reminderDay = newDate.getDayOfMonth();
            Log.i("newDate", String.valueOf(newDate));
            Log.i("newDate", String.valueOf(reminderMonth));
            Log.i("newDate", String.valueOf(reminderDay));
        }
        else if(units.equals("weeks prior")) {
            LocalDate newDate = date.minusWeeks(numberPrior);
            reminderMonth = newDate.getMonthValue();
            Log.i("newDate", String.valueOf(newDate));
            Log.i("newDate", String.valueOf(reminderMonth));
            reminderDay = newDate.getDayOfMonth();
            Log.i("newDate", String.valueOf(reminderDay));
        }

        if(reminderDay != 0 && reminderMonth != 0) {
            Calendar beginTime = Calendar.getInstance();
            beginTime.set(_year, reminderMonth, reminderDay, 00, 00);

            if (Build.VERSION.SDK_INT >= 14) {
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setData(CalendarContract.Events.CONTENT_URI);
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,beginTime.getTimeInMillis());
                intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
                intent.putExtra(CalendarContract.Events.TITLE, title);
                intent.putExtra(CalendarContract.Events.ALLOWED_REMINDERS,
                        CalendarContract.Reminders.METHOD_ALERT);
                intent.putExtra(CalendarContract.Events.RRULE, "FREQ=YEARLY");
                intent.putExtra(CalendarContract.Events.DURATION, "+P24H");
                intent.putExtra(CalendarContract.Events.AVAILABILITY,
                        CalendarContract.Events.AVAILABILITY_FREE);
                startActivity(intent);
            } else {
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra("beginTime", beginTime.getTimeInMillis());
                intent.putExtra("allDay", true);
                intent.putExtra("rrule", "FREQ=YEARLY");
                intent.putExtra("endTime", beginTime.getTimeInMillis() + 60 * 60 * 1000);
                intent.putExtra("title", title);
                startActivity(intent);
            }
        }
    }
}