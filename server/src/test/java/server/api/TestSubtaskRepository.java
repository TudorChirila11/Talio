package server.api;

import commons.Subtask;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.SubtaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

//CHECKSTYLE OFF
public class TestSubtaskRepository implements SubtaskRepository {
    private ArrayList<Subtask> subtasks;

    TestSubtaskRepository()
    {
        subtasks = new ArrayList<>();
    }
    @Override
    public List<Subtask> findAll() {
        return subtasks;
    }

    @Override
    public List<Subtask> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Subtask> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Subtask> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {
        Subtask toBeDeleted = null;
        for(Subtask s: subtasks)
        {
            if(s.getId() == (long) aLong)
                toBeDeleted = s;
        }
        subtasks.remove(toBeDeleted);
    }

    @Override
    public void delete(Subtask entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Subtask> entities) {

    }

    @Override
    public void deleteAll() {
        subtasks = new ArrayList<>();
    }

    @Override
    public <S extends Subtask> S save(S entity) {
        subtasks.add(entity);
        return entity;
    }

    @Override
    public <S extends Subtask> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Subtask> findById(Long aLong) {
        Optional<Subtask> res = Optional.empty();
        for(Subtask s : subtasks)
        {
            if(s.getId() == (long) aLong)
                res = Optional.of(s);
        }
        return res;
    }

    @Override
    public boolean existsById(Long aLong) {
        for(Subtask s : subtasks)
        {
            if(s.getId() == (long) aLong)
                return true;
        }
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Subtask> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Subtask> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Subtask> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Subtask getOne(Long aLong) {
        return null;
    }

    @Override
    public Subtask getById(Long aLong) {
        for(Subtask s : subtasks)
        {
            if(s.getId() == (long) aLong)
                return s;
        }
        return null;
    }

    @Override
    public <S extends Subtask> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Subtask> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Subtask> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Subtask> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Subtask> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Subtask> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Subtask, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
