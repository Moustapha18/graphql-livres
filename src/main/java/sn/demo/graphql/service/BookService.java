package sn.demo.graphql.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import sn.demo.graphql.exception.NotFoundException;
import sn.demo.graphql.model.Author;
import sn.demo.graphql.model.Book;
import sn.demo.graphql.repository.AuthorRepository;
import sn.demo.graphql.repository.BookRepository;

/** Logique métier : les données sont maintenant lues et écrites dans la base H2. */
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final Sinks.Many<Book> bookAddedSink = Sinks.many().multicast().onBackpressureBuffer();

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> findBook(String id) {
        return bookRepository.findById(parseId(id));
    }

    public List<Author> findAllAuthors() {
        return authorRepository.findAll();
    }

    public Optional<Author> findAuthor(String id) {
        return authorRepository.findById(parseId(id));
    }

    public List<Book> findBooksByAuthor(Long authorId) {
        return bookRepository.findByAuthorId(authorId);
    }

    public Book addBook(String title, String authorId) {
        checkTitle(title);
        Author author = authorRepository.findById(parseId(authorId))
                .orElseThrow(() -> new NotFoundException("Auteur introuvable avec l'id : " + authorId));
        Book saved = bookRepository.save(new Book(title, author));
        bookAddedSink.tryEmitNext(saved);
        return saved;
    }

    public Book updateBook(String id, String title) {
        checkTitle(title);
        Book book = bookRepository.findById(parseId(id))
                .orElseThrow(() -> new NotFoundException("Livre introuvable avec l'id : " + id));
        book.setTitle(title);
        return bookRepository.save(book);
    }

    public boolean deleteBook(String id) {
        Long bookId = parseId(id);
        if (!bookRepository.existsById(bookId)) {
            return false;
        }
        bookRepository.deleteById(bookId);
        return true;
    }

    public Flux<Book> bookAdded() {
        return bookAddedSink.asFlux();
    }

    private void checkTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Le titre ne doit pas être vide");
        }
    }

    /** Dans GraphQL, un ID arrive sous forme de texte : on le convertit en nombre. */
    private Long parseId(String id) {
        try {
            return Long.valueOf(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Id invalide : " + id);
        }
    }
}
