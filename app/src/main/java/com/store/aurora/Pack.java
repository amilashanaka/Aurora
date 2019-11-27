package com.store.aurora;

public class Pack {


    String pkg_des;
    String part_package_weight;
    String to_pick;
    String picked;
    String out_of_stock;
    String waiting;
    String location_key;
    String part_reference;
    String item_note;
    String status;
    Boolean item_pick;
    String  inventory_transaction_key;
    String part_sku;

    public Pack(String pkg_des, String part_package_weight, String to_pick, String picked, String out_of_stock, String waiting, String location_key, String part_reference, String item_note, Boolean item_pick, String inventory_transaction_key, String part_sku, String status) {
        this.pkg_des = pkg_des;
        this.part_package_weight = part_package_weight;
        this.to_pick = to_pick;
        this.picked = picked;
        this.out_of_stock = out_of_stock;
        this.waiting = waiting;
        this.location_key = location_key;
        this.part_reference = part_reference;
        this.item_note = item_note;
        this.item_pick = item_pick;
        this.inventory_transaction_key = inventory_transaction_key;
        this.part_sku = part_sku;
        this.status=status;
    }




    //==============================================================================================

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getItem_pick() {
        return item_pick;
    }

    public void setItem_pick(Boolean item_pick) {
        this.item_pick = item_pick;
    }

    public String getItem_note() {
        return item_note;
    }

    public void setItem_note(String item_note) {
        this.item_note = item_note;
    }



    public String getInventory_transaction_key() {
        return inventory_transaction_key;
    }

    public void setInventory_transaction_key(String inventory_transaction_key) {
        this.inventory_transaction_key = inventory_transaction_key;
    }

    public String getPart_sku() {
        return part_sku;
    }

    public void setPart_sku(String part_sku) {
        this.part_sku = part_sku;
    }

    public String getPkg_des() {
        return pkg_des;
    }

    public void setPkg_des(String pkg_des) {
        this.pkg_des = pkg_des;
    }

    public String getPart_package_weight() {
        return part_package_weight;
    }

    public void setPart_package_weight(String part_package_weight) {
        this.part_package_weight = part_package_weight;
    }

    public String getTo_pick() {
        return to_pick;
    }

    public void setTo_pick(String to_pick) {
        this.to_pick = to_pick;
    }

    public String getPicked() {
        return picked;
    }

    public void setPicked(String picked) {
        this.picked = picked;
    }

    public String getOut_of_stock() {
        return out_of_stock;
    }

    public void setOut_of_stock(String out_of_stock) {
        this.out_of_stock = out_of_stock;
    }

    public String getWaiting() {
        return waiting;
    }

    public void setWaiting(String waiting) {
        this.waiting = waiting;
    }

    public String getLocation_key() {
        return location_key;
    }

    public void setLocation_key(String location_key) {
        this.location_key = location_key;
    }

    public String getPart_reference() {
        return part_reference;
    }

    public void setPart_reference(String part_reference) {
        this.part_reference = part_reference;
    }


}
