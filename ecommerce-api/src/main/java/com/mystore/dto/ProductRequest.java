package com.mystore.dto;

//import lombok.Data;
//
//@Data
public class ProductRequest {
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    
    
    
    
    
	public ProductRequest() {
		super();
		// TODO Auto-generated constructor stub
	}





	public ProductRequest(String name, String description, Double price, Integer stock) {
		super();
		this.name = name;
		this.description = description;
		this.price = price;
		this.stock = stock;
	}





	public String getName() {
		return name;
	}





	public void setName(String name) {
		this.name = name;
	}





	public String getDescription() {
		return description;
	}





	public void setDescription(String description) {
		this.description = description;
	}





	public Double getPrice() {
		return price;
	}





	public void setPrice(Double price) {
		this.price = price;
	}





	public Integer getStock() {
		return stock;
	}





	public void setStock(Integer stock) {
		this.stock = stock;
	}
    
	
    
    
}
