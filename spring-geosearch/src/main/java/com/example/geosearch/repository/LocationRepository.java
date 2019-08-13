package com.example.geosearch.repository;

import java.util.List;
import java.util.Optional;

import com.example.geosearch.model.Location;
import com.example.geosearch.model.LocationDistance;

import org.springframework.data.geo.Point;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface LocationRepository extends CrudRepository<Location, Long> {
    List<Location> findAll();

    Optional<Location> findById(Long id);

    // @Query(value = "SELECT id, prefecture_name, city_name, town_name, ST_AsText(geometric) as geometric, ST_Distance(ST_GeographyFromText(:point), geometric) as distance FROM Location WHERE ST_DWithin(geometric, ST_GeographyFromText(:point), :within) ORDER BY distance", nativeQuery = true)
    // List<LocationDistance> findLocationsWithin(@Param("point") String point, @Param("within") double within);

    // default List<LocationDistance> findLocationsWithinByPoint(Point point, double within) {
    //     return this.findLocationsWithin(String.format("SRID=4326;POINT(%f %f)", point.getX(), point.getY()), within);
    // }

    @Query(nativeQuery = true, name = "locationDistance")
    List<LocationDistance> findLocationsWithin(@Param("point") String point, @Param("within") double within);

    default List<LocationDistance> findLocationsWithinByPoint(Point point, double within) {
        return this.findLocationsWithin(String.format("SRID=4326;POINT(%f %f)", point.getX(), point.getY()), within);
    }
}
