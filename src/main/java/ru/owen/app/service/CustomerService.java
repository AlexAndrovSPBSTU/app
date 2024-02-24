package ru.owen.app.service;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.owen.app.constants.ProjectConstants;
import ru.owen.app.dto.CustomerDTO;
import ru.owen.app.model.Customer;
import ru.owen.app.repository.CustomerRepository;
import ru.owen.app.security.AuthenticationRequest;
import ru.owen.app.security.AuthenticationResponse;

import java.util.Date;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
//    private final EmailSenderService emailSenderService;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, AuthenticationManager authenticationManager,
                           PasswordEncoder passwordEncoder/*, EmailSenderService emailSenderService*/) {
        this.customerRepository = customerRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
//        this.emailSenderService = emailSenderService;
    }

    public ResponseEntity<?> authenticate(AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = null;
        if (null != authentication) {
            jwt = Jwts.builder().setIssuer(ProjectConstants.JWT_ISSUER).setSubject(ProjectConstants.SUBJECT)
                    .claim(ProjectConstants.EMAIL, authentication.getName())
                    .claim(ProjectConstants.ROLE, authentication.getAuthorities().iterator().next().getAuthority())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + ProjectConstants.ONE_DAY))
                    .signWith(ProjectConstants.KEY).compact();
        }
        return ResponseEntity.ok(AuthenticationResponse
                .builder()
                .customer(customerRepository.findByEmail(authentication.getName()).get())
                .role(customerRepository.findByEmail(authentication.getName()).get().getRole())
                .jwtToken(jwt).build()
        );
    }

    public void verify(String confirm) {
//        if (ProjectConstants.verifiers.containsKey(confirm)) {
            CustomerDTO customerDTO = ProjectConstants.verifiers.get(confirm);
            customerRepository.save(Customer.builder()
                    .email(customerDTO.getEmail())
                    .password(passwordEncoder.encode(customerDTO.getPassword()))
                    .registrationDate(new Date())
                    .role(ProjectConstants.USER_ROLE)
                    .build());
//            ProjectConstants.verifiers.remove(confirm);
//        }
    }

    public void register(CustomerDTO customerDTO) {
//        String hash = String.valueOf(customerDTO.getEmail().hashCode());
//        ProjectConstants.verifiers.put(hash, customerDTO);
//        emailSenderService.sendEmail(customerDTO.getEmail(), hash);

        customerRepository.save(Customer.builder()
                .email(customerDTO.getEmail())
                .password(passwordEncoder.encode(customerDTO.getPassword()))
                .registrationDate(new Date())
                .role(ProjectConstants.USER_ROLE)
                .build());
    }
}
