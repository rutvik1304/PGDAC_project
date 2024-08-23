package com.hotelmanagement.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.beans.BeanUtils;

import com.hotelmanagement.dto.FacilityFetchResponse;

@Entity
public class Facility {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;

	private String description;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static FacilityFetchResponse toFacilityFetchResponse(Facility facility) {
		FacilityFetchResponse facilityFetchResponse = new FacilityFetchResponse();
		BeanUtils.copyProperties(facility, facilityFetchResponse);
		return facilityFetchResponse;
	}

}
