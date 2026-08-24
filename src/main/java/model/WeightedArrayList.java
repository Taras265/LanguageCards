package model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeightedArrayList<T> {
    private final List<Item<T>> list = new ArrayList<>();
    @JsonIgnore
    private final Random random = new Random();

    public WeightedArrayList() {}

    public int size() {
        return list.size();
    }

    public void add(T item) {
        Item<T> i;
        if (size() == 0) {
            i = new Item<>(item, 1);
        } else {
            list.forEach(item1 -> item1.setWeight(item1.getWeight() / 2));
            i = new Item<>(item, 0.5);
        }
        list.add(i);
    }

    public void addFromList(List<T> l) {
        for (T item:  l) {
            add(item);
        }
    }

    public T getRandomItem() {
        double randomNumber = random.nextDouble();
        double current = 0d;
        for (int i=0; i<size(); i++) {
            Item<T> item = list.get(i);
            current += item.getWeight();

            if (current >= randomNumber) {
                decreaseVariation(i);
                return item.getItem();
            }
        }
        return null;
    }

    public void setSameWeights() {
        if (list.isEmpty()) {
            return;
        }

        double w = 1d / size();
        for (Item<T> item: list) {
            item.setWeight(w);
        }
    }

    private void decreaseVariation(int index) {
        Item<T> mainItem = list.get(index);
        mainItem.setWeight(mainItem.getWeight() / 2);
        double delta = mainItem.getWeight();

        for (int i=0; i<size(); i++) {
            if (i != index) {
                Item<T> item = list.get(i);
                double weight = item.getWeight();
                double dec = 1-(delta*2);
                if (dec == 0) {
                    setSameWeights();
                    break;
                }
                item.setWeight(weight + (delta * (weight / dec)));
            }
        }
    }

    public String toString() {
        return list.toString();
    }

    public List<Item<T>> getList() {
        return list;
    }

    public static class Item<T> {
        private T item;
        private double weight;

        Item(T item, double weight) {
            this.item = item;
            this.weight = weight;
        }

        public T getItem() {
            return item;
        }

        public void setItem(T item) {
            this.item = item;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public String toString() {
            return item + " : " + weight;
        }
    }

}
