package com.oakspro.flipshop;

public class ProductsModel {
    String name, cImageUri1, mrp, price;


    public ProductsModel(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getcImageUri1() {
        return cImageUri1;
    }

    public void setcImageUri1(String cImageUri1) {
        this.cImageUri1 = cImageUri1;
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
}
