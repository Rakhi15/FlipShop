package com.oakspro.flipshop;

import android.text.TextUtils;

public class ProductUpload {
    private String brand, name, mrp, price, description, stock, color, size, category, key;
    private String cImageUri1, cImageUri2, cImageUri3, cImageUri4;

    public ProductUpload() {

    }

    public ProductUpload(String key,String category, String brand, String name, String mrp, String price, String description, String stock, String color, String size, String cImageUri1, String cImageUri2, String cImageUri3, String cImageUri4) {

        if (TextUtils.isEmpty(category)){
            this.category="no category";
        }
        if (TextUtils.isEmpty(name)) {
            this.name = "no name";
        }
        if (TextUtils.isEmpty(brand)) {
            this.brand = "no brand";
        }
        if (TextUtils.isEmpty(price)) {
            this.price = "NOT MENTIONED";
        }
        if (TextUtils.isEmpty(mrp)) {
            this.mrp = "NOT MENTIONED";
        }
        if (TextUtils.isEmpty(stock)) {
            this.stock = "NO INFORMATION";
        }
        if (TextUtils.isEmpty(color)) {
            this.color = "NO INFORMATION";
        }
        if (TextUtils.isEmpty(size)) {
            this.size = "NOT MENTIONED";
        }
        if (TextUtils.isEmpty(description)) {
            this.description = "NO DESCRIPTION";
        }
        this.brand = brand;
        this.name = name;
        this.mrp = mrp;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.color = color;
        this.size = size;
        this.cImageUri1 = cImageUri1;
        this.cImageUri2 = cImageUri2;
        this.cImageUri3 = cImageUri3;
        this.cImageUri4 = cImageUri4;
        this.category=category;
        this.key=key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getcImageUri1() {
        return cImageUri1;
    }

    public void setcImageUri1(String cImageUri1) {
        this.cImageUri1 = cImageUri1;
    }

    public String getcImageUri2() {
        return cImageUri2;
    }

    public void setcImageUri2(String cImageUri2) {
        this.cImageUri2 = cImageUri2;
    }

    public String getcImageUri3() {
        return cImageUri3;
    }

    public void setcImageUri3(String cImageUri3) {
        this.cImageUri3 = cImageUri3;
    }

    public String getcImageUri4() {
        return cImageUri4;
    }

    public void setcImageUri4(String cImageUri4) {
        this.cImageUri4 = cImageUri4;
    }
}



