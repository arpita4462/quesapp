package com.atrio.quesapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.atrio.quesapp.R;
import com.atrio.quesapp.model.CustomSpinner;

import java.util.ArrayList;

/**
 * Created by Admin on 12-07-2017.
 */

public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<CustomSpinner> arrayList;
    LayoutInflater inflter;

    public CustomAdapter(Context context, ArrayList<CustomSpinner> data) {
        super();
        this.context = context;
        arrayList = data;

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


  /*  @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        ((TextView) convertView).setText(arrayList.get(position).getSub());
        return convertView;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) View.inflate(context, android.R.layout.simple_list_item_1, null);
        textView.setText(arrayList.get(position).getSub());
        return textView;
    }
*/

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi=view;
        ViewHolder holder;
        if(view==null) {

            inflter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflter.inflate(R.layout.custom_spinner, null);
            holder = new ViewHolder();
            holder.tv_tittle = (TextView) vi.findViewById(R.id.tv_subName);

            vi.setTag(holder);
        }else{
            holder = (ViewHolder) vi.getTag();

            Log.i("data775",arrayList.get(i).getSub());
            holder.tv_tittle.setText(arrayList.get(i).getSub());

        }

        return vi;
    }

    public class ViewHolder {

        TextView tv_tittle;

    }
}