package com.registration.course.serverapp.api.authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.registration.course.serverapp.api.user.User;
import com.registration.course.serverapp.api.user.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AppUserDetailService implements UserDetailsService {

  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsernameOrMember_Email(username, username)
        .orElseThrow(() -> new UsernameNotFoundException("username atau email tidak ditemukan"));
    return new AppUserDetail(user);
  }

}
