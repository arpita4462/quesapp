package com.atrio.quesapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.atrio.quesapp.Model.ShowData;
import com.atrio.quesapp.QuestionActivity;
import com.atrio.quesapp.R;
import com.atrio.quesapp.SubjectActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Admin on 14-06-2017.
 */

public class RecycleviewAdapter extends RecyclerView.Adapter<RecycleviewAdapter.MyViewHolder>{
    private Context c;
    ArrayList<ShowData> list_data;

    public RecycleviewAdapter(Context context, ArrayList<ShowData> arrayList) {
          this.c = context;
          this.list_data = arrayList;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.tv_text.setText(list_data.get(position).getSub());
        Picasso.with(c)
                .load(list_data.get(position).getImg())
                .placeholder(R.drawable.book)
                .resize(400,400)                        // optional
                .into(holder.img_sub);
        holder.tittle = list_data.get(position).getSub();


    }


    @Override
    public int getItemCount() {
        return list_data.size();
    }
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

      public TextView tv_text;
        public ImageView img_sub;
        public String tittle;

        public MyViewHolder(View itemView) {
            super(itemView);
            img_sub = (ImageView) itemView.findViewById(R.id.sub_pic);
            tv_text = (TextView) itemView.findViewById(R.id.sub_tittle);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), QuestionActivity.class);
            Log.i("tittle44",""+tittle);
            intent.putExtra("Sub",tittle);
            view.getContext().startActivity(intent);

        }
    }
}


