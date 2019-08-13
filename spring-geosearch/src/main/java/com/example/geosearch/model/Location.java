package com.example.geosearch.model;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

@Entity
@Table(name = "location")
@NamedNativeQuery(
    name = "locationDistance",
    resultClass = LocationDistance.class,
    resultSetMapping = "findLocationsWithinMapping",
    query = "SELECT id, prefecture_name, city_name, town_name, ST_AsText(geometric) as geometric, ST_Distance(ST_GeographyFromText(:point), geometric) as distance FROM Location WHERE ST_DWithin(geometric, ST_GeographyFromText(:point), :within) ORDER BY distance"
)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "findLocationsWithinMapping",
        classes = @ConstructorResult(
            targetClass = LocationDistance.class,
            columns = {
                @ColumnResult(name = "id", type = long.class),
                @ColumnResult(name = "prefecture_name", type = String.class),
                @ColumnResult(name = "city_name", type = String.class),
                @ColumnResult(name = "town_name", type = String.class),
                @ColumnResult(name = "geometric", type = String.class),
                @ColumnResult(name = "distance", type = Double.class)
            }
        )
    )
})
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "prefecture_name")
    private String prefectureName;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "town_name")
    private String townName;

    @Column(name = "geometric", columnDefinition = "GEOGRAPHY(Point,4326)")
    // Spring Data JPAが自動生成するクエリで必要geography型をPOINT(longitude, latitude)の書式にしないと、
    // LocationGeometricConverter.convertToEntityAttribute(String dbData)がパースできない
    @Formula("ST_AsText(geometric)")
    @Convert(converter = LocationGeometricConverter.class)
    private Point geometric;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrefectureName() {
        return prefectureName;
    }

    public void setPrefectureName(String prefectureName) {
        this.prefectureName = prefectureName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public Point getGeometric() {
        return geometric;
    }

    public void setGeometric(Point geometric) {
        this.geometric = geometric;
    }
}
