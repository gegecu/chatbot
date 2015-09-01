import java.time.LocalTime;


public class Restaurant {
	private String cuisine;
	private Double averagePrice;
	private String place;
	private Integer averageServingTime;
	private String name;
	private boolean suggested;
	
	public Restaurant(String cuisine, Double averagePrice, String place, Integer averageServingTime, String name) {
		this.cuisine = cuisine;
		this.averagePrice = averagePrice;
		this.place = place;
		this.averageServingTime = averageServingTime;
		this.name = name;
		this.suggested = false;
	}
	
	public boolean isSuggested() {
		return suggested;
	}

	public void setSuggested(boolean suggested) {
		this.suggested = suggested;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCuisine() {
		return cuisine;
	}
	public void setCuisine(String cuisine) {
		this.cuisine = cuisine;
	}
	public Double getAveragePrice() {
		return averagePrice;
	}
	public void setAveragePrice(Double averagePrice) {
		this.averagePrice = averagePrice;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public Integer getAverageServingTime() {
		return averageServingTime;
	}
	public void setAverageServingTime(Integer averageServingTime) {
		this.averageServingTime = averageServingTime;
	}
	
	public Boolean match(String cuisine, Double price, String place, Integer duration) {
		
		if(this.cuisine.equalsIgnoreCase(cuisine) && this.averagePrice.equals(price)
				&& this.place.equalsIgnoreCase(place) && duration.equals(this.averageServingTime) && !this.suggested) {
			//System.out.println(this.averagePrice + ", " + this.cuisine + ", " + this.averageServingTime + ", " + this.place);
			return true;
		}
		else {
			return false;
		}
	}

}
