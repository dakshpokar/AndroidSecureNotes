package com.dakshpokar.asn;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FrameLayout mainFrame;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ProgressDialog progress;
    View v;
    private RecyclerView recycler_view;
    DatabaseHelper mDatabaseHelper;
    private List<Note> lstNotes;
    public static int X;
    public static int Y;
    private RecyclerViewAdapter recyclerViewAdapter;
    public ArrayList<Integer> deleteArray;
    //private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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

    private void populateListView()
    {
        Cursor data = mDatabaseHelper.getData();
        while (data.moveToNext())
        {
            Note note = new Note();
            note.setID(data.getInt(0));
            note.setTitle(data.getString(1));
            note.setNote(data.getString(2));
            note.setDate(data.getString(3));
            lstNotes.add(note);        //for COL2
        }
        recyclerViewAdapter = new RecyclerViewAdapter(getContext(), lstNotes);
        recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(recyclerViewAdapter);
    }
    public void ActionBarBackPressed()
    {
       // recyclerViewAdapter.myViewHolder.item_recycler.setBackgroundColor();
    }
    public void redraw()
    {
        ValueAnimator anim = ValueAnimator.ofArgb(Color.LTGRAY, Color.WHITE);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                recyclerViewAdapter.myViewHolder.item_recycler.setBackgroundColor((Integer)valueAnimator.getAnimatedValue());
            }
        });
    }

    public void delete()
    {
        deleteArray = recyclerViewAdapter.selectedArray;

        Log.i("Hi","Hi");
        String s = "";
        Collections.sort(deleteArray, Collections.<Integer>reverseOrder());
        deleteArray.add(new Integer(61));
        Iterator<Integer> iterator = deleteArray.iterator();
        lstNotes.removeAll(deleteArray);
        int position = iterator.next();

        while(iterator.hasNext())
        {
            s = s + String.valueOf(position);
            int ID = lstNotes.get(position).getID();
            mDatabaseHelper.remove(ID);
            lstNotes.remove(position);
            recyclerViewAdapter.notifyItemRemoved(position);
            position = iterator.next();
        }
        recyclerViewAdapter.selectionToolbar = false;



        recyclerViewAdapter.selectedArray.removeAll(recyclerViewAdapter.selectedArray);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        lstNotes = new ArrayList<>();
        v = inflater.inflate(R.layout.fragment_main,container,false);

        mainFrame = (FrameLayout)v.findViewById(R.id.fragment_main);

        recycler_view = (RecyclerView) v.findViewById(R.id.recycler_view);
        mDatabaseHelper = new DatabaseHelper(getActivity());
        populateListView();


        final FloatingActionButton fab_add = (FloatingActionButton)v.findViewById(R.id.fab_add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                android.app.FragmentManager initialFragment = getFragmentManager();
                FragmentTransaction fragmentTransaction = initialFragment.beginTransaction();
                notes notesObject = new notes();
                notesObject.setCods((int)fab_add.getX(), (int)fab_add.getY());
                fragmentTransaction.replace(R.id.frame_layout, notesObject);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                    }
                });



        return v;
    }

        // TODO: Rename method, update argument and hook method into UI event
   /*
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onMainFragmentInteraction(uri);
        }
    }
    */

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    */

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
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
    /*
    public interface OnFragmentInteractionListener {

        // TODO: Update argument type and name
        void onMainFragmentInteraction(Uri uri);
    }
    */

    @Override
    public void onStart() {
        super.onStart();

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
