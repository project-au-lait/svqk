package dev.aulait.svqk.arch.test;

import java.util.ResourceBundle;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationMessageUtils {

  private static ResourceBundle rb;

  static {
    rb = ResourceBundle.getBundle("org.hibernate.validator.ValidationMessages");
  }

  public static String getNotBlankMsg() {
    return rb.getString("jakarta.validation.constraints.NotBlank.message");
  }
}
