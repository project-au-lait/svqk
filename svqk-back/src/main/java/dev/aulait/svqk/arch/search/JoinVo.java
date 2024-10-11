package dev.aulait.svqk.arch.search;

import lombok.Builder;
import lombok.Value;

/** (LEFT) JOIN (FETCH) {fieldOwner}.{field} {alias} */
@Value
@Builder
public class JoinVo {
  private String fieldOwner;
  private String field;
  private String alias;
  private boolean fetch;
  private boolean left;
}
