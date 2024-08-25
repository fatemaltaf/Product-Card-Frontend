/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Test.Product.Card.Controller;

//import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Locale;

@RestController
public class LocaleController {

    @Autowired
    private LocaleResolver localeResolver;

    @RequestMapping("/setLocale")
    public void setLocale(@RequestParam("lang") String lang, HttpServletRequest request, HttpServletResponse response) {
        Locale locale = new Locale(lang);
        localeResolver.setLocale(request, response, locale);
    }
}
