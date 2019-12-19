package com.cybr406.echo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class ExampleController {

    @GetMapping("/")
    private String index() {
        return "index";
    }

    @RequestMapping("/echo/**")
    private String exampleSubmission(HttpServletRequest request, Model model) throws IOException {
        String responseDescription = RequestUtil.requestToString(request);
        model.addAttribute("responseDescription", responseDescription);
        return "index";
    }


}
