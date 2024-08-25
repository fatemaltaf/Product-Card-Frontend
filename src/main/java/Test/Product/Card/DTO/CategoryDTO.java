package Test.Product.Card.DTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Fatema
 */
public class CategoryDTO {
    
    private int id; 
    
    @NotBlank(message = "name is mandatory")
    @NotNull
    private String name;    
    
    @NotBlank(message = "description is mandatory")
    @NotNull
    private String description;
    
    private int activeStatus;

    public CategoryDTO() {
    }

    public CategoryDTO(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(int activeStatus) {
        this.activeStatus = activeStatus;
    }
        
}
