package com.hotelmanagement.dto;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class HotelRoomStatusResponse extends CommanApiResponse {

	// August 2024 2024-08-12 Booked/Available
	Map<String, Map<String, String>> bookingStatus = new LinkedHashMap<>();

	public Map<String, Map<String, String>> getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(Map<String, Map<String, String>> bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

}
