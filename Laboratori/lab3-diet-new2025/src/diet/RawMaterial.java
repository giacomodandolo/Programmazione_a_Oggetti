package diet;

/**
 * Class that defines a raw material
 */
public class RawMaterial implements NutritionalElement {
    
    private String name;
    private double kcal, proteins, carbs, fat;

    public RawMaterial(String name, double kcal, double proteins, double carbs, double fat) {
        this.name = name;
        this.kcal = kcal;
        this.proteins = proteins;
        this.carbs = carbs;
        this.fat = fat;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getCalories() {
        return kcal;
    }

    @Override
    public double getProteins() {
        return proteins;
    }

    @Override
    public double getCarbs() {
        return carbs;
    }

    @Override
    public double getFat() {
        return fat;
    }

    @Override
    public boolean per100g() {
        return true;
    }

}
