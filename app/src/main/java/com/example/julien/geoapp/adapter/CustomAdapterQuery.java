package com.example.julien.geoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.julien.geoapp.R;
import com.example.julien.geoapp.models.Doors;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapterQuery extends ArrayAdapter<Doors> {
    private LayoutInflater layoutInflater;
    private List<Doors> mDoors;

    private Filter mFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            return ((Doors) resultValue).getTitle();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null) {
                ArrayList<Doors> suggestions = new ArrayList<Doors>();
                for (Doors Doors : mDoors) {
                    if (Doors.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(Doors);
                    }
                }

                results.values = suggestions;
                results.count = suggestions.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            if (results != null && results.count > 0) {
                addAll((ArrayList<Doors>) results.values);
            } else {
                addAll(mDoors);
            }
            notifyDataSetChanged();
        }
    };

    public CustomAdapterQuery(Context context, int textViewResourceId, List<Doors> Doorss) {
        super(context, textViewResourceId, Doorss);
        mDoors = new ArrayList<Doors>(Doorss.size());
        mDoors.addAll(Doorss);
        layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.spinner_item_test, null);
        }

        Doors Doors = getItem(position);

        TextView name = (TextView) view.findViewById(R.id.textView1);
        TextView description = (TextView) view.findViewById(R.id.textView2);
        name.setText(Doors.getTitle());
        description.setText(Doors.getTeacher());
        return view;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }
}