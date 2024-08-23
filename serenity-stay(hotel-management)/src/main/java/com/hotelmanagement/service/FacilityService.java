package com.hotelmanagement.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotelmanagement.dao.FacilityDao;
import com.hotelmanagement.entity.Facility;

@Service
public class FacilityService {
	
	@Autowired
	private FacilityDao facilityDao;
	
	public Facility addFacility(Facility facility) {
		return facilityDao.save(facility);
	}
	
	public Set<Facility> fetchAllFacilities() {
		return new HashSet<Facility>(facilityDao.findAll());	
	}
	
	public Facility getFacilityById(int facilityId) {
		return facilityDao.findById(facilityId).get();
	}
 
}
