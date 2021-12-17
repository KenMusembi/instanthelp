package com.example.instanthelp;

public class Categories {

    String categoryID, categoryName;

     public Categories() {
         //this constructor is required
     }

    public Categories(String categoryID, String categoryName) {
         this.categoryID= categoryID;
         this.categoryName = categoryName;
    }

    //getters for the Category class
    public String getCategoryID(){
         return categoryID;
    }

    public String getCategoryName(){
         return categoryName;
    }

}
