package it.polito.extgol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

/**
 * Repository per l'accesso ai dati della Board.
 * Estende GenericExtGOLRepository parametrizzato con Board e chiave primaria Long.
 */
public class BoardRepository extends GenericExtGOLRepository<Board, Long> {
    
    /**
     * Costruttore di default che invoca il costruttore della superclasse
     * specificando la classe Board.
     */
    public BoardRepository() {
        super(Board.class);
    }

    /**
     * Carica la Board associata a un game, insieme alle celle collegate alle Tile.
     * 
     * @param gameId            l'identificativo del game di cui recuperare la board
     * @param cellRepository    repository per il caricamento delle celle dal database
     * @return                  la Board completa di Tile e celle associate
     */
    public Board load(Long gameId, CellRepository cellRepository) {
        EntityManager em = JPAUtil.getEntityManager(); // ottiene l'EntityManager per le operazioni JPA
        Board board;
        Cell cellInsert;
        List<Cell> cells;
        // mappa per associare coordinate a Cell
        Map<Coord, Cell> cellsCoordinates = new HashMap<>();

        try {
            // Query per ottenere la Board dal database associata 1:1 con il game
            TypedQuery<Board> obtainBoardQuery = em.createQuery(
                "SELECT b FROM Board b WHERE b.game.id = :gameId", 
                Board.class
            );
            obtainBoardQuery.setParameter("gameId", gameId);
            board = obtainBoardQuery.getSingleResult(); // esegue la query e ottiene la Board

            // Carica tutte le celle associate al game tramite CellRepository
            cells = cellRepository.load(gameId);
            // Popola la mappa delle celle usando le coordinate come chiave
            for (Cell cell : cells) {
                cellsCoordinates.put(cell.getCoordinates(), cell);
            }

            // Associa a ogni Tile della Board la Cell corrispondente tramite le coordinate
            for (Tile tile : board.getTiles()) {
                cellInsert = cellsCoordinates.get(tile.getCoordinates());
                tile.setCell(cellInsert);
            }

            return board; // restituisce la Board completa di Tiles e Cells
        } finally {
            em.close(); // chiude sempre l'EntityManager per rilasciare risorse
        }
    }
}
