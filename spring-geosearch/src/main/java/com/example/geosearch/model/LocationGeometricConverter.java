package com.example.geosearch.model;

import javax.persistence.AttributeConverter;

import com.example.geosearch.model.Point;
import com.example.geosearch.repository.LocationRepository;

public class LocationGeometricConverter implements AttributeConverter<Point, String> {

    @Override
    public String convertToDatabaseColumn(Point attribute) {
        return String.format("SRID=4326;POINT(%f %f)", attribute.getLon(), attribute.getLat());
    }

    /**
     * POINT(longitude latitude)という書式の文字列をPoint型に変換します。
     *
     * Locationを返すLocationRepositoryクラスのメソッドがこれを呼び出します。
     * @see LocationRepository
     */
    @Override
    public Point convertToEntityAttribute(String dbData) {
        String[] values = dbData.replaceFirst(".*POINT\\(", "").replaceFirst("\\).*", "").split(" ");

        return new Point(Double.parseDouble(values[0]), Double.parseDouble(values[1]));
    }

}
