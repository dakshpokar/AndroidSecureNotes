package com.dakshpokar.asn;

import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    Context mContext;
    List<Note>  mData;
    notes x;
    Boolean selectionToolbar = false;
    ActionMode mMode;
    public ArrayList<Integer> selectedArray = new ArrayList<>();

    public MyViewHolder myViewHolder;

    public RecyclerViewAdapter(Context mContext, List<Note> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v ;
        v = LayoutInflater.from(mContext).inflate(R.layout.notes_rows, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(v);
        myViewHolder = viewHolder;
        ValueAnimator anim = ValueAnimator.ofArgb(Color.LTGRAY, Color.WHITE);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                viewHolder.item_recycler.setBackgroundColor((Integer)valueAnimator.getAnimatedValue());
            }
        });
        viewHolder.item_recycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity)mContext;

                if(selectionToolbar == false) {
                    android.app.FragmentManager fragmentManager = ((Activity) mContext).getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    int id;
                    x = new notes();
                    String title, info, date;
                    id = viewHolder.getAdapterPosition();
                    title = mData.get(viewHolder.getAdapterPosition()).getTitle();
                    info = mData.get(viewHolder.getAdapterPosition()).getNote();
                    date = mData.get(viewHolder.getAdapterPosition()).getDate();
                    x.setArgs(id, title, info, date);
                    fragmentTransaction.replace(R.id.frame_layout, x);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else
                {
                    if(viewHolder.selected == true)
                    {
                        viewHolder.selected = false;
                        selectedArray.remove(new Integer(viewHolder.getAdapterPosition()));
                        ValueAnimator anim = ValueAnimator.ofArgb(Color.LTGRAY, Color.WHITE);
                        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                viewHolder.item_recycler.setBackgroundColor((Integer)valueAnimator.getAnimatedValue());
                            }
                        });

                        anim.setDuration(200);
                        anim.start();
                        if(selectedArray.isEmpty())
                        {
                            selectionToolbar = false;
                            mMode.finish();
                        }
                    }
                    else
                    {

                        ValueAnimator anim = ValueAnimator.ofArgb(Color.WHITE, Color.LTGRAY);
                        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                viewHolder.item_recycler.setBackgroundColor((Integer)valueAnimator.getAnimatedValue());
                            }
                        });

                        anim.setDuration(200);
                        anim.start();
                        //viewHolder.item_recycler.setBackgroundColor(Color.LTGRAY);
                        viewHolder.selected = true;
                        selectedArray.add(viewHolder.getAdapterPosition());
                    }
                }

            }
        });
        viewHolder.item_recycler.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                MainActivity mainActivity = (MainActivity)mContext;
                mMode = mainActivity.startActionMode(mainActivity.ABCCreator());
                if(selectionToolbar == false) {
                    ValueAnimator anim = ValueAnimator.ofArgb(Color.WHITE, Color.LTGRAY);
                    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            viewHolder.item_recycler.setBackgroundColor((Integer)valueAnimator.getAnimatedValue());
                        }
                    });

                    anim.setDuration(200);
                    anim.start();
                    selectionToolbar = true;
                    viewHolder.selected = true;
                    selectedArray.add(viewHolder.getAdapterPosition());
                }

                return true;
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.title.setText(mData.get(position).getTitle());
        holder.note.setText(mData.get(position).getNote());
        MainActivity mainActivity = (MainActivity)mContext;

        ValueAnimator anim = ValueAnimator.ofArgb(Color.LTGRAY, Color.WHITE);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                holder.item_recycler.setBackgroundColor((Integer)valueAnimator.getAnimatedValue());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    /**/

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView note;
        Boolean selected = false;
        LinearLayout item_recycler;


        public MyViewHolder(View itemView) {
            super(itemView);
            item_recycler = (LinearLayout)itemView.findViewById(R.id.item_recycler);
            title = (TextView)itemView.findViewById(R.id.post_title);
            note = (TextView)itemView.findViewById(R.id.post_info);

        }
        public void ActionModeBackPressed()
        {
            ValueAnimator anim = ValueAnimator.ofArgb(Color.LTGRAY, Color.WHITE);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    item_recycler.setBackgroundColor((Integer)valueAnimator.getAnimatedValue());
                }
            });

            anim.setDuration(200);
            anim.start();
        }

    }
}
;