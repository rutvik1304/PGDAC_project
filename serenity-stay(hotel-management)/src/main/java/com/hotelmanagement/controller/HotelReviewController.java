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

import com.hotelmanagement.entity.HotelReview;
import com.hotelmanagement.resource.HotelReviewResource;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("api/hotel/review")
@CrossOrigin(origins = "http://localhost:3000")
public class HotelReviewController {

	Logger LOG = LoggerFactory.getLogger(HotelController.class);

	@Autowired
	private HotelReviewResource hotelReviewResource;

	@PostMapping("add")
	@ApiOperation(value = "Api to add hotel REVIEW")
	public ResponseEntity<?> register(@RequestBody HotelReview review) {
		return this.hotelReviewResource.addReview(review);
	}

	@GetMapping("fetch")
	@ApiOperation(value = "Api to fetch all reviews of hotel")
	public ResponseEntity<?> fetchHotelReview(@RequestParam("hotelId") int hotelId) {
		return this.hotelReviewResource.fetchHotelReview(hotelId);
	}

}
