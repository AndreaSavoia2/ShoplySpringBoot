package com.project.shoply.service;

import com.project.shoply.entity.User;
import com.project.shoply.exception.GenericException;
import com.project.shoply.payload.request.LoginRequest;
import com.project.shoply.payload.request.RegistrationRequest;
import com.project.shoply.payload.response.AuthenticationResponse;
import com.project.shoply.repository.AuthorityRepository;
import com.project.shoply.repository.UserRepository;
import com.project.shoply.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final JwtService jwtService;
    private final CartService cartService;

    public String registerUser(RegistrationRequest registrationRequest) {
        String username = registrationRequest.getUsername().trim();
        String email = registrationRequest.getEmail().trim().toLowerCase();
        if (userRepository.existsByUsername(username))
            throw new GenericException("Username is already in use", HttpStatus.CONFLICT);

        if (userRepository.existsByEmail(email))
            throw new GenericException("Email is already in use", HttpStatus.CONFLICT);

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .authority(authorityRepository.findByAuthorityDefaultTrue())
                .email(email)
                .enabled(true)
                .build();
        userRepository.save(user);
        cartService.createCartForUser(user);

        return "User created";
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        String username = loginRequest.getUsername().trim();
        User user = findUserUsername(username);
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
            throw new BadCredentialsException("Bad credentials");
        String jwt = jwtService.generateToken(user, user.getId());
        long cartId = cartService.findCartById(user.getId()).getUser().getId();
        return AuthenticationResponse.builder()
                .id(user.getId())
                .username(username)
                .cartId(cartId)
                .token(jwt)
                .build();
    }

    protected User findUserUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Bad credentials"));
    }
}
