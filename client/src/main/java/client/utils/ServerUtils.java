/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import commons.Board;
import commons.Card;
import commons.Collection;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import commons.Quote;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

public class ServerUtils {

    private static String server = "http://localhost:8080/";

    /**
     * @param ip the ip that the user will be connecting to
     */
    public static void changeIP(String ip) {
        server = "http://" + ip + ":8080/";
    }

    /**
     * Manual logger of all the active quates
     *
     * @throws IOException no ref
     */
    public void getQuotesTheHardWay() throws IOException {
        var url = new URL("http://localhost:8080/api/quotes");
        var is = url.openConnection().getInputStream();
        var br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    /**
     * Retrieving all quotes from server by making GET req
     *
     * @return a list of quotes
     */
    public List<Quote> getQuotes() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/quotes") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Quote>>() {
                });
    }

    /**
     * Adding a new quote to the server DB
     *
     * @param quote a Quote object
     * @return returns a Quote
     */
    public Quote addQuote(Quote quote) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/quotes") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(quote, APPLICATION_JSON), Quote.class);
    }


    /**
     * Adding a new Card to the server DB
     *
     * @param card a Card object
     * @return returns a Card
     */
    public Card addCard(Card card) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/cards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    /**
     * deletes a card
     *
     * @param id the id of a card
     * @return a response
     */
    public Response deleteCard(long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/cards/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();
    }

    /**
     * deletes a collection and all associated cards
     *
     * @param id the id of a collection
     * @return a response
     */
    public Response deleteCollection(long id) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/collections/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();

        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/cards/" + id + "/ofCollection") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();

    }


    /**
     * Adds a card to collection
     *
     * @param card       card
     * @param collection collection
     * @return a collection.
     */
    public Collection addCardCollection(Card card, Collection collection) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/collections/CardAddTo/" + card.getId() + "/" + card.getCollectionId()) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(collection, APPLICATION_JSON), Collection.class);
    }

    /**
     * Retrieves all Cards
     *
     * @param collection to use to filter cards by
     * @return List of cards
     */
    public List<Card> getCardsForCollection(Collection collection) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/cards/" + collection.getId() + "/ofCollection") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Card>>() {
                });
    }

    /**
     * Adding a new quote to the server DB
     *
     * @param collection a Quote object
     * @return returns a Quote
     */
    public Collection addCollection(Collection collection) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/collections") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(collection, APPLICATION_JSON), Collection.class);
    }

    /**
     * Retrieves all Collections
     *
     * @return List of collections
     */
    public List<Collection> getCollections() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/collections") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Collection>>() {
                });
    }

    /**
     * Retrieves all Collections
     */
    public void resetState() {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/collections") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON)
                .delete();

        ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/boards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON)
                .delete();

        ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/cards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON)
                .delete();
    }

    /**
     * Adds a board to the Server DB
     *
     * @param board type
     * @return board the board added
     */
    public Board addBoard(Board board) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/boards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    /**
     * Retrieves the only board
     *
     * @return a board
     */
    public Board getBoard() {
        List<Board> boards = ClientBuilder.newClient(new ClientConfig())
                .target(SERVER)
                .path("api/boards")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Board>>() {
                });
        if (boards.isEmpty()) {
            // create a new board and add it to the database
            Board newBoard = new Board("Main Board");
            addBoard(newBoard);
            return newBoard;
        }

        return boards.get(0);
    }
}
