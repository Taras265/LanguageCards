package repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Card;
import dto.CardDTO;
import mapper.CardMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CardsJsonRepository implements CardsRepositoryInterface {

    private static final CardsJsonRepository instance = new CardsJsonRepository();

    private final File file = new File("cards.json");
    private final ObjectMapper mapper = new ObjectMapper();

    private ArrayList<Card> cache = new ArrayList<>();

    private CardsJsonRepository() {
        load();
    }

    public static CardsJsonRepository getInstance() {
        return instance;
    }

    private void load() {
        if (!file.exists()) return;

        try {
            ArrayList<CardDTO> dtos = mapper.readValue(
                    file,
                    new TypeReference<ArrayList<CardDTO>>() {}
            );

            cache = dtos.stream()
                    .map(CardMapper::fromDTO)
                    .collect(Collectors.toCollection(ArrayList::new));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void save() {
        try {
            ArrayList<CardDTO> dtos = cache.stream()
                    .map(CardMapper::toDTO)
                    .collect(Collectors.toCollection(ArrayList::new));

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(file, dtos);

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