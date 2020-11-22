package com.meteor.pitchbooker.domain;

public enum AgeGrouping {
    //Originally called Grouping, but failed sql insert as Group was a reserved word!
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

    private final String ageGrouping;

    AgeGrouping(String ageGrouping) {
        this.ageGrouping = ageGrouping;
    }
    public String getAgeGrouping() {return ageGrouping;}

}
