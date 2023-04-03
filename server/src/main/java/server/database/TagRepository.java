package server.database;

import commons.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    /**
     * This method is used to get all the tags that belong to a specific board using the board's id
     * @param boardId the id using which this method will find all the necessary tags
     * @return the list of tags that the client is requesting
     */
    List<Tag> findByBoardId(Long boardId);
}
