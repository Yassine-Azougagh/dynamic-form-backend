package com.project.dynamicformbuilderbackend.dtos;

public record RefreshTokenRequest(String grantType, String refreshToken) {
}
