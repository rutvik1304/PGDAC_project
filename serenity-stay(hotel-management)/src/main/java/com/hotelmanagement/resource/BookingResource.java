package com.hotelmanagement.resource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.hotelmanagement.dto.BookingDetailDto;
import com.hotelmanagement.dto.BookingDto;
import com.hotelmanagement.dto.CommanApiResponse;
import com.hotelmanagement.dto.HotelRoomStatusResponse;
import com.hotelmanagement.dto.UpdateBookingStatusRequestDto;
import com.hotelmanagement.entity.Booking;
import com.hotelmanagement.entity.Hotel;
import com.hotelmanagement.entity.HotelRoom;
import com.hotelmanagement.entity.User;
import com.hotelmanagement.exception.BookingNotFoundException;
import com.hotelmanagement.service.BookingService;
import com.hotelmanagement.service.HotelRoomService;
import com.hotelmanagement.service.HotelService;
import com.hotelmanagement.service.UserService;
import com.hotelmanagement.utility.Constants.BookingStatus;
import com.hotelmanagement.utility.Constants.BookingType;
import com.hotelmanagement.utility.Constants.ResponseCode;
import com.hotelmanagement.utility.Helper;

@Component
public class BookingResource {

	Logger LOG = LoggerFactory.getLogger(BookingResource.class);

	@Autowired
	private BookingService bookingService;

	@Autowired
	private HotelService hotelService;

	@Autowired
	private HotelRoomService hotelRoomService;

	@Autowired
	private UserService userService;

	@Autowired
	private HotelResource hotelResource;

	public ResponseEntity<?> addBooking(Booking booking) {
		CommanApiResponse response = new CommanApiResponse();

		if (booking == null) {
//		response.setResponseCode(ResponseCode.FAILED.value());
//		response.setResponseMessage("Hotel Booking Failed");
//		return new ResponseEntity(response, HttpStatus.BAD_REQUEST);

			throw new BookingNotFoundException();
		}

		if (booking.getUserId() == 0) {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("User is not not looged in");
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}

		if (booking.getHotelId() == 0) {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Hotel not found to Book");
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}

		if (booking.getHotelRoomId() == 0) {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Select Hotel Room");
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}

		if (booking.getTotalPerson() <= 0) {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Enter Valid Total Person Detail");
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}

		Hotel hotel = hotelService.fetchHotel(booking.getHotelId());

		if (hotel == null) {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("No Hotel present with this Id");
		}

		HotelRoom room = null;

		for (HotelRoom hotelRoom : hotel.getRooms()) {
			if (hotelRoom.getId() == booking.getHotelRoomId()) {
				room = hotelRoom;
			}
		}

		// Given start and end dates as strings in the format "YYYY-MM-DD"
		String startDateStr = booking.getCheckIn();
		String endDateStr = booking.getCheckOut();

		// Convert the strings to LocalDate objects
		LocalDate startDate = LocalDate.parse(startDateStr);
		LocalDate endDate = LocalDate.parse(endDateStr);

		// Get the list of dates between the start and end dates
		List<String> datesInRange = getDatesInRange(startDate, endDate);

		List<Booking> existingBookings = this.bookingService.getByHotelRoomAndCheckInInAndTypeAndStatus(room,
				datesInRange, BookingType.DATE_WISE.value(), BookingStatus.PAID.value());

		if (!CollectionUtils.isEmpty(existingBookings)) {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage(
					"Room No. " + room.getRoomNumber() + " is already booked on Selected Time Range!!!");
			return new ResponseEntity(response, HttpStatus.OK);
		}

		booking.setType(BookingType.NORMAL.value());
		booking.setRoom(room);
		booking.setStatus(BookingStatus.PENDING.value());
		booking.setTotalDay(datesInRange.size());

		booking.setBookingId(Helper.getAlphaNumericId());

		Booking bookedHotel = this.bookingService.bookHotel(booking); // type normal entry

		if (bookedHotel == null) {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Failed to Book Hotel");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		addDateWiseBooking(booking);
		response.setResponseCode(ResponseCode.SUCCESS.value());
		response.setResponseMessage("Hotel Booked Added Successfully, Please Check Approval Status on Booking Option");
		return new ResponseEntity(response, HttpStatus.OK);
	}

	public ResponseEntity<?> fetchAlBooking() {
		LOG.info("Recieved request for fetch all booking");

		BookingDetailDto response = new BookingDetailDto();

		List<BookingDto> bookings = new ArrayList<>();

		List<Booking> allBookings = this.bookingService.getByType(BookingType.NORMAL.value());

		for (Booking booking : allBookings) {

			BookingDto dto = new BookingDto();

			dto.setBookingId(booking.getBookingId());
			dto.setCheckIn(booking.getCheckIn());
			dto.setCheckOut(booking.getCheckOut());

			User customer = this.userService.getUserById(booking.getUserId());
			dto.setCustomerName(customer.getFirstName() + " " + customer.getLastName());

			Hotel hotel = this.hotelService.fetchHotel(booking.getHotelId());
			User hotelUser = this.userService.getUserById(hotel.getUserId());
			dto.setHotelEmail(hotelUser.getEmailId());
			dto.setHotelContact(hotelUser.getContact());
			dto.setHotelId(hotel.getId());
			dto.setStatus(booking.getStatus());
			dto.setTotalDay(booking.getTotalDay());
			dto.setUserId(customer.getId());
			dto.setHotelName(hotel.getName());
			dto.setHotelImage(hotel.getImage1());
			dto.setCustomerContact(customer.getContact());
			dto.setTotalAmount(String.valueOf(hotel.getPricePerDay() * booking.getTotalDay()));
			dto.setId(booking.getId());
			dto.setTotalPerson(booking.getTotalPerson());
			dto.setHotelRoomId(booking.getRoom().getRoomNumber());
			dto.setRoomPrice(hotel.getPricePerDay());
			dto.setId(booking.getId());
			dto.setTotalPerson(booking.getTotalPerson());

			bookings.add(dto);
		}

		response.setBookings(bookings);
		response.setResponseCode(ResponseCode.SUCCESS.value());
		response.setResponseMessage("Booking Fetched Successfully");
		return new ResponseEntity(response, HttpStatus.OK);
	}

	public ResponseEntity<?> fetchMyBooking(int userId) {
		LOG.info("Recieved request for fetch all booking");

		BookingDetailDto response = new BookingDetailDto();

		List<BookingDto> bookings = new ArrayList<>();

		List<Booking> allBookings = this.bookingService.getMyBookingsByType(userId, BookingType.NORMAL.value());

		for (Booking booking : allBookings) {

			BookingDto dto = new BookingDto();

			dto.setBookingId(booking.getBookingId());
			dto.setCheckIn(booking.getCheckIn());
			dto.setCheckOut(booking.getCheckOut());

			User customer = this.userService.getUserById(booking.getUserId());
			dto.setCustomerName(customer.getFirstName() + " " + customer.getLastName());

			// Given start and end dates as strings in the format "YYYY-MM-DD"
			String startDateStr = booking.getCheckIn();
			String endDateStr = booking.getCheckOut();

			// Convert the strings to LocalDate objects
			LocalDate startDate = LocalDate.parse(startDateStr);
			LocalDate endDate = LocalDate.parse(endDateStr);

			// Get the total number of days between the start and end dates, inclusive of
			// both start and end dates
			int totalDays = getTotalDaysInclusive(startDate, endDate);

			Hotel hotel = this.hotelService.fetchHotel(booking.getHotelId());
			User hotelUser = this.userService.getUserById(hotel.getUserId());
			dto.setHotelEmail(hotelUser.getEmailId());
			dto.setHotelContact(hotelUser.getContact());
			dto.setHotelId(hotel.getId());
			dto.setStatus(booking.getStatus());
			dto.setTotalDay(booking.getTotalDay());
			dto.setUserId(customer.getId());
			dto.setHotelName(hotel.getName());
			dto.setHotelImage(hotel.getImage1());
			dto.setCustomerContact(customer.getContact());
			dto.setTotalAmount(String.valueOf(hotel.getPricePerDay() * booking.getTotalDay()));
			dto.setId(booking.getId());
			dto.setTotalPerson(booking.getTotalPerson());
			dto.setHotelRoomId(booking.getRoom().getRoomNumber());
			dto.setRoomPrice(hotel.getPricePerDay());
			dto.setId(booking.getId());
			dto.setTotalPerson(booking.getTotalPerson());

			bookings.add(dto);
		}

		response.setBookings(bookings);
		response.setResponseCode(ResponseCode.SUCCESS.value());
		response.setResponseMessage("Booking Fetched Successfully");
		return new ResponseEntity(response, HttpStatus.OK);
	}

	public ResponseEntity<?> fetchBookingById(int bookingId) {
		LOG.info("Recieved request for fetch booking by Id");

		Booking booking = this.bookingService.getBookingById(bookingId);

		if (booking == null) {
			throw new BookingNotFoundException();
		}

		BookingDto dto = new BookingDto();

		dto.setBookingId(booking.getBookingId());
		dto.setCheckIn(booking.getCheckIn());
		dto.setCheckOut(booking.getCheckOut());

		User customer = this.userService.getUserById(booking.getUserId());
		dto.setCustomerName(customer.getFirstName() + " " + customer.getLastName());

		Hotel hotel = this.hotelService.fetchHotel(booking.getHotelId());
		User hotelUser = this.userService.getUserById(hotel.getUserId());
		dto.setHotelEmail(hotelUser.getEmailId());
		dto.setHotelContact(hotelUser.getContact());
		dto.setHotelId(hotel.getId());
		dto.setStatus(booking.getStatus());
		dto.setTotalDay(booking.getTotalDay());
		dto.setUserId(customer.getId());
		dto.setHotelName(hotel.getName());
		dto.setHotelImage(hotel.getImage1());
		dto.setCustomerContact(customer.getContact());
		dto.setTotalAmount(String.valueOf(hotel.getPricePerDay() * booking.getTotalDay()));
		dto.setId(booking.getId());
		dto.setTotalPerson(booking.getTotalPerson());
		dto.setHotelRoomId(booking.getRoom().getRoomNumber());
		dto.setRoomPrice(hotel.getPricePerDay());
		dto.setId(booking.getId());
		dto.setTotalPerson(booking.getTotalPerson());

		return new ResponseEntity(dto, HttpStatus.OK);
	}

	public ResponseEntity<?> fetchMyHotelBooking(int hotelId) {
		LOG.info("Recieved request for fetch all booking");

		BookingDetailDto response = new BookingDetailDto();

		List<BookingDto> bookings = new ArrayList<>();

		List<Booking> allBookings = this.bookingService.getByHotelIdAndType(hotelId, BookingType.NORMAL.value());

		for (Booking booking : allBookings) {

			BookingDto dto = new BookingDto();

			dto.setBookingId(booking.getBookingId());
			dto.setCheckIn(booking.getCheckIn());
			dto.setCheckOut(booking.getCheckOut());

			User customer = this.userService.getUserById(booking.getUserId());
			dto.setCustomerName(customer.getFirstName() + " " + customer.getLastName());

			Hotel hotel = this.hotelService.fetchHotel(booking.getHotelId());
			User hotelUser = this.userService.getUserById(hotel.getUserId());
			dto.setHotelEmail(hotelUser.getEmailId());
			dto.setHotelContact(hotelUser.getContact());
			dto.setHotelId(hotel.getId());
			dto.setStatus(booking.getStatus());
			dto.setTotalDay(booking.getTotalDay());
			dto.setUserId(customer.getId());
			dto.setHotelName(hotel.getName());
			dto.setHotelImage(hotel.getImage1());
			dto.setCustomerContact(customer.getContact());
			dto.setTotalAmount(String.valueOf(hotel.getPricePerDay() * booking.getTotalDay()));
			dto.setId(booking.getId());
			dto.setTotalPerson(booking.getTotalPerson());
			dto.setHotelRoomId(booking.getRoom().getRoomNumber());
			dto.setRoomPrice(hotel.getPricePerDay());
			dto.setId(booking.getId());
			dto.setTotalPerson(booking.getTotalPerson());

			bookings.add(dto);
		}

		response.setBookings(bookings);
		response.setResponseCode(ResponseCode.SUCCESS.value());
		response.setResponseMessage("Booking Fetched Successfully");
		return new ResponseEntity(response, HttpStatus.OK);
	}

	public ResponseEntity<?> fetchAllBookingStatus() {
		LOG.info("Recieved request for fetch all booking status");

		List<String> response = new ArrayList<>();

		for (BookingStatus status : BookingStatus.values()) {
			response.add(status.value());
		}

		return new ResponseEntity(response, HttpStatus.OK);
	}

	public ResponseEntity<?> updateHotelBookingStatus(UpdateBookingStatusRequestDto request) {
		LOG.info("Recieved request for updating the Hotel Booking Status");

		CommanApiResponse response = new CommanApiResponse();

		Booking b = this.bookingService.getBookingById(request.getBookingId());

		if (b == null) {
			throw new BookingNotFoundException();
		}

		if (request.getStatus().equals("") || request.getStatus() == null) {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Booking Status can not be empty");
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}

		b.setStatus(request.getStatus());
		this.bookingService.bookHotel(b);

		response.setResponseCode(ResponseCode.SUCCESS.value());
		response.setResponseMessage("Booking Status Updated");
		return new ResponseEntity(response, HttpStatus.OK);
	}

	public ResponseEntity<?> fetchHotelBookingStatus(Integer hotelRoomId) {
		LOG.info("Recieved request for fetch hotel room booking status");

		HotelRoomStatusResponse response = new HotelRoomStatusResponse();

		if (hotelRoomId == null) {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Hotel Room Not Selected!!!");
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}

		HotelRoom room = this.hotelRoomService.getRoomById(hotelRoomId);

		LocalDate currentDate = LocalDate.now();

		Map<String, Map<String, String>> bookingStatusOfCurrentMonth = this.hotelResource
				.getHotelRoomBookingStatus(room, LocalDate.now()); // current month booking status

		LocalDate nextMonth = currentDate.plusMonths(1);

		Map<String, Map<String, String>> bookingStatusOfNextMonth = this.hotelResource.getHotelRoomBookingStatus(room,
				nextMonth); // next month booking status

		LocalDate nextToNextMonth = nextMonth.plusMonths(1);

		Map<String, Map<String, String>> bookingStatusOfNextToNexMonth = this.hotelResource
				.getHotelRoomBookingStatus(room, nextToNextMonth); // next to next month booking status

		// Merge all three maps into a single map
		Map<String, Map<String, String>> all3MonthhotelRoom1BookingStatus = new HashMap<>();
		all3MonthhotelRoom1BookingStatus.putAll(bookingStatusOfCurrentMonth);
		all3MonthhotelRoom1BookingStatus.putAll(bookingStatusOfNextMonth);
		all3MonthhotelRoom1BookingStatus.putAll(bookingStatusOfNextToNexMonth);

		try {

			response.setBookingStatus(all3MonthhotelRoom1BookingStatus);
			response.setResponseCode(ResponseCode.SUCCESS.value());
			response.setResponseMessage("Hotel Fetched Successfully");

			return new ResponseEntity(response, HttpStatus.OK);

		} catch (Exception e) {
			LOG.error("Exception Caught");
		}

		return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity<?> payAndConfirm(Integer bookingId) {
		LOG.info("Recieved request for booking hotel");

		CommanApiResponse response = new CommanApiResponse();

		if (bookingId == null) {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Booking Id not found");
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}

		Booking booking = bookingService.getBookingById(bookingId); // Normal Booking

		if (!booking.getStatus().equals(BookingStatus.APPROVED.value())) {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Hotel Approval Pending");
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}

		List<Booking> datewiseBookings = this.bookingService.getByBookingIdAndType(booking.getBookingId(),
				BookingType.DATE_WISE.value()); // Datewise Booking

		boolean alreadyBooked = false;

		for (Booking dateBooking : datewiseBookings) {
			Booking existingPaidBookingForSameCheckIn = this.bookingService.findByHotelRoomAndCheckInAndTypeAndStatus(
					dateBooking.getRoom(), dateBooking.getCheckIn(), BookingType.DATE_WISE.value(),
					BookingStatus.PAID.value());

			dateBooking.setStatus(BookingStatus.PAID.value());

			if (existingPaidBookingForSameCheckIn != null) {
				alreadyBooked = true;
				break;
			}
		}

		if (alreadyBooked) {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Selected Room already Booked, please check room availabity and Book!!!");
			return new ResponseEntity(response, HttpStatus.OK);
		}

		booking.setStatus(BookingStatus.PAID.value());

		this.bookingService.bookHotel(booking);

		datewiseBookings.stream().forEach(x -> {
			x.setStatus(BookingStatus.PAID.value());
			this.bookingService.bookHotel(x);
		});

		response.setResponseCode(ResponseCode.SUCCESS.value());
		response.setResponseMessage("Congratulations, your payment is successful & Booking is Confirmed");
		return new ResponseEntity(response, HttpStatus.OK);
	}

	private void addDateWiseBooking(Booking booking) {

		// Given start and end dates as strings in the format "YYYY-MM-DD"
		String startDateStr = booking.getCheckIn();
		String endDateStr = booking.getCheckOut();

		// Convert the strings to LocalDate objects
		LocalDate startDate = LocalDate.parse(startDateStr);
		LocalDate endDate = LocalDate.parse(endDateStr);

		// Get the total number of days between the start and end dates, inclusive of
		// both start and end dates
		int totalDays = getTotalDaysInclusive(startDate, endDate);

		System.out.println("Total Days : " + totalDays);

		for (int i = 0; i < totalDays; i++) {

			Booking dateWiseBooking = new Booking();
			dateWiseBooking.setBookingId(booking.getBookingId());
			dateWiseBooking.setHotelId(booking.getHotelId());
			dateWiseBooking.setHotelRoomId(booking.getHotelRoomId());
			dateWiseBooking.setRoom(booking.getRoom());
			dateWiseBooking.setTotalPerson(booking.getTotalPerson());
			dateWiseBooking.setUserId(booking.getUserId());

			dateWiseBooking.setCheckIn(startDate.toString());
			dateWiseBooking.setCheckOut(startDate.toString());
			dateWiseBooking.setType(BookingType.DATE_WISE.value());
			dateWiseBooking.setStatus(BookingStatus.PENDING.value());

			this.bookingService.bookHotel(dateWiseBooking);

			startDate = startDate.plusDays(1);

		}

	}

	private static int getTotalDaysInclusive(LocalDate startDate, LocalDate endDate) {
		// Calculate the number of days between the start and end dates, including both
		// start and end dates
		return (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
	}

	public static List<String> getDatesInRange(LocalDate startDate, LocalDate endDate) {

		// Create a list to store the dates
		List<String> datesInRange = new ArrayList<>();

		// Define a date formatter for formatting LocalDate objects to strings
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		// Iterate over each date between the start and end dates, inclusive of both
		// start and end dates
		LocalDate currentDate = startDate;
		while (!currentDate.isAfter(endDate)) {
			// Add the current date to the list after formatting it to a string
			datesInRange.add(currentDate.format(formatter));
			// Move to the next date
			currentDate = currentDate.plusDays(1);
		}

		return datesInRange;
	}

}
