package study.buddy.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

// Used for testing
@RestController
public class HomeController {

    @RequestMapping("/")
    @GetMapping("/")
    public @ResponseBody String homeTest() {
        return "This is a test";
    }

}