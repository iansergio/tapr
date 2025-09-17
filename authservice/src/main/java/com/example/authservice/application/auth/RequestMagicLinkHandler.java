package com.example.authservice.application.auth;

import com.example.authservice.domain.auth.MagicLinkRepository;
import com.example.authservice.domain.user.UserRepository;
import com.example.authservice.infrastructure.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestMagicLinkHandler {
    private final UserRepository userRepository;
    private final AppProperties appProperties;
    private final MagicLinkRepository magicLinkRepository;
}
