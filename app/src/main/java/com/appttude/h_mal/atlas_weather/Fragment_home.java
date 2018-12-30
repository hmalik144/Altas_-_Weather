package com.appttude.h_mal.atlas_weather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.appttude.h_mal.atlas_weather.RetrieveJSON.UriBuilder;
import static com.appttude.h_mal.atlas_weather.RetrieveJSON.createUrl;
import static com.appttude.h_mal.atlas_weather.getLatLong.latitude;
import static com.appttude.h_mal.atlas_weather.getLatLong.longitude;


public class Fragment_home extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<List<ForecastItem>>{

    protected static String APIkey = "2e914b44f94a4f07853100835181104";
    android.support.v4.app.LoaderManager loaderManager;
    android.support.v4.app.LoaderManager.LoaderCallbacks callbacks;
    public static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private static final int NEWS_LOADER_ID = 1;

    RecyclerView forecastsListView;
    LinearLayout emptyList;
    ProgressBar mainPG;
    Button button;

    private RecyclerViewAdapter mAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

//        try {
//            getLatLong.configLatLong(getContext());
//        }catch (Exception e){
//            System.out.println("error msg: " + e);
//        }

        if (ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }

        forecastsListView = rootView.findViewById(R.id.forecast_listview);
        forecastsListView.setLayoutManager(new LinearLayoutManager(getContext()));

        emptyList = rootView.findViewById(R.id.emptyView);
        mainPG = rootView.findViewById(R.id.mainPB);

        emptyList.setVisibility(View.GONE);
        button = emptyList.findViewById(R.id.emptyViewButton);

        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loaderManager.restartLoader(NEWS_LOADER_ID,null,callbacks);
                mSwipeRefreshLayout.setRefreshing(true);
            }


        });

        loaderManager = getLoaderManager();
        callbacks = this;

//        if (MainActivity.networkInfo != null && MainActivity.networkInfo.isConnected()) {
        loaderManager.initLoader(NEWS_LOADER_ID, null, callbacks);
//        } else {
//            emptyList.setVisibility(View.VISIBLE);
//            mainPG.setVisibility(View.GONE);
//            button.setVisibility(View.GONE);
//        }

        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION){
            if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    getLatLong.configLatLong(getContext());
                }catch (Exception e){
                    System.out.println("error msg: " + e);
                }
                    loaderManager.restartLoader(NEWS_LOADER_ID, null, callbacks);
                    Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_SHORT).show();

            } else {
                    Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
        }

    }

    @Override
    public android.support.v4.content.Loader<List<ForecastItem>> onCreateLoader(int id, Bundle args) {
        mainPG.setVisibility(View.VISIBLE);
        ArrayList<String> entries = new ArrayList<>();
        try {
            getLatLong.configLatLong(getContext());
        }catch (Exception e){
            System.out.println("error msg: " + e);
        }finally {
            if (latitude != null){
                URL url = createUrl(UriBuilder(7));
                entries.add(url.toString());
            }

        }
//            getLatLong.configLatLong(getContext());
////        Toast.makeText(getContext(), "" + getLatLong.latitude.toString(), Toast.LENGTH_SHORT).show();
//        URL url = createUrl(UriBuilder());
//        ArrayList<String> entries = new ArrayList<>();
//        entries.add(url.toString());

        return new ForecastLoader(getContext(),entries);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<List<ForecastItem>> loader, List<ForecastItem> data) {

        if (mSwipeRefreshLayout.isRefreshing()){
            mSwipeRefreshLayout.setRefreshing(false);
        }

        if (ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            emptyList.setVisibility(View.VISIBLE);
            TextView tv = emptyList.findViewById(R.id.emptyViewText);
            tv.setText("Location Required");

            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    grantPermission();
                }
            });

        } else {
            if (data == null){
                forecastsListView.setVisibility(View.GONE);
                emptyList.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Could not retrieve data", Toast.LENGTH_SHORT).show();
            }else{
                mAdapter = new RecyclerViewAdapter(getContext(),data.get(0),latitude,longitude);
                mAdapter.notifyDataSetChanged();
                forecastsListView.setAdapter(mAdapter);
                forecastsListView.setVisibility(View.VISIBLE);
                emptyList.setVisibility(View.GONE);
                button.setVisibility(View.GONE);
            }

        }

        mainPG.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<ForecastItem>> loader) {
        loader.reset();
    }

    public void grantPermission(){
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        loaderManager.restartLoader(NEWS_LOADER_ID, null, callbacks);
    }

}

