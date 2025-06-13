package mountainhuts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class {@code Region} represents the main facade
 * class for the mountains hut system.
 *
 * It allows defining and retrieving information about
 * municipalities and mountain huts.
 *
 */
public class Region {
	private final String DEFAULT_ALTITUDE_RANGE = "0-INF";

	private String name;
	private Map<String, ArrayList<MountainHut>> altitudeRanges;
	private Map<String, Municipality> municipalities;
	private Map<String, MountainHut> mountainHuts;
	/**
	 * Create a region with the given name.
	 *
	 * @param name
	 *            the name of the region
	 */
	public Region(String name) {
		this.name = name;
		this.altitudeRanges = new HashMap<>();
		this.municipalities = new HashMap<>();
		this.mountainHuts = new HashMap<>();
	}

	/**
	 * Return the name of the region.
	 *
	 * @return the name of the region
	 */
	public String getName() {
		return name;
	}

	/**
	 * Create the ranges given their textual representation in the format
	 * "[minValue]-[maxValue]".
	 *
	 * @param ranges
	 *            an array of textual ranges
	 */
	public void setAltitudeRanges(String... ranges) {
		this.altitudeRanges = 
		Stream.of(ranges)
		.collect(
			Collectors.toMap(
				r -> r,
				r -> new ArrayList<>()
			)
		);
	}

	/**
	 * Return the textual representation in the format "[minValue]-[maxValue]" of
	 * the range including the given altitude or return the default range "0-INF".
	 *
	 * @param altitude
	 *            the geographical altitude
	 * @return a string representing the range
	 */
	public String getAltitudeRange(Integer altitude) {
		return
		Stream.of(altitudeRanges.keySet().toArray(String[]::new))
		.sorted(Comparator.naturalOrder())
		.filter(r -> {
			Integer i1 = Integer.valueOf(r.split("-")[0]);
			Integer i2 = Integer.valueOf(r.split("-")[1]);
			return i1 <= altitude && i2 >= altitude;
		})
		.findFirst()
		.orElse(DEFAULT_ALTITUDE_RANGE);
	}

	/**
	 * Return all the municipalities available.
	 *
	 * The returned collection is unmodifiable
	 *
	 * @return a collection of municipalities
	 */
	public Collection<Municipality> getMunicipalities() {
		return municipalities.values();
	}

	/**
	 * Return all the mountain huts available.
	 *
	 * The returned collection is unmodifiable
	 *
	 * @return a collection of mountain huts
	 */
	public Collection<MountainHut> getMountainHuts() {
		return mountainHuts.values();
	}

	/**
	 * Create a new municipality if it is not already available or find it.
	 * Duplicates must be detected by comparing the municipality names.
	 *
	 * @param name
	 *            the municipality name
	 * @param province
	 *            the municipality province
	 * @param altitude
	 *            the municipality altitude
	 * @return the municipality
	 */
	public Municipality createOrGetMunicipality(String name, String province, Integer altitude) {		
		municipalities.putIfAbsent(name, 
			new Municipality(
				name, 
				province, 
				altitude
			)
		);
		
		return municipalities.get(name);
	}

	/**
	 * Create a new mountain hut if it is not already available or find it.
	 * Duplicates must be detected by comparing the mountain hut names.
	 *
	 * @param name
	 *            the mountain hut name
	 * @param category
	 *            the mountain hut category
	 * @param bedsNumber
	 *            the number of beds in the mountain hut
	 * @param municipality
	 *            the municipality in which the mountain hut is located
	 * @return the mountain hut
	 */
	public MountainHut createOrGetMountainHut(String name, String category,
											  Integer bedsNumber, Municipality municipality) {
		mountainHuts.putIfAbsent(name, 
			new MountainHut(
				name, 
				category, 
				bedsNumber, 
				municipality
			)
		);

		return mountainHuts.get(name);
	}

	/**
	 * Create a new mountain hut if it is not already available or find it.
	 * Duplicates must be detected by comparing the mountain hut names.
	 *
	 * @param name
	 *            the mountain hut name
	 * @param altitude
	 *            the mountain hut altitude
	 * @param category
	 *            the mountain hut category
	 * @param bedsNumber
	 *            the number of beds in the mountain hut
	 * @param municipality
	 *            the municipality in which the mountain hut is located
	 * @return a mountain hut
	 */
	public MountainHut createOrGetMountainHut(String name, Integer altitude, String category,
											  Integer bedsNumber, Municipality municipality) {
		mountainHuts.putIfAbsent(name, 
			new MountainHut(
				name, 
				Optional.ofNullable(altitude), 
				category, 
				bedsNumber, 
				municipality
			)
		);

		return mountainHuts.get(name);
	}

	/**
	 * Creates a new region and loads its data from a file.
	 *
	 * The file must be a CSV file and it must contain the following fields:
	 * <ul>
	 * <li>{@code "Province"},
	 * <li>{@code "Municipality"},
	 * <li>{@code "MunicipalityAltitude"},
	 * <li>{@code "Name"},
	 * <li>{@code "Altitude"},
	 * <li>{@code "Category"},
	 * <li>{@code "BedsNumber"}
	 * </ul>
	 *
	 * The fields are separated by a semicolon (';'). The field {@code "Altitude"}
	 * may be empty.
	 *
	 * @param name
	 *            the name of the region
	 * @param file
	 *            the path of the file
	 */
	public static Region fromFile(String name, String file) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(file);
		Region r = new Region(name);

		List<String[]> s = 
		Stream.of(readData(file).toArray(String[]::new))
		.skip(1)
		.map(data -> data.split(";"))
		.toList();

		s.stream()
		.forEach(data -> 
			r.createOrGetMunicipality(
				data[1], 
				data[0], 
				Integer.valueOf(data[2])
			)
		);
		
		s.stream()
		.forEach(
			data -> {
				if (data[4].equals(""))
					r.createOrGetMountainHut(
						data[3],
						data[5],
						Integer.valueOf(data[6]), 
						r.createOrGetMunicipality(data[1], data[0], Integer.valueOf(data[2]))
					);
				else 
					r.createOrGetMountainHut(
						data[3],
						Integer.valueOf(data[4]),
						data[5],
						Integer.valueOf(data[6]), 
						r.createOrGetMunicipality(data[1], data[0], Integer.valueOf(data[2]))
					);
			}
		);

		return r;
	}

	/**
	 * Reads the lines of a text file.
	 *
	 * @param file path of the file
	 * @return a list with one element per line
	 */
	public static List<String> readData(String file) {
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			return in.lines().toList();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return new ArrayList<>();
		}
	}

	/**
	 * Count the number of municipalities with at least a mountain hut per each
	 * province.
	 *
	 * @return a map with the province as key and the number of municipalities as
	 *         value
	 */
	public Map<String, Long> countMunicipalitiesPerProvince() {
		return 
		Stream.of(municipalities.values().toArray())
		.map(m -> ((Municipality) m).getProvince())
		.collect(
			Collectors.groupingBy(
				Function.identity(),
				HashMap::new,
				Collectors.counting()
			)
		);
	}

	/**
	 * Count the number of mountain huts per each municipality within each province.
	 *
	 * @return a map with the province as key and, as value, a map with the
	 *         municipality as key and the number of mountain huts as value
	 */
	public Map<String, Map<String, Long>> countMountainHutsPerMunicipalityPerProvince() {
		return 
		mountainHuts.values().stream()
		.collect(
			Collectors.groupingBy(
				mh -> mh.getMunicipality().getProvince(), 	// primo filtro di raggruppamento
				Collectors.groupingBy(
					mh -> mh.getMunicipality().getName(), 	// secondo filtro di raggruppamento
					Collectors.counting()
				)
			)
		);
	}

	/**
	 * Count the number of mountain huts per altitude range. If the altitude of the
	 * mountain hut is not available, use the altitude of its municipality.
	 *
	 * @return a map with the altitude range as key and the number of mountain huts
	 *         as value
	 */
	public Map<String, Long> countMountainHutsPerAltitudeRange() {
		return 
		mountainHuts.entrySet().stream()
		.collect(
			Collectors.groupingBy(
				mh -> getAltitudeRange(
					mh.getValue().getAltitude()
					.orElse(mh.getValue().getMunicipality().getAltitude())
				),
				Collectors.counting()
			)
		);
	}

	/**
	 * Compute the total number of beds available in the mountain huts per each
	 * province.
	 *
	 * @return a map with the province as key and the total number of beds as value
	 */
	public Map<String, Integer> totalBedsNumberPerProvince() {
		return 
		mountainHuts.values().stream()
		.collect(
			Collectors.groupingBy(
				mh -> mh.getMunicipality().getProvince(),
				Collectors.summingInt(mh -> mh.getBedsNumber())
			)
		);
	}

	/**
	 * Compute the maximum number of beds available in a single mountain hut per
	 * altitude range. If the altitude of the mountain hut is not available, use the
	 * altitude of its municipality.
	 *
	 * @return a map with the altitude range as key and the maximum number of beds
	 *         as value
	 */
	public Map<String, Optional<Integer>> maximumBedsNumberPerAltitudeRange() {
		return 
		mountainHuts.values().stream()
		.collect(
			Collectors.groupingBy(
				mh -> getAltitudeRange(
					mh.getAltitude()
					.orElse(mh.getMunicipality().getAltitude())
				),

				Collectors.mapping(
					mh -> mh.getBedsNumber(), 
					Collectors.maxBy((a, b)-> Integer.compare(a,b))
				)
			)
		);
	}

	/**
	 * Compute the municipality names per number of mountain huts in a municipality.
	 * The lists of municipality names must be in alphabetical order.
	 *
	 * @return a map with the number of mountain huts in a municipality as key and a
	 *         list of municipality names as value
	 */
	public Map<Long, List<String>> municipalityNamesPerCountOfMountainHuts() {
		return 
		// numero di mountainHuts per ogni municipalità 
		mountainHuts.values().stream()
		.collect(
			Collectors.groupingBy(
				mh -> mh.getMunicipality().getName(),
				Collectors.counting()
			)
		)
		// nomi di municipalità con un determinato numero di mountainHuts
		.entrySet().stream()
		.collect(
			Collectors.groupingBy(
				// numeri di mountainHuts
				c -> c.getValue(),

				Collectors.mapping(
					// utilizza i nomi delle municipalità
					c -> c.getKey(),

					// colleziona in una lista, ordina lo stream e trasforma in lista
					Collectors.collectingAndThen(
						Collectors.toList(), 
						l -> l.stream().sorted().toList()
					)
				)
			)
		)
		;
	}

}
