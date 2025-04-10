package university;
import java.util.logging.Logger;

/**
 * This class represents a university education system.
 * 
 * It manages students and courses.
 *
 */
public class University {
	
	private final int MAX_STUDENTS = 1000;
	private final int MAX_COURSES = 50;
    private final int STARTING_ID = 10000;
    private final int STARTING_CODE = 10;

	private String name;
	private String rector;
	private Student[] students;
	private int numberEnrolled; 
	private Course[] courses;
	private int numberCourses;

	public int translateIDToIndex(int ID) {
        return ID-STARTING_ID;
    }

	private int translateCodeToIndex(int code) {
        return code-STARTING_CODE;
    }

// R1
	/**
	 * Constructor
	 * @param name name of the university
	 */
	public University(String name){
		// Example of logging
		// logger.info("Creating extended university object");
		this.name = name;
		this.students = new Student[MAX_STUDENTS];
		this.numberEnrolled = 0;
		this.courses = new Course[MAX_COURSES];
		this.numberCourses = 0;
	}
	
	/**
	 * Getter for the name of the university
	 * 
	 * @return name of university
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Defines the rector for the university
	 * 
	 * @param first first name of the rector
	 * @param last	last name of the rector
	 */
	public void setRector(String first, String last){
		rector = first + " " + last;
	}
	
	/**
	 * Retrieves the rector of the university with the format "First Last"
	 * 
	 * @return name of the rector
	 */
	public String getRector(){
		return rector;
	}
	
// R2
	/**
	 * Enrol a student in the university
	 * The university assigns ID numbers 
	 * progressively from number 10000.
	 * 
	 * @param first first name of the student
	 * @param last last name of the student
	 * 
	 * @return unique ID of the newly enrolled student
	 */
	public int enroll(String first, String last){
		students[numberEnrolled] = new Student(STARTING_ID + numberEnrolled, first, last);
		numberEnrolled++;
		logger.info("New student enrolled: " + (STARTING_ID+numberEnrolled-1) + ", " + first + " " + last);
		return students[numberEnrolled-1].getID();
	}
	
	/**
	 * Retrieves the information for a given student.
	 * The university assigns IDs progressively starting from 10000
	 * 
	 * @param id the ID of the student
	 * 
	 * @return information about the student
	 */
	public String student(int id){
		int index = translateIDToIndex(id); 
		return students[index].getInfo();
	}
	
// R3
	/**
	 * Activates a new course with the given teacher
	 * Course codes are assigned progressively starting from 10.
	 * 
	 * @param title title of the course
	 * @param teacher name of the teacher
	 * 
	 * @return the unique code assigned to the course
	 */
	public int activate(String title, String teacher){
		courses[numberCourses] = new Course(STARTING_CODE + numberCourses, title, teacher);
		numberCourses++;
		logger.info("New course activated: " + (STARTING_CODE+numberCourses-1) + ", " + title + " " + teacher);
		return courses[numberCourses-1].getCode();
	}
	
	/**
	 * Retrieve the information for a given course.
	 * 
	 * The course information is formatted as a string containing 
	 * code, title, and teacher separated by commas, 
	 * e.g., {@code "10,Object Oriented Programming,James Gosling"}.
	 * 
	 * @param code unique code of the course
	 * 
	 * @return information about the course
	 */
	public String course(int code){
		int index = translateCodeToIndex(code);
		return courses[index].getInfo();
	}
	
// R4
	/**
	 * Register a student to attend a course
	 * @param studentID id of the student
	 * @param courseCode id of the course
	 */
	public void register(int studentID, int courseCode){
		int indexStudent = translateIDToIndex(studentID), 
			indexCourse = translateCodeToIndex(courseCode);

		if (indexStudent >= numberEnrolled || indexCourse >= numberCourses)
			return;

		students[indexStudent].addCourse(courseCode);
		courses[indexCourse].addAttendee(studentID);

		logger.info("Student " + studentID + " signed up for course " + courseCode);
	}
	
	/**
	 * Retrieve a list of attendees.
	 * 
	 * The students appear one per row (rows end with `'\n'`) 
	 * and each row is formatted as describe in in method {@link #student}
	 * 
	 * @param courseCode unique id of the course
	 * @return list of attendees separated by "\n"
	 */
	public String listAttendees(int courseCode){
		int i, studentID, numberAttendeesCourse;
		String tmp = "";
		Course course = courses[translateCodeToIndex(courseCode)];

		if (course == null)
			return null;

		numberAttendeesCourse = course.getNumberAttendees();
		for (i = 0; i < numberAttendeesCourse; i++) {
			studentID = translateIDToIndex(course.getAttendee(i));
			tmp = tmp + students[studentID].getInfo() + "\n";
		}

		return tmp;
	}

	/**
	 * Retrieves the study plan for a student.
	 * 
	 * The study plan is reported as a string having
	 * one course per line (i.e. separated by '\n').
	 * The courses are formatted as describe in method {@link #course}
	 * 
	 * @param studentID id of the student
	 * 
	 * @return the list of courses the student is registered for
	 */
	public String studyPlan(int studentID){
		int i, courseCode, numberCoursesStudent;
		String tmp = "";
		Student student = students[translateIDToIndex(studentID)];

		if (student == null)
			return null;

		numberCoursesStudent = student.getNumberCourses();
		for (i = 0; i < numberCoursesStudent; i++) {
			courseCode = translateCodeToIndex(student.getCourse(i));
			tmp = tmp + courses[courseCode].getInfo() + "\n";
		}

		return tmp;
	}

// R5
	/**
	 * records the grade (integer 0-30) for an exam can 
	 * 
	 * @param studentId the ID of the student
	 * @param courseID	course code 
	 * @param grade		grade ( 0-30)
	 */
	public void exam(int studentId, int courseID, int grade) {
		int studentID = translateIDToIndex(studentId);
		int courseCode = translateCodeToIndex(courseID);
		if (students[studentID].checkCourse(courseID) == true) {
			students[studentID].addExam(courseID, grade);
			courses[courseCode].addExam(studentId, grade);
			logger.info("Student " + studentID + " took an exam in course " + courseCode + " with grade " + grade);
		}
	}

	/**
	 * Computes the average grade for a student and formats it as a string
	 * using the following format 
	 * 
	 * {@code "Student STUDENT_ID : AVG_GRADE"}. 
	 * 
	 * If the student has no exam recorded the method
	 * returns {@code "Student STUDENT_ID hasn't taken any exams"}.
	 * 
	 * @param studentId the ID of the student
	 * @return the average grade formatted as a string.
	 */
	public String studentAvg(int studentId) {
		int studentID = translateIDToIndex(studentId);
		float avg = students[studentID].getAverageGrade();

		if (avg == 0)
			return ("Student " + studentId + " hasn't taken any exams");
		
		return ("Student " + studentId + " : " + avg);
	}
	
	/**
	 * Computes the average grades of all students that took the exam for a given course.
	 * 
	 * The format is the following: 
	 * {@code "The average for the course COURSE_TITLE is: COURSE_AVG"}.
	 * 
	 * If no student took the exam for that course it returns {@code "No student has taken the exam in COURSE_TITLE"}.
	 * 
	 * @param courseId	course code 
	 * @return the course average formatted as a string
	 */
	public String courseAvg(int courseId) {
		int courseCode = translateCodeToIndex(courseId);
		float avg = courses[courseCode].getAverageGrade();

		if (avg == 0)
			return ("No student has taken the exam in " + courses[courseCode].getTitle());
		
		return ("The average for the course" + courses[courseCode].getTitle() + " is: " + avg);
	}
	

// R6
	/**
	 * Retrieve information for the best students to award a price.
	 * 
	 * The students' score is evaluated as the average grade of the exams they've taken. 
	 * To take into account the number of exams taken and not only the grades, 
	 * a special bonus is assigned on top of the average grade: 
	 * the number of taken exams divided by the number of courses the student is enrolled to, multiplied by 10.
	 * The bonus is added to the exam average to compute the student score.
	 * 
	 * The method returns a string with the information about the three students with the highest score. 
	 * The students appear one per row (rows are terminated by a new-line character {@code '\n'}) 
	 * and each one of them is formatted as: {@code "STUDENT_FIRSTNAME STUDENT_LASTNAME : SCORE"}.
	 * 
	 * @return info on the best three students.
	 */
	public String topThreeStudents() {
		int i, j;
		float[] scores = new float[numberEnrolled];
		float tmpS;
		int[] studentsIndex = new int[numberEnrolled];
		int tmpI;
		String leaderboard = "";

		for (i = 0; i < numberEnrolled; i++) {
			scores[i] = students[i].getScore();
			studentsIndex[i] = i;
		}

		for (i = 0; i < 3 && i < numberEnrolled; i++)
			for (j = 0; j < numberEnrolled-i-1; j++)
				if (scores[j] > scores[j+1]) {
					tmpS = scores[j];
					scores[j] = scores[j+1];
					scores[j+1] = tmpS;
					tmpI = studentsIndex[j];
					studentsIndex[j] = studentsIndex[j+1];
					studentsIndex[j+1] = tmpI;
				}
		
		for (i = 0; i < 3 && i < numberEnrolled; i++)
			if (scores[numberEnrolled-i-1] != 0.0)
				leaderboard = leaderboard + students[studentsIndex[numberEnrolled-i-1]].getInfoScore() + "\n";

		return leaderboard;
	}

// R7
    /**
     * This field points to the logger for the class that can be used
     * throughout the methods to log the activities.
     */
    public static final Logger logger = Logger.getLogger("University");

}
