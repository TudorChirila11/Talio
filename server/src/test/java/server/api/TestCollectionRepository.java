package server.api;

import commons.Collection;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.CollectionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

//CHECKSTYLE:OFF

public class TestCollectionRepository implements CollectionRepository {

    private ArrayList<Collection> collections = new ArrayList<>();
    private ArrayList<String> methods = new ArrayList<>();

    @Override
    public List<Collection> findAll() {
        return collections;
    }

    @Override
    public List<Collection> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Collection> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Collection> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Collection entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Collection> entities) {
        for (Collection c : entities)  {
            collections.remove(c);
        }

    }

    @Override
    public void deleteAll() {
        collections = new ArrayList<>();

    }

    @Override
    public <S extends Collection> S save(S entity) {
        boolean add = collections.add(entity);
        Collection c = collections.get(collections.size()-1);
        return (S) c;
    }

    @Override
    public <S extends Collection> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Collection> findById(Long aLong) {
        Optional<Collection> res = Optional.empty();
        for(Collection c: collections)
            if((long)c.getId() == (long) aLong)
                res = Optional.of(c);
        return res;
    }

    @Override
    public boolean existsById(Long aLong) {
        for(Collection c : collections)
            if((long)c.getId() == (long) aLong)
                return true;
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Collection> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Collection> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Collection> entities) {
        CollectionRepository.super.deleteInBatch(entities);
    }

    @Override
    public void deleteAllInBatch(Iterable<Collection> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Collection getOne(Long aLong) {
        return null;
    }

    @Override
    public Collection getById(Long aLong) {
        Collection res = null;
        for(Collection c: collections)
            if((long)c.getId() == (long) aLong)
                res = c;
        return res;
    }

    @Override
    public <S extends Collection> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Collection> List<S> findAll(Example<S> example) {
        return (List<S>) collections;
    }

    @Override
    public <S extends Collection> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Collection> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Collection> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Collection> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Collection, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}

//CHECKSTYLE:ON