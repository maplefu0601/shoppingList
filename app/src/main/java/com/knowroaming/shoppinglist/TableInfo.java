package com.knowroaming.shoppinglist;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Daddy on 2016-03-11.
 */
public class TableInfo {

    private static TableInfo instance = new TableInfo();

    public static TableInfo getInstance() {
        return instance;
    }

    private JSONObject tableObj = init();

    private JSONObject init() {

        JSONObject tableObj = new JSONObject();
        try {
            JSONObject product = new JSONObject();
            product.put("TableName", "product");
            product.put("CreateDatabase", "create table if not exists product(id integer not null primary key autoincrement,userid text, name text, amount integer, price double, description text, addedDate datetime, status integer);");
            product.put("UpdateDatabase", "drop table if exists product");
            JSONObject columns = new JSONObject();
            columns.put("id", "id");
            columns.put("userid", "userid");
            columns.put("name", "name");
            columns.put("amount", "amount");
            columns.put("price", "price");
            columns.put("description", "description");
            product.put("Columns", columns);

            tableObj.put("product", product);

            JSONObject user = new JSONObject();
            user.put("TableName", "user");
            user.put("CreateDatabase", "create table if not exists user(id integer not null primary key autoincrement, USERNAME text, PASSWORD text, createdDate datetime, status integer);");
            user.put("UpdateDatabase", "drop table if exists user");
            JSONObject columnsUser = new JSONObject();
            columnsUser.put("id", "id");
            columnsUser.put("USERNAME", "USERNAME");
            columnsUser.put("PASSWORD", "PASSWORD");
            columnsUser.put("createdDate", "createdDate");
            columnsUser.put("status", "status");
            user.put("Columns", columnsUser);

            tableObj.put("user", user);


            JSONObject cart = new JSONObject();
            cart.put("TableName", "cart");
            cart.put("CreateDatabase", "create table if not exists cart(id integer not null primary key autoincrement, userid integer, productid integer, createdDate datetime, status integer);");
            cart.put("UpdateDatabase", "drop table if exists cart");
            JSONObject columnsCart = new JSONObject();
            columnsCart.put("id", "id");
            columnsCart.put("userid", "userid");
            columnsCart.put("productid", "productid");
            columnsCart.put("createdDate", "createdDate");
            columnsCart.put("status", "status");
            cart.put("Columns", columnsCart);

            tableObj.put("cart", cart);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tableObj;
    }

    public JSONObject getTableInfo(String name) {
        try {
            JSONObject obj = this.tableObj.getJSONObject(name);

            return obj;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}


