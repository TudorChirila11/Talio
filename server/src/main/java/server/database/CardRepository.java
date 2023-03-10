package server.database;

import commons.Card;
import commons.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, String> {}