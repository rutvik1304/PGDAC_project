package com.hotelmanagement.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotelmanagement.dto.UserLoginRequest;
import com.hotelmanagement.entity.User;
import com.hotelmanagement.resource.UserResource;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("api/user/")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

	Logger LOG = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserResource userResource;

	@GetMapping("roles")
	@ApiOperation(value = "Api to get all user roles")
	public ResponseEntity<?> getAllUsers() {
		return this.userResource.getAllUsers();
	}

	@GetMapping("gender")
	@ApiOperation(value = "Api to get all user gender")
	public ResponseEntity<?> getAllUserGender() {
		return this.userResource.getAllUserGender();
	}

	@PostMapping("register")
	@ApiOperation(value = "Api to register any User")
	public ResponseEntity<?> register(@RequestBody User user) {
		return this.userResource.register(user);
	}

	@PostMapping("login")
	@ApiOperation(value = "Api to login any User")
	public ResponseEntity<?> login(@RequestBody UserLoginRequest userLoginRequest) {
		return this.userResource.login(userLoginRequest);
	}

	@GetMapping("hotel")
	@ApiOperation(value = "Api to fetch all the Users whose Role is Hotel and not Admin of other Hotel")
	public ResponseEntity<?> fetchAllHotelUsers() {
		return this.userResource.fetchAllHotelUsers();
	}

	@GetMapping("id")
	@ApiOperation(value = "Api to fetch the User using user Id")
	public ResponseEntity<?> fetchUser(@RequestParam("userId") int userId) {
		return this.userResource.fetchUser(userId);
	}

}
