package dev.aulait.svqk.domain.tracker;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tracker")
public class TrackerEntity extends dev.aulait.svqk.arch.jpa.BaseEntity
    implements java.io.Serializable {

  @Id
  @Column(name = "id")
  private Integer id;

  @Column(name = "name")
  private String name;
}
