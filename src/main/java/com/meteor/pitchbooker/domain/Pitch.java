package com.meteor.pitchbooker.domain;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Pitch {
 private Long id;
 private String name;
 private Club club;

 @Id
 @GeneratedValue
 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 @ManyToOne
 public Club getClub(){return club;}
 public void setClub(Club club){this.club = club;}
}
