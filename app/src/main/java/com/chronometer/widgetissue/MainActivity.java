package com.chronometer.widgetissue;

import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.timers_list);
        listView.setAdapter(new CustomTimerAdapter(getApplicationContext(), R.layout.widget_timer));

        findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper.getInstance(MainActivity.this).createActivity(null);
                loadData();
            }
        });

        loadData();
    }

    public void loadData() {
        CustomTimerAdapter adapter = (CustomTimerAdapter)listView.getAdapter();
        adapter.clear();
        adapter.addAll(DBHelper.getInstance(this).findActivities());
        ((CustomTimerAdapter)listView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        Long id = (Long)view.getTag();
        DBHelper.getInstance(this).stopActivity(id);
        loadData();
    }

    private class CustomTimerAdapter extends ArrayAdapter<CustomTimer> {

        CustomTimerAdapter(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {

            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.widget_timer, parent, false);
            }

            CustomTimer customTimer = getItem(position);

            final Chronometer chronometer = view.findViewById(R.id.widget_timer);
            chronometer.setBase(SystemClock.elapsedRealtime() - System.currentTimeMillis() + customTimer.getStart());

            chronometer.start();

            Button button = view.findViewById(R.id.widget_stop);
            button.setTag(customTimer.getId());

            button.setOnClickListener(MainActivity.this);

            return view;
        }
    }
}
