package com.beaverbyte.financial_tracker_application.dto.response;

import org.springframework.http.ResponseCookie;

public record LoginResponse(
		UserInfoResponse userInfoResponse,
		ResponseCookie jwtCookie,
		ResponseCookie jwtRefreshCookie) {
}
