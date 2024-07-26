package com.registration.course.serverapp.api.dto.response;

import com.registration.course.serverapp.api.course.Course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class CoursesMember extends Course {
    private Boolean isRegistered;

    public void setCourse(Course course) {
        this.setId(course.getId());
        this.setThumbnail(course.getThumbnail());
        this.setTitle(course.getTitle());
        this.setInstructor(course.getInstructor());
        this.setDescription(course.getDescription());
        this.setPrice(course.getPrice());
        this.setPeriode(course.getPeriode());
        this.setStart(course.getStart());
        this.setEnd(course.getEnd());
        this.setIs_enabled(course.getIs_enabled());
        this.setCreatedAt(course.getCreatedAt());
        this.setCategory(course.getCategory());
    }
}
