package com.store.aurora;

public class AuroraContract {



    // Database structure variables
    public static final String DATABASE_NAME = "my.db";

    // User table structure variables
    public static final String USERS_TABLE_NAME = "users_table";
    public static final String USERS_COL_1 = "ID";
    public static final String USERS_COL_2 = "account_code";
    public static final String USERS_COL_3 = "account_name";
    public static final String USERS_COL_4 = "locale";
    public static final String USERS_COL_5 = "worker_user_id";
    public static final String USERS_COL_6 = "worker_alias";
    public static final String USERS_COL_7 = "worker_type";
    public static final String USERS_COL_8 = "auth_key";

    // Items to Collect table structure variables
    public static final String ITC_TABLE_NAME = "itc_table";
    public static final String ITC_COL_1 = "DeliveryNoteCustomerKey";
    public static final String ITC_COL_2 = "DeliveryNoteCustomerName";
    public static final String ITC_COL_3 = "DeliveryNoteCustomerDate";
    public static final String ITC_COL_4 = "DeliveryNoteEstimatedWeight";
    public static final String ITC_COL_5 = "DeliveryNoteID";
    public static final String ITC_COL_6 = "DeliveryNoteKey";
    public static final String ITC_COL_7 = "DeliveryNoteNumberOrderedParts";
    public static final String ITC_COL_8 = "DeliveryNoteStoreKey";
    public static final String ITC_COL_9 = "DeliveryNoteType";
    public static final String ITC_COL_10 = "StoreCode";
    public static final String ITC_COL_11 = "StoreName";

    // Staff table
    public static final String STAFF_TABLE_NAME = "staff_table";
    public static final String STAFF_COL_1 = "StaffKey";
    public static final String STAFF_COL_2 = "StaffAlias";
    public static final String STAFF_COL_3 = "StaffName";
    public static final String STAFF_COL_4 = "StaffID";
    public static final String STAFF_COL_5 = "StaffMainImage";
    public static final String STAFF_COL_6 = "StaffRoles";

    // Delivery Items table

    public static final String ITM_TABLE_NAME = "itm_table";
    public static final String ITM_COL_1 = "delivery_note_key";
    public static final String ITM_COL_2 = "inventory_transaction_key";
    public static final String ITM_COL_3 = "part_sku";
    public static final String ITM_COL_4 = "pkg_des";
    public static final String ITM_COL_5= "part_package_weight";
    public static final String ITM_COL_6= "to_pick";
    public static final String ITM_COL_7= "picked";
    public static final String ITM_COL_8= "out_of_stock";
    public static final String ITM_COL_9= "waiting";
    public static final String ITM_COL_10= "location_key";
    public static final String ITM_COL_11= "part_reference";
    public static final String ITM_COL_12= "part_sko_barcode";
    public static final String ITM_COL_13= "status";


    // App Setting table


    public static final String APP_TABLE_NAME = "app_table";
    public static final String APP_COL_1 = "net_stat";
    public static final String APP_COL_2 = "db_stat";









}
