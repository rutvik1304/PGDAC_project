package com.hotelmanagement.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotelmanagement.dto.HotelFacilityAddRequest;
import com.hotelmanagement.entity.Facility;
import com.hotelmanagement.resource.FacilityResource;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("api/facility/")
@CrossOrigin(origins = "http://localhost:3000")
public class FacilityController {

	Logger LOG = LoggerFactory.getLogger(FacilityController.class);

	@Autowired
	private FacilityResource facilityResource;

	@ApiOperation(value = "Api to add facility")
	public ResponseEntity<?> addFacility(@RequestBody Facility facility) {
		return this.facilityResource.addFacility(facility);
	}

	@GetMapping("fetch")
	@ApiOperation(value = "Api to fetch all facilities")
	public ResponseEntity<?> fetchAllFacilities() {
		return this.facilityResource.fetchAllFacilities();
	}

	@GetMapping("/hotel")
	@ApiOperation(value = "Api to fetch all facilities of hotel by using hotel Id")
	public ResponseEntity<?> fetchAllFacilitiesByHotelId(@RequestParam("hotelId") int hotelId) {
		return this.facilityResource.fetchAllFacilitiesByHotelId(hotelId);
	}

	@PostMapping("/hotel/add")
	@ApiOperation(value = "Api to add facility to Hotel")
	public ResponseEntity<?> addHotelFacility(@RequestBody HotelFacilityAddRequest addFacility) {
		return this.facilityResource.addHotelFacility(addFacility);
	}

}
