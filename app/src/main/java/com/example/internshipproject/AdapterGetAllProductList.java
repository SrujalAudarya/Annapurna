package com.example.internshipproject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.internshipproject.common.Urls;

import java.util.List;

public class AdapterGetAllProductList extends BaseAdapter {

    Activity activity;
    List<POJOGetAllProductList> pojoGetAllProductList;

    public AdapterGetAllProductList(Activity activity, List<POJOGetAllProductList> list) {
        this.activity = activity;
        this.pojoGetAllProductList = list;
    }

    @Override
    public int getCount() {
        return pojoGetAllProductList.size();
    }

    @Override
    public Object getItem(int position) {
        return pojoGetAllProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.gv_product_list, null);
            holder.ivDishImage = view.findViewById(R.id.ivAllProductsList);
            holder.tvDishName = view.findViewById(R.id.tvAllProductsListDishName);
            holder.tvDishRating = view.findViewById(R.id.tvAllProductsListDishRating);
            holder.tvDishCategoryName = view.findViewById(R.id.tvAllProductsListCategoryName);
            holder.tvDishCategory = view.findViewById(R.id.tvAllProductsListDishCategory);
            holder.tvDishOffer = view.findViewById(R.id.tvAllProductsListDishOffer);
            holder.tvDishPrice = view.findViewById(R.id.tvAllProductsListDishPrice);
            holder.tvDishDescription = view.findViewById(R.id.tvAllProductsListDishDescription);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final POJOGetAllProductList obj = pojoGetAllProductList.get(position);

        // Set the text for each TextView
        holder.tvDishName.setText(obj.getDishName());
        holder.tvDishOffer.setText(obj.getDishOffer() + " off");
        holder.tvDishRating.setText(obj.getDishRating());
        holder.tvDishCategoryName.setText(obj.getCategoryName());
        holder.tvDishCategory.setText(obj.getDishCategory());
        holder.tvDishPrice.setText(obj.getDishPrice());
        holder.tvDishDescription.setText(obj.getDishDescription());

        // Load image with Glide
        Glide.with(activity)
                .load(Urls.imageUrls + obj.getDishImage())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.notfound) // Optional error image
                .into(holder.ivDishImage);

        return view;
    }

    // ViewHolder class to hold the views for each item
    static class ViewHolder {
        ImageView ivDishImage;
        TextView tvDishName;
        TextView tvDishRating, tvDishCategoryName, tvDishCategory, tvDishOffer, tvDishPrice, tvDishDescription;
    }
}
