//package server.api;
//
//import commons.Card;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import server.database.CardRepository;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//public class CardControllerTest {
//
//    private CardRepository repoCard;
//    private CardController cardController;
//
//    private Card card1;
//    private Card card2;
//
//    @BeforeEach
//    public void setUp() {
//        // make a mock repo but a good controller
//        this.repoCard = mock(CardRepository.class);
//        cardController = new CardController(repoCard);
//
//        card1 = new Card(1L, "Card: big cheese hat", "Description is very cool", 1L);
//        card2 = new Card(2L, "Card non hat person", "Description anti-vo", 2L);
//    }
//
//    @Test
//    public void testGetById_notFound() {
//        when(repoCard.existsById(1L)).thenReturn(false);
//
//        ResponseEntity<Card> response = cardController.getById(1L);
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        System.out.println("dit you get hier");
//    }
//
//    @Test
//    public void testGetById_found() {
//        Card card = new Card(1L, "Card 1", "Description 1", 1L);
//        when(repoCard.existsById(1L)).thenReturn(true);
//        when(repoCard.findById(1L)).thenReturn(Optional.of(card));
//
//        ResponseEntity<Card> response = cardController.getById(1L);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(card, response.getBody());
//    }
//
//    @Test
//    public void testGetByTitle_notFound() {
//        when(repoCard.findAll()).thenReturn(List.of());
//        ResponseEntity<List<Card>> response = cardController.getByTitle("Card 1");
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//    }
//
//    @Test
//    public void testGetByTitle_found() {
//        Card card = new Card(1L, "Card 1", "Description 1", 1L);
//        when(repoCard.findAll()).thenReturn(Arrays.asList(card));
//        ResponseEntity<List<Card>> response = cardController.getByTitle("Card 1");
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(1, response.getBody().size());
//        assertEquals(card, response.getBody().get(0));
//    }
//
//    @Test
//    public void testUpdateCard_notFound() {
//        when(repoCard.existsById(1L)).thenReturn(false);
//        ResponseEntity<Card> response = cardController.updateCard(1L, new Card());
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//    }
//
//    @Test
//    public void testUpdateCard_updated() {
//        Card oldCard = new Card(1L, "Card 1", "Description 1", 1L);
//        Card newCard = new Card(1L, "New Card 1", "New Description 1", 2L);
//        when(repoCard.existsById(1L)).thenReturn(true);
//        when(repoCard.findById(1L)).thenReturn(Optional.of(oldCard));
//        when(repoCard.save(any(Card.class))).thenReturn(newCard);
//
//        ResponseEntity<Card> response = cardController.updateCard(1L, newCard);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(newCard, response.getBody());
//    }
//}