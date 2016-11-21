package com.socratesdiaz.personalnotes;

import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by socratesdiaz on 11/21/16.
 */
public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    public static interface OnItemClickListener {
        public void onItemClick(View view, int position);
        public void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mListener;
    private GestureDetector mGestureDetector;

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
