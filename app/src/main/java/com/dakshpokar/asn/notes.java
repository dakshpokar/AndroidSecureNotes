package com.dakshpokar.asn;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link notes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class notes extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static boolean open = false;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    MotionEvent event;
    DatabaseHelper mDatabaseHelper;
    EditText title_text;
    EditText note_text;
    int ID;
    String titleString, dateString, noteString;
    boolean already = false;
    public notes() {

        titleString = "";
        dateString = "";
        noteString = "";
        // Required empty public constructor
    }
    public void setCods(int cx, int cy)
    {
        this.cx = cx;
        this.cy = cy;
    }

    public void setArgs(int ID, String title, String note, String date)
    {
        already = true;
        this.ID = ID;
        titleString = title;
        noteString = note;
        dateString = date;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment notes.
     */
    // TODO: Rename and change types and number of parameters
    public static notes newInstance(String param1, String param2) {
        notes fragment = new notes();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    int cx,cy;

    @Override
    public void onAttach(Context context) {
        FloatingActionButton fab_add = ((Activity)context).getWindow().getDecorView().findViewById(R.id.fab_add);
        View v = ((Activity)context).getWindow().getDecorView().getRootView();

        int finalRadius = Math.max(v.getWidth(), v.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, finalRadius);
        v.setBackgroundColor(getResources().getColor(R.color.noteback));
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

            }
        });
        anim.setDuration(400);
        anim.start();
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private void AddData(String title, String note, String date)
    {
        boolean insertData = mDatabaseHelper.addData(title, note, date);
    }
    private void ModifyData(int ID, String title, String note, String date)
    {
        mDatabaseHelper.modifyData(ID, title, note, date);
        Toast.makeText(getActivity(), "Saved!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_notes, container, false);
        FloatingActionButton fab_save;
        mDatabaseHelper  = new DatabaseHelper(getActivity());
        fab_save = (FloatingActionButton)v.findViewById(R.id.fab_save);
        title_text = (EditText)v.findViewById(R.id.note_title);
        note_text = (EditText)v.findViewById(R.id.note_info);
        if(!titleString.equals(""))
        {
            title_text.setText(titleString);
        }
        if(!noteString.equals("")){
            note_text.setText(noteString);
        }
        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = title_text.getText().toString();
                String note = note_text.getText().toString();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date date;
                String currentDateTime;
                if(dateString.equals("")) {
                    date = new Date();
                    currentDateTime = formatter.format(date);
                }
                if (title.equals("") && note.equals("")) {
                    Toast.makeText(getActivity(), "Can`t save empty Note", Toast.LENGTH_SHORT).show();
                } else {
                    if(already == false) {
                        currentDateTime = dateString;
                        AddData(title, note, currentDateTime);
                        title_text.setText("");
                        note_text.setText("");
                    }
                    else
                    {
                        date = new Date();
                        title = title_text.getText().toString();
                        note = note_text.getText().toString();
                        currentDateTime = formatter.format(date);
                        ModifyData(ID, title, note, currentDateTime);
                    }
                }
            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event




    public boolean saveNote(View v)
    {
        title_text = (EditText)v.findViewById(R.id.note_title);
        note_text = (EditText)v.findViewById(R.id.note_info);
        String title = title_text.getText().toString();
        String note = note_text.getText().toString();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        String currentDateTime = formatter.format(date);
        if(title.equals("") && note.equals(""))
        {
            return true;
        }
        else {
            if(already == false){
                AddData(title, note, currentDateTime);
                title_text.setText("");
                note_text.setText("");
                return false;
            }
            else if(already == true)
            {
                ModifyData(ID, title, note, currentDateTime);
                return true;
            }
        }
        return true;
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
