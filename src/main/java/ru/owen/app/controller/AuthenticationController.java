package ru.owen.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.owen.app.dto.CustomerDTO;
import ru.owen.app.security.AuthenticationRequest;
import ru.owen.app.service.CustomerService;

@RestController
public class AuthenticationController {

    private final CustomerService customerService;

    public AuthenticationController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CustomerDTO customerDTO) {
        customerService.register(customerDTO);
        return ResponseEntity.ok("Confirm your email!");
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String confirm) {
        customerService.verify(confirm);
        return ResponseEntity.ok("Customer has been registered");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        return customerService.authenticate(authenticationRequest);
    }
}
