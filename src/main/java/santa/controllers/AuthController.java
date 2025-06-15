package santa.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import santa.dtos.*;
import santa.entities.RefreshToken;
import santa.entities.User;
import santa.exceptions.EmailAlreadyUsedException;
import santa.exceptions.UserNotFoundException;
import santa.exceptions.UserNotVerifiedException;
import santa.jwt.CustomUserDetails;
import santa.jwt.JwtTokenProvider;
import santa.repositories.UserRepository;
import santa.services.ConfirmationSenderService;
import santa.services.ConfirmationService;
import santa.services.RefreshTokenService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final ConfirmationSenderService confirmationSenderService;
    private final ConfirmationService confirmationService;

    @Operation(summary = "Create user")
    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.email())) {
            throw new EmailAlreadyUsedException(signUpRequest.email());
        }
        User user = new User();
        user.setEmail(signUpRequest.email());
        user.setPasswordHash(passwordEncoder.encode(signUpRequest.password()));
        user.setDisplayName(signUpRequest.displayName());
        userRepository.save(user);

        confirmationSenderService.sendConfirmation(user);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Resend confirmation code")
    @PostMapping("/resend")
    public ResponseEntity<Void> resendConfirmationCode(@RequestBody ResendConfirmationCodeRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException(request.email()));
        confirmationSenderService.sendConfirmation(user);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Confirm email")
    @PostMapping("/confirm")
    public ResponseEntity<Void> confirmEmail(@RequestBody ConfirmMailRequest request) {
        confirmationService.confirm(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Login user")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new UserNotFoundException(userDetails.getId()));
        if (!user.isVerified()) {
            throw new UserNotVerifiedException(user.getId());
        }

        String jwt = tokenProvider.generateToken(authentication);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        LoginResponse response = new LoginResponse(jwt, refreshToken.getToken());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Logout user")
    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUser(@RequestBody RefreshTokenDto refreshTokenDto) {
        refreshTokenService.deleteByToken(refreshTokenDto.token());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Refresh access token")
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(@Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        RefreshToken refreshToken = refreshTokenService.verifyExpiration(
                refreshTokenService.findByToken(refreshTokenDto.token())
        );
        String token = tokenProvider.generateTokenFromUserId(refreshToken.getUser().getId());
        LoginResponse response = new LoginResponse(token, refreshTokenDto.token());
        return ResponseEntity.ok(response);
    }
}
