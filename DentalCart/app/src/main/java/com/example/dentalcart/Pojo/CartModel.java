package com.example.dentalcart.Pojo;

public class CartModel {
    private String ID ;
    private int quantity ;
    private String price ;
    private String name ;
    private String image ;
    private  int quanCount ;

    public CartModel(String ID, String price , String name , String image , int quantity , int quanCount ) {
        this.ID = ID;
        this.price = price ;
        this.name = name ;
        this.image = image ;
        this.quantity = quantity;
        this.quanCount = quanCount ;
    }

    public CartModel() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuanCount() {
        return quanCount;
    }

    public void setQuanCount(int quanCount) {
        this.quanCount = quanCount;
    }
}
