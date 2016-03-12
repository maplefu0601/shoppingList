package com.knowroaming.shoppinglist;

/**
 * Created by Daddy on 2016-03-11.
 */

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Raymond Fu
 * @date    2016-03-11
 */
public class ShoppingListAdapter extends ArrayAdapter<productItem> {

    protected static final String LOG_TAG = ShoppingListAdapter.class.getSimpleName();

    private List<productItem> items;
    private int layoutResourceId;
    private Context context;

    public ShoppingListAdapter(Context context, int layoutResourceId, List<productItem> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ProductListHolder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new ProductListHolder();
        holder.item = items.get(position);
        holder.btnRemoveList = (Button)row.findViewById(R.id.btnRemoveList);
        holder.btnRemoveList.setTag(holder.item);
        holder.name = (TextView)row.findViewById(R.id.productNameList);
        holder.price = (TextView)row.findViewById(R.id.priceList);

        row.setTag(holder);

        setupItem(holder);
        return row;
    }

    private void setupItem(ProductListHolder holder) {
        holder.name.setText(holder.item.getProductName());
        holder.price.setText(String.valueOf(holder.item.getPrice()));
    }

    public static class ProductListHolder {
        productItem item;
        TextView name;
        TextView price;
        Button btnRemoveList;
    }


}