package com.sag.atwork;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter extends BaseAdapter implements Filterable {
    Context c;
    private ArrayList<worker> mOriginalValues; // Original Values
    private ArrayList<worker> mDisplayedValues;    // Values to be displayed
    LayoutInflater inflater;


    public Adapter(Context context,ArrayList<worker> mProductArrayList){
        this.mOriginalValues = mProductArrayList;
        this.mDisplayedValues = mProductArrayList;
        this.c=context;
        inflater = LayoutInflater.from(context);

    }
    @Override
    public int getCount() {
        return mDisplayedValues.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class ViewHolder {
        RelativeLayout llContainer;
        TextView name,location,work,starRating;
        ImageView profile;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView= LayoutInflater.from(c).inflate(R.layout.item_search,parent,false);
            holder.llContainer=convertView.findViewById(R.id.relative);
            holder.name=convertView.findViewById(R.id.name);
            holder.location=convertView.findViewById(R.id.location);
            holder.work=convertView.findViewById(R.id.work);
            holder.profile=convertView.findViewById(R.id.profile);
            holder.starRating=convertView.findViewById(R.id.starRating);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(mDisplayedValues.get(position).name);
        holder.location.setText(mDisplayedValues.get(position).location);
        holder.work.setText(mDisplayedValues.get(position).work);
        holder.starRating.setText(mDisplayedValues.get(position).starRating+"/ 5");

        Picasso.get()
                .load(Urls.ip+mDisplayedValues.get(position).profile)
                .placeholder(R.drawable.ic_profile_icon)
                .error(R.drawable.ic_profile_icon)
                .into(holder.profile);

       holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(c,worker_info.class);
                in.putExtra("id",mDisplayedValues.get(position).wid);
                c.startActivity(in);
            }
        });
        return convertView;
    }


    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                mDisplayedValues = (ArrayList<worker>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<worker> FilteredArrList = new ArrayList<worker>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<worker>(mDisplayedValues); // saves the original data in mOriginalValues
                }

                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String data = mOriginalValues.get(i).location;
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(new worker(mOriginalValues.get(i).name,mOriginalValues.get(i).location,mOriginalValues.get(i).work,mOriginalValues.get(i).profile,mOriginalValues.get(i).wid,mOriginalValues.get(i).starRating));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}
