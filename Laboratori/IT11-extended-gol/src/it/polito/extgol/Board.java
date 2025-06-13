package it.polito.extgol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MapKey;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;

/**
 * Entity representing the game board grid in the Extended Game of Life.
 *
 * This class models a two-dimensional board of fixed dimensions (defaulting to 5×5)
 * composed of Tile entities. It maintains the mapping from coordinates to tiles,
 * establishes neighbor relationships, and provides both basic Conway visualization
 * and hooks for extended behaviors such as currentEnergy modifiers, interactive tiles,
 * and analytic methods over cell lifePoints.
 *
 * Core responsibilities:
 * - Persistence via JPA annotations (@Entity, @Id, @OneToMany, etc.)
 * - Initialization of the tile grid and adjacency links
 * - Retrieval of tiles and cells for simulation logic
 * - String-based visualization of cell states in a generation
 * - Factory support for the extended version (interactable tiles, default moods/types)
 * - Analytic operations over generations (e.g., counting, grouping, statistics)
 */
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Number of columns on the board. */
    @Column(nullable = false)
    private Integer width=5;

    /** Number of rows on the board. */
    @Column(nullable = false)    
    private Integer height=5;

    /** Inverse one-to-one back to owning Game. */
    @OneToOne(mappedBy = "board", fetch = FetchType.LAZY)
    private Game game;

    /**
     * Map of tile coordinates to Tile entities.  
     */
    @OneToMany(
      mappedBy      = "board",
      cascade       = CascadeType.ALL,
      orphanRemoval = true,
      fetch         = FetchType.LAZY
    )
    @MapKey(name = "tileCoord")
    private Map<Coord, Tile> tiles = new HashMap<>();
    
    /**
     * Default constructor required by JPA.
     */
    public Board() {}

    /**
     * Constructs a Board of the given width and height, associates it with the
     * specified Game (if non-null), and initializes all Tiles and their neighbor
     * relationships.
     *
     * @param width  the number of columns in the board grid
     * @param height the number of rows in the board grid
     * @param g      the Game instance this board belongs to;
     */
    public Board(int width, int height, Game g) {
        this.width = width;
        this.height = height;
        this.game = g;
        initializeTiles();
    }

    /**
     * Factory method to create a fully initialized Board for the extended Game of Life.
     * 
     * This sets up a new Board of the given dimensions, associates it with the provided
     * Game instance, and applies default extended settings:
     * 
     *   -All tiles are made interactable with a lifePointModifier of 0.
     *   -All cells are initialized with a NAIVE mood and BASIC cell type.
     *
     *
     * @param width  the number of columns on the board
     * @param height the number of rows on the board
     * @param game   the Game instance to which this board belongs
     * @return the Board instance ready for use in the extended simulation
     */
    public static Board createExtended(int width, int height, Game game) {
        Board board = new Board(width, height, game);

        // Initialize all tiles as interactable with zero modifier
        for (Tile t : board.getTiles()) {
            Board.setInteractableTile(board, t.getCoordinates(), 0);
        }

        // Set default mood and type for every cell
        for (Cell c : board.getCellSet()) {
            c.setMood(CellMood.NAIVE);
            c.setType(CellType.BASIC);
        }

        return board;
    }

    /**
     * Populates and links all Tile instances for this Board.
     *
     * This method clears any existing tiles, then:
     *   1. Creates a Tile at each (x, y) coordinate within the board’s width and height,
     *      associates it with this Board and its Game, and stores it in the tiles map.
     *   2. Iterates over every Tile to establish neighbor relationships by
     *      calling Tile's initializeNeighbors(...) with the adjacent tiles.
     *
     * This setup ensures each tile knows its position and its surrounding tiles,
     * enabling neighbor-based logic in the simulation.
     */
    private void initializeTiles() {
        tiles.clear();        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Tile tile = new Tile(x, y, this, this.game);
                tile.setBoard(this);
                tiles.put(tile.getCoordinates(), tile);
            }
        }
        for (Tile t : tiles.values()) {   
            t.initializeNeighbors(getAdjacentTiles(t));
        }
    }

    /**
     * Computes and returns all neighboring Tiles surrounding the specified tile.
     *
     * Iterates over the eight possible offsets (dx, dy) around the tile’s coordinates,
     * skips the tile itself, and includes only those tiles that exist within the neighborhood.
     *
     * @param tile the central Tile for which neighbors are sought
     * @return a Set of adjacent Tile instances (up to eight) surrounding the given tile
     */
    public Set<Tile> getAdjacentTiles(Tile tile) {
        Set<Tile> adj = new HashSet<>();
        int cx = tile.getX();
        int cy = tile.getY();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                // skip the center tile itself
                if (dx == 0 && dy == 0) continue;

                Tile t = getTile(new Coord(cx + dx, cy + dy));
                
                if (t != null) { // skipping null references (e.g., border conditions)
                    adj.add(t);
                }
            }
        }
        return adj;
    }

    /**
    * Getters and setters
    */

    /**
     * Returns the unique identifier for this Board.
     *
     * @return the board’s id
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieves the Tile at the specified coordinates.
     *
     * @param c the Coord position to look up
     * @return the Tile at those coordinates
     */
    public Tile getTile(Coord c){
        return tiles.get(c);
    }

    /**
     * Returns an immutable list of all Tiles on this Board.
     *
     * This defensive copy prevents external modification of the board’s tile collection.
     *
     * @return a List of all Tile instances on the board
     */
    public List<Tile> getTiles() {
        return List.copyOf(tiles.values());
    }

    /**
     * Gathers and returns the set of all Cells currently placed on this Board.
     *
     * @return a Set of all Cell instances belonging to this board
     */
    public Set<Cell> getCellSet() {
        Set<Cell> cellSet = new HashSet<>();
        for (Tile t : tiles.values()) {
            cellSet.add(t.getCell());
        }
        return cellSet;
    }
    
    /**
     * Returns a textual representation of the board for the specified generation.
     * Each live cell is displayed with a different character based on its type:
     * - BASIC      → 'C'
     * - HIGHLANDER → 'H'
     * - LONER      → 'L'
     * - SOCIAL     → 'S'
     * All other positions (dead cells) are represented with '0'.
     *
     * The output is a string where each line corresponds to a row of the board,
     * and rows are separated by System.lineSeparator().
     *
     * @param generation the generation of living cells to visualize
     * @return a multiline string representing the board’s state
     */
    public String visualize(Generation generation) {
        //    based on the type of each live cell.
        Map<Coord, Character> symbolMap = new HashMap<>();
        for (Cell c : generation.getAliveCells()) {
            Coord coord = c.getCoordinates();
            // Depending on the cell type, associate the corresponding character
            switch (c.getType()) {
                case BASIC:
                    symbolMap.put(coord, 'C');        
                    break;
                case HIGHLANDER:
                    symbolMap.put(coord, 'H');       
                    break;
                case LONER:
                    symbolMap.put(coord, 'L');        
                    break;
                case SOCIAL:
                    symbolMap.put(coord, 'S');        
                    break;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            //    check if there is a live cell at that position
            for (int x = 0; x < width; x++) {
                Coord currentCoord = new Coord(x, y);
                if (symbolMap.containsKey(currentCoord)) {
                    // If the map contains the coordinate, append the corresponding character
                    sb.append(symbolMap.get(currentCoord));
                } else {
                    // Otherwise, append '0' to indicate a dead cell
                    sb.append('0');
                }
            }
            // Add the line separator only if this is not the last row
            if (y < height - 1) {
                sb.append(System.lineSeparator());
            }
        }
        //Convert the StringBuilder to a string and return it
        return sb.toString();
    }

    
    // EXTENDED BEHAVIORS

    /**
     * Creates an interactable Tile with the specified lifePoints modifier 
     * at the given coordinate on the board.
     *
     * @param board               the Board on which to place the tile
     * @param coord               the Coord position for the new tile
     * @param lifePointsModifier  the amount of lifePoints this tile will add (or subtract) each generation
     * @return the newly created Interactable tile
     */
    public static Interactable setInteractableTile(Board board, Coord coord, Integer lifePointsModifier) {
        Tile tile=board.getTile(coord);
        tile.setLifePointModifier(lifePointsModifier);

        return tile;
    }

    /**
     * Returns the total number of alive cells in the given generation.
     *
     * @param gen the Generation instance to analyze
     * @return the count of alive cells in gen
     */
    public Integer countCells(Generation generation) {
        return generation.getAliveCells().size();
    }

    /**
     * Finds the single cell with the highest lifePoints in the given generation.
     * In case of a tie, returns the cell closest to the top-left corner.
     *
     * @param gen the Generation instance to analyze
     * @return the Cell with maximum lifePoints, or null if no cells are alive
     */
    public Cell getHighestEnergyCell(Generation gen) {
        Cell highestEnergyCell = null;
        int maxEnergy = Integer.MIN_VALUE;

        for (Cell c : gen.getAliveCells()) {
            int currentEnergy = c.getLifePoints();
            if (currentEnergy > maxEnergy) {
                // nuovo massimo
                maxEnergy = currentEnergy;
                highestEnergyCell = c;
            } else if (currentEnergy == maxEnergy) {
                // pareggio: confronto la distanza quadrata da (0,0)
                int currentDistance = c.getX()*c.getX() + c.getY()*c.getY();
                int bestDistance = highestEnergyCell.getX()*highestEnergyCell.getX() + highestEnergyCell.getY()*highestEnergyCell.getY();
                if (currentDistance < bestDistance) {
                    highestEnergyCell = c;
                }
            }
        }        

        return highestEnergyCell;
    }

    /**
     * Groups all alive cells in the generation by their currentCoord lifePoints.
     *
     * @param gen the Generation instance to analyze
     * @return a Map from lifePoints value to the List of Cells having that currentEnergy
     */
    public Map<Integer, List<Cell>> getCellsByEnergyLevel(Generation gen) {
        Map<Integer, List<Cell>> cellGroupMap= new HashMap<>();
        Integer maxEnergy=this.getHighestEnergyCell(gen).getLifePoints();
        Set<Cell> cellSet=gen.getAliveCells();
        
        for(Integer i=0;i<=maxEnergy;i++){
            List<Cell> cellList=new ArrayList<>();
            for(Cell c:cellSet){
                if(c.getLifePoints()==i){
                    cellList.add(c);
                }
            }
            cellGroupMap.put(i,cellList);
        }
        return cellGroupMap;
    }

    /**
     * Counts alive cells per CellType in the given generation.
     *
     * @param gen the Generation instance to analyze
     * @return a Map from CellType to the count of alive cells of that type
     */
    public Map<CellType, Integer> countCellsByType(Generation gen) {
        Map<CellType, Integer> cellNumberForType = new HashMap<>();
        for (Cell c : gen.getAliveCells()) {
            CellType type = c.getType();
            cellNumberForType.put(type, cellNumberForType.getOrDefault(type, 0) + 1);
        }
        return cellNumberForType;
    }

    /**
     * Returns the top n cells sorted by descending lifePoints.
     *
     * @param gen the Generation instance to analyze
     * @param n   the number of top-currentEnergy cells to return
     * @return a List of the top n Cells by lifePoints, in descending order
     */
    public List<Cell> topEnergyCells(Generation gen, int n) {
        Integer maxEnergy=this.getHighestEnergyCell(gen).getLifePoints();
        Map<Integer, List<Cell>> cellGroupMap = getCellsByEnergyLevel(gen);
        Integer size=0,flag=0;
        List<Cell> topEnergyCells= new ArrayList<>();
        for(Integer i=maxEnergy;i>=0;i--){
            if(flag==1){
                break;
            }
            List<Cell>cellList=cellGroupMap.remove(i);
            for(Cell c:cellList){
                if(size==n){
                    flag=1;
                    break;
                }
                topEnergyCells.add(c);
                size++;
            }
        }
        return topEnergyCells;
    }


    
    /**
     * Finds the single cell with the most neighbors in the given generation.
     * In case of a tie, returns the cell closest to the top-left corner.
     * Method like getHighestEnergyCell for groupByAliveNeighborCount()
     * 
     * @param gen the Generation instance to analyze
     * @return the Cell with maximum lifePoints, or null if no cells are alive
     */
    public Cell getCellWithMostNeighbor(Generation gen) {
        int mostNeighbors=Integer.MIN_VALUE;
        Cell mostNeighborsCell=null;

        for(Cell c: gen.getAliveCells()){
            int currentNeighbors=c.countAliveNeighbors();
            if (currentNeighbors>mostNeighbors){
                // nuovo massimo
                mostNeighbors=currentNeighbors;
                mostNeighborsCell=c;
            }else if(currentNeighbors==mostNeighbors){
                // pareggio: confronto la distanza quadrata da (0,0)
                int currentDistance = c.getX()*c.getX() + c.getY()*c.getY();
                int bestDistance = mostNeighborsCell.getX()*mostNeighborsCell.getX() + mostNeighborsCell.getY()*mostNeighborsCell.getY();
                if (currentDistance < bestDistance) {
                    mostNeighborsCell = c;
                }
            }
        }
        return mostNeighborsCell;
    }

    /**
     * Groups each alive cell by its number of live neighbors.
     *
     * @param gen the Generation instance to analyze
     * @return a Map from neighbor count to the List of Cells having that many alive neighbors
     */
    public Map<Integer, List<Cell>> groupByAliveNeighborCount(Generation gen) {
        Map<Integer, List<Cell>> cellGroupMap= new HashMap<>();
        Integer mostNeighbours=this.getCellWithMostNeighbor(gen).countAliveNeighbors();
        Set<Cell> cellSet=gen.getAliveCells();
        
        for(Integer i=0;i<=mostNeighbours;i++){
            List<Cell> cellList=new ArrayList<>();
            for(Cell c:cellSet){
                if(c.countAliveNeighbors()==i){
                    cellList.add(c);
                }
            }
            cellGroupMap.put(i,cellList);
        }
        return cellGroupMap;
    }

    /**
     * Computes summary statistics (count, min, maxEnergy, sum, average) over all alive cells’ lifePoints.
     *
     * @param gen the Generation instance to analyze
     * @return an IntSummaryStatistics with aggregated lifePoints metrics
     */
    public IntSummaryStatistics energyStatistics(Generation gen) {    
        return game.getGenerations()
            .get(game.getGenerations().indexOf(gen))
            .getAliveCells()
            // crea uno Stream<Cell>
            .stream()
            // trasforma lo stream in un IntStream mappati a valori interi 
            // che rappresentano la vita della cella
            .mapToInt(cell -> gen.getCellLifePoints().get(cell))
            // raccoglie count, min, maxEnergy, sum e average
            .summaryStatistics();
    }

    /**
     * Returns a time series of currentEnergy statistics for each generation step in [fromStep, toStep].
     *
     * @param fromStep the starting generation index (inclusive)
     * @param toStep   the ending generation index (inclusive)
     * @return a Map from generation step index to its IntSummaryStatistics
     */
    public Map<Integer, IntSummaryStatistics> getTimeSeriesStats(int fromStep, int toStep) {
        return IntStream.rangeClosed(fromStep, toStep)
            .boxed()
            .collect(
                Collectors.toMap(
                    step -> step,
                    step -> energyStatistics(
                        game.getGenerations().get(step)
                    )
                )
            );
    }
}
