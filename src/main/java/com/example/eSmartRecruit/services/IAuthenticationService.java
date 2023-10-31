package com.example.eSmartRecruit.services;

import com.example.eSmartRecruit.authentication.AuthenticationRequest;
import com.example.eSmartRecruit.authentication.request_reponse.AuthenticationResponse;

public interface IAuthenticationService {
    public AuthenticationResponse authenticate(AuthenticationRequest request);
}
