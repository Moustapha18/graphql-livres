package sn.demo.graphql.exception;

/** Levée quand une ressource (auteur, livre...) n'existe pas. */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
