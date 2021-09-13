package com.mj.taskagile.web.results;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

public class Result {
    
    private Result() {}

    public static ResponseEntity<ApiResult> created() {
        return ResponseEntity.status(HttpStatus.CREATED).build();
        // status created = 201
        // 요청 성공 처리, 자원 생성 알림
        // 일반적으로 POST 요청에 대한 응답결과로 사용
    }

    public static ResponseEntity<ApiResult> ok() {
        return ResponseEntity.ok().build();
    }

    public static ResponseEntity<ApiResult> ok(String message) {
        Assert.hasText(message, "Parameter `message` must not be blank");

        return ok(ApiResult.message(message));
    }

    public static ResponseEntity<ApiResult> ok(ApiResult payload) {
        return ResponseEntity.ok(payload);
    }

    public static ResponseEntity<ApiResult> failure(String message) {
        return ResponseEntity.badRequest().body(ApiResult.message(message));
    }
    
    public static ResponseEntity<ApiResult> serverError(String message, String errorReferenceCode) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResult.error(message, errorReferenceCode));
    }

    public static ResponseEntity<ApiResult> notFound() {
    return ResponseEntity.notFound().build();
    }

    public static ResponseEntity<ApiResult> unauthenticated() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public static ResponseEntity<ApiResult> forbidden() {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
