package com.beaverbyte.financial_tracker_application.dto.response;
import org.springframework.http.ResponseCookie;

public record RefreshTokenResponse( 
	MessageResponse messageResponse,
	ResponseCookie jwtCookie
) {}

