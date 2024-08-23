package com.hotelmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotelmanagement.dto.UpdateBookingStatusRequestDto;
import com.hotelmanagement.entity.Booking;
import com.hotelmanagement.resource.BookingResource;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("api/book/hotel")
@CrossOrigin(origins = "http://localhost:3000")
public class BookingController {

	@Autowired
	private BookingResource bookingResource;

	@PostMapping("/")
	@ApiOperation(value = "Api to book hotel")
	public ResponseEntity<?> register(Booking booking) {
		return this.bookingResource.addBooking(booking);
	}

	@GetMapping("/fetch/all")
	@ApiOperation(value = "Api to fetch all booked hotel for Admin")
	public ResponseEntity<?> fetchAllHotelBooking() {
		return this.bookingResource.fetchAlBooking();
	}

	@GetMapping("/fetch")
	@ApiOperation(value = "Api to fetch customer booked hotel")
	public ResponseEntity<?> fetchMyBooking(@RequestParam("userId") int userId) {
		return this.bookingResource.fetchMyBooking(userId);
	}

	@GetMapping("/fetch/id")
	@ApiOperation(value = "Api to fetch booking by booking Id")
	public ResponseEntity<?> fetchBookingById(@RequestParam("bookingId") int bookingId) {
		return this.bookingResource.fetchBookingById(bookingId);
	}

	@GetMapping("/fetch/bookings")
	@ApiOperation(value = "Api to fetch all hotel booking for Hotel Manager")
	public ResponseEntity<?> fetchMyHotelBooking(@RequestParam("hotelId") int hotelId) {
		return this.bookingResource.fetchMyHotelBooking(hotelId);
	}

	@GetMapping("/fetch/status")
	@ApiOperation(value = "Api to fetch all booking status")
	public ResponseEntity<?> fetchAllBookingStatus() {
		return this.bookingResource.fetchAllBookingStatus();
	}

	@PostMapping("/update/status")
	@ApiOperation(value = "Api to update the booking status")
	public ResponseEntity<?> updateHotelBookingStatus(@RequestBody UpdateBookingStatusRequestDto request) {
		return this.bookingResource.updateHotelBookingStatus(request);
	}

	@GetMapping("/fetch/hotel/room/booking/status")
	@ApiOperation(value = "Api to fetch hotel room booking status")
	public ResponseEntity<?> fetchHotelBookingStatus(@RequestParam("hotelRoomId") Integer hotelRoomId) {
		return this.bookingResource.fetchHotelBookingStatus(hotelRoomId);
	}

	@PostMapping("/payment/confirmation")
	@ApiOperation(value = "Api to pay for booking and confirm")
	public ResponseEntity<?> payAndConfirm(@RequestParam("bookingId") Integer bookingId) {
		return this.bookingResource.payAndConfirm(bookingId);
	}

}
