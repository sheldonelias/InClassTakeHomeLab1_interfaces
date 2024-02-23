package Controller;

//Package classes
import Model.Student;
import View.ResponseReport;
import Program.Program;

//Generics classes

import java.io.IOException;



import java.util.ArrayList;

//Utilities classes
import java.util.Random;
import java.util.Scanner;

public class RandomStudentPicker
{
    //STATIC VARIABLES


    //Model data variables
    public static ArrayList<Student> students;
    private static ArrayList<Student> unpickedStudents = new ArrayList<Student>();
    private static String classNam = "", classNum = "";

    //Controller data variables
    private static ResponseTally[] responseTallies;

    public static void initRandomStudentPicker(ArrayList<Student> attendingStudents,
                                               String classNumber, String className)
    {
        System.out.println("We are loading the Attendance List.");

        //Pass parameters to static variables
        classNum = classNumber;
        classNam = className;
        students = attendingStudents;

        //Initialize setup
        setUpRandomPickTally();

        //Run the program
        runRandomStudentPicker();

    }

    private static void loadUnpickedStudents()
    {
        unpickedStudents.addAll(students);
    }

    public static void runRandomStudentPicker()
    {
        System.out.println("Welcome to Random Student Picker.");
        System.out.println("Ready to run a round of Random Student Picker? (Y)es, (N)o, E(x)it");
        Scanner scnr = new Scanner(System.in);
        String userInput = scnr.next();
        switch (userInput)
        {
            case "y":
            case "Y":
                loadUnpickedStudents();
                pickStudent();
                break;
            case "n":
            case "N": System.out.println("Exporting response tally.");
                try
                {
                    ResponseReport.exportResponseTally(classNum, classNam, responseTallies);
                }catch(IOException excpt)
                {
                    System.out.println("Output file inaccessible. " + excpt.getMessage());
                }
                System.out.println("Bye bye, from Random Student Picker.");
                System.out.println("Bye bye. Exiting");
                System.exit(0);
                //TODO: We go back to main. Must clear all static variables.
                //Program.main(new String[0]);
                break;
            default: System.out.println("Entry invalid. Try again.");
                runRandomStudentPicker();
                break;
        }
    }



    public static void setUpRandomPickTally()
    {
        responseTallies = new ResponseTally[students.size()];

        for(int i = 0; i < students.size(); ++i)
        {
            responseTallies[i] = new ResponseTally();
            responseTallies[i].student = students.get(i);
        }
    }

    public static void pickStudent()
    {
        while(!unpickedStudents.isEmpty())
        {
            Random rand = new Random();
            int randIndex = rand.nextInt(unpickedStudents.size());

            System.out.println();
            System.out.println(unpickedStudents.get(randIndex).getFirstName() + " " +
                    unpickedStudents.get(randIndex).getLastName() +
                    " has been randomly picked.");

            System.out.println("Do they choose (A)nswer or (P)ass?");

            Scanner scnr = new Scanner(System.in);
            String userInput = scnr.next();

            switch(userInput)
            {
                case "a":
                case "A":
                    tallyStudentResponse(unpickedStudents.get(randIndex),"A");
                    unpickedStudents.remove(randIndex);
                    break;
                case "p":
                case "P":
                    tallyStudentResponse(unpickedStudents.get(randIndex),"P");
                    unpickedStudents.remove(randIndex);
                    break;
                default: System.out.println("Invalid entry. Try again.");
                    pickStudent();
                    break;
            }

            System.out.println("Pick another random student? (Y)es, (N)o");
            userInput = scnr.next();
            switch (userInput)
            {
                case "y":
                case "Y": pickStudent();
                    break;
                case "n":
                case "N": unpickedStudents.clear();
                case "e":
                case "E": System.out.println("Exporting response tally before exiting. ");
                    try
                    {
                        ResponseReport.exportResponseTally(classNum, classNam, responseTallies);
                    }catch(IOException excpt)
                    {
                        System.out.println("Output file inaccessible. " + excpt.getMessage());
                    }
                    System.out.println("Bye bye, from Random Student Picker.");
                    System.out.println("Bye bye. Exiting");
                    System.exit(0);
                    //TODO: We go back to main. Must clear all static variables.
                    //Program.main(new String[0]);
                    break;
                default: System.out.println("Invalid entry. Try again.");
                    break;
            }
        }

        runRandomStudentPicker();
    }

    private static void tallyStudentResponse(Student student, String response)
    {
        for(ResponseTally responseTally: responseTallies)
        {
            if(responseTally.student.getLastName().equals(student.getLastName())
                    && responseTally.student.getFirstName().equals(student.getFirstName()))
            {
                switch (response)
                {
                    case "A": ++responseTally.answers;
                        break;
                    case "P": ++responseTally.passes;
                        break;
                    default: System.out.println("The response received is invalid.");
                }
            }
        }
    }
}

