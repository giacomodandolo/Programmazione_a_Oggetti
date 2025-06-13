package mountainhuts;

import java.util.Optional;

/**
 * Represents a mountain hut
 * 
 * It includes a name, optional altitude, category,
 * number of beds and location municipality.
 *  
 *
 */
public class MountainHut {

	private String name;
	private Optional<Integer> altitude;
	private String category;
	private Integer bedsNumber;
	private Municipality municipality;

	public MountainHut(String name,
					   String category, Integer bedsNumber, 
					   Municipality municipality) {
		this.name = name;
		this.altitude = Optional.empty();
		this.category = category;
		this.bedsNumber = bedsNumber;
		this.municipality = municipality;
	}

	public MountainHut(String name, Optional<Integer> altitude,
					   String category, Integer bedsNumber, 
					   Municipality municipality) {
		this.name = name;
		this.altitude = altitude;
		this.category = category;
		this.bedsNumber = bedsNumber;
		this.municipality = municipality;
	}

	/**
	 * Retrieves the name of the hut
	 * @return name of the hut
	 */
	public String getName() {
		return name;
	}

	/**
	 * Retrieves altituted if available
	 * 
	 * @return optional hut altitude
	 */
	public Optional<Integer> getAltitude() {
		return altitude.isPresent() ? altitude : Optional.empty();
	}

	/**
	 * Retrieves the category of the hut
	 * @return hut category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Retrieves the number of beds available in the hut
	 * @return number of beds
	 */
	public Integer getBedsNumber() {
		return bedsNumber;
	}

	/**
	 * Retrieves the municipality of the hut
	 * @return hut municipality
	 */
	public Municipality getMunicipality() {
		return municipality;
	}
}
