package com.hotelmanagement.resource;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelmanagement.dto.CommanApiResponse;
import com.hotelmanagement.dto.HotelAddRequest;
import com.hotelmanagement.dto.HotelAddResponse;
import com.hotelmanagement.dto.HotelResponseDto;
import com.hotelmanagement.entity.Booking;
import com.hotelmanagement.entity.Hotel;
import com.hotelmanagement.entity.HotelRoom;
import com.hotelmanagement.entity.Location;
import com.hotelmanagement.entity.User;
import com.hotelmanagement.exception.HotelNotFoundException;
import com.hotelmanagement.service.BookingService;
import com.hotelmanagement.service.HotelRoomService;
import com.hotelmanagement.service.HotelService;
import com.hotelmanagement.service.LocationService;
import com.hotelmanagement.service.UserService;
import com.hotelmanagement.utility.Constants.BookingAvailableStatus;
import com.hotelmanagement.utility.Constants.BookingStatus;
import com.hotelmanagement.utility.Constants.BookingType;
import com.hotelmanagement.utility.Constants.ResponseCode;
import com.hotelmanagement.utility.StorageService;

@Component
public class HotelResource {

	Logger LOG = LoggerFactory.getLogger(HotelResource.class);

	@Autowired
	private HotelService hotelService;

	@Autowired
	private HotelRoomService hotelRoomService;

	@Autowired
	private LocationService locationService;

	@Autowired
	private StorageService storageService;

	@Autowired
	private UserService userService;

	@Autowired
	private BookingService bookingService;

	@Autowired
	private ObjectMapper objectMapper;

	public ResponseEntity<?> addHotel(HotelAddRequest hotelAddRequest) {
		LOG.info("Recieved request for Add Hotel");

		CommanApiResponse response = new CommanApiResponse();

		if (hotelAddRequest == null) {
			throw new HotelNotFoundException();
		}

		if (hotelAddRequest.getLocationId() == 0) {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Hotel Location is not selected");
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}

		if (hotelAddRequest.getUserId() == 0) {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Hotel Admin is not selected");
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}

		if (hotelAddRequest.getTotalRoom() == 0) {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Hotel Rooms not present");
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}

		Hotel hotel = HotelAddRequest.toEntity(hotelAddRequest);
		Location location = locationService.getLocationById(hotelAddRequest.getLocationId());
		hotel.setLocation(location);

		String image1 = storageService.store(hotelAddRequest.getImage1());
		String image2 = storageService.store(hotelAddRequest.getImage2());
		String image3 = storageService.store(hotelAddRequest.getImage3());
		hotel.setImage1(image1);
		hotel.setImage2(image2);
		hotel.setImage3(image3);

		int totalRoom = hotel.getTotalRoom();

		// hotel.setRooms(rooms);

		Hotel addedHotel = hotelService.addHotel(hotel);

		if (addedHotel != null) {

			List<HotelRoom> rooms = getHotelRoomsByTotalRoom(totalRoom, addedHotel);

			hotelRoomService.addHotelRooms(rooms);

			User hotelAdmin = userService.getUserById(hotelAddRequest.getUserId());
			hotelAdmin.setHotelId(addedHotel.getId());
			this.userService.updateUser(hotelAdmin);

			response.setResponseCode(ResponseCode.SUCCESS.value());
			response.setResponseMessage("Hotel Added Successfully");
			return new ResponseEntity(response, HttpStatus.OK);
		}

		else {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Failed to add Hotel");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<?> fetchHotel(int hotelId) {
		LOG.info("Recieved request for Fetch Hotel using hotel Id");

		HotelResponseDto response = new HotelResponseDto();

		Hotel hotel = hotelService.fetchHotel(hotelId);

		if (hotel == null) {
			throw new HotelNotFoundException();
		}

		HotelRoom room1 = hotel.getRooms().get(0); // by default we will show Room Booking Detail for Hotel Room 1

		LocalDate currentDate = LocalDate.now();

		Map<String, Map<String, String>> bookingStatusOfCurrentMonth = getHotelRoomBookingStatus(room1,
				LocalDate.now()); // current month booking status

		LocalDate nextMonth = currentDate.plusMonths(1);

		Map<String, Map<String, String>> bookingStatusOfNextMonth = getHotelRoomBookingStatus(room1, nextMonth); // next

		LocalDate nextToNextMonth = nextMonth.plusMonths(1);

		Map<String, Map<String, String>> bookingStatusOfNextToNexMonth = getHotelRoomBookingStatus(room1,
				nextToNextMonth); // next to next month booking status

		// Merge all three maps into a single map
		Map<String, Map<String, String>> all3MonthhotelRoom1BookingStatus = new HashMap<>();
		all3MonthhotelRoom1BookingStatus.putAll(bookingStatusOfCurrentMonth);
		all3MonthhotelRoom1BookingStatus.putAll(bookingStatusOfNextMonth);
		all3MonthhotelRoom1BookingStatus.putAll(bookingStatusOfNextToNexMonth);

		try {
			response.setHotel(hotel);
			response.setBookingStatus(all3MonthhotelRoom1BookingStatus);
			response.setResponseCode(ResponseCode.SUCCESS.value());
			response.setResponseMessage("Hotel Fetched Successfully");

			System.out.println(objectMapper.writeValueAsString(response));

			return new ResponseEntity(response, HttpStatus.OK);

		} catch (Exception e) {
			LOG.error("Exception Caught");
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Failed to Fetch Hotel");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	Map<String, Map<String, String>> getHotelRoomBookingStatus(HotelRoom hotelRoom, LocalDate date) {

		// August 2024
		Map<String, Map<String, String>> bookingStatusWithMonthYear = new LinkedHashMap<>();

		// Get the month from the LocalDate object
		Month month = date.getMonth();

		// Get the month name in title case
		String monthName = month.getDisplayName(TextStyle.FULL, Locale.ENGLISH);

		String dateInString = date.toString();

		System.out.println(date.toString());
		// 2024-08-10

		// month should be in format --> like 2024-08 yyyy-mm-
		// bcoz we will use like query here to fetch the booking status
		String likeQueryParam = dateInString.substring(0, 8); // 2024-08-

		// fetching month wise booked hotel rooms
		List<Booking> bookings = this.bookingService.getByHotelRoomAndCheckInAndTypeAndStatus(hotelRoom, likeQueryParam,
				BookingType.DATE_WISE.value(), BookingStatus.PAID.value());

		// Get the first date of the current month
		LocalDate firstDateOfMonth = date.withDayOfMonth(1);

		// Get the last date of the current month
		LocalDate lastDateOfMonth = date.withDayOfMonth(date.lengthOfMonth());

		Map<String, String> dayWiseBookingStatus = new LinkedHashMap<>();

		while (true) {

			String bookingStatus = BookingAvailableStatus.AVAILABLE.value();

			for (Booking booking : bookings) {
				if (booking.getCheckIn().equals(firstDateOfMonth.toString())) {
					bookingStatus = BookingAvailableStatus.BOOKED.value();
					break;
				}
			}

			dayWiseBookingStatus.put(String.valueOf(extractDay(firstDateOfMonth.toString())), bookingStatus);

			// break at month end date, because it covered all date with booking status
			if (firstDateOfMonth.equals(lastDateOfMonth))
				break;

			// Update firstDateOfMonth to the next day
			firstDateOfMonth = firstDateOfMonth.plusDays(1);

		}

		bookingStatusWithMonthYear.put(monthName + " " + date.getYear(), dayWiseBookingStatus);

		return bookingStatusWithMonthYear;
	}

	public List<HotelRoom> getHotelRoomsByTotalRoom(int totalRoom, Hotel hotel) {
		List<HotelRoom> rooms = new ArrayList<>();
		for (int i = 1; i <= totalRoom; i++) {
			HotelRoom room = new HotelRoom();
			room.setRoomNumber(String.valueOf(i));
			room.setHotel(hotel);
			rooms.add(room);
		}
		return rooms;
	}

	public int extractDay(String dateString) {
		// Split the date string by the hyphen separator
		String[] parts = dateString.split("-");

		// Get the day part from the second element of the array
		String dayPart = parts[2];

		// Remove leading zeros (if any) from the day part
		dayPart = dayPart.replaceFirst("^0+(?!$)", "");

		// Parse the day part to an integer
		return Integer.parseInt(dayPart);
	}

	public ResponseEntity<?> fetchAllHotels() {
		LOG.info("Recieved request for Fetch Hotels");

		HotelAddResponse hotelAddResponse = new HotelAddResponse();

		List<Hotel> hotels = hotelService.fetchAllHotels();
		try {
			hotelAddResponse.setHotels(hotels);
			hotelAddResponse.setResponseCode(ResponseCode.SUCCESS.value());
			hotelAddResponse.setResponseMessage("Hotels Fetched Successfully");
			return new ResponseEntity(hotelAddResponse, HttpStatus.OK);

		} catch (Exception e) {
			LOG.error("Exception Caught");
			hotelAddResponse.setResponseCode(ResponseCode.FAILED.value());
			hotelAddResponse.setResponseMessage("Failed to Fetch Hotels");
			return new ResponseEntity(hotelAddResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<?> getProductsByCategories(int locationId) {
		System.out.println("request came for getting all hotels by locations");

		HotelAddResponse hotelAddResponse = new HotelAddResponse();

		List<Hotel> hotels = new ArrayList<Hotel>();

		Location location = locationService.getLocationById(locationId);

		hotels = this.hotelService.fetchHotelsByLocation(location);

		try {
			hotelAddResponse.setHotels(hotels);
			hotelAddResponse.setResponseCode(ResponseCode.SUCCESS.value());
			hotelAddResponse.setResponseMessage("Hotels Fetched Successfully");
			return new ResponseEntity(hotelAddResponse, HttpStatus.OK);

		} catch (Exception e) {
			LOG.error("Exception Caught");
			hotelAddResponse.setResponseCode(ResponseCode.FAILED.value());
			hotelAddResponse.setResponseMessage("Failed to Fetch Hotels");
			return new ResponseEntity(hotelAddResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public void fetchProductImage(String hotelImageName, HttpServletResponse resp) {
		System.out.println("request came for fetching product pic");
		System.out.println("Loading file: " + hotelImageName);
		Resource resource = storageService.load(hotelImageName);
		if (resource != null) {
			try (InputStream in = resource.getInputStream()) {
				ServletOutputStream out = resp.getOutputStream();
				FileCopyUtils.copy(in, out);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("response sent!");
	}

}
