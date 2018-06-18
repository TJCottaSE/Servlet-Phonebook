package edu.asupoly.ser422.model;

public class Person {

	private String firstName;
	private String lastName;
	private String languages;
	private String days;
	private String hairColor;
	private String creator;
	private String id;
	
	public Person(){
		firstName = "";
		lastName = "";
		languages = "";
		days = "";
		hairColor = "";
	}
	
	public Person(String firstName, String lastName, String languages, String days, String hairColor, String creator, String id){
		this.firstName = firstName;
		this.lastName = lastName;
		this.languages = languages;
		this.days = days;
		this.hairColor = hairColor;
		this.creator = creator;
		this.id = id;
	}
	
	public String getID(){
		return id;
	}

	public void setID(String id){
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getLanguages() {
		return languages;
	}
	
	public void setLanguages(String languages) {
		this.languages = languages;
	}
	
	public String getDays() {
		return days;
	}
	
	public void setDays(String days) {
		this.days = days;
	}
	
	public String getHairColor() {
		return hairColor;
	}
	
	public void setHairColor(String hairColor) {
		this.hairColor = hairColor;
	}
	
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	@Override
	public String toString(){
		return firstName + " " + lastName + " Languages: " + languages + " Days: " + days + " Hair: " + hairColor + " ID: " + id + " created by " + creator; 
	}
}
