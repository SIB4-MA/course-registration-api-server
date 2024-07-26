package com.registration.course.serverapp.api.dto.response;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class ResponseData<T> {
  private boolean status;
  private List<String> messages = new ArrayList<>();
  private List<T> payload = new ArrayList<>();
}

