package hu.webuni.loginmicroservice.web;

import hu.webuni.loginmicroservice.dto.LoginDTO;
import hu.webuni.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;



    @PostMapping()
    public String login(@RequestBody LoginDTO loginDTO) {
        log.debug(loginDTO.toString());

        UserDetails principal = null;
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUserName(), loginDTO.getPassword()));
            principal = (UserDetails) authentication.getPrincipal();

        return jwtService.createJwTToken(principal);
    }
}
