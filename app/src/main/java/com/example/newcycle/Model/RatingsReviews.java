package com.example.newcycle.Model;

import java.io.Serializable;

public class RatingsReviews implements Serializable, Comparable<RatingsReviews> {
    private String name;
    private int rating;
    private String review;
    private String date;

    public RatingsReviews(String name, int rating, String review, String date){
        this.name = name;
        this.rating = rating;
        this.review = review;
        this.date = date;
    }

    public String getName(){
        return name;
    }

    public int getRating(){
        return rating;
    }

    public String getReview(){
        return review;
    }

    public String getDate(){
        return date;
    }

    @Override
    public int compareTo(RatingsReviews o) {
        return String.valueOf(rating).compareTo(String.valueOf(o.rating));
    }
}
