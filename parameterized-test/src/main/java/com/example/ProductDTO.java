package com.example;

public class ProductDTO {
    String name;
    String price;
    String usedPrice;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Product { ");
        sb.append("name = ");
        sb.append(name);
        sb.append(", price = ");
        sb.append(price);
        sb.append(", used price = ");
        sb.append(" }");
        return sb.toString();
    }
}
