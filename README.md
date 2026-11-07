# GraphQL Livres (Java / Spring Boot)

Mini-API GraphQL : livres et auteurs, avec queries, mutations et une subscription.

## Prérequis
- Java 17+
- Maven 3.8+

## Lancer
    mvn spring-boot:run

- Endpoint GraphQL : http://localhost:8080/graphql
- Interface de test (GraphiQL) : http://localhost:8080/graphiql

## Exemples (à coller dans GraphiQL)

Query :
    { books { id title author { name } } }

Query imbriquée :
    { author(id: "1") { name books { title } } }

Mutation :
    mutation { addBook(title: "Nouveau livre", authorId: "2") { id title } }

Modifier / supprimer :
    mutation { updateBook(id: "1", title: "Titre modifié") { id title } }
    mutation { deleteBook(id: "3") }

Subscription (ouvrir un 2e onglet GraphiQL, lancer, puis faire un addBook) :
    subscription { bookAdded { id title author { name } } }

## Structure
- `schema.graphqls` : le contrat (types, Query, Mutation, Subscription)
- `BookController` : les resolvers
- `BookService` : données en mémoire

## Tester avec Postman
Importer `postman_collection.json` (Import > fichier). Chaque requête est un POST sur
http://localhost:8080/graphql avec le corps en mode **GraphQL**.
La subscription se teste dans GraphiQL (WebSocket).

## Gestion des erreurs
`exception/GlobalExceptionHandler` convertit les exceptions en erreurs GraphQL :
- `NOT_FOUND` : auteur ou livre inexistant (addBook, updateBook)
- `BAD_REQUEST` : titre vide

Le code HTTP reste 200 : l'erreur se lit dans le champ `errors` de la réponse.

## Base de données H2
Les données sont stockées dans une base H2 (fichier `./data/livresdb`), via Spring Data JPA.
- Les tables `authors` et `books` sont créées automatiquement au démarrage.
- Des données de départ (2 auteurs, 3 livres) sont insérées si la base est vide.
- Console H2 : http://localhost:8080/h2-console
  (JDBC URL : `jdbc:h2:file:./data/livresdb`, utilisateur : `sa`, mot de passe vide)
- Pour repartir de zéro : arrêter l'application et supprimer le dossier `data`.
