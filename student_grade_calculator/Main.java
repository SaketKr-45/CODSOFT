package student_grade_calculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
            ArrayList<Float> marks = new ArrayList<>();

            int totalSubjects;
            while(true){
                try{
                    System.out.print("Enter number of subjects: ");
                    totalSubjects = Integer.parseInt(br.readLine());
                    if(totalSubjects < 1){
                        System.out.println("Number of subjects should be atleast 1.");
                        continue;
                    }
                    break;
                }catch (NumberFormatException e){
                    System.out.println("Invalid Input!! Please enter a number.");
                }
            }

            for (int i = 0 ; i < totalSubjects ; i++){
                float inputMarks;
                while(true){
                    try{
                        System.out.printf("Enter marks of subject %d: ", i+1);
                        inputMarks = Float.parseFloat(br.readLine());

                        if(inputMarks < 0 || inputMarks > 100){
                            System.out.println("Marks should be in the range of 0-100.");
                            continue;
                        }

                        marks.add(inputMarks);
                        break;
                    }catch (NumberFormatException e){
                        System.out.println("Invalid Input!! Please enter a number.");
                    }
                }
            }

            System.out.printf("\nTotal marks: %.2f\nAverage marks: %.2f\nGrade: %s", sum(marks) , average(marks) , grade(average(marks)));
        }catch (IOException e){
            System.out.println("An error occurred while reading input: " + e.getMessage());
        }
    }

    private static float sum(ArrayList<Float> marks){
        float total = 0;
        for (float num : marks){
            total += num;
        }
        return total;
    }

    private static float average(ArrayList<Float> marks){
        float total = sum(marks);

        return total / marks.size();
    }

    private static String grade(float average){
        if (average > 90) return "A+";
        else if (average > 80) return "A-";
        else if (average > 70) return "B";
        else if (average > 60) return "C";
        else if (average > 50) return "D";
        else if (average > 40) return "E";
        else return "F";
    }
}