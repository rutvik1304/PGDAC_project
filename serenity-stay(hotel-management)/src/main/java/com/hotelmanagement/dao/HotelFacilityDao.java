package com.hotelmanagement.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hotelmanagement.entity.HotelFacility;

@Repository
public interface HotelFacilityDao extends JpaRepository<HotelFacility, Integer> {
	
	List<HotelFacility> findByHotelId(int hotelId);

}
