package com.store.aurora;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class DatabaseHelper extends SQLiteOpenHelper {


    private int result;


    private boolean checkNet = false;
    private String status;
    private Session session;
    private String[] detail = new String[10];
    private String partSku;
    private int jsonError;
    private Item item;
    private int dbError = 0;
    private String auth_key;
    private boolean login=false;

    private String dev_note_key;

//=================================================================================================

    // get settes


    public String getDev_note_key() {
        return dev_note_key;
    }

    public void setDev_note_key(String dev_note_key) {
        this.dev_note_key = dev_note_key;
    }
    public boolean isCheckNet() {
        return checkNet;
    }

    public void setCheckNet(boolean checkNet) {
        this.checkNet = checkNet;
    }

    public String getAuth_key() {
        return auth_key;
    }

    public void setAuth_key(String auth_key) {
        this.auth_key = auth_key;
    }
    public int getDbError() {
        return dbError;
    }

    public void setDbError(int dbError) {
        this.dbError = dbError;
    }


    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }


    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getJsonError() {
        return jsonError;
    }

    public void setJsonError(int jsonError) {
        this.jsonError = jsonError;
    }


    public String getPartSku() {

        System.out.println("The Sku Get " + this.partSku);
        return this.partSku;


    }

    public void setPartSku(String partSku) {
        System.out.println("The Sku set " + partSku);
        this.partSku = partSku;


    }

    public String[] getDetail() {
        return detail;
    }

    public void setDetail(String detail) {

        this.detail[0] = detail;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public DatabaseHelper(Context context) {
        super(context, AuroraContract.DATABASE_NAME, null, 1);
        session = new Session(context);
    }

//===============================================================================================

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + AuroraContract.USERS_TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, account_code TEXT, account_name TEXT, locale TEXT, worker_user_id TEXT,worker_alias TEXT, worker_type TEXT,auth_key TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + AuroraContract.ITC_TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,DeliveryNoteCustomerKey TEXT, DeliveryNoteCustomerName TEXT, DeliveryNoteCustomerDate TEXT, DeliveryNoteEstimatedWeight TEXT, DeliveryNoteID TEXT,DeliveryNoteKey TEXT, DeliveryNoteNumberOrderedParts TEXT, DeliveryNoteStoreKey TEXT, DeliveryNoteType TEXT, StoreCode TEXT, StoreName TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + AuroraContract.STAFF_TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,StaffKey TEXT, StaffAlias TEXT, StaffName TEXT, StaffID TEXT, StaffMainImage TEXT,StaffRoles TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + AuroraContract.ITM_TABLE_NAME + " ( ID   INTEGER PRIMARY KEY AUTOINCREMENT,delivery_note_key TEXT, inventory_transaction_key TEXT,part_sku   TEXT,pkg_des TEXT,part_package_weight    TEXT,to_pick  TEXT,picked   TEXT,out_of_stock    TEXT,waiting   TEXT,location_key   TEXT,part_reference   TEXT,part_sko_barcode  TEXT,status INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + AuroraContract.APP_TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,net_stat INTEGER,db_stat TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + AuroraContract.USERS_TABLE_NAME);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS " + AuroraContract.ITC_TABLE_NAME);

        db.execSQL("DROP TABLE IF EXISTS " + AuroraContract.STAFF_TABLE_NAME);

        db.execSQL("DROP TABLE IF EXISTS " + AuroraContract.ITM_TABLE_NAME);

        db.execSQL("DROP TABLE IF EXISTS " + AuroraContract.APP_TABLE_NAME);

        onCreate(db);
    }

//=============================Function Logout Check================================================

    public Boolean logOutCheck() {

        SQLiteDatabase db = this.getWritableDatabase();
        String sqlLogcount = "SELECT * FROM " + AuroraContract.USERS_TABLE_NAME;
        Cursor res = db.rawQuery(sqlLogcount, null);
        if (res.getCount() > 0) {
            user_validate();
            return false;
        } else {
            setNetStat(0);
            return true;

        }
    }
//==================================================================================================

//=================================  registerUser ==================================================
    //Function User Register

    public Boolean registerUser(String url) {



        final String userKey = url;
        url = url + "&action=initialize";

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        System.out.println(url);
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String jsona = new String(responseBody);

                System.out.println(jsona);

                JSONObject jsonObj = null;
                JSONObject jsonObjC = null;

                try {
                    jsonObj = new JSONObject(jsona);
                    String state = jsonObj.getString("state");
                    if (state.contains("Error")) {
                        clearTable();
                        String errorCode = jsonObj.getString("msg");
                        setStatus(errorCode);
                    } else if(state.contains("OK")) {
                        String data = jsonObj.getString("data");
                        jsonObjC = new JSONObject(data);
                        String account_code = jsonObjC.getString("account_code");
                        String account_name = jsonObjC.getString("account_name");
                        String locale = jsonObjC.getString("locale");
                        String worker_user_id = jsonObjC.getString("worker_user_id");
                        String worker_alias = jsonObjC.getString("worker_alias");
                        String worker_type = jsonObjC.getString("worker_type");

                        ArrayList<String> querary = new ArrayList<String>();

                        querary.add(account_code);
                        querary.add(account_name);
                        querary.add(locale);
                        querary.add(worker_user_id);
                        querary.add(worker_alias);
                        querary.add(worker_type);
                        querary.add(userKey);
                        clearTable();
                        insertUser(querary);
                        setCheckNet(true);
                        setNetStat(2);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    setCheckNet(false);
                    setJsonError(1);
                    setNetStat(0);

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                setStatus("No Network");
                setCheckNet(false);
                String json = new String(responseBody);
                System.out.println(json);
                setJsonError(1);
                setNetStat(0);


            }

        });

        setCheckNet(true);

       // stat=getNetStat();
        return isCheckNet();

    }

    //==============================  Clear Table   ================================================

    public void clearTable() {
        SQLiteDatabase database = this.getWritableDatabase();
        if(database.isOpen()){
            database.execSQL("DELETE FROM users_table");
        }

       database.close();
    }


    //================================= Insert User ================================================

    public void insertUser(ArrayList<String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AuroraContract.USERS_COL_2, queryValues.get(0));
        values.put(AuroraContract.USERS_COL_3, queryValues.get(1));
        values.put(AuroraContract.USERS_COL_4, queryValues.get(2));
        values.put(AuroraContract.USERS_COL_5, queryValues.get(3));
        values.put(AuroraContract.USERS_COL_6, queryValues.get(4));
        values.put(AuroraContract.USERS_COL_7, queryValues.get(5));
        values.put(AuroraContract.USERS_COL_8, queryValues.get(6));

        try {

            database.insert(AuroraContract.USERS_TABLE_NAME, null, values);
            result = 1;

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.toString());
            result = 0;
        }
        System.out.println("Database Check =" + database.isOpen());
        database.close();
    }


    //==============================================================================================
    //================================= Insert App Status ================================================

    public void insertApp(ArrayList<String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AuroraContract.APP_COL_1, queryValues.get(0));
        values.put(AuroraContract.APP_COL_2, queryValues.get(1));

        try {

            database.insert(AuroraContract.APP_TABLE_NAME, null, values);
            result = 1;

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.toString());
            result = 0;
        }
        System.out.println("Database Check =" + database.isOpen());
        database.close();
    }


    //==============================================================================================

    //Get Account Information

    public String[] getUserData() {

        String[] datasetOfuser = new String[3];
        SQLiteDatabase database = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM users_table ORDER BY ROWID ASC LIMIT 1";


        Cursor res = database.rawQuery(selectQuery, new String[]{});
        if (res != null) {
            res.moveToFirst();

            datasetOfuser[0] = res.getString(5);//res.getColumnName("worker_alias");
            datasetOfuser[1] = res.getString(6);
            datasetOfuser[2] = res.getString(7);// Return user auth Key
        }

        return datasetOfuser;
    }

    //==============================================================================================

    //Delete account table
    public Boolean clearAccount() {

        SQLiteDatabase db = this.getWritableDatabase();


        if (db.delete(AuroraContract.USERS_TABLE_NAME, "1", null) > 0) {
            return true;
        } else {
            return false;
        }


    }


    //==============================================================================================


    // Get Delivery Note  Information by delivery note key
    public String[] getDeliliveryNoteDataBykey(String key) {


        SQLiteDatabase database = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM itc_table where DeliveryNoteKey='"+key+"'";


        Cursor res = database.rawQuery(selectQuery, new String[]{});

        String[] itemArrayy= new String[10];

        if (res != null) {
            res.moveToFirst();

                itemArrayy[0] = res.getString(5);
                itemArrayy[1] = res.getString(4);
                itemArrayy[2] = res.getString(10);
                itemArrayy[3] = res.getString(9);
            String[] separated = res.getString(3).split(" ");
                itemArrayy[4] = separated[0];
            itemArrayy[5] = res.getString(7);
            itemArrayy[6] = res.getString(2);
        }


        return itemArrayy;

    }


    //==============================================================================================

    // Get Delivery Note  Information
    public String[][] getPickItemData() {


        SQLiteDatabase database = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM itc_table";


        Cursor res = database.rawQuery(selectQuery, new String[]{});

        String[][] itemArrayy = new String[5][res.getCount()];

        if (res != null) {
            res.moveToFirst();
            for (int i = 0; i < res.getCount(); i++) {
                itemArrayy[0][i] = res.getString(5);
                itemArrayy[1][i] = res.getString(4);
                itemArrayy[2][i] = res.getString(7);
                itemArrayy[3][i] = res.getString(3);
                itemArrayy[4][i] = res.getString(6);
                res.moveToNext();
            }


        }


        return itemArrayy;

    }


    //==============================================================================================


    //=================Get worker List from Json ===============================================
    // Action = get_staff

    public int getWorkerList(String url) {

        result=0;
        String keyAction = "&action=get_staff";
        url = url + keyAction;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        System.out.println(url);

        client.post(url, params, new AsyncHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                ArrayList<String> itemList = new ArrayList<String>();


                String jsona = new String(responseBody);

                System.out.println(jsona);

                JSONObject jsonObj = null;


                try {
                    jsonObj = new JSONObject(jsona);

                    String state = jsonObj.getString("state");// future use validation
                    String data = jsonObj.getString("data");


                    JSONArray items = jsonObj.getJSONArray("data");
                    clearStaff();

                    // looping through All items
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject c = items.getJSONObject(i);

                        // details fetch from object

                        String staff_key = c.getString("Staff Key");
                        String staff_Alias = c.getString("Staff Alias");
                        String staff_name = c.getString("Staff Name");
                        String staff_id = c.getString("Staff ID");
                        String staff_img_key = c.getString("Staff Main Image Key");
                        String staff_roles = c.getString("Staff Roles");

                        itemList.add(staff_key);
                        itemList.add(staff_Alias);
                        itemList.add(staff_name);
                        itemList.add(staff_id);
                        itemList.add(staff_img_key);
                        itemList.add(staff_roles);

                        System.out.println(itemList);
                        insertWorkerList(itemList);

                        itemList.clear();

                        result=1;

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    result=0;
                    setNetStat(0);
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                String json = new String(responseBody);
                System.out.println(json );
                result=0;
                setNetStat(0);

            }

        });

    return  result;
    }


    //==========================================================================================

    //=============================================================================================

    //Clear Staff

    public Boolean clearStaff() {

        SQLiteDatabase database = this.getWritableDatabase();
        String selectQuery = "DELETE FROM staff_table";


        Cursor res = database.rawQuery(selectQuery, new String[]{});


        if (res.getCount() > 0) {
            return true;
        } else {
            return false;
        }


    }

//==================================================================================================
    //==================================================================================================

    //Clear Items

    public Boolean clearItems() {

        SQLiteDatabase database = this.getWritableDatabase();
        String selectQuery = "DELETE FROM itm_table";


        Cursor res = database.rawQuery(selectQuery, new String[]{});

        System.out.println("Itm table clear");

        if (res.getCount() > 0) {
            return true;
        } else {
            return false;
        }


    }

    //=============================================================================================

    //==================================================================================================

    //Clear Items

    public Boolean clearDeliveryNotes() {

        SQLiteDatabase database = this.getWritableDatabase();
        String selectQuery = "DELETE FROM itc_table";


        Cursor res = database.rawQuery(selectQuery, new String[]{});


        if (res.getCount() > 0) {
            return true;
        } else {
            return false;
        }


    }

    //=============================================================================================

    //==========================================================================================
    //Worker List table create

    public void insertWorkerList(ArrayList<String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(AuroraContract.STAFF_COL_1, queryValues.get(0));
        values.put(AuroraContract.STAFF_COL_2, queryValues.get(1));
        values.put(AuroraContract.STAFF_COL_3, queryValues.get(2));
        values.put(AuroraContract.STAFF_COL_4, queryValues.get(3));
        values.put(AuroraContract.STAFF_COL_5, queryValues.get(4));
        values.put(AuroraContract.STAFF_COL_6, queryValues.get(5));
        try {

            database.insert(AuroraContract.STAFF_TABLE_NAME, null, values);
            result = 1;

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.toString());
            System.out.println("Error : Fail to insert Staff");
            result = 0;
        }


        System.out.println("Database Check " + database.isOpen());
        database.close();
    }

    //==========================================================================================

    //==============================================================================================

    // Get Staff information

    public String[][] getStaffDetails() {


        SQLiteDatabase database = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM staff_table";


        Cursor res = database.rawQuery(selectQuery, new String[]{});

        String[][] itemArrayy = new String[7][res.getCount()];

        if (res != null) {
            res.moveToFirst();
            for (int i = 0; i < res.getCount(); i++) {
                itemArrayy[0][i] = res.getString(0);
                itemArrayy[1][i] = res.getString(1);
                itemArrayy[2][i] = res.getString(2);
                itemArrayy[3][i] = res.getString(3);
                itemArrayy[4][i] = res.getString(4);
                res.moveToNext();
            }


        }

        return itemArrayy;

    }


    //================================================================================================
    // Set Picker


    public int setPicker(String url, String deliveryNote, String staffId) {
        result=0;
        final String userKey = url;
        url = url + "&action=set_picker";

        // Gson gson = new GsonBuilder().create();

        AsyncHttpClient client = new AsyncHttpClient();

        System.out.println("Delivery Note =" + deliveryNote);
        RequestParams params = new RequestParams();
        params.put("staff_key", staffId);
        params.put("delivery_note_key", deliveryNote);
        params.setUseJsonStreamer(false);

        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String jsona = new String(responseBody);

                System.out.println("Server Respond =" + jsona);

                JSONObject jsonObj = null;


                try {
                    jsonObj = new JSONObject(jsona);
                    String state = jsonObj.getString("state");

                    if (state.contentEquals("OK")) {
                        System.out.println("Set in Server");
                        result = 1;
                    }
                    else{
                        result=2;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {




            }


        });


        return result;

    }

//==================================================================================================


    // Get Item Information
    public String[][] getPickItemDetails() {


        SQLiteDatabase database = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM itc_table";


        Cursor res = database.rawQuery(selectQuery, new String[]{});

        String[][] itemArrayy = new String[11][res.getCount()];

        if (res != null) {
            res.moveToFirst();
            for (int i = 0; i < res.getCount(); i++) {
                itemArrayy[0][i] = res.getString(5);
                itemArrayy[1][i] = res.getString(1);
                itemArrayy[2][i] = res.getString(2);
                itemArrayy[3][i] = res.getString(3);
                itemArrayy[4][i] = res.getString(4);
                itemArrayy[6][i] = res.getString(6);
                itemArrayy[7][i] = res.getString(7);
                itemArrayy[8][i] = res.getString(8);
                itemArrayy[9][i] = res.getString(9);
                itemArrayy[10][i] = res.getString(10);
                res.moveToNext();
            }


        }

        return itemArrayy;

    }


    //==============================================================================================

    //function inset Delivery Notes

    public void insertDeliveryNotes(ArrayList<String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(AuroraContract.ITC_COL_1, queryValues.get(0));
        values.put(AuroraContract.ITC_COL_2, queryValues.get(1));
        values.put(AuroraContract.ITC_COL_3, queryValues.get(2));
        values.put(AuroraContract.ITC_COL_4, queryValues.get(3));
        values.put(AuroraContract.ITC_COL_5, queryValues.get(4));
        values.put(AuroraContract.ITC_COL_6, queryValues.get(5));
        values.put(AuroraContract.ITC_COL_7, queryValues.get(6));
        values.put(AuroraContract.ITC_COL_8, queryValues.get(7));
        values.put(AuroraContract.ITC_COL_9, queryValues.get(8));
        values.put(AuroraContract.ITC_COL_10, queryValues.get(9));
        values.put(AuroraContract.ITC_COL_11, queryValues.get(10));


        try {

            database.insert(AuroraContract.ITC_TABLE_NAME, null, values);
            result = 1;

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.toString());
            System.out.println("Error Insert to Delivery note Table ");
            result = 0;
        }


        System.out.println("Database Check " + database.isOpen());
        database.close();
    }


    //==========================================================================================

    //function inset items

    public void insertItems(ArrayList<String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(AuroraContract.ITM_COL_1, queryValues.get(0));
        values.put(AuroraContract.ITM_COL_2, queryValues.get(1));
        values.put(AuroraContract.ITM_COL_3, queryValues.get(2));
        values.put(AuroraContract.ITM_COL_4, queryValues.get(3));
        values.put(AuroraContract.ITM_COL_5, queryValues.get(4));
        values.put(AuroraContract.ITM_COL_6, queryValues.get(5));
        values.put(AuroraContract.ITM_COL_7, queryValues.get(6));
        values.put(AuroraContract.ITM_COL_8, queryValues.get(7));
        values.put(AuroraContract.ITM_COL_9, queryValues.get(8));
        values.put(AuroraContract.ITM_COL_10, queryValues.get(9));
        values.put(AuroraContract.ITM_COL_11, queryValues.get(10));
        values.put(AuroraContract.ITM_COL_12, queryValues.get(11));
        values.put(AuroraContract.ITM_COL_13, queryValues.get(12));


        try {

            database.insert(AuroraContract.ITM_TABLE_NAME, null, values);
            result = 1;

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.toString());
            System.out.println("Error Insert to Item Table ");
            result = 0;
        }

        System.out.println("Database Check " + database.isOpen());
        database.close();
    }


    //==========================================================================================

    // Get Delivery Note from Server

    public void getDeliveryNotes() {

        //user_validate();
        String authKey = getUserData()[2];
        if(authKey!=null){
            user_validate();
        }
        else{
            clearDeliveryNotes();
        }

        authKey = getUserData()[2];
        authKey = authKey + "&action=get_deliveries_ready_to_be_picked";
        String url =  authKey;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        System.out.println(url);
        client.post(url, params, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                ArrayList<String> itemList = new ArrayList<String>();


                String jsona = new String(responseBody);

                System.out.println(jsona);

                JSONObject jsonObj = null;


                try {
                    jsonObj = new JSONObject(jsona);

                    String state = jsonObj.getString("state");// future use validation
                    String data = jsonObj.getString("data");
                    System.out.println("*******************************"+state);
                    if(state.contains("Error")){

                        clearDeliveryNotes();


                    }else if(state.contains("OK")){

                        JSONArray items = jsonObj.getJSONArray("data");
                        clearDeliveryNotes();

                        // looping through All items
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject c = items.getJSONObject(i);

                            // details fetch from object

                            String note_cu_key = c.getString("Delivery Note Customer Key");
                            String note_customer_name = c.getString("Delivery Note Customer Name");
                            String note_create_date = c.getString("Delivery Note Date Created");
                            String delivery_weight = c.getString("Delivery Note Estimated Weight");
                            String delivery_note_id = c.getString("Delivery Note ID");
                            String note_key = c.getString("Delivery Note Key");
                            String note_nop = c.getString("Delivery Note Number Ordered Parts");
                            String dns_key = c.getString("Delivery Note Store Key");
                            String dnt = c.getString("Delivery Note Type");
                            String st_code = c.getString("Store Code");
                            String st_name = c.getString("Store Name");
                            itemList.add(note_cu_key);
                            itemList.add(note_customer_name);
                            itemList.add(note_create_date);
                            itemList.add(delivery_weight);
                            itemList.add(delivery_note_id);
                            itemList.add(note_key);
                            itemList.add(note_nop);
                            itemList.add(dns_key);
                            itemList.add(dnt);
                            itemList.add(st_code);
                            itemList.add(st_name);
                            System.out.println(itemList);
                            insertDeliveryNotes(itemList);
                            itemList.clear();

                        }
                    }





                } catch (JSONException e) {
                    e.printStackTrace();

                    setJsonError(1);

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                String json = new String(responseBody);
                System.out.println("Server Error + "+json );
                setJsonError(1);


            }

        });


    }


    //==============================================================================================


    public void getPendingDeliveryNotes() {


        String authKey = getUserData()[2];
        authKey = authKey + "&action=get_pending_deliveries";
        String url = authKey;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        System.out.println(url);
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                ArrayList<String> itemList = new ArrayList<String>();


                String jsona = new String(responseBody);

                System.out.println(jsona);

                JSONObject jsonObj = null;


                try {
                    jsonObj = new JSONObject(jsona);

                    String state = jsonObj.getString("state");// future use validation
                    String data = jsonObj.getString("data");


                    JSONArray items = jsonObj.getJSONArray("data");
                    clearDeliveryNotes();

                    // looping through All items
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject c = items.getJSONObject(i);

                        // details fetch from object

                        String note_cu_key = c.getString("Delivery Note Customer Key");
                        String note_customer_name = c.getString("Delivery Note Customer Key");
                        String note_create_date = c.getString("Delivery Note Date Created");
                        String delivery_weight = c.getString("Delivery Note Estimated Weight");
                        String delivery_note_id = c.getString("Delivery Note ID");
                        String note_key = c.getString("Delivery Note Key");
                        String note_nop = c.getString("Delivery Note Number Ordered Parts");
                        String dns_key = c.getString("Delivery Note Store Key");
                        String dnt = c.getString("Delivery Note Type");
                        String st_code = c.getString("Store Code");
                        String st_name = c.getString("Store Name");
                        String note_status=c.getString("Delivery Note State");
                        itemList.add(note_cu_key);
                        itemList.add(note_customer_name);
                        itemList.add(note_create_date);
                        itemList.add(delivery_weight);
                        itemList.add(delivery_note_id);
                        itemList.add(note_key);
                        itemList.add(note_nop);
                        itemList.add(dns_key);
                        itemList.add(dnt);
                        itemList.add(st_code);
                        itemList.add(st_name);
                        System.out.println(itemList);
                        insertDeliveryNotes(itemList);

                        itemList.clear();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                    setJsonError(1);

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                String json = new String(responseBody);
                System.out.println("Server Error + "+json );
                setJsonError(1);


            }

        });


    }


    //==============================================================================================


    public int syncDeliveryNoteItemList(final String deliveryNote) {

        result=0;

        System.out.println("Sync Delivery note Item Status ....");

        String authKey = getUserData()[2];
        authKey = authKey + "&action=get_delivery_note_items";

        final String url = authKey;
        AsyncHttpClient client = new AsyncHttpClient();

        System.out.println(deliveryNote);
        RequestParams params = new RequestParams();
        params.put("delivery_note_key", deliveryNote);
        params.setUseJsonStreamer(false);
        System.out.println(url);
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String jsona = new String(responseBody);
                System.out.println("Server respond= " + jsona);

                JSONObject jsonObj = null;
                JSONObject jsonItem = null;
                JSONObject printob;

                ArrayList<JSONObject> itemobjectList = new ArrayList<JSONObject>();

                ArrayList<String> itemList = new ArrayList<String>();


                try {
                    jsonObj = new JSONObject(jsona);
                    String state = jsonObj.getString("state");
                    if(state.contains("OK")){

                        String data = jsonObj.getString("data");
                        System.out.println(url );
                        // Clear Item Table
                        clearItems();
                        JSONArray jsonarray = new JSONArray(data);

                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject obj = jsonarray.getJSONObject(i);

                            String item = obj.getString("item");
                            String url = obj.getString("part_location");
                            jsonItem = new JSONObject(item);
                            itemobjectList.add(jsonItem);
                            System.out.println(item);

                        }

                        printob = new JSONObject();

                        for (int j = 0; j < itemobjectList.size(); j++) {


                            printob = itemobjectList.get(j);

                            String ITKey = printob.getString("Inventory Transaction Key");
                            String sku = printob.getString("Part SKU");
                            String pkgDes = printob.getString("Part Package Description");
                            String pkgWeight = printob.getString("Part Package Weight");
                            String toPick = printob.getString("Required");
                            String picked = printob.getString("Picked");
                            String outStk = printob.getString("Out of Stock");
                            String wait = printob.getString("Waiting");
                            String lckey = printob.getString("Location Code");
                            String ref = printob.getString("Part Reference");
                            String barcode = printob.getString("Part SKO Barcode");
                            String status = cal_status(toPick,picked,outStk,wait);//"0";// ned to replace after Raul done the system change

                            // Items Add To Insert
                            itemList.add(deliveryNote);
                            itemList.add(ITKey);
                            itemList.add(sku);
                            itemList.add(pkgDes);
                            itemList.add(pkgWeight);
                            itemList.add(toPick);
                            itemList.add(picked);
                            itemList.add(outStk);
                            itemList.add(wait);
                            itemList.add(lckey);
                            itemList.add(ref);
                            itemList.add(barcode);
                            itemList.add(status);
                            System.out.println(itemList);
                            insertItems(itemList);
                            itemList.clear();


                        }

                        setNetStat(1);
                        result=0;

                    }else if(state=="Error"){
                        setNetStat(0);
                        //clearItems();
                        result=2;
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    setNetStat(0);
                    setJsonError(1);

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                detail[0] = "error";
                setNetStat(0);


            }


        });

     return result;
    }


    //==============================================================================================


    // Get Delivery Note  Information
    public String[][] getItemDetail() {


        SQLiteDatabase database = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM itm_table";


        Cursor res = database.rawQuery(selectQuery, new String[]{});

        String[][] itemArrayy = new String[15][res.getCount()];

        if (res.getCount()>0) {
            res.moveToFirst();


            for (int i = 0; i < res.getCount(); i++) {
                itemArrayy[0][i] = res.getString(1);
                itemArrayy[1][i] = res.getString(2);
                itemArrayy[2][i] = res.getString(3);
                itemArrayy[3][i] = res.getString(4);
                itemArrayy[4][i] = res.getString(5);
                itemArrayy[5][i] = res.getString(6);
                itemArrayy[6][i] = res.getString(7);
                itemArrayy[7][i] = res.getString(8);
                itemArrayy[8][i] = res.getString(9);
                itemArrayy[9][i] = res.getString(10);
                itemArrayy[10][i] = res.getString(11);
                itemArrayy[11][i] = res.getString(12);
                itemArrayy[12][i] = res.getString(13);

              if(!res.isLast()){

                  res.moveToNext();
              }

            }


        }
        else{

            itemArrayy=null;
        }



        return itemArrayy;

    }


    //==============================================================================================

    //==============================================================================================


    // Get Delivery Note  Information
    public String[][] getItemDetail_remove_current(String key) {


        SQLiteDatabase database = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM itm_table "+key;

        Cursor res = database.rawQuery(selectQuery, new String[]{});


        String[][] itemArrayy = new String[14][res.getCount()];

        if (res != null) {
            res.moveToFirst();
            for (int i = 0; i < res.getCount(); i++) {
                itemArrayy[0][i] = res.getString(1);
                itemArrayy[1][i] = res.getString(2);
                itemArrayy[2][i] = res.getString(3);
                itemArrayy[3][i] = res.getString(4);
                itemArrayy[4][i] = res.getString(5);
                itemArrayy[5][i] = res.getString(6);
                itemArrayy[6][i] = res.getString(7);
                itemArrayy[7][i] = res.getString(8);
                itemArrayy[8][i] = res.getString(9);
                itemArrayy[9][i] = res.getString(10);
                itemArrayy[10][i] = res.getString(11);
                itemArrayy[11][i] = res.getString(12);
                itemArrayy[12][i] = res.getString(13);
                res.moveToNext();
            }


        }
        else{
            itemArrayy=null;
        }


        return itemArrayy;


    }


    //==============================================================================================


    //==============================================================================================


    // Get Delivery Note  Information
    public String[] getItemDetailByTrkey(String val_key) {


        SQLiteDatabase database = this.getWritableDatabase();
     String selectQuery = "SELECT * FROM itm_table where inventory_transaction_key ='" + val_key + "'";
        //String selectQuery = "SELECT * FROM itm_table ";


        Cursor res = database.rawQuery(selectQuery, new String[]{});

        String[] itemArrayy = new String[13];

        if (res != null) {
            res.moveToFirst();
            itemArrayy[0] = "Delivery Note key = " + res.getString(1);
            itemArrayy[1] = "Inventory Transaction Key = " + res.getString(2);
            itemArrayy[2] = "Part SKU = " + res.getString(3);
            itemArrayy[3] = "Part Package Description = " + res.getString(4);

            itemArrayy[4] = "Part Package Weight = " + res.getString(5);
            itemArrayy[5] = "ToPick = " + res.getString(6);

            itemArrayy[6] = "Picked = " + res.getString(7);
            itemArrayy[7] = "Out of Stock = " + res.getString(8);
            itemArrayy[8] = "Waiting = " + res.getString(9);
            itemArrayy[9] = "Location Key = " + res.getString(10);
            itemArrayy[10] = "Part Reference = " + res.getString(11);
            itemArrayy[11] = "Part SKO Barcode = " + res.getString(12);
            itemArrayy[12] =  " "+res.getString(1);


        }
        for(int i=0;i<13;i++) {
            System.out.println("JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ" + itemArrayy[i]);
        }
        return itemArrayy;

    }


    //==============================================================================================


//============================= Find The Item ======================================================

    public String findTheItem(String val) {

        SQLiteDatabase database = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM itm_table where part_sko_barcode='"+val+"'";
        System.out.println(selectQuery);
        Cursor res = database.rawQuery(selectQuery, new String[]{});

        if (res != null) {

            res.moveToFirst();
            if (res.getCount() > 0) {

                System.out.println(res.getString(2));

                return  res.getString(2);

            }


            return "not found";
        } else {
            return "not found";
        }


    }


//==================================================================================================


    public void pickAction(String tr_key) {

        result=0;

        String current_picked = "0";
        String current_to_pick = "0";
        String current_out_stk = "0";
        String current_wait = "0";
        String current_status = "0";
        String dev_note_key;
        int picked = 0;
        int toPick = 0;


        SQLiteDatabase database = this.getWritableDatabase();

        String selectQuery = "SELECT delivery_note_key,picked,to_pick,out_of_stock,waiting,status FROM itm_table where inventory_transaction_key='" + tr_key + "'";

        Cursor res = database.rawQuery(selectQuery, new String[]{});

        if (res != null) {

            res.moveToFirst();
            if (res.getCount() > 0) {

                dev_note_key = res.getString(0);
                current_picked = res.getString(1);
                current_to_pick = res.getString(2);
                current_out_stk= res.getString(3);
                current_wait= res.getString(4);
                current_status= res.getString(5);


                picked = Integer.parseInt(current_picked);
                toPick = Integer.parseInt(current_to_pick);
                System.out.println("SQL = " + selectQuery +"    "+current_picked+"   "+current_to_pick);
                 picked = picked + 1;

                current_picked = Integer.toString(picked);
                current_to_pick = Integer.toString(toPick);
                String updateQuery = "update itm_table set picked='" + current_picked + "' , to_pick='" + current_to_pick + "'  WHERE inventory_transaction_key='"+tr_key+"'";
                database.execSQL(updateQuery);
                System.out.println("SQL Update Table = " + updateQuery);

                if( toPick==picked){
                    current_status="1";
                    String update_sat_Query = "update itm_table set status='1'   WHERE inventory_transaction_key='"+tr_key+"'";
                    Cursor res3 = database.rawQuery(update_sat_Query, new String[]{});
                    res3.moveToFirst();
                }

                current_picked="1";
                System.out.println("Updating Server ................... ");
                if(update_pick_items(dev_note_key,tr_key,current_to_pick, current_picked,current_out_stk,current_wait,current_status)){

                }


            }

        }




    }

//bulck pic

    public void bulckPickAction(String tr_key, String qty) {

        result=0;
        String current_picked = "0";
        String current_to_pick = "0";
        String current_out_stk = "0";
        String current_wait = "0";
        String current_status = "0";
        String dev_note_key;
        int picked = 0;
        int toPick = 0;

        SQLiteDatabase database = this.getWritableDatabase();

        String selectQuery = "SELECT delivery_note_key,picked,to_pick,out_of_stock,waiting,status FROM itm_table where inventory_transaction_key='" + tr_key + "'";

        Cursor res = database.rawQuery(selectQuery, new String[]{});

        if (res != null) {

            res.moveToFirst();
            if (res.getCount() > 0) {

                dev_note_key = res.getString(0);
                current_picked = res.getString(1);
                current_to_pick = res.getString(2);
                current_out_stk= res.getString(3);
                current_wait= res.getString(4);
                current_status= res.getString(5);


                picked = Integer.parseInt(current_picked);
                toPick = Integer.parseInt(current_to_pick);
                System.out.println("SQL = " + selectQuery +"    "+current_picked+"   "+current_to_pick);
                picked = picked + Integer.parseInt(qty);
                //toPick = toPick - 1;

                current_picked = Integer.toString(picked);
                current_to_pick = Integer.toString(toPick);
                String updateQuery = "update itm_table set picked='" + current_picked + "' , to_pick='" + current_to_pick + "'  WHERE inventory_transaction_key='"+tr_key+"'";
                database.execSQL(updateQuery);
                System.out.println("SQL Update Table = " + updateQuery);

                if( toPick==0){
                    current_status="1";
                    String update_sat_Query = "update itm_table set status='1'   WHERE inventory_transaction_key='"+tr_key+"'";
                    Cursor res3 = database.rawQuery(update_sat_Query, new String[]{});
                    res3.moveToFirst();

                }

                System.out.println("Updating Server ................... ");
                if(update_pick_items(dev_note_key,tr_key,current_to_pick, current_picked,current_out_stk,current_wait,current_status)){

                }


            }

        }




    }

//=============================================================================================
    public int validatePickingItems(String tr_key,String qty){
        result=0;
        String current_picked = "0";
        String current_to_pick = "0";
        String current_out_stk = "0";
        String current_wait = "0";
        String current_status = "0";
        String dev_note_key;
        int picked = 0;
        int toPick = 0;
        int pick_balance=0;

        SQLiteDatabase database = this.getWritableDatabase();

        String selectQuery = "SELECT delivery_note_key,picked,to_pick,out_of_stock,waiting,status FROM itm_table where inventory_transaction_key='" + tr_key + "'";

        Cursor res = database.rawQuery(selectQuery, new String[]{});

        if (res != null) {

            res.moveToFirst();
            if (res.getCount() > 0) {

                dev_note_key = res.getString(0);
                current_picked = res.getString(1);
                current_to_pick = res.getString(2);
                current_out_stk= res.getString(3);
                current_wait= res.getString(4);
                current_status= res.getString(5);


                picked = Integer.parseInt(current_picked);
                toPick = Integer.parseInt(current_to_pick);
                System.out.println("SQL = " + selectQuery +"    "+current_picked+"   "+current_to_pick);
                picked = picked + Integer.parseInt(qty);
                //toPick = toPick - 1;


                pick_balance=toPick-picked;

                System.out.println("Pick Balance PPPPPPPPPPPPPPPPP =" +pick_balance);

            }

        }

        return pick_balance;
    }
//==============================================================================================
// Out Of Stock and waiting Action

    public int not_avilable_Action(String tr_key ,int action_type) {
        String current_picked = "0";
        String current_to_pick = "0";
        String current_out_stk = "0";
        String current_wait = "0";
        String dev_note_key;
        String current_status;
        int picked = 0;
        int toPick = 0;
        int status = 0;
        result=0;

        SQLiteDatabase database = this.getWritableDatabase();

        String selectQuery = "SELECT delivery_note_key,picked,to_pick,out_of_stock,waiting,status FROM itm_table where inventory_transaction_key='" + tr_key + "'";

        Cursor res = database.rawQuery(selectQuery, new String[]{});

        if (res != null) {

            res.moveToFirst();
            if (res.getCount() > 0) {

                dev_note_key = res.getString(0);
                current_picked = res.getString(1);
                current_to_pick = res.getString(2);
                current_out_stk= res.getString(3);
                current_wait= res.getString(4);
                current_status= res.getString(5);

                picked=Integer.parseInt(current_picked);

                System.out.println("To Pick ="+current_to_pick);
                System.out.println("get Action Type  ="+action_type);

                int out_of_stk_int = Integer.parseInt(current_out_stk);
                int item_waiting_int= Integer.parseInt(current_wait);

                toPick = Integer.parseInt(current_to_pick);
                status=Integer.parseInt(current_status);

                if(toPick>=picked) {

                    if( toPick==picked){


                        current_status="1";
                        String update_sat_Query = "update itm_table set status='1'   WHERE inventory_transaction_key='"+tr_key+"'";
                        database.execSQL(update_sat_Query);
                        result=1;

                    }

                    if(action_type==1){

                        System.out.println("get Action Type  ="+action_type);

                        out_of_stk_int = out_of_stk_int + 1;

                        current_picked = Integer.toString(picked);
                        current_to_pick = Integer.toString(toPick);
                        current_out_stk= Integer.toString( out_of_stk_int);
                        current_wait= Integer.toString( item_waiting_int);

                        update_not_picked_item(dev_note_key,tr_key,current_to_pick, current_picked,current_out_stk,current_wait,current_status,"Out of Stock");
                        result=1;

                    }


                    if(action_type==0){

                        item_waiting_int=item_waiting_int+1;

                        current_picked = Integer.toString(picked);
                        current_to_pick = Integer.toString(toPick);
                        current_out_stk= Integer.toString( out_of_stk_int);
                        current_wait= Integer.toString( item_waiting_int);

                        update_not_waiting_item(dev_note_key,tr_key,current_to_pick, current_picked,current_out_stk,current_wait,current_status,"Warehouse");
                        result=1;


                    }




                }

                current_picked = Integer.toString(picked);
                current_to_pick = Integer.toString(toPick);
                current_out_stk= Integer.toString( out_of_stk_int);
                current_wait= Integer.toString( item_waiting_int);

                System.out.println("Updating Table.... Out of Stock  ="+current_out_stk);

                String updateQuery = "update itm_table set out_of_stock='" + current_out_stk + "' , waiting='" +  current_wait + "'  WHERE inventory_transaction_key='"+tr_key+"'";


                database.execSQL(updateQuery);


//                if(update_delivery_note_in_server(dev_note_key,tr_key,current_to_pick, current_picked,current_out_stk,current_wait,current_status)){
//                    result=1;
//                }


            }

        }
               return result;
    }


//=================================================================================================



    public int[] get_to_all_info(){

        int[] total= new int[5];

        String st_picked,st_to_pick,st_wating,st_out_of_stock;
        int int_picked=0;
        int int_to_pick=0;
        int int_wating=0;
        int int_out_of_stock=0;
        SQLiteDatabase database = this.getWritableDatabase();

        String selectQuery = "SELECT picked,to_pick,waiting,out_of_stock FROM itm_table";
        Cursor res = database.rawQuery(selectQuery, new String[]{});

        if (res != null) {

            res.moveToFirst();

             for(int i=0;i<res.getCount();i++)
             {
                st_picked=res.getString(0);
                st_to_pick=res.getString(1);
                st_wating=res.getString(2);
                st_out_of_stock=res.getString(3);

                //========================================================
                int_picked+=Integer.parseInt(st_picked);
                int_to_pick+=Integer.parseInt(st_to_pick);
                int_wating+=Integer.parseInt(st_wating);
                int_out_of_stock+=Integer.parseInt(st_out_of_stock);

                //========================================================
                res.moveToNext();

            }

        }

        total[0]=int_picked;
        total[1]=int_to_pick;
        total[2]=int_wating;
        total[3]= int_out_of_stock;
        total[4]=int_picked+int_to_pick+int_out_of_stock+int_wating;
        return total;
    }

    //===================================================================================================================================================

    public Boolean update_delivery_note_in_server(String dev_note_key,String tr_kry,String to_pick,String picked,String out_of_stock,String waiting,String ststus)
    {


        setAuth_key(getUserData()[2]);

        final String url = getAuth_key()+"&action=update_delivery_note_item_status";


        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("delivery_note_key", dev_note_key);
        params.put("itf_key", tr_kry);
        params.put("to_pick", to_pick);
        params.put("picked",picked);
        params.put("out_of_stock", out_of_stock);
        params.put("waiting",  waiting);
        params.put("status", ststus);

        params.setUseJsonStreamer(false);

        System.out.println( params);
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                String jsona = new String(responseBody);


                System.out.println("Server Result for Update Pick Action =" + jsona);
                setNetStat(1);

                JSONObject jsonObj = null;


                try {
                    jsonObj = new JSONObject(jsona);
                    String state = jsonObj.getString("state");

                    if (state.contentEquals("Ok")) {

                       setNetStat(1);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                  setNetStat(0);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                setNetStat(0);


            }


        });

        setCheckNet(getNetStat());

        System.out.println(" The network check "+ isCheckNet());
        return isCheckNet();
    }



    public Boolean update_pick_items(String dev_note_key,String tr_kry,String to_pick,String picked,String out_of_stock,String waiting,String status)
    {

           System.out.println("LLLLLLLLLLLLLLLLLLLLLLLLl"+picked);
        setAuth_key(getUserData()[2]);

        final String url = getAuth_key()+"&action=pick_item";
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("delivery_note_key", dev_note_key);
        params.put("itf_key", tr_kry);
        params.put("to_pick", to_pick);
        params.put("quantity",picked);
        params.put("out_of_stock", out_of_stock);
        params.put("waiting",  waiting);
        params.put("status", status);
        params.setUseJsonStreamer(false);

        System.out.println( "pick action send ="+params );
        System.out.println( "pick action url ="+url );
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                String jsona = new String(responseBody);


                System.out.println("Server Result for Update Pick Action =" + jsona);
                setNetStat(1);

                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(jsona);
                    String state = jsonObj.getString("state");

                    if (state.contentEquals("Ok")) {

                        setNetStat(1);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    setNetStat(0);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                setNetStat(0);


            }


        });

        setCheckNet(getNetStat());

        System.out.println(" The network check "+ isCheckNet());
        return isCheckNet();
    }

    public Boolean update_not_picked_item(String dev_note_key,String tr_kry,String to_pick,String picked,String out_of_stock,String waiting,String status,String action)
    {


        setAuth_key(getUserData()[2]);

        final String url = getAuth_key()+"&action=set_as_not_picked_item";

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("delivery_note_key", dev_note_key);
        params.put("itf_key", tr_kry);
        params.put("to_pick", to_pick);
        params.put("quantity",picked);
        params.put("out_of_stock", out_of_stock);
        params.put("waiting",  waiting);
        params.put("status", status);
        params.put("type", action);
        params.setUseJsonStreamer(false);

        System.out.println( "not pick action send ="+params );
        System.out.println( "not pick action url ="+url );
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                String jsona = new String(responseBody);


                System.out.println("Server Result for Update Pick Action =" + jsona);
                setNetStat(1);

                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(jsona);
                    String state = jsonObj.getString("state");

                    if (state.contentEquals("Ok")) {

                        setNetStat(1);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    setNetStat(0);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                setNetStat(0);


            }


        });

        setCheckNet(getNetStat());

        System.out.println(" The network check "+ isCheckNet());
        return isCheckNet();
    }

    public Boolean update_cancel_picked_item(String dev_note_key,String tr_kry)
    {


        setAuth_key(getUserData()[2]);

        final String url = getAuth_key()+"&action=cancel_picked_item";

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("delivery_note_key",dev_note_key);
        params.put("itf_key", tr_kry);
        params.setUseJsonStreamer(false);

        System.out.println( "not pick action send ="+params );
        System.out.println( "not pick action url ="+url );
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                String jsona = new String(responseBody);


                System.out.println("Server Result for Update Pick Action =" + jsona);
                setNetStat(1);

                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(jsona);
                    String state = jsonObj.getString("state");

                    if (state.contentEquals("Ok")) {

                        setNetStat(1);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    setNetStat(0);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                setNetStat(0);


            }


        });

        setCheckNet(getNetStat());

        System.out.println(" The network check "+ isCheckNet());
        return isCheckNet();
    }



    public Boolean update_not_waiting_item(String dev_note_key,String tr_kry,String to_pick,String picked,String out_of_stock,String waiting,String status,String action)
    {


        setAuth_key(getUserData()[2]);

        final String url = getAuth_key()+"&action=set_as_waiting_item";

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("delivery_note_key", dev_note_key);
        params.put("itf_key", tr_kry);
        params.put("to_pick", to_pick);
        params.put("quantity",picked);
        params.put("out_of_stock", out_of_stock);
        params.put("waiting",  waiting);
        params.put("status", status);
        params.put("type", action);
        params.setUseJsonStreamer(false);

        System.out.println( "pick action send ="+params );
        System.out.println( "pick action url ="+url );
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                String jsona = new String(responseBody);


                System.out.println("Server Result for Update Pick Action =" + jsona);
                setNetStat(1);

                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(jsona);
                    String state = jsonObj.getString("state");

                    if (state.contentEquals("Ok")) {

                        setNetStat(1);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    setNetStat(0);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                setNetStat(0);


            }


        });

        setCheckNet(getNetStat());

        System.out.println(" The network check "+ isCheckNet());
        return isCheckNet();
    }



    public void setNetStat(int val)
    {
        SQLiteDatabase database = this.getWritableDatabase();

        database.execSQL("update app_table set net_stat ="+val);


        System.out.println("Set Net to =" +val);


    }

    public boolean getNetStat()
    {
        SQLiteDatabase database = this.getWritableDatabase();
        String status="";
        int val=0;
        String selectQuery = "SELECT net_stat FROM app_table";
        Cursor res = database.rawQuery(selectQuery, new String[]{});

        if (res != null) {

            res.moveToFirst();
            status = res.getString(0);

        }
        System.out.println("vale of app_table kkkkkkkkkkkkkkkkkkkkkkkkkkkkk="+status);
        val=Integer.parseInt(status);

        System.out.println("vale of app_table ="+val);

        if(val>0){
            return true;
        }
        else{
            return  false;
        }



    }

    public String cal_status(String item_required, String  item_picked, String item_out_stk, String item_wating){

         int val=0;
         String status="0";

        val=Integer.parseInt(item_required)-(Integer.parseInt(item_picked)+Integer.parseInt(item_out_stk)+Integer.parseInt(item_wating));

        if(val<=0){
            status="1";
        }

        return status;
    }


    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public void user_validate(){
        setAuth_key(getUserData()[2]);
        registerUser(getAuth_key());

    }
}
