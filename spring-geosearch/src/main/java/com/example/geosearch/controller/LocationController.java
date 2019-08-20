package com.example.geosearch.controller;

import java.util.List;
import java.util.Optional;

import com.example.geosearch.model.Location;
import com.example.geosearch.model.LocationDistance;
import com.example.geosearch.repository.LocationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;

@RestController
public class LocationController {
    @Autowired
    private LocationRepository locationRepository;

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public void notFound() {
    }

    @RequestMapping("/location/{id}")
    public Location location(@PathVariable long id) throws Exception {
        Optional<Location> maybeLocation = locationRepository.findById(id);

        return maybeLocation.orElseThrow(() -> new NotFoundException(String.format("id = %d に該当するロケーションは存在しません。", id)));
    }

    @RequestMapping("/location")
    public List<Location> location(@RequestParam(value = "prefecture", required = true) String prefectureName, @RequestParam(value = "city", required = false) String cityName, @RequestParam(value = "town", required = false) String townName) {
        if (cityName != null && townName != null) {
            return locationRepository.findByPrefectureNameAndCityNameAndTownNameContaining(prefectureName, cityName, townName);
        } else if (cityName != null) {
            return locationRepository.findByPrefectureNameAndCityName(prefectureName, cityName);
        }

        return locationRepository.findByPrefectureName(prefectureName);
    }

    @RequestMapping("/distance")
    public List<LocationDistance> distance(@RequestParam(value = "lon") double longitude, @RequestParam(value = "lat") double latitude, @RequestParam(value = "within") double within) {
        Point point = new Point(longitude, latitude);
        return locationRepository.findLocationsWithinByPoint(point, within);
    }
}
