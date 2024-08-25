/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Test.Product.Card.DTO;

import Test.Product.Card.Model.Category;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Fatema
 */
public class ProductDTO {
    
    private int id;    
    private String name;    
    private String brand;
    private String description;
    private Double price;
    private String imageUrl;
    private MultipartFile image;
    private int activeStatus;
    private Integer category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(int activeStatus) {
        this.activeStatus = activeStatus;
    }      

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    
    
}
