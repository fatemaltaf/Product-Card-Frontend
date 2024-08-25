/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Test.Product.Card.DTO;

import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Fatema
 */
public class CategoryResponse {

    private int status;    
    private int http_code;
    private Map<String, String> errors;
    private int activeStatus;
    private String message;
    private ArrayList<CategoryDTO> categoryList;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getHttp_code() {
        return http_code;
    }

    public void setHttp_code(int http_code) {
        this.http_code = http_code;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public int getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(int activeStatus) {
        this.activeStatus = activeStatus;
    }

    public ArrayList<CategoryDTO> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(ArrayList<CategoryDTO> categoryList) {
        this.categoryList = categoryList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
        
    
}
