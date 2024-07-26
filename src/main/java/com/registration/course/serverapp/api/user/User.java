package com.registration.course.serverapp.api.user;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.registration.course.serverapp.api.member.Member;
import com.registration.course.serverapp.api.role.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @NotBlank(message = "username tidak boleh kosong")
  @Column(name = "username", nullable = false, unique = true)
  private String username;

  @NotBlank(message = "password tidak boleh kosong")
  @Column(name = "password", nullable = false)
  private String password;

  private boolean is_enabled = true;
  private boolean is_account_non_locked = true;

  @Column(name = "created_at")
  private Timestamp createdAt;

  @OneToOne
  @MapsId
  @JoinColumn(name = "user_id")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private Member member;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "tb_user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

}
