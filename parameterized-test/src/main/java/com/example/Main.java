package com.example;

public class Main {
    public Asin parseArgs(String[] args) {
        return new Asin(args[0]);
    }

    public void print(Product product) {
        StringBuilder builder = new StringBuilder();
        ProductDTO productDTO = product.represent();
        builder.append("cheapest: ");
        builder.append(productDTO);
        builder.append(".");
        String line = builder.toString();
        System.out.println(line);
    }

    public static void main(String[] args) {
        Main main = new Main();
        Asin asin = main.parseArgs(args);
        AmazonMarketPlace place = new AmazonMarketPlace();
        ProductList productList = place.find(asin);
        Product product = productList.cheapest();
        main.print(product);
    }
}
