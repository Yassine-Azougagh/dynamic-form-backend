package com.project.dynamicformbuilderbackend.dtos;

import lombok.Builder;

@Builder
public record LoginResponseDto(String token, String refreshToken, int expiresIn, String username) {
}
