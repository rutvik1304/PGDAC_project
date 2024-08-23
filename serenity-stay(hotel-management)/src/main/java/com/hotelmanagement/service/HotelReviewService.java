package com.hotelmanagement.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hotelmanagement.dao.HotelReviewDao;
import com.hotelmanagement.entity.HotelReview;

@Service
public class HotelReviewService {
	
	@Autowired
	private HotelReviewDao hotelReviewDao;
	
	public HotelReview addHotelReview(HotelReview review) {
		return hotelReviewDao.save(review);
	}
	
	public List<HotelReview> fetchHotelReviews(int hotelId) {
		return hotelReviewDao.findByHotelId(hotelId);
	}

}
