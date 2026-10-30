package demo.controller;

import demo.service.DemoService;
import org.springframework.stereotype.Controller;

@Controller
public class DemoController {

    private final DemoService demoService;

    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }
}
