package com.example.internshipproject;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class MenuFragment extends Fragment {

    GridView gvAllCategory;
    TextView tvNoCategory;
    SearchView searchCategory;

    List<POJOGetAllCategoryDetails> pojoGetAllCategoryDetails;
    AdapterGetAllCategoryDetails adapterGetAllCategoryDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        pojoGetAllCategoryDetails = new ArrayList<>();

        gvAllCategory = view.findViewById(R.id.gv_menuFragmentMultipleCategory);
        tvNoCategory = view.findViewById(R.id.tv_menuFragmentNoCategory);
        searchCategory = view.findViewById(R.id.sv_menuFragmentSearch);

        searchCategory.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchCategory(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchCategory(query);
                return false;
            }
        });

        getAllCategory();

        return view;
    }

    private void searchCategory(String query) {
        List<POJOGetAllCategoryDetails> searchList = new ArrayList<>();
        searchList.clear();
        for (POJOGetAllCategoryDetails obj : pojoGetAllCategoryDetails) {
            if (obj.getCategoryName().toLowerCase().contains(query.toLowerCase())) {
                searchList.add(obj);
            }

            adapterGetAllCategoryDetails = new AdapterGetAllCategoryDetails(getActivity(), searchList);
            gvAllCategory.setAdapter(adapterGetAllCategoryDetails);
        }
    }

    private void getAllCategory() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.post(Urls.getAllCategoryWebService, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray jsonArray = response.getJSONArray("getAllCategory");
                    if (jsonArray.isNull(0)) {
                        tvNoCategory.setVisibility(View.VISIBLE);
                        gvAllCategory.setVisibility(View.GONE);
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String categoryImage = jsonObject.getString("categoryImage");
                            String categoryName = jsonObject.getString("categoryName");

                            pojoGetAllCategoryDetails.add(new POJOGetAllCategoryDetails(id, categoryImage, categoryName));
                            adapterGetAllCategoryDetails = new AdapterGetAllCategoryDetails(getActivity(), pojoGetAllCategoryDetails);
                            gvAllCategory.setAdapter(adapterGetAllCategoryDetails);
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);


                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getActivity(), "Sever Error...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}