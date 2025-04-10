package diet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

/**
 * Facade class for the diet management.
 * It allows defining and retrieving raw materials and products.
 *
 */
public class Food {

	static double GRAMS_PER_PORTION = 100.0;
	private ArrayList<RawMaterial> rawMaterials = new ArrayList<>();
	private ArrayList<Product> products = new ArrayList<>();
	private ArrayList<Recipe> recipes = new ArrayList<>();
	private ArrayList<Menu> menus = new ArrayList<>();

	/**
	 * Define a new raw material.
	 * The nutritional values are specified for a conventional 100g quantity
	 * @param name unique name of the raw material
	 * @param calories calories per 100g
	 * @param proteins proteins per 100g
	 * @param carbs carbs per 100g
	 * @param fat fats per 100g
	 */
	public void defineRawMaterial(String name, double calories, double proteins, double carbs, double fat) {
		rawMaterials.add(new RawMaterial(name, calories, proteins, carbs, fat));
	}

	/**
	 * Obtains the info of an ArrayList of type E
	 * 
	 * @param <T> generic type
	 * @param <E> specific type
	 * @param original original information
	 * @param comparable comparison definition
	 * @return sorted Collection of type T
	 */
	private <T, E extends T> Collection<T> getSortedInfo(ArrayList<E> original, Comparator<E> comparator) {
		ArrayList<T> s = new ArrayList<>();

		original.sort(comparator);

		for (E element : original)
			s.add((T) element);
		
		return s;
	}

	/**
	 * Obtains the info of an ArrayList of type E
	 * 
	 * @param <T> generic type
	 * @param <E> specific type
	 * @param original original information
	 * @param comparable comparison definition
	 * @return searched element of type E
	 */
	private <T, E extends T> E getInfo(ArrayList<E> original, Comparable<T> comparable) {
		int index = 0;
		
		for (T nutritionalElement : original) {
			if (comparable.compareTo(nutritionalElement) == 0)
				break;
			index++;
		}

		return index < original.size() ? original.get(index) : null;
	}

	/**
	 * Retrieves the collection of all defined raw materials
	 * @return collection of raw materials though the {@link NutritionalElement} interface
	 */
	public Collection<NutritionalElement> rawMaterials() {
		return getSortedInfo(rawMaterials, (rm1, rm2) -> rm1.getName().compareTo(rm2.getName()));
	}

	/**
	 * Retrieves a specific raw material, given its name
	 * @param name  name of the raw material
	 * @return  a raw material though the {@link NutritionalElement} interface
	 */
	public NutritionalElement getRawMaterial(String name) {
		return getInfo(rawMaterials, (p1) -> p1.getName().compareTo(name));
	}

	/**
	 * Define a new packaged product.
	 * The nutritional values are specified for a unit of the product
	 * @param name unique name of the product
	 * @param calories calories for a product unit
	 * @param proteins proteins for a product unit
	 * @param carbs carbs for a product unit
	 * @param fat fats for a product unit
	 */
	public void defineProduct(String name, double calories, double proteins, double carbs, double fat) {
		products.add(new Product(name, carbs, fat, calories, proteins));
	}

	/**
	 * Retrieves the collection of all defined products
	 * @return collection of products though the {@link NutritionalElement} interface
	 */
	public Collection<NutritionalElement> products() {
		return getSortedInfo(products, (p1, p2) -> p1.getName().compareTo(p2.getName()));
	}

	/**
	 * Retrieves a specific product, given its name
	 * @param name  name of the product
	 * @return  a product though the {@link NutritionalElement} interface
	 */
	public NutritionalElement getProduct(String name) {
		return getInfo(products, (p1) -> p1.getName().compareTo(name));
	}

	/**
	 * Creates a new recipe stored in this Food container.
	 *  
	 * @param name name of the recipe
	 * @return the newly created Recipe object
	 */
	public Recipe createRecipe(String name) {
		Recipe r = new Recipe(name, this);
		
		recipes.add(r);

		return r;
	}
	
	/**
	 * Retrieves the collection of all defined recipes
	 * @return collection of recipes though the {@link NutritionalElement} interface
	 */
	public Collection<NutritionalElement> recipes() {
		return getSortedInfo(recipes, (r1, r2) -> r1.getName().compareTo(r2.getName()));
	}

	/**
	 * Retrieves a specific recipe, given its name
	 * @param name  name of the recipe
	 * @return  a recipe though the {@link NutritionalElement} interface
	 */
	public NutritionalElement getRecipe(String name) {		
		return getInfo(recipes, (p1) -> p1.getName().compareTo(name));
	}

	/**
	 * Creates a new menu
	 * 
	 * @param name name of the menu
	 * @return the newly created menu
	 */
	public Menu createMenu(String name) {
		Menu m = new Menu(name, this);
		
		menus.add(m);

		return m;
	}
}