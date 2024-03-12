import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;

class Student implements Serializable {
    private String name;
    private int rollNumber;
    private String grade;

    public Student(String name, int rollNumber, String grade) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grade = grade;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public int getRollNumber() {
        return rollNumber;
    }

    public String getGrade() {
        return grade;
    }

    @Override
    public String toString() {
        return "Student{name='" + name + "', rollNumber=" + rollNumber + ", grade='" + grade + "'}";
    }
}

public class StudentManagementSystem {
    private List<Student> students;

    public StudentManagementSystem() {
        this.students = new ArrayList<>();
        loadStudents();
    }

    public void addStudent(Student student) {
        students.add(student);
        saveStudents();
    }

    public void removeStudent(int rollNumber) {
        students.removeIf(student -> student.getRollNumber() == rollNumber);
        saveStudents();
    }

    public Student searchStudent(int rollNumber) {
        return students.stream()
                .filter(student -> student.getRollNumber() == rollNumber)
                .findFirst()
                .orElse(null);
    }

    public void displayStudents() {
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            students.forEach(System.out::println);
        }
    }

    // File I/O methods for persistence
    private void loadStudents() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("students.dat"))) {
            students = (List<Student>) in.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No existing data found. Starting with an empty list.");
        } catch (Exception e) {
            System.out.println("Problem reading data: " + e.getMessage());
        }
    }

    private void saveStudents() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("students.dat"))) {
            out.writeObject(students);
        } catch (IOException e) {
            System.out.println("Problem saving students: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        StudentManagementSystem sms = new StudentManagementSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Student Management System ---");
            System.out.println("1. Add Student\n2. Remove Student\n3. Search for Student\n4. Display All Students\n5. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter roll number: ");
                    int rollNumber = scanner.nextInt();
                    System.out.print("Enter grade: ");
                    String grade = scanner.next();
                    sms.addStudent(new Student(name, rollNumber, grade));
                    System.out.println("Student added successfully.");
                    break;
                case 2:
                    System.out.print("Enter roll number of the student to remove: ");
                    int roll = scanner.nextInt();
                    sms.removeStudent(roll);
                    System.out.println("Student removed successfully.");
                    break;
                case 3:
                    System.out.print("Enter roll number to search for: ");
                    int searchRoll = scanner.nextInt();
                    Student foundStudent = sms.searchStudent(searchRoll);
                    if (foundStudent != null) {
                        System.out.println("Student found: " + foundStudent);
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;
                case 4:
                    System.out.println("Displaying all students:");
                    sms.displayStudents();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }
}