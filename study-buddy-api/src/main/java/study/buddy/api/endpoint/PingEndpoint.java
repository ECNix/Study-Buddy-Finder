package study.buddy.api.endpoint;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
public class PingEndpoint {
    @GetMapping("/ping")
    public String ping() {
        return "pong!!";
    }
    @GetMapping("/ding")
    public String ding() {
        return "dong!!";
    }

    //@CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/greet")
    public String greet(@RequestParam String stuName) {
        return "hello " + stuName + "!";
    }

    @GetMapping("/memory-ping")
    public String memoryPing() {
        return String.format("Max available memory: %.3f MB", (Runtime.getRuntime().maxMemory() / (1024.0 * 1024.0)));
    }
}