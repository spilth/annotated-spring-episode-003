package demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PagesController {

    @RequestMapping("/one")
    public String one() {
        return "one";
    }

    @RequestMapping("/two")
    public String two() {
        return "two";
    }
}
