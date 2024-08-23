package com.hotelmanagement.resource;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hotelmanagement.dto.CommanApiResponse;
import com.hotelmanagement.dto.UserLoginRequest;
import com.hotelmanagement.dto.UserLoginResponse;
import com.hotelmanagement.dto.UserRoleResponse;
import com.hotelmanagement.dto.UsersResponseDto;
import com.hotelmanagement.entity.User;
import com.hotelmanagement.service.CustomUserDetailsService;
import com.hotelmanagement.service.UserService;
import com.hotelmanagement.utility.Constants.ResponseCode;
import com.hotelmanagement.utility.Constants.Sex;
import com.hotelmanagement.utility.Constants.UserRole;
import com.hotelmanagement.utility.JwtUtil;

@Component
public class UserResource {

	Logger LOG = LoggerFactory.getLogger(UserResource.class);

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	public ResponseEntity<?> getAllUsers() {
		UserRoleResponse response = new UserRoleResponse();
		List<String> roles = new ArrayList<>();

		for (UserRole role : UserRole.values()) {
			roles.add(role.value());
		}

		if (roles.isEmpty()) {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Failed to Fetch User Roles");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		else {
			response.setRoles(roles);
			response.setResponseCode(ResponseCode.SUCCESS.value());
			response.setResponseMessage("User Roles Fetched success");
			return new ResponseEntity(response, HttpStatus.OK);
		}
	}

	public ResponseEntity<?> getAllUserGender() {
		UserRoleResponse response = new UserRoleResponse();
		List<String> genders = new ArrayList<>();

		for (Sex gender : Sex.values()) {
			genders.add(gender.value());
		}

		if (genders.isEmpty()) {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Failed to Fetch User Genders");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		else {
			response.setGenders(genders);
			response.setResponseCode(ResponseCode.SUCCESS.value());
			response.setResponseMessage("User Genders Fetched success");
			return new ResponseEntity(response, HttpStatus.OK);
		}
	}

	public ResponseEntity<?> register(User user) {
		LOG.info("Recieved request for User  register");

		CommanApiResponse response = new CommanApiResponse();
		String encodedPassword = passwordEncoder.encode(user.getPassword());

		user.setPassword(encodedPassword);

		User registerUser = userService.registerUser(user);

		if (registerUser != null) {
			response.setResponseCode(ResponseCode.SUCCESS.value());
			response.setResponseMessage(user.getRole() + " User Registered Successfully");
			return new ResponseEntity(response, HttpStatus.OK);
		}

		else {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Failed to Register " + user.getRole() + " User");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<?> login(UserLoginRequest userLoginRequest) {
		LOG.info("Recieved request for User Login");

		String jwtToken = null;
		UserLoginResponse useLoginResponse = new UserLoginResponse();
		User user = null;
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginRequest.getEmailId(),
					userLoginRequest.getPassword()));
		} catch (Exception ex) {
			LOG.error("Autthentication Failed!!!");
			useLoginResponse.setResponseCode(ResponseCode.FAILED.value());
			useLoginResponse.setResponseMessage("Failed to Login as " + userLoginRequest.getEmailId());
			return new ResponseEntity(useLoginResponse, HttpStatus.BAD_REQUEST);
		}

		UserDetails userDetails = customUserDetailsService.loadUserByUsername(userLoginRequest.getEmailId());

		for (GrantedAuthority grantedAuthory : userDetails.getAuthorities()) {
			if (grantedAuthory.getAuthority().equals(userLoginRequest.getRole())) {
				jwtToken = jwtUtil.generateToken(userDetails.getUsername());
			}
		}

		// user is authenticated
		if (jwtToken != null) {

			user = userService.getUserByEmailId(userLoginRequest.getEmailId());

			useLoginResponse = User.toUserLoginResponse(user);

			useLoginResponse.setResponseCode(ResponseCode.SUCCESS.value());
			useLoginResponse.setResponseMessage(user.getFirstName() + " logged in Successful");
			useLoginResponse.setJwtToken(jwtToken);
			return new ResponseEntity(useLoginResponse, HttpStatus.OK);

		}

		else {

			useLoginResponse.setResponseCode(ResponseCode.FAILED.value());
			useLoginResponse.setResponseMessage("Failed to Login as " + userLoginRequest.getEmailId());
			return new ResponseEntity(useLoginResponse, HttpStatus.BAD_REQUEST);
		}
	}

	public ResponseEntity<?> fetchAllHotelUsers() {
		UsersResponseDto response = new UsersResponseDto();

		List<User> users = userService.getUsersByRoleAndHotelId(UserRole.HOTEL.value(), 0);

		if (users == null || users.isEmpty()) {
			response.setResponseCode(ResponseCode.SUCCESS.value());
			response.setResponseMessage("No Users with Role Hotel found");
		}

		response.setUsers(users);
		response.setResponseCode(ResponseCode.SUCCESS.value());
		response.setResponseMessage("Hotel Users Fetched Successfully");

		return new ResponseEntity(response, HttpStatus.OK);
	}

	public ResponseEntity<?> fetchUser(int userId) {
		UsersResponseDto response = new UsersResponseDto();

		User user = userService.getUserById(userId);

		if (user == null) {
			response.setResponseCode(ResponseCode.SUCCESS.value());
			response.setResponseMessage("No User with this Id present");
		}

		response.setUser(user);
		response.setResponseCode(ResponseCode.SUCCESS.value());
		response.setResponseMessage("User Fetched Successfully");

		return new ResponseEntity(response, HttpStatus.OK);
	}

}
