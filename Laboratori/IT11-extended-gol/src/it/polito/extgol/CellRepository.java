package it.polito.extgol;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class CellRepository extends GenericExtGOLRepository<Cell, Long> {

    public CellRepository() {
        super(Cell.class);
    }


    // Save a cell to the database
    public List<Cell> load(Long gameId){
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Cell> obtainCellQuery = em.createQuery("SELECT c FROM Cell c WHERE game_id='?1'", Cell.class);
            obtainCellQuery.setParameter(1, gameId);
            return obtainCellQuery.getResultList();
        } finally {
            em.close();
        }
    }



    
}
