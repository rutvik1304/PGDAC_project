package com.hotelmanagement.dto;

import java.util.HashMap;
import java.util.Map;

import com.hotelmanagement.entity.Hotel;

public class HotelResponseDto extends CommanApiResponse {

	private Hotel hotel;

	Map<String, Map<String, String>> bookingStatus = new HashMap<>();

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	public Map<String, Map<String, String>> getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(Map<String, Map<String, String>> bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

}
