/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Test.Product.Card.Controller;

import Test.Product.Card.DTO.CategoryDTO;
import Test.Product.Card.DTO.CategoryRequest;
import Test.Product.Card.DTO.CategoryResponse;
import Test.Product.Card.Model.Category;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Fatema
 */
@Controller
public class CategoryController {
    
    @Autowired
    private LocaleResolver localeResolver;

    @RequestMapping(value = "/categoryForm", method = RequestMethod.GET)
    public String getCategorySave(Model model) {
        
        model.addAttribute("requestDTO", new CategoryDTO());
        CategoryResponse responseDTO = new CategoryResponse();
        model.addAttribute("responseDTO",responseDTO);
        return "AddCategories";
    }
    
    @RequestMapping(value = "/categoryAdd", method = RequestMethod.POST)
    public String addproduct(@ModelAttribute("requestDTO") @Valid CategoryDTO requestDTO,
                         BindingResult bindingResult,
                         Model model,
                         HttpServletRequest request) {

        String apiUrl = "http://localhost:8089/api/categories/save";

        HttpEntity<CategoryDTO> requestEntity = new HttpEntity<>(requestDTO);

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<CategoryResponse> responseEntity = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    CategoryResponse.class);

            CategoryResponse responseDTO = responseEntity.getBody();
            model.addAttribute("responseDTO", responseDTO);

            return "redirect:/categoryAll";

        } catch (HttpClientErrorException e) {
            // Log the error message from the API
            System.out.println("API error: " + e.getResponseBodyAsString());
            model.addAttribute("error", "Failed to add category: " + e.getResponseBodyAsString());

            return "redirect:/categoryAll";
        }
    }


        @RequestMapping(value = "/categoryAll", method = RequestMethod.GET)
            public String all(@Valid CategoryRequest requestDTO,
                                BindingResult bindingResult,
                                Model model,
                                HttpServletRequest request) {

            if (requestDTO == null) {
                requestDTO = new CategoryRequest();
            }

           String apiUrl = "http://localhost:8089/api/categories/show";

           HttpEntity<CategoryRequest> requestEntity = new HttpEntity<>(requestDTO);

           RestTemplate restTemplate = new RestTemplate();
            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
            ObjectMapper objectMapper = new ObjectMapper();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            objectMapper.setDateFormat(dateFormat);
            converter.setObjectMapper(objectMapper);
            restTemplate.getMessageConverters().add(0, converter);


           ResponseEntity<CategoryResponse> responseEntity = restTemplate.exchange(
                   apiUrl,
                   HttpMethod.POST,
                   requestEntity,
                   CategoryResponse.class);

           CategoryResponse categoryResponse = responseEntity.getBody();

           model.addAttribute("categoryResponse", categoryResponse);


            model.addAttribute("requestDTO", requestDTO);

               return "CategoriesDetails";
            }    
            
        @RequestMapping(value = "/categoryDelete", method = RequestMethod.POST)
        public String deleteCategory(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
            String deleteApiUrl = "http://localhost:8089/api/categories/delete";

            CategoryDTO deleteRequest = new CategoryDTO();
            deleteRequest.setId(id);
            HttpEntity<CategoryDTO> requestEntity = new HttpEntity<>(deleteRequest);

            RestTemplate restTemplate = new RestTemplate();
            try {
                restTemplate.exchange(deleteApiUrl, HttpMethod.POST, requestEntity, Void.class);
                redirectAttributes.addFlashAttribute("message", "Category deleted successfully!");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Error deleting category: " + e.getMessage());
            }

            return "redirect:/categoryAll";
        }
    
    @RequestMapping(value = "/categoryUpdate", method = RequestMethod.POST)
    public String updateCategory(@RequestParam("id") int id,
                             @RequestParam("name") String name,
                             @RequestParam("description") String description,
                             @RequestParam("activeStatus") int activeStatus,
                             Model model) {
        CategoryDTO categoryRequest = new CategoryDTO();
        categoryRequest.setId(id);
        categoryRequest.setName(name);
        categoryRequest.setDescription(description);
        categoryRequest.setActiveStatus(activeStatus);

        model.addAttribute("categoryRequest", categoryRequest);

        return "EditCategories";
    }

    @RequestMapping(value = "/categorySave", method = RequestMethod.POST)
    public String update(@ModelAttribute("requestDTO") @Valid CategoryDTO requestDTO,
                             BindingResult bindingResult,
                             Model model,
                             HttpServletRequest request) {

        String apiUrl = "http://localhost:8089/api/categories/update";

        HttpEntity<CategoryDTO> requestEntity = new HttpEntity<>(requestDTO);

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<CategoryResponse> responseEntity = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    CategoryResponse.class);

            CategoryResponse responseDTO = responseEntity.getBody();
            model.addAttribute("responseDTO", responseDTO);

            return "redirect:/categoryAll";

        } catch (HttpClientErrorException e) {
            System.out.println("API error: " + e.getResponseBodyAsString());
            model.addAttribute("error", "Failed to add category: " + e.getResponseBodyAsString());

            return "redirect:/categoryAll";
        }
    }
       
}
