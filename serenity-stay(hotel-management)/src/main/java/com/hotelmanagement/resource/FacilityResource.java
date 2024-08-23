package com.hotelmanagement.resource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.hotelmanagement.dto.CommanApiResponse;
import com.hotelmanagement.dto.FacilityFetchResponse;
import com.hotelmanagement.dto.HotelFacilityAddRequest;
import com.hotelmanagement.entity.Facility;
import com.hotelmanagement.entity.HotelFacility;
import com.hotelmanagement.service.FacilityService;
import com.hotelmanagement.service.HotelFacilityService;
import com.hotelmanagement.service.HotelService;
import com.hotelmanagement.utility.Constants.ResponseCode;

@Component
public class FacilityResource {

	Logger LOG = LoggerFactory.getLogger(FacilityResource.class);

	@Autowired
	private FacilityService facilityService;

	@Autowired
	private HotelService hotelService;

	@Autowired
	private HotelFacilityService hotelFacilityService;

	public ResponseEntity<?> addFacility(Facility facility) {
		LOG.info("Recieved request for Add Facility");

		CommanApiResponse response = new CommanApiResponse();

		Facility addedFacility = facilityService.addFacility(facility);

		if (addedFacility != null) {
			response.setResponseCode(ResponseCode.SUCCESS.value());
			response.setResponseMessage("Facility Added Successfully");
			return new ResponseEntity(response, HttpStatus.OK);
		}

		else {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Failed to add Facility");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<?> fetchAllFacilities() {
		LOG.info("Recieved request for Fetch Facility");

		FacilityFetchResponse facilityFetchResponse = new FacilityFetchResponse();

		Set<Facility> facilities = facilityService.fetchAllFacilities();

		try {
			facilityFetchResponse.setFacilities(facilities);
			facilityFetchResponse.setResponseCode(ResponseCode.SUCCESS.value());
			facilityFetchResponse.setResponseMessage("Facilities Fetched Successfully");
			return new ResponseEntity(facilityFetchResponse, HttpStatus.OK);

		} catch (Exception e) {
			LOG.error("Exception Caught");
			facilityFetchResponse.setResponseCode(ResponseCode.FAILED.value());
			facilityFetchResponse.setResponseMessage("Failed to Fetch Facility");
			return new ResponseEntity(facilityFetchResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<?> fetchAllFacilitiesByHotelId(int hotelId) {
		LOG.info("Recieved request for Fetch Facility");

		FacilityFetchResponse facilityFetchResponse = new FacilityFetchResponse();

		List<HotelFacility> hotelFacilities = this.hotelFacilityService.getHotelFacilitiesByHotelId(hotelId);

		Set<Facility> facilities = new HashSet<>();

		for (HotelFacility hotelFacility : hotelFacilities) {
			facilities.add(this.facilityService.getFacilityById(hotelFacility.getFacilityId()));
		}

		try {
			facilityFetchResponse.setFacilities(facilities);
			facilityFetchResponse.setResponseCode(ResponseCode.SUCCESS.value());
			facilityFetchResponse.setResponseMessage("Facilities Fetched Successfully");

			System.out.println(facilityFetchResponse);

			return new ResponseEntity(facilityFetchResponse, HttpStatus.OK);

		} catch (Exception e) {
			LOG.error("Exception Caught");
			facilityFetchResponse.setResponseCode(ResponseCode.FAILED.value());
			facilityFetchResponse.setResponseMessage("Failed to Fetch Facility");
			return new ResponseEntity(facilityFetchResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<?> addHotelFacility(HotelFacilityAddRequest addFacility) {
		LOG.info("Recieved request for Add Facility");

		CommanApiResponse response = new CommanApiResponse();

		Facility facility = facilityService.getFacilityById(addFacility.getFacilityId());

		if (facility == null) {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Facility not found");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		List<HotelFacility> hotelFacilities = this.hotelFacilityService
				.getHotelFacilitiesByHotelId(addFacility.getHotelId());

		Set<Facility> facilities = new HashSet<>();

		for (HotelFacility hotelFacility : hotelFacilities) {
			if (hotelFacility.getFacilityId() == facility.getId()) {
				response.setResponseCode(ResponseCode.FAILED.value());
				response.setResponseMessage("Facility already added");
				return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
			}
		}

		HotelFacility hotelFacility = new HotelFacility();
		hotelFacility.setHotelId(addFacility.getHotelId());
		hotelFacility.setFacilityId(addFacility.getFacilityId());

		hotelFacilities.add(hotelFacility);

		HotelFacility addedHotelFacility = hotelFacilityService.addFacility(hotelFacility);

		if (addedHotelFacility != null) {
			response.setResponseCode(ResponseCode.SUCCESS.value());
			response.setResponseMessage("Hotel Facility Added Successfully");
			return new ResponseEntity(response, HttpStatus.OK);
		}

		else {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Failed to add Hotel Facility");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
