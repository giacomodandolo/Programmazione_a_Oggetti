package it.polito.extgol;

/**
 * Defines the types of cells, each with distinct behaviors in the extended Game of Life.
 */
public enum CellType {
    
    // (minNeighbors, maxNeighbors, lp)

    /**
     * Standard Conway cell: follows default Game of Life rules.
     */
    BASIC(2,3),
    
    /**
     * High-energy cell: it can withstand death-inducing conditions for three generations.
     */ 
    HIGHLANDER(2,3),
    
    /**
     * Isolationist cell: survives with as few as one neighbor.
     */
    LONER(1,3),
    
    /**
     * Crowd-loving cell: can survive even in highly populated situations,
     * up to eight neighbors.
     */
    SOCIAL(2,8);


    //Variables for the cell types    
    
    // The number of neighbors required to survive
    private int minNeighbors;

    // The maximum number of neighbors allowed

    private int maxNeighbors;

     // Constructor for CellType enum
    CellType(int minNeighbors, int maxNeighbors) {
        this.minNeighbors = minNeighbors;
        this.maxNeighbors = maxNeighbors;
    }

    //getters
    public int getMinNeighbors() {
        return minNeighbors;
    }
    public int getMaxNeighbors() {
        return maxNeighbors;
    }
}
