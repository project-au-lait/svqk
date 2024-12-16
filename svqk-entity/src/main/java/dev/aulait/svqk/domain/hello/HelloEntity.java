package dev.aulait.svqk.domain.hello;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hello")
public class HelloEntity implements java.io.Serializable {

  @Id
  @Column(name = "id")
  private Integer id;

  @Column(name = "message")
  private String message;
}
