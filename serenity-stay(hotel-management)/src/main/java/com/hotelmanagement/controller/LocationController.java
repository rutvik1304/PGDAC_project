package com.hotelmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotelmanagement.entity.Location;
import com.hotelmanagement.resource.LocationResource;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("api/location/")
@CrossOrigin(origins = "http://localhost:3000")
public class LocationController {

	@Autowired
	private LocationResource locationResource;

	@PostMapping("add")
	@ApiOperation(value = "Api to add location")
	public ResponseEntity<?> register(@RequestBody Location location) {
		return this.locationResource.addLocation(location);
	}

	@GetMapping("fetch")
	@ApiOperation(value = "Api to fetch all locations")
	public ResponseEntity<?> fetchAllLocations() {

		return this.locationResource.fetchAllLocations();

	}

}
