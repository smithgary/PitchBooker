package com.meteor.pitchbooker.domain;

public enum Group {
    UNDER_6("UNDER_6"),
    UNDER_8("UNDER_8"),
    UNDER_10("UNDER_10"),
    UNDER_12("UNDER_12"),
    UNDER_14("UNDER_14"),
    UNDER_16("UNDER_16"),
    UNDER_17("UNDER_17"),
    UNDER_18("UNDER_18"),
    UNDER_21("UNDER_21"),
    MINOR("MINOR"),
    JUNIOR("JUNIOR"),
    RESERVE("RESERVE"),
    SENIOR("SENIOR"),
    OVER_35("OVER_35"),
    SLOW("SLOW");

    private final String group;

    Group (String group) {
        this.group = group;
    }
    public String getGroup() {return group;}

}
