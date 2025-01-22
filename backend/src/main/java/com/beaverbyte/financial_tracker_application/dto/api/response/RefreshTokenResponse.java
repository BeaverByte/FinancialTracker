package com.beaverbyte.financial_tracker_application.dto.api.response;
import org.springframework.http.ResponseCookie;

public record RefreshTokenResponse( 
	MessageResponse messageResponse,
	ResponseCookie jwtCookie
) {}

