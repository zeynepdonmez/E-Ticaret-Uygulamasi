package com.example.zeynep.e_ticaret;


public class urunOzellik {

    private String productId;
    private String productName;
    private String brief;
    private String description;
    private String price;
    private String resim;

    public urunOzellik(String brief) {
    }

    public urunOzellik( String resim,String price)
    {

        this.resim = resim;
        this.price=price;

    } public urunOzellik() {
    }


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getResim() {
        return resim;
    }

    public void setResim(String resim) {
        this.resim = resim;
    }
}
