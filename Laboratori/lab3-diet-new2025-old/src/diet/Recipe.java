package diet;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * Represents a recipe of the diet.
 * 
 * A recipe consists of a a set of ingredients that are given amounts of raw materials.
 * The overall nutritional values of a recipe can be computed
 * on the basis of the ingredients' values and are expressed per 100g
 * 
 *
 */
public class Recipe implements NutritionalElement {
	
	private double totalQty;
	private String name;
	private Food food;
	private ArrayList<Ingredient> ingredients = new ArrayList<>();

	/**
	 * Internal class that defines an ingredient wuth its quantity
	 */
	private class Ingredient {

		String material;
		double quantity;

		private Ingredient(String material, double quantity) {
			this.material = material;
			this.quantity = quantity;
		}
		
		private String getName() {
			return material;
		}

		private double getQuantity() {
			return quantity;
		}
	}

	public Recipe(String name, Food food) {
		this.totalQty = 0.0;
		this.name = name;
		this.food = food;
	}

	/**
	 * Adds the given quantity of an ingredient to the recipe.
	 * The ingredient is a raw material.
	 * 
	 * @param material the name of the raw material to be used as ingredient
	 * @param quantity the amount in grams of the raw material to be used
	 * @return the same Recipe object, it allows method chaining.
	 */
	public Recipe addIngredient(String material, double quantity) {
		ingredients.add(new Ingredient(material, quantity));
		totalQty += quantity;

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
	private double getNutritionalValue(Function<NutritionalElement, Double> m) {
		double qty = 0.0, currQty;

		for (Ingredient ingredient : ingredients) {
			currQty = m.apply(food.getRawMaterial(ingredient.getName())) 
						* (ingredient.getQuantity()) / totalQty;
			qty += currQty;
		}
		
		return qty;
	}

	@Override
	public double getCalories() {
		return getNutritionalValue(
			(i) -> i.getCalories()
		);
	}

	@Override
	public double getProteins() {
		return getNutritionalValue(
			(i) -> i.getProteins()
		);
	}

	@Override
	public double getCarbs() {
		return getNutritionalValue(
			(i) -> i.getCarbs()
		);
	}

	@Override
	public double getFat() {
		return getNutritionalValue(
			(i) -> i.getFat()
		);
	}

	/**
	 * Indicates whether the nutritional values returned by the other methods
	 * refer to a conventional 100g quantity of nutritional element,
	 * or to a unit of element.
	 * 
	 * For the {@link Recipe} class it must always return {@code true}:
	 * a recipe expresses nutritional values per 100g
	 * 
	 * @return boolean indicator
	 */
	@Override
	public boolean per100g() {
		return true;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		String in;

		for (Ingredient i : ingredients) {
			in = i.getName() + ": " + i.getQuantity() + "g\n";
			s.append(in);
		}

		return s.toString();
	}
	
}
