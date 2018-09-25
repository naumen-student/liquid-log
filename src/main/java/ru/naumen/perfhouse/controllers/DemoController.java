package ru.naumen.perfhouse.controllers;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Created by doki on 23.10.16.
 */
@Controller
public class DemoController
{
    @RequestMapping(path = "/demo")
    public ModelAndView index()
    {
        return new ModelAndView("demo", new HashMap<>(), HttpStatus.OK);
    }

    @RequestMapping(path = "/demo/upload", method = RequestMethod.POST)
    public String upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes)
    {
        return "redirect:/demo/result";
    }

    @RequestMapping(path = "/demo/result", method = RequestMethod.GET)
    public ModelAndView demoResult(RedirectAttributes redirectAttributes)
    {
        return new ModelAndView("demo_result", new HashMap<>(), HttpStatus.OK);
    }
}
