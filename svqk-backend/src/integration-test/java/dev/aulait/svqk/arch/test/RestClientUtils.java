package dev.aulait.svqk.arch.test;

import static jakarta.ws.rs.core.Response.Status.*;

import dev.aulait.svqk.arch.exception.ErrorResponseDto;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Locale;

public class RestClientUtils {

  public static RequestSpecification given() {
    return RestAssured.given()
        .baseUri(TestConfig.getInstance().getBaseUrl())
        .contentType("application/json; charset=UTF-8")
        .header("Accept-Language", Locale.getDefault().toString().replace("_", "-"))
        .filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
  }

  public static <T> T get(String path, Class<T> responseType, Object... pathParams) {
    return as(
        given().get(path, pathParams).then().statusCode(OK.getStatusCode()).extract(),
        responseType);
  }

  public static ErrorResponseDto getWithError(String path, Object... pathParams) {
    return given().get(path, pathParams).then().extract().as(ErrorResponseDto.class);
  }

  public static <T> T post(
      String path, Object requestBody, Class<T> responseType, Object... pathParams) {
    return as(
        given()
            .body(requestBody)
            .post(path, pathParams)
            .then()
            .statusCode(OK.getStatusCode())
            .extract(),
        responseType);
  }

  public static ConstraintViolationResponseDto postWithBadRequest(
      String path, Object requestBody, Object... pathParams) {
    return given()
        .body(requestBody)
        .post(path, pathParams)
        .then()
        .statusCode(BAD_REQUEST.getStatusCode())
        .extract()
        .as(ConstraintViolationResponseDto.class);
  }

  public static ErrorResponseDto postWithError(
      String path, Object requestBody, Object... pathParams) {
    return given()
        .body(requestBody)
        .post(path, pathParams)
        .then()
        .extract()
        .as(ErrorResponseDto.class);
  }

  public static <T> T put(
      String path, Object requestBody, Class<T> responseType, Object... pathParams) {
    return as(
        given()
            .body(requestBody)
            .put(path, pathParams)
            .then()
            .statusCode(OK.getStatusCode())
            .extract(),
        responseType);
  }

  public static ConstraintViolationResponseDto putWithBadRequest(
      String path, Object requestBody, Object... pathParams) {
    return given()
        .body(requestBody)
        .put(path, pathParams)
        .then()
        .statusCode(BAD_REQUEST.getStatusCode())
        .extract()
        .as(ConstraintViolationResponseDto.class);
  }

  public static ErrorResponseDto putWithError(
      String path, Object requestBody, Object... pathParams) {
    return given()
        .body(requestBody)
        .put(path, pathParams)
        .then()
        .extract()
        .as(ErrorResponseDto.class);
  }

  public static <T> T delete(
      String path, Object requestBody, Class<T> responseType, Object... pathParams) {
    return as(
        given()
            .body(requestBody)
            .delete(path, pathParams)
            .then()
            .statusCode(OK.getStatusCode())
            .extract(),
        responseType);
  }

  public static ConstraintViolationResponseDto deleteWithBadRequest(
      String path, Object requestBody, Object... pathParams) {
    return given()
        .body(requestBody)
        .delete(path, pathParams)
        .then()
        .statusCode(BAD_REQUEST.getStatusCode())
        .extract()
        .as(ConstraintViolationResponseDto.class);
  }

  public static ErrorResponseDto deleteWithError(
      String path, Object requestBody, Object... pathParams) {
    return given()
        .body(requestBody)
        .delete(path, pathParams)
        .then()
        .extract()
        .as(ErrorResponseDto.class);
  }

  @SuppressWarnings("unchecked")
  private static <T> T as(ExtractableResponse<Response> response, Class<T> responseType) {
    if (responseType == String.class) {
      return (T) response.asString();
    } else if (responseType == Integer.class) {
      return (T) Integer.valueOf(response.asString());
    } else {
      return response.as(responseType);
    }
  }
}
