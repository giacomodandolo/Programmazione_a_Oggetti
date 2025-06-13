package social;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Facade class for the social network system.
 * 
 */
public class Social {

  private final PersonRepository personRepository = new PersonRepository();
  private final GroupRepository groupRepository = new GroupRepository();
  private final PostRepository postRepository = new PostRepository();
  
  /**
   * Creates a new account for a person
   * 
   * @param code    nickname of the account
   * @param name    first name
   * @param surname last name
   * @throws PersonExistsException in case of duplicate code
   */
  public void addPerson(String code, String name, String surname) throws PersonExistsException {
    if (personRepository.findById(code).isPresent()){    // check if db already contains the code
        throw new PersonExistsException();
    }
    Person person = new Person(code, name, surname);    // create the person as a POJO
    personRepository.save(person);                      // save it to db
  }

  /**
   * Retrieves information about the person given their account code.
   * The info consists in name and surname of the person, in order, separated by
   * blanks.
   * 
   * @param code account code
   * @return the information of the person
   * @throws NoSuchCodeException if a person with that code does not exist
   */
  public String getPerson(String code) throws NoSuchCodeException {
    Optional<Person> person = personRepository.findById(code);
    if (person.isEmpty()) {
      throw new NoSuchCodeException();
    }
    
    return person.get().toString();
  }

  /**
   * Define a friendship relationship between two persons given their codes.
   * <p>
   * Friendship is bidirectional: if person A is adding as friend person B, that means
   * that person B automatically adds as friend person A.
   * 
   * @param codePerson1 first person code
   * @param codePerson2 second person code
   * @throws NoSuchCodeException in case either code does not exist
   */
  public void addFriendship(String codePerson1, String codePerson2)
      throws NoSuchCodeException {
    Optional<Person> p1 = personRepository.findById(codePerson1);
    Optional<Person> p2 = personRepository.findById(codePerson2);

    if (p1.isEmpty() || p2.isEmpty()) {
      throw new NoSuchCodeException();
    }

    p1.get().addFriend(p2.get());
    p2.get().addFriend(p1.get());
    
    personRepository.update(p1.get());
    personRepository.update(p2.get());
  }

  /**
   * Retrieve the collection of their friends given the code of a person.
   *
   * @param codePerson code of the person
   * @return the list of person codes
   * @throws NoSuchCodeException in case the code does not exist
   */
  public Collection<String> listOfFriends(String codePerson)
      throws NoSuchCodeException {
    Optional<Person> person = personRepository.findById(codePerson);

    if (person.isEmpty()) {
      throw new NoSuchCodeException();
    }
    
    return person.get().getFriends();
  }

  /**
   * Creates a new group with the given name
   * 
   * @param groupName name of the group
   * @throws GroupExistsException if a group with given name does not exist
   */
  public void addGroup(String groupName) throws GroupExistsException {
    if (groupRepository.getGroupByName(groupName).isPresent()){
        throw new GroupExistsException();
    }
    Group group = new Group(groupName);
    groupRepository.save(group);
  }

  /**
   * Deletes the group with the given name
   * 
   * @param groupName name of the group
   * @throws NoSuchCodeException if a group with given name does not exist
   */
  public void deleteGroup(String groupName) throws NoSuchCodeException {
    Optional<Group> group = groupRepository.getGroupByName(groupName);
    
    if (group.isEmpty())
      throw new NoSuchCodeException();
    groupRepository.delete(group.get());
  }

  /**
   * Modifies the group name
   * 
   * @param groupName name of the group
   * @throws NoSuchCodeException if the original group name does not exist
   * @throws GroupExistsException if the target group name already exist
   */
  public void updateGroupName(String groupName, String newName) throws NoSuchCodeException, GroupExistsException {
    Optional<Group> group = groupRepository.getGroupByName(groupName);
    Optional<Group> otherGroup = groupRepository.getGroupByName(newName);
    
    if (group.isEmpty())
      throw new NoSuchCodeException();

    if (otherGroup.isPresent())
      throw new GroupExistsException();

    group.get().updateName(newName);
    groupRepository.update(group.get());
  }

  /**
   * Retrieves the list of groups.
   * 
   * @return the collection of group names
   */
  public Collection<String> listOfGroups() {
    List<Group> groups = groupRepository.findAll();
    List<String> groupNames = new ArrayList<>();
    for (Group g : groups)
      groupNames.add(g.getName());

    return groupNames; 
  }

  /**
   * Add a person to a group
   * 
   * @param codePerson person code
   * @param groupName  name of the group
   * @throws NoSuchCodeException in case the code or group name do not exist
   */
  public void addPersonToGroup(String codePerson, String groupName) throws NoSuchCodeException {
    Optional<Person> person = personRepository.findById(codePerson);
    Optional<Group> group = groupRepository.getGroupByName(groupName);

    if (person.isEmpty() || group.isEmpty())
      throw new NoSuchCodeException();

    person.get().addToGroup(group.get());
    group.get().addPersonToGroup(person.get());

    personRepository.update(person.get());
    groupRepository.update(group.get());
  }

  /**
   * Retrieves the list of people on a group
   * 
   * @param groupName name of the group
   * @return collection of person codes
   */
  public Collection<String> listOfPeopleInGroup(String groupName) {
    Optional<Group> group = groupRepository.getGroupByName(groupName);

    return (group.isEmpty()) ? null : group.get().getPeople();
  }

  /**
   * Retrieves the code of the person having the largest
   * group of friends
   * 
   * @return the code of the person
   */
  public String personWithLargestNumberOfFriends() {
    List<Person> people = personRepository.findAll();
    
    Optional<Person> maxPerson = 
      people.stream()
      .max(Comparator.comparing(Person::numberOfFriends));

    return (maxPerson.isEmpty()) ? null : maxPerson.get().getCode(); 
  }

  /**
   * Find the name of group with the largest number of members
   * 
   * @return the name of the group
   */
  public String largestGroup() {
    List<Group> groups = groupRepository.findAll();
    
    Optional<Group> maxGroup = 
      groups.stream()
      .max(Comparator.comparing(Group::numberOfPeople));

    return (maxGroup.isEmpty()) ? null : maxGroup.get().getName(); 
  }

  /**
   * Find the code of the person that is member of
   * the largest number of groups
   * 
   * @return the code of the person
   */
  public String personInLargestNumberOfGroups() {
    List<Person> people = personRepository.findAll();
    
    Optional<Person> maxPerson = 
      people.stream()
      .max(Comparator.comparing(Person::numberOfGroups));

    return (maxPerson.isEmpty()) ? null : maxPerson.get().getCode(); 
  }

  // R5

  /**
   * add a new post by a given account
   * 
   * @param authorCode the id of the post author
   * @param text   the content of the post
   * @return a unique id of the post
   */
  public String post(String authorCode, String text) {
    Person person = personRepository.findById(authorCode).get();
    Post post = new Post(authorCode, text, person);
    postRepository.save(post);

    person.addPost(post);
    personRepository.update(person);

    return post.getId();
  }

  /**
   * retrieves the content of the given post
   * 
   * @param pid    the id of the post
   * @return the content of the post
   */
  public String getPostContent(String pid) {
    Optional<Post> post = postRepository.findById(pid);
    
    return post.get().getContent(); // TO BE IMPLEMENTED
  }

  /**
   * retrieves the timestamp of the given post
   * 
   * @param pid    the id of the post
   * @return the timestamp of the post
   */
  public long getTimestamp(String pid) {
    Optional<Post> post = postRepository.findById(pid);
    
    return post.get().getTime(); // TO BE IMPLEMENTED
  }

  /**
   * returns the list of post of a given author paginated
   * 
   * @param author     author of the post
   * @param pageNo     page number (starting at 1)
   * @param pageLength page length
   * @return the list of posts id
   */
  public List<String> getPaginatedUserPosts(String author, int pageNo, int pageLength) {
    Set<Post> posts = personRepository.findById(author)
      .get().getPosts();
    
    List<Post> sortedPosts = posts.stream()
    .sorted(Comparator.comparing(Post::getTime).reversed())
    .toList();
    
    int start = (pageNo-1)*pageLength;
    int end = start+pageLength;

    List<String> limitedPosts = new ArrayList<>();
    for (int i = start; i < sortedPosts.size() && i < end; i++)
      limitedPosts.add(sortedPosts.get(i).getId());

    return limitedPosts;
  }

  private String formatPost(String postInfo) {
    String user, postId;
    String[] postSplit;

    postSplit = postInfo.split("_");

    user = postSplit[0];
    postId = postInfo;

    return user + ":" + postId;
  }

  /**
   * returns the paginated list of post of friends.
   * The returned list contains the author and the id of a post separated by ":"
   * 
   * @param author     author of the post
   * @param pageNo     page number (starting at 1)
   * @param pageLength page length
   * @return the list of posts key elements
   */
  public List<String> getPaginatedFriendPosts(String author, int pageNo, int pageLength) {
    // TODO: il metodo non funziona correttamente
    Collection<String> friends = personRepository.findById(author).get().getFriends();
    List<String> userPosts = new ArrayList<>();
    List<String> friendPosts = new ArrayList<>();
    List<String> sortedPosts;
    Set<Post> posts;
    int i;

    for (String friend : friends) {
      posts = personRepository.findById(friend)
        .get().getPosts();

      userPosts.addAll(
        posts.stream()
        .sorted(Comparator.comparing(Post::getTime).reversed())
        .map(p -> p.getId())
        .toList()
      );
    }

    sortedPosts = userPosts.stream()
      .sorted(
        Comparator.comparing((up) -> ((String) up).split("_")[1].split(":")[0]).reversed()
      )
      .toList();

    for (i = 0; i < pageLength; i++)
      if ((pageNo-1)*pageLength + i < sortedPosts.size())
        friendPosts.add(formatPost(sortedPosts.get((pageNo-1)*pageLength + i)));

    return friendPosts; 
  }

}