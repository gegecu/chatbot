/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.time.LocalTime;

/**
 *
 * @author ShenJingBing
 */
public class Human {
    private Boolean hungry;
    private String cuisine;
    private Float budget;
    private String place;
    private LocalTime duration;
    
    public Human() {
        this.hungry = null;
        this.cuisine = null;
        this.budget = null;
        this.place = null;
        this.duration = null;
        
    }

    public Boolean isHungry() {
        return hungry;
    }

    public void setHungry(boolean hungry) {
        this.hungry = hungry;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public Float getBudget() {
        return budget;
    }

    public void setBudget(float budget) {
        this.budget = budget;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public LocalTime getDuration() {
        return duration;
    }

    public void setDuration(LocalTime duration) {
        this.duration = duration;
    }
    
    
    
}
