package com.example.dcproject.controller;

import com.example.dcproject.service.FcOnlineService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/")
@AllArgsConstructor
public class FcOnlineController {

    private final FcOnlineService fcService;
    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("/fconline/fc")
    public void fcOnline() {

    }

    @PostMapping("/fconline/fc")
    public void fcOnline(Model model, String word) {
        try {
            fcService.user(model, word);
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @RequestMapping(value="/fconline/match", method=RequestMethod.GET)
    public String match(Model model, String word, HttpServletRequest request) {
        try {
            fcService.match(model, word, request);
        } catch (Exception exception) {
            System.out.println(exception);
            exception.printStackTrace();
        }
        return "/fconline/match";
    }

    @RequestMapping(value="/fconline/matchDetail", method=RequestMethod.GET)
    public String matchDetail(@RequestParam("jsonData") String jsonData,
                              @RequestParam("word")String word, Model model,HttpServletRequest request) {
        try {
            fcService.matchDetail(jsonData, word, model, request);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "/fconline/matchDetail";
    }

    @RequestMapping(value="/fconline/user", method=RequestMethod.GET)
    public void user() {

    }
}
