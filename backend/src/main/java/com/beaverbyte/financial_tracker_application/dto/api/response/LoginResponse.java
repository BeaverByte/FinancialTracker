package com.beaverbyte.financial_tracker_application.dto.api.response;

import org.springframework.http.ResponseCookie;

public record LoginResponse( 
	UserInfoResponse userInfoResponse,
	ResponseCookie jwtCookie,
	ResponseCookie jwtRefreshCookie
) {}
