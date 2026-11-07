package sn.demo.graphql.exception;

import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.web.bind.annotation.ControllerAdvice;

/** Transforme les exceptions Java en erreurs GraphQL claires (champ "errors"). */
@ControllerAdvice
public class GlobalExceptionHandler {

    @GraphQlExceptionHandler
    public GraphQLError handleNotFound(NotFoundException ex, DataFetchingEnvironment env) {
        return build(ErrorType.NOT_FOUND, ex.getMessage(), env);
    }

    @GraphQlExceptionHandler
    public GraphQLError handleBadRequest(IllegalArgumentException ex, DataFetchingEnvironment env) {
        return build(ErrorType.BAD_REQUEST, ex.getMessage(), env);
    }

    private GraphQLError build(ErrorType type, String message, DataFetchingEnvironment env) {
        return GraphQLError.newError()
                .errorType(type)
                .message(message)
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
    }
}
