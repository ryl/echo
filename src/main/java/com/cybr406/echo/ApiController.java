package com.cybr406.echo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class ApiController {

    @RequestMapping("/api/echo/**")
    public String echo(HttpServletRequest request) throws IOException {
        return RequestUtil.requestToString(request);
    }

}
