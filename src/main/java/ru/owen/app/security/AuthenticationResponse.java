package ru.owen.app.security;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.*;
import ru.owen.app.model.Customer;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private String jwtToken;
    @JsonUnwrapped
    private Customer customer;
    private String role;
}
