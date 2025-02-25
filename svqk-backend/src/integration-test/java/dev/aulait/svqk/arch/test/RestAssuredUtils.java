package dev.aulait.svqk.arch.test;

import java.util.Locale;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

public class RestAssuredUtils {

  public static RequestSpecification given() {
    return io.restassured.RestAssured.given()
        .baseUri(TestConfig.getInstance().getBaseUrl())
        .contentType("application/json; charset=UTF-8")
        .header("Accept-Language", Locale.getDefault().toString().replace("_", "-"))
        .filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
  }
}
