package sn.demo.graphql.controller;

import java.util.List;
import sn.demo.graphql.model.Author;
import sn.demo.graphql.model.Book;
import sn.demo.graphql.service.BookService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    // ---------- Queries ----------
    @QueryMapping
    public List<Book> books() {
        return service.findAllBooks();
    }

    @QueryMapping
    public Book book(@Argument String id) {
        return service.findBook(id).orElse(null);
    }

    @QueryMapping
    public List<Author> authors() {
        return service.findAllAuthors();
    }

    @QueryMapping
    public Author author(@Argument String id) {
        return service.findAuthor(id).orElse(null);
    }

    // ---------- Mutations ----------
    @MutationMapping
    public Book addBook(@Argument String title, @Argument String authorId) {
        return service.addBook(title, authorId);
    }

    @MutationMapping
    public Book updateBook(@Argument String id, @Argument String title) {
        return service.updateBook(id, title);
    }

    @MutationMapping
    public boolean deleteBook(@Argument String id) {
        return service.deleteBook(id);
    }

    // ---------- Subscription ----------
    @SubscriptionMapping
    public Flux<Book> bookAdded() {
        return service.bookAdded();
    }

    // ---------- Resolvers des champs imbriqués ----------
    @SchemaMapping(typeName = "Book", field = "author")
    public Author bookAuthor(Book book) {
        return book.getAuthor();
    }

    @SchemaMapping(typeName = "Author", field = "books")
    public List<Book> authorBooks(Author author) {
        return service.findBooksByAuthor(author.getId());
    }
}
