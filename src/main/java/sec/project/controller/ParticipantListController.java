package sec.project.controller;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.config.CustomUserDetailsService;
import sec.project.repository.SignupRepository;

@Controller
public class ParticipantListController {
    
    @Autowired
    private SignupRepository signupRepository;
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    @RequestMapping("/participants")
    public String participantList(Model model) {
        model.addAttribute("list", signupRepository.findAll());
        return "participants";
    }
    
    @RequestMapping(value = "/password", method = RequestMethod.GET)
    public String loadPasswordForm(){
        return "password";
    }
    
    @RequestMapping(value = "/password", method = RequestMethod.POST)
    public String submitPasswordForm(@RequestParam String password, @RequestParam String confirm){
        User user = (User) userDetailsService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        BCryptPasswordEncoder b = new BCryptPasswordEncoder();
        if(Objects.equals(password, confirm)){
            userDetailsService.changeUserPassword(user.getUsername(), b.encode(password));
            return "confirmed";
        } else {
            return "nomatch";
        }
    }    
}
