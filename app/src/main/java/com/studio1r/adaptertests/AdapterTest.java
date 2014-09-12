package com.studio1r.adaptertests;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import junit.framework.Test;


public class AdapterTest extends Activity implements View.OnClickListener {


    private ListView mListView;
    private TestAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adapter_test);
        mListView = (ListView) findViewById(android.R.id.list);
//        mListView.setEmptyView(findViewById(R.id.txt_empty));
        mAdapter = new TestAdapter();
        mListView.setAdapter(mAdapter);
        findViewById(R.id.btn_set_filled).setOnClickListener(this);
        findViewById(R.id.btn_set_empty).setOnClickListener(this);
        findViewById(R.id.btn_set_loading).setOnClickListener(this);
//
//        this.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAdapter.setData(new String[]{"one", "two", "three"});
//            }
//        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.adapter_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_set_empty:
                mAdapter.setData(new String[]{});
                break;
            case R.id.btn_set_filled:
                mAdapter.setData(new String[]{"one", "two", "three"});
                break;
            case R.id.btn_set_loading:
                mAdapter.showLoadSpinner();
                break;
        }
    }

    private class TestAdapter extends BaseAdapter {

        private String[] strings = new String[]{};
        private static final int TYPE_EMPTY = 0;
        private static final int TYPE_LOADING = 1;
        private static final int TYPE_NORMAL = 2;
        private static final int TYPE_COUNT = 3;
        private boolean showLoader = false;


        public TestAdapter() {
            showLoader = true;
        }


        public void showLoadSpinner() {
            strings = new String[]{};
            showLoader = true;
            notifyDataSetChanged();
        }

        public void hideLoadSpinner() {
            showLoader = false;
            notifyDataSetChanged();
        }

        @Override
        public boolean isEmpty() {
            Log.d("NN", "isEmpty " + getCount());
            return getCount() == 0;
        }

        @Override
        public int getCount() {
            if (strings.length == 0) {
                return 1;
            } else {
                return strings.length;
            }
        }

        @Override
        public String getItem(int position) {
            return strings[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            int type = TYPE_NORMAL;
            //if the data is actually empty return empty type
            if (strings.length == 0) {
                type = TYPE_EMPTY;
            }
            if (showLoader) {
                type = TYPE_LOADING;
            }
            return type;
        }

        @Override
        public int getViewTypeCount() {
            return TYPE_COUNT;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int type = getItemViewType(position);
            if (convertView == null) {
                switch (type) {
                    case TYPE_EMPTY:
                        convertView = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.empty_text, parent, false);
                        break;
                    case TYPE_LOADING:
                        convertView = new ProgressBar(parent.getContext());
                        ProgressBar pb = (ProgressBar) convertView;
                        pb.setIndeterminate(true);
                        break;
                    case TYPE_NORMAL:
                        convertView = new TextView(parent.getContext());
                        break;
                }


            }
            if (type == TYPE_NORMAL) {
                ((TextView) convertView).setText(getItem(position));
            }
            return convertView;
        }

        public void setData(String[] strings) {
            Log.d("NN", strings.toString());
            hideLoadSpinner();
            this.strings = strings;
            notifyDataSetChanged();
        }
    }
}
