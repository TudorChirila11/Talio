package server.database;

package server.database;

import commons.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import commons.Quote;

public interface CollectionRepository extends JpaRepository<Collection, String> {}