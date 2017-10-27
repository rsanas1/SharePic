package com.rishi.sharepic;

import android.view.View;



public interface RecyclerOnItemClickListener {

    /**
     * Called when an item is clicked.
     *
     * @param childView View of the item that was clicked.
     * @param position  Position of the item that was clicked.
     */
    public void onItemClick(View childView, int position);

}
