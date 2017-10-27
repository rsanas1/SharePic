package com.rishi.sharepic;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rishi on 10/26/2017.
 */

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {

    private List<String> mData = Collections.emptyList();
    //private LayoutInflater mInflater;
    RecyclerOnItemClickListener mItemClickListener;

    // data is passed into the constructor
    public UserRecyclerViewAdapter( List<String> data, RecyclerOnItemClickListener recyclerOnItemClickListener) {

        mItemClickListener = recyclerOnItemClickListener;
        this.mData = data;
    }


    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row,null);
        //View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String user = mData.get(position);
        holder.usernameTv.setText(user);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.usernameTv) TextView usernameTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.e("TAG","OnClick");

            if (mItemClickListener != null)
            {
                Log.e("TAG","mItemClickListener != null");
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
            else{

                Log.e("TAG","mItemClickListener == null");
            }
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return mData.get(id);
    }


}
