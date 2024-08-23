package com.hotelmanagement.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotelmanagement.dto.HotelAddRequest;
import com.hotelmanagement.resource.HotelResource;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("api/hotel/")
@CrossOrigin(origins = "http://localhost:3000")
public class HotelController {

	Logger LOG = LoggerFactory.getLogger(HotelController.class);

	@Autowired
	private HotelResource hotelResource;

	@PostMapping("add")
	@ApiOperation(value = "Api to add hotel")
	public ResponseEntity<?> register(HotelAddRequest hotelAddRequest) {
		return this.hotelResource.addHotel(hotelAddRequest);
	}

	@GetMapping("id")
	@ApiOperation(value = "Api to fetch hotel by using Hotel Id")
	public ResponseEntity<?> fetchHotel(@RequestParam("hotelId") int hotelId) {
		return this.hotelResource.fetchHotel(hotelId);
	}

	@GetMapping("fetch")
	@ApiOperation(value = "Api to fetch all hotels")
	public ResponseEntity<?> fetchAllHotels() {
		return this.hotelResource.fetchAllHotels();
	}

	@GetMapping("location")
	@ApiOperation(value = "Api to fetch all hotels by using location Id")
	public ResponseEntity<?> getProductsByCategories(@RequestParam("locationId") int locationId) {
		return this.hotelResource.getProductsByCategories(locationId);

	}

	@GetMapping(value = "/{hotelImageName}", produces = "image/*")
	@ApiOperation(value = "Api to fetch hotel image by using image name")
	public void fetchProductImage(@PathVariable("hotelImageName") String hotelImageName, HttpServletResponse resp) {
		this.hotelResource.fetchProductImage(hotelImageName, resp);
	}

}
