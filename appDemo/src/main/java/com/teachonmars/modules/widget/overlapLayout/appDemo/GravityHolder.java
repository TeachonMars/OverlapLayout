package com.teachonmars.modules.widget.overlapLayout.appDemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

class GravityHolder extends RecyclerView.ViewHolder {
    private int viewPos;

    public GravityHolder(ViewGroup parent, LayoutInflater layoutInflater, SelectionListener selectionListener) {
        super(layoutInflater.inflate(R.layout.item_gravity, parent, false));
        ((Spinner) itemView).setAdapter(new ArrayAdapter<>(parent.getContext(), android.R.layout.simple_spinner_item, GravityHelper.getSringList()));
        ((Spinner) itemView).setOnItemSelectedListener(buildListener(selectionListener));
    }

    private AdapterView.OnItemSelectedListener buildListener(final SelectionListener selectionListener) {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectionListener.selected(viewPos, GravityHelper.getValueList().get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    public void setGravity(int position, Integer gravity) {
        viewPos = position;
        ((Spinner) itemView).setSelection(GravityHelper.positionOf(gravity));
    }

    public interface SelectionListener {
        void selected(int viewPos, Integer gravity);
    }
}
