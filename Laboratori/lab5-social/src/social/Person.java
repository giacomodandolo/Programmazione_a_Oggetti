package social;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
class Person {
  @Id
  private String code;
  private String name;
  private String surname;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable (
    name = "friendship",
    joinColumns = @JoinColumn(name = "person1_id"),
    inverseJoinColumns = @JoinColumn(name = "person2_id")
  )
  private Set<Person> friends = new HashSet<>();

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name = "group_of_people",
    joinColumns = @JoinColumn(name = "group_id"),
    inverseJoinColumns = @JoinColumn(name = "person_id")
  )
  private Set<Group> groups = new HashSet<>();

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "person")
  private Set<Post> posts = new HashSet<>();

  public Person() {
    // default constructor is needed by JPA
  }

  Person(String code, String name, String surname) {
    this.code = code;
    this.name = name;
    this.surname = surname;
  }

  String getCode() {
    return code;
  }

  String getName() {
    return name;
  }

  String getSurname() {
    return surname;
  }

  Collection<String> getFriends() {
    List<String> friendCodes = new ArrayList<>();
    for (Person f : friends)
      friendCodes.add(f.getCode());

    return friendCodes;
  }

  public void addFriend(Person person) {
    friends.add(person);
  }

  Collection<String> getGroups() {
    List<String> groupNames = new ArrayList<>();
    for (Group g : groups)
      groupNames.add(g.getName());

    return groupNames;
  }
  
  public void addToGroup(Group group) {
    groups.add(group);
  }

  public int numberOfFriends() {
    return friends.size();
  }

  public int numberOfGroups() {
    return groups.size();
  }

  Set<Post> getPosts() {
    return posts;
  }

  void addPost(Post p) {
    posts.add(p);
  }

  @Override
  public String toString() {
    return code + " " + name + " " + surname;
  }

  //....

}


