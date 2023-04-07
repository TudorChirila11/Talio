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

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import client.scenes.*;
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

    private static String server;
    private String url;
    private String adminKey;

    private StompSession session;

    private BoardCtrl boardCtrl;

    private BoardOverviewCtrl boardOverviewCtrl;

    private TagOverviewCtrl tagOverviewCtrl;

    private CardInformationCtrl cardInformationCtrl;
    private TagCreatorCtrl tagCreatorCtrl;

    private ColorManagementCtrl colorManagementCtrl;
    private AdminLogInCtrl adminLogInCtrl;


    /**
     * Method used to generate a String of 8 random alphanumeric characters
     * @return String of 8 random alphanumeric characters
     */
    private String generateAdminKey() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards/adminKey") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(String.class);
    }

    /**
     * Admin Key getter method
     * @return String admin key
     */
    public String getAdminKey() {
        return generateAdminKey();
    }

    /**
     * @param url the ip that the user will be connecting to
     */
    public void changeIP(String url) {

        server = "http://" + url + "/";
        this.url = url;
    }

    /**
     * Returns the current ip the client is connected too.
     * @return the ip
     */
    public String getServer(){
        return server;
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
     * Adding a new Subtask to the server DB
     *
     * @param subtask a Subtask object
     * @return returns a Subtask
     */
    public Subtask addSubTask(Subtask subtask){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/subtasks") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(subtask, APPLICATION_JSON), Subtask.class);
    }

    /**
     * Deleting a Subtask from the server DB
     * @param id the id of the subtask
     *
     * @return returns a response
     */
    public Response deleteSubTask(long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/subtasks/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();
    }

    /**
     * gets a Card with this ID
     * @param id - card name
     * @return - a card object
     */
    public Card getCardById(Long id)
    {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/cards/"+id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(Card.class);

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
     * deletes a collection and all associated cards
     *
     * @param id the id of a collection
     * @return a response
     */
    public Response deleteBoard(long id) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/collections/" + id + "/ofBoard") //
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
     * Gets all the collections from a board.
     * @param board
     * @return a list of collections.
     */
    public List<Collection> getCollectionsFromBoard(Board board){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/collections/" + board.getId() + "/ofBoard") //
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
     * Gets a board by id
     * @param id id of the board
     * @return returns the board object
     */
    public Board getBoardById(long id){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/boards/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(Board.class);
    }

    /**
     * Retrieves all boards
     *
     * @return list of boards
     */
    public List<Board> getBoards() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server)
                .path("api/boards")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Board>>() {
                });

    }

    /***
     * gets collection by Id
     * @param collectionId - the Id of the collection
     * @return requested collection
     */
    public Collection getCollectionById(Long collectionId) {
        List<Collection> collections = getCollections();
        for(Collection c : collections)
            if(Long.compare(c.getId(), collectionId) == 0)
                return c;
        return null;
    }

    /**
     * Retrieves all tags
     * @param boardId get only the tags that belong to this board
     * @return List of tags
     */
    public List<Tag> getTags(Long boardId) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/tags/"+boardId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Tag>>() {
                });
    }

    /**
     * Retrieves tags that are connected to a card
     * @param card get only the tags that belong to this card
     * @return List of tags
     */
    public List<Tag> getTagsByCard(Card card) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/tags/card/" + card.getId()) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Tag>>() {
                });
    }

    /**
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
     * gets all the subtasks for a specific card
     * @param card
     * @return list of subtasks
     */
    public List<Subtask> getSubtasksForCard(Card card) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/subtasks/getFromCard/" + card.getId()) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Subtask>>() {
                });
    }

    /**
     * Retrieves all color presets that belong to that specific board
     * @param boardId get only the presets that belong to this board
     * @return List of presets
     */
    public List<ColorPreset> getPresets(Long boardId) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/presets/"+boardId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<ColorPreset>>() {
                });
    }

    /**
     * Retrieves the default color preset
     * @param boardId get only the default preset that belong to this board
     * @return the default preset
     */
    public ColorPreset getDefaultPresets(Long boardId) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/presets/default/" + boardId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<ColorPreset>() {
                });
    }


    public static final ExecutorService EXEC = Executors.newSingleThreadExecutor();

    /**
     * Registers updates for deleted cards in the delete by Id method
     * @param consumer the id of the card
     */
    public void registerForUpdates(Consumer<Long> consumer) {
        EXEC.submit(() -> {
            while (!Thread.interrupted()) {
                var res = ClientBuilder.newClient(new ClientConfig())
                        .target(server).path("api/cards/updates")
                        .request(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .get(Response.class);

                if (res.getStatus() == 204) {
                    continue;
                }

                var l = res.readEntity(Long.class);
                consumer.accept(l);
            }

        });
    }

    /**
     *
     * @return - the cardinformationCtrl method
     */
    public CardInformationCtrl getCardInformationCtrl()
    {
        return cardInformationCtrl;
    }

    /**
     * stops the Thread executor doing the long polling
     */
    public void stop(){
        EXEC.shutdownNow();
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



    /**
     * switches the position of card from indexOld, Collection old to indexNew, Collection newCol
     *
     * @param old      - old collection
     * @param indexOld - old index
     * @param newCol   - new collection
     * @param indexNew - new id
     * @return new collection
     */
    public Card changeCardIndex(Collection old, long indexOld, Collection newCol, long indexNew)
    {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/collections/"+old.getId() +"/"+indexOld + "/" + newCol.getId() + "/" + indexNew) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<Card>() {
                });
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
     * gets a list of the subtasks of card 'id'
     * @param id - the id of the card we want to search the subtasks of
     * @return the subtasks of card 'id'
     */
    public List<Subtask> getSubtasksOfCard(Long id) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/subtasks/getFromCard/"+id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Subtask>>(){
                });
    }

    /**
     * saves this preset in the databse
     * @param preset - the color preset stored in the databse
     * @return the saved colorpreset
     */
    public ColorPreset savePreset(ColorPreset preset)
    {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/presets")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(preset, APPLICATION_JSON), ColorPreset.class);
    }

    /**
     * returns this card's board object
     * @param cardId - the card we want to search the board of
     * @return board object
     */
    public Board getBoardOfCard(Long cardId) {
        Card c = getCardById(cardId);
        Collection collection = getCollectionById(c.getCollectionId());
        Board board = getBoardById(collection.getBoardId());
        return board;
    }

    /**
     * update subtask with id 'id'
     * @param id - the id of the subtask we want to update
     * @param s - the new subtask info
     * @return - the new subtask object
     */
    public Subtask updateSubtask(Long id, Subtask s) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/subtasks/"+id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(s, APPLICATION_JSON), Subtask.class);
    }

    /**
     * store subtask in the database
     *
     * @param s - the subtask we want to store
     * @return - stored Subtask object
     */
    public Subtask addSubtask(Subtask s)
    {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/subtasks") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(s, APPLICATION_JSON), Subtask.class);
    }

    /**
     * delete subtask from the database
     *
     * @param id - id of the Subtask we want to delete
     * @return - response with deletion operation status
     */
    public Response deleteSubtask(Long id)
    {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/subtasks/"+id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();
    }

    /**
     * returns string with how many done subtasks does card 'id' have
     * @param id - card id
     * @return string with format 'doneSubtasks/totalSubtasks'
     */
    public String getDoneSubtasksForCard(Long id) {
        List<Subtask> subtasks = getSubtasksOfCard(id);
        int nr =0 ;
        for(Subtask s : subtasks)
            if(s.getFinished())
                nr++;
        return nr+"/"+subtasks.size();
    }

    /**
     * This method changes the session to the appropriate url when the user connects to a server
     * @param ip the url of the server to which the stomp client will connect to
     */
    public void createStompSession(String ip) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        StompSessionHandlerAdapter sessionHandler = new MySessionHandler(latch);
        session = connect("ws://" + ip + "/websocket", sessionHandler);
        latch.await();
        boardCtrl.subscriber(session);
        boardOverviewCtrl.subscriber(session);
        tagOverviewCtrl.subscriber(session);
        cardInformationCtrl.subscriber(session);
        tagCreatorCtrl.subscriber(session);
        adminLogInCtrl.subscriber(session);
        colorManagementCtrl.subscriber(session);
    }

    /**
     * This method creates a stomp client that connects to the server
     * @param url the url that directs the clients to the server
     * @return a stomp client server instance that lets the user communicate with the server.
     */
    private StompSession connect(String url, StompSessionHandlerAdapter sessionHandler) {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            return stomp.connect(url, sessionHandler).get();
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
     * @param session the session that the register will use to subscribe to a server.
     */
    public <T> void registerForCollections(String dest, Class<T> type, Consumer<T> consumer, StompSession session) {
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
     * @param session StompSession to send the info through
     */
    public void send(String dest, Object o, StompSession session) {
        session.send(dest, o);
    }

    /**
     * This method gets all the controllers that are used in websockets and thus auto-synchronization
     * @param boardCtrl a controller that uses websockets
     * @param boardOverviewCtrl a controller that uses websockets
     * @param tagOverviewCtrl a controller that uses websockets
     * @param cardInformationCtrl a controller that uses websockets
     * @param tagCreatorCtrl a controller that uses websockets
     * @param adminLogInCtrl a controller that uses websockets
     * @param colorManagementCtrl a controller that uses websockets
     */

    public void getControllers(BoardCtrl boardCtrl, BoardOverviewCtrl boardOverviewCtrl, TagOverviewCtrl tagOverviewCtrl, CardInformationCtrl cardInformationCtrl, TagCreatorCtrl tagCreatorCtrl,
                               AdminLogInCtrl adminLogInCtrl, ColorManagementCtrl colorManagementCtrl){
        this.boardCtrl = boardCtrl;
        this.boardOverviewCtrl = boardOverviewCtrl;
        this.tagOverviewCtrl = tagOverviewCtrl;
        this.cardInformationCtrl = cardInformationCtrl;
        this.tagCreatorCtrl = tagCreatorCtrl;
        this.colorManagementCtrl = colorManagementCtrl;
        this.adminLogInCtrl = adminLogInCtrl;
    }

    /**
     * Method to set a color preset as default for cards
     * @param colorPreset the color preset to be set as default
     * @param board the board that the color preset should be set as default in
     * @param session the current session
     */
    public void setDefaultPreset(ColorPreset colorPreset, Board board, StompSession session) {
        List<ColorPreset> presets = getPresets(board.getId());
        for(ColorPreset preset : presets)
            if(preset!=colorPreset)
            {
                preset.setIsDefault(false);
                savePreset(preset);
                //send("app/presets", preset, session);
            }
        colorPreset.setIsDefault(true);
        savePreset(colorPreset);
       // send("app/presets", colorPreset, session);
    }

    /**
     * Method to delete a preset
     * @param id the id of the color preset
     * @return response
     */
    public Response deletePreset(Long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/presets/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();
    }

    public ColorPreset getDefaultPresetForCard(Card card) {
        Board b = getBoardOfCard(card.getId());
        ColorPreset ans = getDefaultPresets(b.getId());
        return ans;
    }
}
