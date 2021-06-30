package com.example.blucyk.birthdaytracker;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BirthdayList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BirthdayList extends Fragment {

    ArrayList<Birthday> birthdayArrayList;
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recyclerViewManager;

    private static DBAdapter db;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BirthdayList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment birthday_list.
     */
    // TODO: Rename and change types and number of parameters
    public static BirthdayList newInstance(String param1, String param2) {
        BirthdayList fragment = new BirthdayList();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_birthday_list, container, false);
        recyclerView = view.findViewById( R.id.bdayListRecyclerView );
        recyclerViewManager = new LinearLayoutManager( getActivity().getApplicationContext() );
        recyclerView.setLayoutManager( recyclerViewManager );
        recyclerView.setHasFixedSize( true );

        birthdayArrayList = new ArrayList<Birthday>();

        // load the sessions into the array list
        db = new DBAdapter(getActivity().getApplicationContext());

        // get the existing database file or from the assets folder if it doesn't exist
        try {
            String destPath = "data/data/"+getActivity().getPackageName()+"/databases";
            File f = new File(destPath);
            if(!f.exists()){
                f.mkdirs();
                CopyDB(getActivity().getBaseContext().getAssets().open("mydb"),
                        new FileOutputStream(destPath + "/MyDB"));
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        db.open();
        Cursor cBdays = db.getAllBirthdays();

        if(cBdays.moveToFirst())
        {
            do{
                // create objects for the sessions stored in the database
                Birthday bday = new Birthday(
                        cBdays.getInt(0),
                        cBdays.getString(1),
                        cBdays.getInt(2),
                        cBdays.getInt(3));

                birthdayArrayList.add(bday);
            } while(cBdays.moveToNext());
        }
        db.close();

        // enable/disable the button to clear all sessions
        Button clearButton = view.findViewById(R.id.btnClearAll);

        if(birthdayArrayList.isEmpty()) {
            clearButton.setEnabled(false);
            clearButton.setText(getResources().getString(R.string.btn_no_bdays));
        }
        else {
            clearButton.setEnabled(true);
            clearButton.setText(getResources().getString(R.string.btn_clear_all));
        }

        recyclerViewAdapter = new BirthdayRecyclerViewAdapter( getActivity().getApplicationContext(),
                birthdayArrayList);
        recyclerView.setAdapter( recyclerViewAdapter );

        // Inflate the layout for this fragment
        return view;
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
}