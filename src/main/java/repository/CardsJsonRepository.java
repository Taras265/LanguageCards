package repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import models.Card;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CardsJsonRepository implements CardsRepositoryInterface {

    private static final CardsJsonRepository instance = new CardsJsonRepository();

    private final File file = new File("cards.json");
    private final ObjectMapper mapper = new ObjectMapper();

    private ArrayList<Card> cache = new ArrayList<>();

    private CardsJsonRepository() {
        mapper.registerModule(new JavaTimeModule());
        load();
    }

    public static CardsJsonRepository getInstance() {
        return instance;
    }

    private void load() {
        if (!file.exists()) return;

        try {
            cache = mapper.readValue(file, new TypeReference<ArrayList<Card>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void save() {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, cache);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Card> getCards() {
        return new ArrayList<>(cache);
    }

    @Override
    public void addCard(Card card) {
        cache.add(card);
        save();
    }

    @Override
    public void addCards(ArrayList<Card> cards) {
        cache.addAll(cards);
        save();
    }

    @Override
    public void updateCard(Card card) {
        for (int i = 0; i < cache.size(); i++) {
            if (cache.get(i).getWord().equals(card.getWord())) {
                cache.set(i, card);
                save();
                return;
            }
        }
    }

    @Override
    public void deleteCard(Card card) {
        cache.removeIf(c -> c.getWord().equals(card.getWord()));
        save();
    }
}