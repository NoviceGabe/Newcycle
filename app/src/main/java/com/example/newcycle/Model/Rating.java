package com.example.newcycle.Model;

public class Rating {
    private int fiveStar = 0;
    private int fourStar = 0;
    private int threeStar = 0;
    private int twoStar = 0;
    private int oneStar = 0;

    public Rating(int fiveStar, int fourStar, int threeStar, int twoStar, int oneStar){
        this.fiveStar = fiveStar;
        this.fourStar = fourStar;
        this.threeStar = threeStar;
        this.twoStar = twoStar;
        this.oneStar = oneStar;
    }

    public float getAverage(){
        int sum = fiveStar + fourStar + threeStar + twoStar + oneStar;
        float average = (5 * fiveStar + 4 * fourStar + 3 * threeStar + 2 * twoStar + 1 * oneStar) / sum;
        return average;
    }
}
