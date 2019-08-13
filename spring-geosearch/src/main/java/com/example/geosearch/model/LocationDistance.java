package com.example.geosearch.model;

public class LocationDistance extends Location {
    private Double distance;

    public LocationDistance() {
    }

    /**
     * SqlResultMappingの@ConstructorResultとマッチするよう、
     * カラム型と互換性のあるJavaの型を受け取るコンストラクターです。
     */
    public LocationDistance(
        long id,
        String prefectureName,
        String cityName,
        String townName,
        String geometric,
        Double distance
    ) {
        this.setId(id);
        this.setPrefectureName(prefectureName);
        this.setCityName(cityName);
        this.setTownName(townName);

        String[] values = geometric.replaceFirst(".*POINT\\(", "").replaceFirst("\\).*", "").split(" ");

        this.setGeometric(new Point(Double.parseDouble(values[0]), Double.parseDouble(values[1])));
        this.distance = distance;
    }

    public Double getDistance() {
        return distance;
    }
    public void setDistance(Double distance) {
        this.distance = distance;
    }

}
