package com.hotelmanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotelmanagement.dao.HotelRoomDao;
import com.hotelmanagement.entity.HotelRoom;

@Service
public class HotelRoomService {

	@Autowired
	private HotelRoomDao hotelRoomDao;

	public HotelRoom addHotelRoom(HotelRoom room) {
		return hotelRoomDao.save(room);
	}

	public List<HotelRoom> addHotelRooms(List<HotelRoom> rooms) {
		return hotelRoomDao.saveAll(rooms);
	}
	
	public HotelRoom getRoomById(int roomId) {
		
		Optional<HotelRoom> optional = this.hotelRoomDao.findById(roomId);
		
		HotelRoom room = null;
		
		if(optional.isPresent()) {
			return optional.get();
		}
			
		return null;
		
	}

}
