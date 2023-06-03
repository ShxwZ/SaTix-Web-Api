package com.gabriel.dto;

public record VerifyRequest(String eventId, String code,String userId) {
}
