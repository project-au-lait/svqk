package dev.aulait.svqk.domain.hello;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HelloRepository extends JpaRepository<HelloEntity, Integer> { // <.>
}