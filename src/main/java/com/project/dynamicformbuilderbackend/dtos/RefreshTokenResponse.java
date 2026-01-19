package com.project.dynamicformbuilderbackend.dtos;

public record RefreshTokenResponse(String id_token, String refresh_token, String expires_in ) {
}
