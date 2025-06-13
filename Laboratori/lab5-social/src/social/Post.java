package social;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;

@Entity
public class Post {
    @Id
    private String id;
    private String personCode;
    private String content;
    private long timeStamp;

    @ManyToOne
    @JoinTable (
        name = "person_post",
        joinColumns = @JoinColumn(name = "person_id"),
        inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private Person person;

    public Post() {

    }

    public Post(String personCode, String content, Person person) {
        this.personCode = personCode;
        this.content = content;
        this.timeStamp = System.currentTimeMillis();
        this.id = personCode + "_" + this.timeStamp;
        this.person = person;
    }

    public String getId() {
        return id;
    }

    public String getPersonCode() {
        return personCode;
    }

    public String getContent() {
        return content;
    }

    public Person getPerson() {
        return person;
    }
    
    public long getTime() {
        return timeStamp;
    }
    
}
