package com.example.internshipproject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.internshipproject.common.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ProductListActivity extends AppCompatActivity {

    SearchView svProductListSearch;
    ListView lvProductList;
    TextView tvProductListNoCategory;
    String categoryName;

    List<POJOGetAllProductList> pojoGetAllProductList;

    AdapterGetAllProductList adapterGetAllProductList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        categoryName = getIntent().getStringExtra("CategoryName");
        pojoGetAllProductList = new ArrayList<>();
        getSupportActionBar().setTitle(categoryName);
        // Inside your activity
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF5722")));


        svProductListSearch = findViewById(R.id.svProductListSearch);
        lvProductList = findViewById(R.id.gvProductList);
        tvProductListNoCategory = findViewById(R.id.tvProductListNoCategory);

        svProductListSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchDish(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchDish(query);
                return false;
            }
        });

        getProductWiseDishes();
    }

    private void searchDish(String query) {
        List<POJOGetAllProductList> searchList = new ArrayList<>();
        searchList.clear();
        for (POJOGetAllProductList obj : pojoGetAllProductList) {
            if (obj.getDishName().toLowerCase().contains(query.toLowerCase()) || obj.getCategoryName().toLowerCase().contains(query.toLowerCase())) {
                searchList.add(obj);
            }
            adapterGetAllProductList = new AdapterGetAllProductList(ProductListActivity.this, searchList);
            lvProductList.setAdapter(adapterGetAllProductList);
        }
    }

    private void getProductWiseDishes() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("categoryName", categoryName);

        client.post(Urls.getCategoryWiseDishWebService, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray jsonArray = response.getJSONArray("categoryWiseDish");
                    if (jsonArray.isNull(0)) {
                        tvProductListNoCategory.setVisibility(View.VISIBLE);
                        lvProductList.setVisibility(View.GONE);
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String categoryName = jsonObject.getString("categoryname");
                            String dishCategory = jsonObject.getString("dishcategory");
                            String dishImage = jsonObject.getString("dishimage");
                            String dishName = jsonObject.getString("dishname");
                            String dishPrice = jsonObject.getString("dishprice");
                            String dishRating = jsonObject.getString("dishrating");
                            String dishOffer = jsonObject.getString("dishoffer");
                            String dishDescription = jsonObject.getString("dishdescription");

                            pojoGetAllProductList.add(new POJOGetAllProductList(id, categoryName, dishCategory, dishImage,
                                    dishName, dishPrice, dishRating, dishOffer, dishDescription));
                            adapterGetAllProductList = new AdapterGetAllProductList(ProductListActivity.this, pojoGetAllProductList);
                            lvProductList.setAdapter(adapterGetAllProductList);
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getApplicationContext(), "Sever Error...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}