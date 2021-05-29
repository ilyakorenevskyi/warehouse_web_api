package com.ilyakor.warehouse_web_api.resources;

import com.ilyakor.warehouse_web_api.MyUserDetailsService;
import com.ilyakor.warehouse_web_api.entities.AuthenticationRequest;
import com.ilyakor.warehouse_web_api.entities.AuthenticationResponse;
import com.ilyakor.warehouse_web_api.entities.User;
import com.ilyakor.warehouse_web_api.repositories.UserRepository;
import com.ilyakor.warehouse_web_api.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationResource {
    @Autowired
    private UserRepository repo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @PostMapping("authenticate/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername()
                    , authenticationRequest.getPassword()));
        }
        catch (BadCredentialsException e){
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            throw new Exception("Incorrect username or password",e);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("authenticate/register")
    public ResponseEntity<?> register(@RequestBody User user) throws Exception {
        System.out.println(user.toString());
        user.setAdmin(false);
        user.setSupervisor(false);
        user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(10)));
        if (repo.findByLogin(user.getLogin()).isPresent()) {
           return new ResponseEntity<String>("This login is already exist", HttpStatus.BAD_REQUEST);
        } else {
            repo.save(user);
            final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getLogin());
            final String jwt = jwtTokenUtil.generateToken(userDetails);
            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        }
    }
}
