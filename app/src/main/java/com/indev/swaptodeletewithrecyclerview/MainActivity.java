package com.indev.swaptodeletewithrecyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView rvSwapToDelete;
    RecyclerViewAdapter mAdapter;
    ArrayList<String> stringArrayList = new ArrayList<>();
    CoordinatorLayout coordinatorLayout;
    GridLayoutManager gridLayoutManager;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean isDarkModeOn=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvSwapToDelete = findViewById(R.id.rvSwapToDelete);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        // Saving state of our app
        // using SharedPreferences
        sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);

        // When user reopens the app
        // after applying dark/light mode
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTitle("Disable Dark Mode");
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setTitle("Enable Dark Mode");
        }

        populateRecyclerView();
        enableSwipeToDeleteAndUndo();
    }

    private void populateRecyclerView() {
        stringArrayList.add("Item 1");
        stringArrayList.add("Item 2");
        stringArrayList.add("Item 3");
        stringArrayList.add("Item 4");
        stringArrayList.add("Item 5");
        stringArrayList.add("Item 6");
        stringArrayList.add("Item 7");
        stringArrayList.add("Item 8");
        stringArrayList.add("Item 9");
        stringArrayList.add("Item 10");
        stringArrayList.add("Item 11");
        stringArrayList.add("Item 12");
        stringArrayList.add("Item 13");
        stringArrayList.add("Item 14");
        stringArrayList.add("Item 15");
        stringArrayList.add("Item 16");
        stringArrayList.add("Item 17");

        /*LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvSwapToDelete.setLayoutManager(mLayoutManager);*/

        /*int spanCount=0;
        for (int i=0;i<stringArrayList.size();i++){
            spanCount= i;
            System.out.println("positions>>>"+stringArrayList.get(i)+" =>>"+i+" >>>"+spanCount%2);
        }
        if(spanCount%2==0) {
            gridLayoutManager = new GridLayoutManager(this, 3);
        }else if(spanCount%2==1){
            gridLayoutManager = new GridLayoutManager(this, 2);
        }*/

        gridLayoutManager = new GridLayoutManager(this, 6);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                // 5 is the sum of items in one repeated section
                switch (position % 5) {
                    // first two items span 3 columns each
                    case 0:
                    case 1:
                        return 3;
                    // next 3 items span 2 columns each
                    case 2:
                    case 3:
                    case 4:
                        return 2;
                }
                throw new IllegalStateException("internal error");
            }
        });
        rvSwapToDelete.setLayoutManager(gridLayoutManager);
        mAdapter = new RecyclerViewAdapter(stringArrayList);
        rvSwapToDelete.setAdapter(mAdapter);
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                final String item = mAdapter.getData().get(position);

                mAdapter.removeItem(position);
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAdapter.restoreItem(item, position);
                        rvSwapToDelete.scrollToPosition(position);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(rvSwapToDelete);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        MenuItem menuItem = menu.findItem(R.id.enable_dark_mode);

        AppCompatToggleButton darkModeSwitch = (AppCompatToggleButton) menuItem.getActionView();
        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                enableDisableDarkMode();
            }
        });

        return true;
    }

    private void enableDisableDarkMode() {
        // When user taps the enable/disable
        // dark mode button
        if (isDarkModeOn) {

            // if dark mode is on it
            // will turn it off
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            // it will set isDarkModeOn
            // boolean to false
            editor.putBoolean("isDarkModeOn", false);
            editor.apply();

            // change text of Button
            setTitle("Enable Dark Mode");
        }
        else {

            // if dark mode is off
            // it will turn it on
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

            // it will set isDarkModeOn
            // boolean to true
            editor.putBoolean("isDarkModeOn", true);
            editor.apply();

            // change text of Button
            setTitle("Disable Dark Mode");
        }
    }
}