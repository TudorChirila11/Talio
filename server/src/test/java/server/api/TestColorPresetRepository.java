package server.api;

import commons.ColorPreset;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.ColorPresetRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestColorPresetRepository implements ColorPresetRepository {
    private ArrayList<ColorPreset> ColorPresets;

    TestColorPresetRepository()
    {
        ColorPresets = new ArrayList<>();
    }

    /**
     * Method for getting all ColorPresets
     * @return ColorPresets
     */
    @Override
    public List<ColorPreset> findAll() {
        return ColorPresets;
    }

    /**
     * Method for getting all ColorPresets
     * @param sort
     * @return ColorPresets
     */
    @Override
    public List<ColorPreset> findAll(Sort sort) {
        return null;
    }

    /**
     * Method for getting ColorPresets
     * @param pageable
     * @return ColorPresets
     */
    @Override
    public Page<ColorPreset> findAll(Pageable pageable) {
        return null;
    }

    /**
     * Method for getting all the ColorPresets that have a specific id
     * @param longs must not be {@literal null} nor contain any {@literal null} values.
     * @return ColorPresets with the specified id
     */
    @Override
    public List<ColorPreset> findAllById(Iterable<Long> longs) {
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
        ColorPreset toBeDeleted = null;
        for(ColorPreset s: ColorPresets)
        {
            if(s.getId() == (long) aLong)
                toBeDeleted = s;
        }
        ColorPresets.remove(toBeDeleted);
    }

    /**
     * Method for deleting by specifying entity
     * @param entity must not be {@literal null}.
     */
    @Override
    public void delete(ColorPreset entity) {

    }

    /**
     * Deleting all ColorPresets with the specified id
     * @param longs must not be {@literal null}. Must not contain {@literal null} elements.
     */
    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    /**
     * delete all ColorPresets
     * @param entities must not be {@literal null}. Must not contain {@literal null} elements.
     */
    @Override
    public void deleteAll(Iterable<? extends ColorPreset> entities) {

    }

    /**
     * Delete all ColorPresets
     */
    @Override
    public void deleteAll() {
        ColorPresets = new ArrayList<>();
    }

    /**
     * saze a ColorPreset
     * @param entity must not be {@literal null}.
     * @param <S>
     * @return the entity that was added
     */
    @Override
    public <S extends ColorPreset> S save(S entity) {
        ColorPresets.add(entity);
        return entity;
    }

    /**
     * Save multiple entities
     * @param entities must not be {@literal null} nor must it contain {@literal null}.
     * @param <S>
     * @return the list of ColorPresets that were added
     */
    @Override
    public <S extends ColorPreset> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    /**
     * Method for finding a ColorPreset by id
     * @param aLong must not be {@literal null}.
     * @return the id that was found, if any were found
     */
    @Override
    public Optional<ColorPreset> findById(Long aLong) {
        Optional<ColorPreset> res = Optional.empty();
        for(ColorPreset s : ColorPresets)
        {
            if(s.getId() == (long) aLong)
                res = Optional.of(s);
        }
        return res;
    }

    /**
     * Find if a ColorPreset exists with the entered id
     * @param aLong must not be {@literal null}.
     * @return a boolean representing whether a ColorPreset by the id exists or not
     */
    @Override
    public boolean existsById(Long aLong) {
        for(ColorPreset s : ColorPresets)
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
     * @return the ColorPreset that was saved
     */
    @Override
    public <S extends ColorPreset> S saveAndFlush(S entity) {
        return null;
    }

    /**
     * Save a list of ColorPresets and flush
     * @param entities entities to be saved. Must not be {@literal null}.
     * @param <S>
     * @return the list of ColorPresets that were added
     */
    @Override
    public <S extends ColorPreset> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    /**
     * Method deleteAllInBatch
     * @param entities entities to be deleted. Must not be {@literal null}.
     */
    @Override
    public void deleteAllInBatch(Iterable<ColorPreset> entities) {

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
     * Method for getting one specific ColorPreset
     * @param aLong must not be {@literal null}.
     * @return ColorPreset
     */
    @Override
    public ColorPreset getOne(Long aLong) {
        return null;
    }

    /**
     * Method for getting a ColorPreset by id
     * @param aLong must not be {@literal null}.
     * @return ColorPreset
     */
    @Override
    public ColorPreset getById(Long aLong) {
        for(ColorPreset s : ColorPresets)
        {
            if(s.getId() == (long) aLong)
                return s;
        }
        return null;
    }

    /**
     * Method for finding one ColorPreset
     * @param example must not be {@literal null}.
     * @param <S>
     * @return ColorPreset
     */
    @Override
    public <S extends ColorPreset> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    /**
     * Method for finding all ColorPresets
     * @param example must not be {@literal null}.
     * @param <S>
     * @return list of ColorPresets
     */
    @Override
    public <S extends ColorPreset> List<S> findAll(Example<S> example) {
        return null;
    }

    /**
     * Method for finding all ColorPresets
     * @param example must not be {@literal null}.
     * @param sort    the {@link Sort} specification to sort the results by, must not be {@literal null}.
     * @param <S>
     * @return list of ColorPresets
     */
    @Override
    public <S extends ColorPreset> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    /**
     * Method for finding all ColorPresets
     * @param example  must not be {@literal null}.
     * @param pageable can be {@literal null}.
     * @param <S>
     * @return list if ColorPresets
     */
    @Override
    public <S extends ColorPreset> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    /**
     * Method for counting ColorPresets
     * @param example the {@link Example} to count instances for. Must not be {@literal null}.
     * @param <S>
     * @return 0
     */
    @Override
    public <S extends ColorPreset> long count(Example<S> example) {
        return 0;
    }

    /**
     * @param example the {@link Example} to use for the existence check. Must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends ColorPreset> boolean exists(Example<S> example) {
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
    public <S extends ColorPreset, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    /**
     * Method for finding ColorPresets by the Board id
     * @param boardId the id using which this method will find all the necessary ColorPresets
     * @return
     */
    @Override
    public List<ColorPreset> findByBoardId(Long boardId) {
        return null;
    }

    @Override
    public ColorPreset findByIsDefaultAndBoardId(Boolean isDefault, Long boardId) {
        return null;
    }
}
