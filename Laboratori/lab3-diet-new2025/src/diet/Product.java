package diet;

/**
 * Class that defines a product
 */
public class Product implements NutritionalElement {

    private String name;
    private double kcal, proteins, carbs, fat;

    public Product(String name, double carbs, double fat, double kcal, double proteins) {
        this.carbs = carbs;
        this.fat = fat;
        this.kcal = kcal;
        this.name = name;
        this.proteins = proteins;
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
        return false;
    }
    
}
