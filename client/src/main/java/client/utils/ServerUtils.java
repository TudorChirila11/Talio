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
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import commons.*;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class ServerUtils {

    private static String server = "http://localhost:8080/";

    /**
     * @param ip the ip that the user will be connecting to
     */
    public void changeIP(String ip) {
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
                .target(server).path("api/cards/" + id) //
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
                .target(server).path("api/collections/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();

        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/cards/" + id + "/ofCollection") //
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
                .target(server).path("api/collections/CardAddTo/" + card.getId() + "/" + card.getCollectionId()) //
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
                .target(server).path("api/cards/" + collection.getId() + "/ofCollection") //
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
                .target(server).path("api/collections") //
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
                .target(server).path("api/collections") //
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
                .target(server).path("api/collections") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON)
                .delete();

        ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON)
                .delete();

        ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/cards") //
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
                .target(server).path("api/boards") //
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
                .target(server)
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

    /**
     * Retrieves all tags
     *
     * @return List of tags
     */
    public List<Tag> getTags() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/tags") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Tag>>() {
                });
    }

    /*
     * deletes card from index, in the Collection col's card array
     * @param col - the collection we want the card deleted from
     * @param index - the index
     * @return response
     */
    public Response deleteCardFromIndex(Collection col, int index)
    {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/collections/" + col.getId() + "/deleteCard/"+index) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();
    }

    /**
     * adds card to specific index inside a specific collection's card array
     * @param col - the collection we want to add the card to
     * @param c - the card object
     * @param index - the new index where Card c will be located in col's card ArrayList
     * @return Collection object
     */
    public Collection addCardToIndex(Collection col, Card c, int index) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/collections/" + col.getId() + "/" + c.getId() + "/" + index) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(col, APPLICATION_JSON), Collection.class);
    }
    
    private StompSession session = connect("ws://localhost:8080/websocket");

    /**
     * This method changes the session to the appropriate url when the user connects to a server
     * @param ip the url of the server to which the stomp client will connect to
     */
    public void createStompSession(String ip) {
        session = connect("ws://" + ip + ":8080/websocket");
    }


    /**
     * switches the position of card from indexOld, Collection old to indexNew, Collection newCol
     *
     * @param old      - old collection
     * @param indexOld - old index
     * @param newCol   - new collection
     * @param indexNew - new id
     * @return new collection
     */
    public Response changeCardIndex(Collection old, long indexOld, Collection newCol, long indexNew)
    {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/collections/"+old.getId() +"/"+indexOld + "/" + newCol.getId() + "/" + indexNew) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get();
    }


    /**
     * updates this card
     * @param cardId - the id of the card we want to update
     * @param newCard - the new card data we want to update with
     * @return Collection object
     */
    public Card updateCard(Long cardId, Card newCard)
    {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/cards/"+cardId)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(newCard, APPLICATION_JSON), Card.class);
    }

    /**
     * returns this card object
     * @param id - card id
     * @return the Card object
     */
    public Card getCard(Long id) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/cards/"+id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<Card>() {
                });
    }

    /**
     * This method creates a stomp client that connects to the server
     * @param url the url that directs the clients to the server
     * @return a stomp client server instance that lets the user communicate with the server.
     */
    private StompSession connect(String url) {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            return stomp.connect(url, new StompSessionHandlerAdapter() {}).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException();
    }

    /**
     * This method sends everything that the stomp client receives to the consumer (client)
     * @param dest where did the information come from
     * @param type what kind of information is received
     * @param consumer the client that is using the stomp client to receive and send data from and to the server
     * @param <T> the arbitrary type that lets this method receive any type of data and send it to the client.
     */
    public <T> void registerForCollections(String dest, Class<T> type, Consumer<T> consumer) {
        session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((T) payload);
            }
        });
    }

    /**
     * This method is used to send data to the server using the stomp client
     * @param dest where to send the data to
     * @param o what to send
     */
    public void send(String dest, Object o) {
        session.send(dest, o);
    }

}
