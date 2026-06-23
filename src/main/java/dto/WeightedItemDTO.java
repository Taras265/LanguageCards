package dto;

public class WeightedItemDTO {
    public String value;
    public double weight;

    public WeightedItemDTO() {}

    public WeightedItemDTO(String value, double weight) {
        this.value = value;
        this.weight = weight;
    }
}