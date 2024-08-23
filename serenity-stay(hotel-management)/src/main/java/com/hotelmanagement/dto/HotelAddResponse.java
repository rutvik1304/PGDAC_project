package com.hotelmanagement.dto;

import java.util.List;

import com.hotelmanagement.entity.Hotel;

public class HotelAddResponse extends CommanApiResponse {

	List<Hotel> hotels;

	public List<Hotel> getHotels() {
		return hotels;
	}

	public void setHotels(List<Hotel> hotels) {
		this.hotels = hotels;
	}
	
	
	
}
