package com.example.internshipproject;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.internshipproject.common.Urls;

import java.util.List;

public class AdapterGetAllCategoryDetails extends BaseAdapter {
    // Adapter = multiple view load show
    // AdapterGetAllCategoryDetails show multiple view collect show listview

    Activity activity;
    List<POJOGetAllCategoryDetails> pojoGetAllCategoryDetails;

    public AdapterGetAllCategoryDetails(Activity activity, List<POJOGetAllCategoryDetails> list) {
        this.activity = activity;
        this.pojoGetAllCategoryDetails = list;
    }

    @Override
    public int getCount() {
        return pojoGetAllCategoryDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return pojoGetAllCategoryDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.lv_get_all_category, null);
            holder.ivCategoryImage = view.findViewById(R.id.ivCategoryImage);
            holder.tvCategoryName = view.findViewById(R.id.tvCategoryName);
            holder.cvCategorylist = view.findViewById(R.id.cvCategory);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final POJOGetAllCategoryDetails obj = pojoGetAllCategoryDetails.get(position);
        holder.tvCategoryName.setText(obj.getCategoryName());

        Glide.with(activity)
                .load(Urls.imageUrls + obj.getCategoryImage())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.notfound)               // Optional error image
                .into(holder.ivCategoryImage);

        holder.cvCategorylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ProductListActivity.class);
                intent.putExtra("CategoryName", obj.getCategoryName());
                activity.startActivity(intent);
            }
        });

        return view;
    }

    class ViewHolder {
        CardView cvCategorylist;
        ImageView ivCategoryImage;
        TextView tvCategoryName;
    }
    // baseAdapter = multiple view load show
    // AdapterGetAllCategoryDetails show multiple view collect show listview

}
