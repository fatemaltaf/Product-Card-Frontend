/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Test.Product.Card.Controller;

import Test.Product.Card.DTO.CategoryDTO;
import Test.Product.Card.DTO.CategoryRequest;
import Test.Product.Card.DTO.CategoryResponse;
import Test.Product.Card.DTO.ProductDTO;
import Test.Product.Card.DTO.ProductRequest;
import Test.Product.Card.DTO.ProductResponse;
import Test.Product.Card.Model.Category;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Fatema
 */
@Controller
public class ProductController {

    
    @RequestMapping(value = "/productForm", method = RequestMethod.GET)
public String getCategorySave(Model model) {
    model.addAttribute("requestDTO", new ProductDTO());

    // Prepare the CategoryRequest with pagination parameters
    CategoryRequest categoryRequest = new CategoryRequest();
    categoryRequest.setLimit(10);  // Set your limit
    categoryRequest.setOffset(0);  // Set your offset
    categoryRequest.setPage(0);    // Set your page number

    // Make the POST request to get categories
    String categoryApiUrl = "http://localhost:8089/api/categories/show";
    RestTemplate restTemplate = new RestTemplate();
    HttpEntity<CategoryRequest> requestEntity = new HttpEntity<>(categoryRequest);
    
    try {
        ResponseEntity<CategoryResponse> categoryResponseEntity = restTemplate.exchange(
                categoryApiUrl, HttpMethod.POST, requestEntity, CategoryResponse.class);

        // Check if the response body is not null and has a status of 200
        if (categoryResponseEntity.getStatusCode() == HttpStatus.OK && categoryResponseEntity.getBody() != null) {
            ArrayList<CategoryDTO> categories = categoryResponseEntity.getBody().getCategoryList();
            model.addAttribute("categories", categories);
        } else {
            // Handle the case where the response is null or the status is not 200
            model.addAttribute("error", "Failed to load categories.");
        }

    } catch (RestClientException e) {
        // Handle any exceptions during the API call
        model.addAttribute("error", "Error fetching categories: " + e.getMessage());
    }

    return "AddProduct";
}

    
    @RequestMapping(value = "/productAdd", method = RequestMethod.POST)
    public String addproduct(@ModelAttribute("requestDTO") @Valid ProductDTO requestDTO,
                         BindingResult bindingResult,
                         Model model,
                         HttpServletRequest request) {

        String apiUrl = "http://localhost:8089/api/products/save";
        
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("name", requestDTO.getName());
        body.add("brand", requestDTO.getBrand());
        body.add("description", requestDTO.getDescription());
        body.add("price", requestDTO.getPrice());
        body.add("activeStatus", requestDTO.getActiveStatus());
        body.add("category", requestDTO.getCategory());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<ProductResponse> responseEntity = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    ProductResponse.class);

            ProductResponse responseDTO = responseEntity.getBody();
            model.addAttribute("responseDTO", responseDTO);

            return "redirect:/productAll";

        } catch (HttpClientErrorException e) {
            // Log the error message from the API
            System.out.println("API error: " + e.getResponseBodyAsString());
            model.addAttribute("error", "Failed to add category: " + e.getResponseBodyAsString());

            return "redirect:/productAll";
        }
    }


         @RequestMapping(value = "/productAll", method = RequestMethod.GET)
            public String all(@Valid ProductRequest requestDTO,
                                BindingResult bindingResult,
                                Model model,
                                HttpServletRequest request) {

            if (requestDTO == null) {
                requestDTO = new ProductRequest();
            }

           String apiUrl = "http://localhost:8089/api/products/display";

           HttpEntity<ProductRequest> requestEntity = new HttpEntity<>(requestDTO);

           RestTemplate restTemplate = new RestTemplate();
            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
            ObjectMapper objectMapper = new ObjectMapper();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            objectMapper.setDateFormat(dateFormat);
            converter.setObjectMapper(objectMapper);
            restTemplate.getMessageConverters().add(0, converter);


           ResponseEntity<ProductResponse> responseEntity = restTemplate.exchange(
                   apiUrl,
                   HttpMethod.POST,
                   requestEntity,
                   ProductResponse.class);

           ProductResponse productResponse = responseEntity.getBody();

           model.addAttribute("productResponse", productResponse);


            model.addAttribute("requestDTO", requestDTO);

               return "ProductDetails";
            }      
            
        @RequestMapping(value = "/productDelete", method = RequestMethod.POST)
        public String deleteCategory(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
            String deleteApiUrl = "http://localhost:8089/api/products/delete";

            ProductDTO deleteRequest = new ProductDTO();
            deleteRequest.setId(id);
            HttpEntity<ProductDTO> requestEntity = new HttpEntity<>(deleteRequest);

            RestTemplate restTemplate = new RestTemplate();
            try {
                restTemplate.exchange(deleteApiUrl, HttpMethod.POST, requestEntity, Void.class);
                redirectAttributes.addFlashAttribute("message", "product deleted successfully!");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Error product category: " + e.getMessage());
            }

            return "redirect:/productAll";
        }
    
        @RequestMapping(value = "/productUpdate", method = RequestMethod.POST)
public String updateCategory(@RequestParam("id") int id,
                             @RequestParam("name") String name,
                             @RequestParam("description") String description,
                             @RequestParam("activeStatus") int activeStatus,
                             @RequestParam("brand") String brand,
                             @RequestParam("price") double price,
                             @RequestParam("category") Integer category,
                             Model model) {
//    model.addAttribute("newDTO", new ProductDTO());

    // Prepare the CategoryRequest with pagination parameters
    CategoryRequest categoryRequest = new CategoryRequest();
    categoryRequest.setLimit(10);  // Set your limit
    categoryRequest.setOffset(0);  // Set your offset
    categoryRequest.setPage(0);    // Set your page number
    
     ProductDTO requestDTO = new ProductDTO();
        requestDTO.setId(id);
        requestDTO.setName(name);
        requestDTO.setDescription(description);
        requestDTO.setActiveStatus(activeStatus);
        requestDTO.setPrice(price);
        requestDTO.setCategory(category);
        requestDTO.setBrand(brand);

        model.addAttribute("requestDTO", requestDTO);

    // Make the POST request to get categories
    String categoryApiUrl = "http://localhost:8089/api/categories/show";
    RestTemplate restTemplate = new RestTemplate();
    HttpEntity<CategoryRequest> requestEntity = new HttpEntity<>(categoryRequest);
    
    try {
        ResponseEntity<CategoryResponse> categoryResponseEntity = restTemplate.exchange(
                categoryApiUrl, HttpMethod.POST, requestEntity, CategoryResponse.class);

        // Check if the response body is not null and has a status of 200
        if (categoryResponseEntity.getStatusCode() == HttpStatus.OK && categoryResponseEntity.getBody() != null) {
            ArrayList<CategoryDTO> categories = categoryResponseEntity.getBody().getCategoryList();
            model.addAttribute("categories", categories);
        } else {
            // Handle the case where the response is null or the status is not 200
            model.addAttribute("error", "Failed to load categories.");
        }

    } catch (RestClientException e) {
        // Handle any exceptions during the API call
        model.addAttribute("error", "Error fetching categories: " + e.getMessage());
    }

    return "EditProducts";
}
        
//    @RequestMapping(value = "/productUpdate", method = RequestMethod.POST)
//    public String updateCategory(@RequestParam("id") int id,
//                             @RequestParam("name") String name,
//                             @RequestParam("description") String description,
//                             @RequestParam("activeStatus") int activeStatus,
//                             @RequestParam("brand") String brand,
//                             @RequestParam("price") double price,
//                             @RequestParam("category") Integer category,
//                             Model model) {
//        ProductDTO requestDTO = new ProductDTO();
//        requestDTO.setId(id);
//        requestDTO.setName(name);
//        requestDTO.setDescription(description);
//        requestDTO.setActiveStatus(activeStatus);
//        requestDTO.setPrice(price);
//        requestDTO.setCategory(category);
//        requestDTO.setBrand(brand);
//
//        model.addAttribute("requestDTO", requestDTO);
//
//        return "EditProducts";
//    }

    @RequestMapping(value = "/productSave", method = RequestMethod.POST)
public String saveProduct(@ModelAttribute("requestDTO") @Valid ProductDTO requestDTO,
                          BindingResult bindingResult,
                          Model model,
                          HttpServletRequest request) {

    String apiUrl = "http://localhost:8089/api/products/update";

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("id", requestDTO.getId());
    body.add("name", requestDTO.getName());
    body.add("brand", requestDTO.getBrand());
    body.add("description", requestDTO.getDescription());
    body.add("price", requestDTO.getPrice());
    body.add("activeStatus", requestDTO.getActiveStatus());
    body.add("category", requestDTO.getCategory());

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

    RestTemplate restTemplate = new RestTemplate();

    try {
        ResponseEntity<ProductResponse> responseEntity = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                ProductResponse.class);

        ProductResponse responseDTO = responseEntity.getBody();
        model.addAttribute("responseDTO", responseDTO);

        return "redirect:/productAll";

    } catch (HttpClientErrorException e) {
        System.out.println("API error: " + e.getResponseBodyAsString());
        model.addAttribute("error", "Failed to save product: " + e.getResponseBodyAsString());

        return "redirect:/productAll";
    }
}

       
}