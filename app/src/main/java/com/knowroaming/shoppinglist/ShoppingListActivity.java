package com.knowroaming.shoppinglist;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLDataException;
import java.util.ArrayList;

/**
 * @author Raymond Fu
 * @date    2016-03-11
 */
public class ShoppingListActivity extends AppCompatActivity implements View.OnClickListener{

    String userName;
    ShoppingFragment shoppingFragment;
    ShoppingListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button btnClose = (Button)findViewById(R.id.btnClose);
        btnClose.setOnClickListener(this);
        Button btnShopping = (Button)findViewById(R.id.btnShopping);
        btnShopping.setOnClickListener(this);

        setupListViewAdapter();


    }

    public void refreshAdapter() {
        Log.d("TEST", "refresh adapter");
        adapter.notifyDataSetChanged();
        showList();
    }

    public void addToAdapter(productItem item) {
        adapter.add(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        showList();
    }

    private void showList() {
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        TextView textUserId = (TextView)findViewById(R.id.userId);
        textUserId.setText(userName);

        JSONArray dataList = getList(userName);
        Log.d("TEST", dataList.toString());

        for(int i=0;i<dataList.length();++i) {
            try {
                JSONObject obj = dataList.getJSONObject(i);
                Integer id = obj.getInt("id");
                String name = obj.getString("name");
                Double price = obj.getDouble("price");

                productItem item = new productItem(id, name, price);
                adapter.add(item);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public String getUserName() { return userName; }

    private void setupListViewAdapter() {
        adapter = new ShoppingListAdapter(this, R.layout.shopping_list_item, new ArrayList<productItem>());
        ListView listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(adapter);
    }

    public void removeListClickHandler(View v) {
        productItem itemToRemove = (productItem)v.getTag();
        adapter.remove(itemToRemove);

        removeList(itemToRemove.getId());
    }

    public void removeList(Integer productId) {

        String dbName = "login.db";
        JSONObject tableInfo = TableInfo.getInstance().getTableInfo("product");

        String sql = String.format("delete from product where id=%d;", productId);
        Log.d("TEST", sql);
        HJDatasource hjdb = new HJDatasource(this, dbName, tableInfo);
        if(hjdb != null) {
            try {
                hjdb.open();

                hjdb.executeSql(sql);


            } catch (SQLDataException e) {
                e.printStackTrace();
            }
        }


    }

    public JSONArray getList(String userName) {

        String dbName = "login.db";
        JSONObject tableInfo = TableInfo.getInstance().getTableInfo("product");

        String sql = String.format("select id, name, amount, price, description from product where userid='%s';", userName);
        Log.d("TEST", sql);
        HJDatasource hjdb = new HJDatasource(this, dbName, tableInfo);
        if(hjdb != null) {
            try {
                hjdb.open();

                JSONArray data = hjdb.query(sql);

                return data;


            } catch (SQLDataException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.btnClose:
                Log.d("TEST", "close");
                finish();
                break;

            case R.id.btnShopping:
                Log.d("TEST", "go shopping");
                Bundle bundle = new Bundle();
                bundle.putString("userName", userName);

                ShoppingFragment shoppingFragment = new ShoppingFragment();
                shoppingFragment.setArguments(bundle);

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fm.findFragmentByTag("shoppingDialog");
                if(fragment != null) {
                    ft.remove(fragment);
                }
                shoppingFragment.show(ft, "shoppingDialog");


        }
    }
}
