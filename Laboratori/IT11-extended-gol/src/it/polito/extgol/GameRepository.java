package it.polito.extgol;

import java.util.Map;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

class GameRepository extends GenericExtGOLRepository<Game, Long> {

    public GameRepository() {
        super(Game.class);
    }

    /**
     * Loads the events to game.
     * 
     * @param gameId identification of the gmae to find the events of
     * @return Map with the events related to the object
     */
    public Map<Integer, EventType> getEventsMapForGame(Long gameId) {
        Optional<Game> game = findById(gameId);
        return game.isPresent() ? game.get().getEventMapInternal() : null;
    }

    /**
     * Loads the game and returns its value the the caller of the game.
     * 
     * @param gameId identification of the game to load
     * @param boardRepository identification of the game to load
     * @param cellRepository identification of the game to load
     * @return obtain game instance 
     */
    public Game load(Long gameId, BoardRepository boardRepository, CellRepository cellRepository) {
        EntityManager em = JPAUtil.getEntityManager();
        Game game;

        try {
            // obtain game from database
            TypedQuery<Game> obtainGameQuery = em.createQuery(
                "SELECT g FROM Game g WHERE game_id='?1'", 
                Game.class
            );
            obtainGameQuery.setParameter(1, gameId);
            
            game = obtainGameQuery.getSingleResult();
            
            // set the board from database by using load() method of boardRepository
            game.setBoard(boardRepository.load(gameId, cellRepository));

            // obtain the list of generations and insert into the game instance
            TypedQuery<Generation> obtainGenerationsQuery = em.createQuery(
                "SELECT g FROM Generation g WHERE game_id='?1'", 
                Generation.class
            );
            obtainGenerationsQuery.setParameter(1, gameId);
            
            obtainGenerationsQuery.getResultStream()
            .forEach(
                g -> game.addGeneration(g)
            );

            return game;
        } finally {
            em.close();
        }
    }
}
