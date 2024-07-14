package com.registration.course.serverapp.api.authentication;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.registration.course.serverapp.api.dto.request.LoginRequest;
import com.registration.course.serverapp.api.dto.request.UserRequest;
import com.registration.course.serverapp.api.dto.response.LoginResponse;
import com.registration.course.serverapp.api.jwt.JwtService;
import com.registration.course.serverapp.api.member.Member;
import com.registration.course.serverapp.api.member.MemberRepository;
import com.registration.course.serverapp.api.role.Role;
import com.registration.course.serverapp.api.role.RoleService;
import com.registration.course.serverapp.api.user.User;
import com.registration.course.serverapp.api.user.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  ModelMapper modelMapper;

  @Autowired
  RoleService roleService;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  AppUserDetailService appUserDetailService;

  private final JwtService jwtService;

  public User register(UserRequest userRequest) {
    Member member = modelMapper.map(userRequest, Member.class);
    User user = modelMapper.map(userRequest, User.class);

    member.setUser(user);
    user.setMember(member);

    if (userRepository.existsByUsername(userRequest.getUsername())) {
      throw new DataIntegrityViolationException("username");
    }

    if (memberRepository.existsByEmail(userRequest.getEmail())) {
      throw new DataIntegrityViolationException("email");

    }

    // set default role 1 = admin 2 = user
    List<Role> roles = new ArrayList<>();
    roles.add(roleService.getById(1));
    user.setRoles(roles);

    // set timestamp
    LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.systemDefault());
    Timestamp timestamp = Timestamp.valueOf(currentDateTime);
    user.setCreatedAt(timestamp);

    // set password with encoded
    user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

    return userRepository.save(user);
  }

  public LoginResponse login(LoginRequest loginRequest) { 

    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    User user = userRepository.findByUsernameOrMember_Email(loginRequest.getUsername(), loginRequest.getUsername())
        .get();

    UserDetails userDetails = appUserDetailService.loadUserByUsername(loginRequest.getUsername());

    List<String> authorities = userDetails.getAuthorities().stream().map(authority -> authority.getAuthority())
        .collect(Collectors.toList());

    var jwtToken = jwtService.generateToken(userDetails);
    // response => user detail = username, email, List<GrantedAuthority>
    return LoginResponse.builder()
        .username(user.getUsername())
        .email(user.getMember().getEmail())
        .authorities(authorities)
        .token(jwtToken)
        .build();
  }
}
