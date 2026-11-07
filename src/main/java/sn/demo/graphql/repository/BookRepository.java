package sn.demo.graphql.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import sn.demo.graphql.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    // Spring génère la requête SQL à partir du nom de la méthode
    List<Book> findByAuthorId(Long authorId);
}
