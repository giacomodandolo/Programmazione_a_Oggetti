package diet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Represents a complete menu.
 * 
 * It can be made up of both packaged products and servings of given recipes.
 *
 */
public class Menu implements NutritionalElement {

	private String name;
	private ArrayList<String> products;
	private Map<String, Double> recipes;
	private Map<String, NutritionalElement> recipesMap;
	private Map<String, NutritionalElement> productsMap;

	public Menu(String name, Map<String, NutritionalElement> recipesMap, Map<String, NutritionalElement> productsMap) {
		this.products = new ArrayList<>();
		this.recipes = new HashMap<>();
		this.name = name;
		this.recipesMap = recipesMap;
		this.productsMap = productsMap;
	}

	/**
	 * Adds a given serving size of a recipe.
	 * The recipe is a name of a recipe defined in the {@code food}
	 * argument of the constructor.
	 * 
	 * @param recipe the name of the recipe to be used as ingredient
	 * @param quantity the amount in grams of the recipe to be used
	 * @return the same Menu to allow method chaining
	 */
    public Menu addRecipe(String recipe, double quantity) {
		recipes.put(recipe, quantity);
		
		return this;
	}

	/**
	 * Adds a unit of a packaged product.
	 * The product is a name of a product defined in the {@code food}
	 * argument of the constructor.
	 * 
	 * @param product the name of the product to be used as ingredient
	 * @return the same Menu to allow method chaining
	 */
    public Menu addProduct(String product) {
		products.add(product);

		return this;
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * Gets the nutritional value defined through a method
	 * 
	 * @param m method to execute
	 * @return nutritional value 
	 */
	private double getNutritionalValue(Function<NutritionalElement, Double> mR, Function<NutritionalElement, Double> mP) {
		double qty = 0.0;

		for (String rName : recipes.keySet())
			qty += mR.apply(recipesMap.get(rName)) * recipes.get(rName) / Food.GRAMS_PER_PORTION;

        for (String pName : products)
            qty += mP.apply(productsMap.get(pName));

		return qty;
	}

	/**
	 * Total KCal in the menu
	 */
	@Override
	public double getCalories() {
		return getNutritionalValue(
			(recipe) -> recipe.getCalories(),
			(product) -> product.getCalories()
		);
	}

	/**
	 * Total proteins in the menu
	 */
	@Override
	public double getProteins() {
		return getNutritionalValue(
			(recipe) -> recipe.getProteins(),
			(product) -> product.getProteins()
		);
	}

	/**
	 * Total carbs in the menu
	 */
	@Override
	public double getCarbs() {
		return getNutritionalValue(
			(recipe) -> recipe.getCarbs(),
			(product) -> product.getCarbs()
		);
	}

	/**
	 * Total fats in the menu
	 */
	@Override
	public double getFat() {
		return getNutritionalValue(
			(recipe) -> recipe.getFat(),
			(product) -> product.getFat()
		);
	}

	/**
	 * Indicates whether the nutritional values returned by the other methods
	 * refer to a conventional 100g quantity of nutritional element,
	 * or to a unit of element.
	 * 
	 * For the {@link Menu} class it must always return {@code false}:
	 * nutritional values are provided for the whole menu.
	 * 
	 * @return boolean indicator
	 */
	@Override
	public boolean per100g() {
		return false;
	}
}