package sn.demo.graphql.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sn.demo.graphql.model.Author;
import sn.demo.graphql.model.Book;
import sn.demo.graphql.repository.AuthorRepository;
import sn.demo.graphql.repository.BookRepository;

/** Insère des données de départ si la base est vide. */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(AuthorRepository authors, BookRepository books) {
        return args -> {
            if (authors.count() > 0) return;
            Author ba = authors.save(new Author("Mariama Bâ"));
            Author kane = authors.save(new Author("Cheikh Hamidou Kane"));
            books.save(new Book("Une si longue lettre", ba));
            books.save(new Book("Un chant écarlate", ba));
            books.save(new Book("L'Aventure ambiguë", kane));
        };
    }
}
