package com.hotelmanagement.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hotelmanagement.entity.Booking;
import com.hotelmanagement.entity.HotelRoom;

@Repository
public interface BookingDao extends JpaRepository<Booking, Integer> {

	List<Booking> findByUserId(int userId);

	List<Booking> findByHotelId(int hotelId);

	List<Booking> findByUserIdAndType(int userId, String type);

	@Query("SELECT b FROM Booking b WHERE b.room = :room AND b.checkIn LIKE CONCAT(:checkInDate, '%') AND b.type = :type AND b.status = :status")
	List<Booking> findByRoomAndCheckInLikeAndTypeAndStatus(@Param("room") HotelRoom room, @Param("checkInDate") String checkInDate, @Param("type") String type, @Param("status") String status);


	@Query("SELECT b FROM Booking b WHERE b.room = :room AND b.checkIn IN :checkInDates AND b.type = :type AND b.status = :status")
	List<Booking> findByRoomAndCheckInInAndTypeAndStatus(@Param("room") HotelRoom room,
			@Param("checkInDates") List<String> checkInDates, @Param("type") String type,
			@Param("status") String status);
	
	List<Booking> findByType(String type);
	
	List<Booking> findByHotelIdAndType(int hotelId, String type);
	
	List<Booking> findByBookingIdAndType(String bookingId, String type);
	
	@Query("SELECT b FROM Booking b WHERE b.room = :room AND b.checkIn = :checkIn AND b.type = :type AND b.status = :status")
	Booking findByRoomAndCheckInAndTypeAndStatus(@Param("room") HotelRoom room,@Param("checkIn") String checkIn, String type, String status);

}
