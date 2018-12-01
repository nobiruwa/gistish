package com.example;

public class Product {
    public Name name;
    public OfferSummary offerSummary;

    public Product(Name name, OfferSummary offerSummary) {
        this.name = name;
        this.offerSummary = offerSummary;
    }

    public ProductDTO represent() {
        return new ProductDTO();
    }
}
