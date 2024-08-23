package com.hotelmanagement.resource;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.hotelmanagement.dto.CommanApiResponse;
import com.hotelmanagement.dto.LocationFetchResponse;
import com.hotelmanagement.entity.Location;
import com.hotelmanagement.service.LocationService;
import com.hotelmanagement.utility.Constants.ResponseCode;

@Component
public class LocationResource {

	Logger LOG = LoggerFactory.getLogger(LocationResource.class);

	@Autowired
	private LocationService locationService;

	public ResponseEntity<?> addLocation(Location location) {
		LOG.info("Recieved request for Add Location");

		CommanApiResponse response = new CommanApiResponse();

		Location addedLocation = locationService.addLocation(location);

		if (addedLocation != null) {
			response.setResponseCode(ResponseCode.SUCCESS.value());
			response.setResponseMessage("Location Added Successfully");
			return new ResponseEntity(response, HttpStatus.OK);
		}

		else {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Failed to add Location");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<?> fetchAllLocations() {
		LOG.info("Recieved request for Fetch Location");

		LocationFetchResponse locationFetchResponse = new LocationFetchResponse();

		List<Location> locations = locationService.fetchAllLocations();

		try {
			locationFetchResponse.setLocations(locations);
			locationFetchResponse.setResponseCode(ResponseCode.SUCCESS.value());
			locationFetchResponse.setResponseMessage("Location Fetched Successfully");

			return new ResponseEntity(locationFetchResponse, HttpStatus.OK);

		} catch (Exception e) {
			LOG.error("Exception Caught");
			locationFetchResponse.setResponseCode(ResponseCode.FAILED.value());
			locationFetchResponse.setResponseMessage("Failed to Fetch Location");
			return new ResponseEntity(locationFetchResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
