package mapper;

import models.Card;
import models.WeightedArrayList;
import dto.CardDTO;
import dto.WeightedItemDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CardMapper {

    public static CardDTO toDTO(Card card) {
        return new CardDTO(
                card.getWord(),
                card.getDescription(),
                toDTOList(card.getExamples()),
                card.getMastery(),
                card.getLevel(),

                card.getRepetitions(),
                card.getInterval(),
                card.getEf(),
                card.getNextReview().toString(),
                card.getStability(),
                card.getDifficulty()
        );
    }

    private static List<WeightedItemDTO> toDTOList(WeightedArrayList<String> list) {
        List<WeightedItemDTO> result = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            var item = list.getRaw(i);
            result.add(new WeightedItemDTO(item.getItem(), item.getWeight()));
        }

        return result;
    }

    public static Card fromDTO(CardDTO dto) {
        Card card = new Card();
        card.setWord(dto.word);
        card.setDescription(dto.description);
        card.setLevel(dto.level);

        card.getExamples().clear();
        card.getMastery().clear();

        fromDTOList(card.getExamples(), dto.examples);
        card.getMastery().addAll(dto.mastery);

        card.setRepetitions(dto.repetitions);
        card.setInterval(dto.interval);
        card.setEf(dto.ef);
        card.setNextReview(LocalDate.parse(dto.nextReview));

        card.setStability(dto.stability);
        card.setDifficulty(dto.difficulty);

        return card;
    }

    private static void fromDTOList(WeightedArrayList<String> target,
                                    List<WeightedItemDTO> source) {
        for (WeightedItemDTO dto : source) {
            target.addDirect(dto.value, dto.weight);
        }
    }
}