package com.hotelmanagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotelmanagement.entity.HotelRoom;

@Repository
public interface HotelRoomDao extends JpaRepository<HotelRoom, Integer> {

}
