package student_management_system;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

class Student implements Serializable {
    private String name;
    private String grade;

    public Student(String name, String grade) {
        this.name = name;
        this.grade = grade;
    }
    public String getName() {
        return this.name;
    }
    public String getGrade() {
        return this.grade;
    }
}

class StudentManagementSystem{
    private final HashMap<Integer , Student> database = new HashMap<>();
    private final String dataFile = "database.dat";

    public StudentManagementSystem(){
        loadFile();
    }

    public void addStudent(int rollNumber , Student student){
        if (this.database.containsKey(rollNumber)){
            System.out.println(rollNumber + " already exists.\nData can not be added.\n");
            return;
        }

        this.database.put(rollNumber , student);
        System.out.println("Student data added successfully.\n");
        saveFile();
    }

    public void deleteStudent(int rollNumber){
        if (this.database.containsKey(rollNumber)){
            database.remove(rollNumber);
            System.out.println("Student data has been deleted successfully.\n");
            saveFile();
        }
        else {
            System.out.println(rollNumber + " does not exists.\n");
        }
    }

    public Student searchStudent(int rollNumber){
        if(this.database.containsKey(rollNumber)){
            return this.database.get(rollNumber);
        }

        return null;
    }

    public void displayAllStudents(){
        if (database.isEmpty()){
            System.out.println("No student record found.\n");
            return;
        }

        System.out.println("Roll Number\t\t\tName\t\t\tGrade");
        for (int rollNumber : this.database.keySet()){
            Student data = this.database.get(rollNumber);
            System.out.printf("%d\t\t\t%s\t\t\t%s\n",rollNumber , data.getName() , data.getGrade());
        }
        System.out.println();
    }

    public void updateStudent(int roll , Student student){
        if (database.containsKey(roll)){
            database.put(roll , student);
            System.out.println("Student data has been updated.\n");
            saveFile();
        }
        else {
            System.out.println("No student data found.\n");
        }
    }

    private void loadFile(){
        File file = new File(dataFile);
        if (!file.exists()){
            System.out.println("No existing data file found.");
            return;
        }

        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object redObject = ois.readObject();

            if (redObject instanceof HashMap){
                HashMap<Integer , Student> readFile = (HashMap<Integer, Student>) redObject;
                database.putAll(readFile);
            }
        } catch (ClassNotFoundException | IOException e) {
            System.out.println("Error loading data!!\n");
        }
    }

    private void saveFile(){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            oos.writeObject(database);
        } catch (IOException e) {
            System.out.println("Error saving file!!\n");
        }
    }
}

public class Main {
    private static Student createStudent(BufferedReader br) throws  IOException{
        HashSet<String> gradeList = new HashSet<>(Arrays.asList("A+" , "A" , "B" , "C" , "D" , "E" , "F"));

        String name;
        while (true){
            System.out.print("Enter name: ");
            name = br.readLine().trim().replaceAll("\\s+"," ").toUpperCase();
            if (name.isEmpty()){
                System.out.println("Input cannot be empty.\n");
                continue;
            }
            break;
        }

        String grade;
        while(true){
            System.out.print("Enter grade: ");
            grade = br.readLine().trim().replaceAll(" ","").toUpperCase();
            if (grade.isEmpty()){
                System.out.println("Input cannot be empty.\n");
                continue;
            }
            if (!gradeList.contains(grade)){
                System.out.println("Invalid grade. Enter grade from grade list: " + gradeList);
                continue;
            }
            break;
        }

        return new Student(name , grade);
    }

    private static int inputRollNumber(BufferedReader br) throws IOException{
        int rollNumber = 0;
        while (true){
            try {
                System.out.print("Roll Number: ");
                rollNumber = Integer.parseInt(br.readLine());
                if (rollNumber < 10000000 || rollNumber > 99999999) {
                    System.out.println("Enter 8-digit roll number.\n");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input!! Enter a number.\n");
            }
        }

        return rollNumber;
    }

    public static void main(String[] args) {
        StudentManagementSystem sms = new StudentManagementSystem();
        System.out.println("MENU:");

        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.print("1. Add new student\n2. Delete existing student\n3. Update data of an existing srudent\n4. Search for an existing student\n5. Display data of all existing students\n6. Exit application\nChoose: ");

                int choice = 0;
                while (true){
                    try {
                        choice = Integer.parseInt(br.readLine());
                        if (choice < 1 || choice > 6) {
                            System.out.println("Choose input between 1-6.\n");
                            continue;
                        }
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid Input!! Enter a number.\n");
                    }
                }

                switch (choice){
                    case 1: {
                        int rollNumber = inputRollNumber(br);
                        Student student = createStudent(br);
                        sms.addStudent(rollNumber , student);
                        break;
                    }
                    case 2: {
                        int rollNumber = inputRollNumber(br);
                        sms.deleteStudent(rollNumber);
                        break;
                    }
                    case 3: {
                        int rollNumber = inputRollNumber(br);
                        Student student = createStudent(br);
                        sms.updateStudent(rollNumber , student);
                        break;
                    }
                    case 4: {
                        int rollNumber = inputRollNumber(br);

                        Student student = sms.searchStudent(rollNumber);
                        if (student != null){
                            System.out.printf("Roll number - %d\nName - %s\nGrade - %s\n\n", rollNumber , student.getName() , student.getGrade());
                        }
                        else {
                            System.out.println("No student record found.\n");
                        }
                        break;
                    }
                    case 5: {
                        sms.displayAllStudents();
                        break;
                    }
                    default: {
                        System.out.println("\n\nExiting . . .\nThank you!!");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Exception caught");
        }
    }
}
