package sn.demo.graphql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.demo.graphql.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
