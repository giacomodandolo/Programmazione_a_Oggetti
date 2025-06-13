package social;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "single_group")
public class Group {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "groups")
    private Set<Person> people = new HashSet<>();

    public Group() {
        
    }

    public Group(String name) {
        this.name = name;
    }
 
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void updateName(String newName) {
        this.name = newName;
    }

    Collection<String> getPeople() {
        List<String> peopleCodes = new ArrayList<>();
        for (Person p : people)
            peopleCodes.add(p.getCode());

        return peopleCodes;
    }

    public void addPersonToGroup(Person person) {
        people.add(person);
    }

    public int numberOfPeople() {
        return people.size();
    }

}
