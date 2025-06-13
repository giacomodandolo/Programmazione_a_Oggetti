package social;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;

public class GroupRepository extends GenericRepository<Group, String> {

    public GroupRepository() {
      super(Group.class);
    }

    public Optional<Group> getGroupByName(String name) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            List<Group> groups = em.createQuery(
              "SELECT g FROM Group g WHERE name=\'" + name + "\'", 
              Group.class
            ).getResultList();

            if (groups.isEmpty())
                return Optional.empty();
            
            return Optional.of(groups.get(0));
        } finally {
            em.close();
        }
    }
}
