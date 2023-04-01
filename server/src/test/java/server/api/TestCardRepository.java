package server.api;

import commons.Card;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.CardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

//CHECKSTYLE:OFF
public class TestCardRepository implements CardRepository {

    private ArrayList<Card> cards = new ArrayList<>();
    private ArrayList<String> methods = new ArrayList<>();


    @Override
    public List<Card> findAll() {
        methods.add("findAll");
        return cards;
    }

    @Override
    public List<Card> findAll(Sort sort) {
        //we only sort increasingly based off index
        ArrayList<Card> arr = new ArrayList<>(findAll());
        for(int i = 0 ; i < arr.size(); ++i) {
            Long minIndex = Long.MAX_VALUE;
            int pos = 0;
            for(int j =i+1 ; j < arr.size();++j)
                if(arr.get(j).getIndex() < minIndex)
                {
                    minIndex = arr.get(j).getId();
                    pos = j;
                }
            var x = arr.get(pos);
            arr.set(pos, arr.get(i));
            arr.set(i, x);
        }
        return arr;
    }

    @Override
    public Page<Card> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Card> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {
        int pos = -1;
        for(int i = 0 ; i <cards.size();++i)
            if((long) cards.get(i).getId() == (long) aLong)
                pos = i;
        cards.remove(pos);
    }

    @Override
    public void delete(Card entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Card> entities) {

    }

    @Override
    public void deleteAll() {
        cards = new ArrayList<>();
    }

    @Override
    public <S extends Card> S save(S entity) {
        boolean add = cards.add(entity);
        Card c = cards.get(cards.size()-1);
        return (S) c;
    }

    @Override
    public <S extends Card> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Card> findById(Long aLong) {
        Optional<Card> res = Optional.empty();
        for(Card c: cards)
            if((long)c.getId() == (long) aLong)
                res = Optional.of(c);
        return res;
    }

    @Override
    public boolean existsById(Long aLong) {
        for(Card c : cards)
            if((long)c.getId() == (long) aLong)
                return true;
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Card> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Card> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Card> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Card getOne(Long aLong) {
        return null;
    }

    @Override
    public Card getById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Card> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Card> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Card> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Card> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Card> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Card> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Card, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
