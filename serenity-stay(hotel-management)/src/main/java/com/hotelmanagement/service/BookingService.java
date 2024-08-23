package com.hotelmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotelmanagement.dao.BookingDao;
import com.hotelmanagement.entity.Booking;
import com.hotelmanagement.entity.HotelRoom;

@Service
public class BookingService {

	@Autowired
	private BookingDao bookingDao;

	public Booking bookHotel(Booking booking) {
		return bookingDao.save(booking);
	}

	public List<Booking> getAllBookings() {
		return bookingDao.findAll();
	}

	public List<Booking> getMyBookings(int userId) {
		return bookingDao.findByUserId(userId);
	}

	public List<Booking> getMyHotelBookings(int hotelId) {
		return bookingDao.findByHotelId(hotelId);
	}

	public Booking getBookingById(int bookingId) {
		return bookingDao.findById(bookingId).get();
	}

	public List<Booking> getMyBookingsByType(int userId, String type) {
		return bookingDao.findByUserIdAndType(userId, type);
	}

	public List<Booking> getByHotelRoomAndCheckInAndTypeAndStatus(HotelRoom room, String checkInDate, String type,
			String status) {
		return bookingDao.findByRoomAndCheckInLikeAndTypeAndStatus(room, checkInDate, type, status);
	}

	public List<Booking> getByHotelRoomAndCheckInInAndTypeAndStatus(HotelRoom room, List<String> checkInDate,
			String type, String status) {
		return bookingDao.findByRoomAndCheckInInAndTypeAndStatus(room, checkInDate, type, status);
	}

	public List<Booking> getByType(String type) {
		return bookingDao.findByType(type);
	}

	public List<Booking> getByHotelIdAndType(int hotelId, String type) {
		return bookingDao.findByHotelIdAndType(hotelId, type);
	}

	public List<Booking> getByBookingIdAndType(String bookingId, String type) {
		return bookingDao.findByBookingIdAndType(bookingId, type);
	}

	public Booking findByHotelRoomAndCheckInAndTypeAndStatus(HotelRoom room, String bookingId, String type, String status) {
		return bookingDao.findByRoomAndCheckInAndTypeAndStatus(room, bookingId, type, status);
	}

}
