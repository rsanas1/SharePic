package com.rishi.sharepic;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rishi on 10/26/2017.
 */

class FeedRecyclerViewAdapter extends RecyclerView.Adapter<FeedRecyclerViewAdapter.ViewHolder>{

    private List<Bitmap> mData = Collections.emptyList();
    RecyclerOnItemClickListener mItemClickListener;

    public FeedRecyclerViewAdapter(List<Bitmap> data, RecyclerOnItemClickListener recyclerOnItemClickListener) {

        mItemClickListener = recyclerOnItemClickListener;
        this.mData = data;
    }

    @Override
    public FeedRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_recyclerview_row, parent, false);
        FeedRecyclerViewAdapter.ViewHolder viewHolder = new FeedRecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FeedRecyclerViewAdapter.ViewHolder holder, int position) {
            Bitmap bitmap = mData.get(position);
            holder.imageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.imageView)
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null)
            {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
