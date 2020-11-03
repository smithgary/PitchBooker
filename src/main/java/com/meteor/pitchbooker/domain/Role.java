package com.meteor.pitchbooker.domain;

public enum  Role {
    COACH("COACH"),
    PLAYER("PLAYER"),
    PARENT("PARENT"),
    MANAGER("MANAGER"),
    PUBIC_RELATIONS_OFFICER("PRO"),
    SECRETARY("SECRETARY"),
    CHILD_PROTECTION_OFFICER("CHILD_PROTECTION_OFFICER"),
    TREASURER("TREASURER"),
    CLUB_MEMBER("CLUB_MEMBER"),
    REFEREE("REFEREE"),
    CHAIRMAN("CHAIRMAN"),
    INTERESTED_PARTY("INTERESTED_PARTY");

    private String role;

    private Role(String role) { this.role = role;}
    public String getRole() {return role;}
}
