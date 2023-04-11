package server.api;

import commons.Board;
import commons.Tag;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestTagRepository implements TagRepository {
    private ArrayList<Tag> tags;

    private Board board;

    TestTagRepository() {
        board = new Board(1L, "board");
        tags = new ArrayList<>(){{
            add(new Tag(1L, "tag", board.getId(), new ArrayList<Long>(), new ArrayList<Double>()));
            add(new Tag(2L, "tag", board.getId(), new ArrayList<Long>(), new ArrayList<Double>()));
            add(new Tag(3L, "tag", board.getId(), new ArrayList<Long>(), new ArrayList<Double>()));
        }};
    }

    /**
     * Method for getting all tags
     * @return tags
     */
    @Override
    public List<Tag> findAll() {
        return tags;
    }

    /**
     * Method for getting all tags
     * @param sort
     * @return tags
     */
    @Override
    public List<Tag> findAll(Sort sort) {
        return null;
    }

    /**
     * Method for getting tags
     * @param pageable
     * @return tags
     */
    @Override
    public Page<Tag> findAll(Pageable pageable) {
        return null;
    }

    /**
     * Method for getting all the tags that have a specific id
     * @param longs must not be {@literal null} nor contain any {@literal null} values.
     * @return tags with the specified id
     */
    @Override
    public List<Tag> findAllById(Iterable<Long> longs) {
        return null;
    }

    /**
     * Method for counting
     * @return count
     */
    @Override
    public long count() {
        return 0;
    }

    /**
     * Method for deleting by id
     * @param aLong must not be {@literal null}.
     */
    @Override
    public void deleteById(Long aLong) {
        Tag toBeDeleted = null;
        for(Tag s: tags)
        {
            if(s.getId() == (long) aLong)
                toBeDeleted = s;
        }
        tags.remove(toBeDeleted);
    }

    /**
     * Method for deleting by specifying entity
     * @param entity must not be {@literal null}.
     */
    @Override
    public void delete(Tag entity) {

    }

    /**
     * Deleting all tags with the specified id
     * @param longs must not be {@literal null}. Must not contain {@literal null} elements.
     */
    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    /**
     * delete all tags
     * @param entities must not be {@literal null}. Must not contain {@literal null} elements.
     */
    @Override
    public void deleteAll(Iterable<? extends Tag> entities) {

    }

    /**
     * Delete all tags
     */
    @Override
    public void deleteAll() {
        tags = new ArrayList<>();
    }

    /**
     * saze a tag
     * @param entity must not be {@literal null}.
     * @param <S>
     * @return the entity that was added
     */
    @Override
    public <S extends Tag> S save(S entity) {
        tags.add(entity);
        return entity;
    }

    /**
     * Save multiple entities
     * @param entities must not be {@literal null} nor must it contain {@literal null}.
     * @param <S>
     * @return the list of tags that were added
     */
    @Override
    public <S extends Tag> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    /**
     * Method for finding a tag by id
     * @param aLong must not be {@literal null}.
     * @return the id that was found, if any were found
     */
    @Override
    public Optional<Tag> findById(Long aLong) {
        Optional<Tag> res = Optional.empty();
        for(Tag s : tags)
        {
            if(s.getId() == (long) aLong)
                res = Optional.of(s);
        }
        return res;
    }

    /**
     * Find if a tag exists with the entered id
     * @param aLong must not be {@literal null}.
     * @return a boolean representing whether a tag by the id exists or not
     */
    @Override
    public boolean existsById(Long aLong) {
        for(Tag s : tags)
        {
            if(s.getId() == (long) aLong)
                return true;
        }
        return false;
    }

    /**
     * A flush method
     */
    @Override
    public void flush() {

    }

    /**
     * Method from saving an entity and flushing
     * @param entity entity to be saved. Must not be {@literal null}.
     * @param <S>
     * @return the tag that was saved
     */
    @Override
    public <S extends Tag> S saveAndFlush(S entity) {
        return null;
    }

    /**
     * Save a list of tags and flush
     * @param entities entities to be saved. Must not be {@literal null}.
     * @param <S>
     * @return the list of tags that were added
     */
    @Override
    public <S extends Tag> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    /**
     * Method deleteAllInBatch
     * @param entities entities to be deleted. Must not be {@literal null}.
     */
    @Override
    public void deleteAllInBatch(Iterable<Tag> entities) {

    }

    /**
     * Method deleteAllByIdInBatch
     * @param longs the ids of the entities to be deleted. Must not be {@literal null}.
     */
    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    /**
     * Method deleteAllInBatch
     */
    @Override
    public void deleteAllInBatch() {

    }

    /**
     * Method for getting one specific tag
     * @param aLong must not be {@literal null}.
     * @return tag
     */
    @Override
    public Tag getOne(Long aLong) {
        return null;
    }

    /**
     * Method for getting a tag by id
     * @param aLong must not be {@literal null}.
     * @return tag
     */
    @Override
    public Tag getById(Long aLong) {
        for(Tag s : tags)
        {
            if(s.getId() == (long) aLong)
                return s;
        }
        return null;
    }

    /**
     * Method for finding one tag
     * @param example must not be {@literal null}.
     * @param <S>
     * @return tag
     */
    @Override
    public <S extends Tag> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    /**
     * Method for finding all tags
     * @param example must not be {@literal null}.
     * @param <S>
     * @return list of tags
     */
    @Override
    public <S extends Tag> List<S> findAll(Example<S> example) {
        return null;
    }

    /**
     * Method for finding all tags
     * @param example must not be {@literal null}.
     * @param sort    the {@link Sort} specification to sort the results by, must not be {@literal null}.
     * @param <S>
     * @return list of tags
     */
    @Override
    public <S extends Tag> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    /**
     * Method for finding all tags
     * @param example  must not be {@literal null}.
     * @param pageable can be {@literal null}.
     * @param <S>
     * @return list if tags
     */
    @Override
    public <S extends Tag> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    /**
     * Method for counting tags
     * @param example the {@link Example} to count instances for. Must not be {@literal null}.
     * @param <S>
     * @return 0
     */
    @Override
    public <S extends Tag> long count(Example<S> example) {
        return 0;
    }

    /**
     * @param example the {@link Example} to use for the existence check. Must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Tag> boolean exists(Example<S> example) {
        return false;
    }

    /**
     * @param example       must not be {@literal null}.
     * @param queryFunction the query function defining projection, sorting, and the result type
     * @param <S>
     * @param <R>
     * @return
     */
    @Override
    public <S extends Tag, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    /**
     * Method for finding tags by the Board id
     * @param boardId the id using which this method will find all the necessary tags
     * @return
     */
    @Override
    public List<Tag> findByBoardId(Long boardId) {
        return null;
    }
}
