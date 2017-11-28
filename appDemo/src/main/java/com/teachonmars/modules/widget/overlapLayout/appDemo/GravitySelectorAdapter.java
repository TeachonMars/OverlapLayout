package com.teachonmars.modules.widget.overlapLayout.appDemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

class GravitySelectorAdapter extends RecyclerView.Adapter<GravityHolder> {
    private final LayoutInflater layoutInflater;
    private ArrayList<Integer>              gravityList             = new ArrayList<>();
    private GravityHolder.SelectionListener gravitySelectedListener = new GravityHolder.SelectionListener() {
        @Override
        public void selected(int viewPos, Integer gravity) {
            gravityList.set(viewPos, gravity);
            updateChild();
        }
    };

    private void updateChild() {
        boolean found = false;
        ArrayList<Integer> gravList = (ArrayList<Integer>) gravityList.clone();
        for (int i = 0; i < gravList.size(); i++) {
            Integer gravity = gravList.get(i);
            if (gravity == Gravity.NO_GRAVITY) {
                if (!found) {
                    found = true;
                } else {
                    gravityList.remove(i);
                }
            }
        }
        if (!found) {
            gravityList.add(Gravity.NO_GRAVITY);
        }
        notifyDataSetChanged();
    }

    public GravitySelectorAdapter(Context context) {
        gravityList.add(Gravity.NO_GRAVITY);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public GravityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GravityHolder(parent, layoutInflater, gravitySelectedListener);
    }

    @Override
    public void onBindViewHolder(GravityHolder holder, int position) {
        holder.setGravity(position, gravityList.get(position));
    }

    @Override
    public int getItemCount() {
        return gravityList.size();
    }

    public int getGravity() {
        int result = 0;
        for (Integer integer : gravityList) {
            result |= integer;
        }
        return result;
    }
}
