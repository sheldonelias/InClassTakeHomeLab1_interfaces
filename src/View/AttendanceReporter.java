package View;
//View classes do not hold long term content
//View classes export data often repeated on the screen

//Package classes
import Controller.AttendanceMonitor;
import Controller.RandomStudentPicker;
import Model.*;
import Model.Class;

//Time and Date classes
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

//I/O Classes
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class AttendanceReporter
{
    //INSTANCE VARIABLES
    //Date and Time
    static DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy/MM/dd 'at' HH:mm:ss");
    static DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy_MM_dd");
    static String dateTimeString;
    static String dateStringFileNaming;

    //Class related data
    static String className;


    /*
    METHOD NAME: decideExportAttendance()
    PARAMETERS: ArrayList<Student>, String, String
    RETURN VALUES: none
    DESCRIPTION: Decision to export or wait
    HELPER METHODS: exportAttendance()
     */
    public static void decideExportAttendance(ArrayList<Student> attendingStudents,
                                              String classNumber, String className)
    {
        System.out.println("Are you ready to export attendance? (Y)es, (N)o, (T)ake Again, E(x)it.");
        Scanner scnr = new Scanner(System.in);
        String userInput = scnr.next();
        switch (userInput)
        {
            case "y":
            case "Y": System.out.println("Exporting attendance.");
                exportAttendance(attendingStudents, classNumber, className);
                break;
            case "n":
            case "N": System.out.println("Awaiting to export attendance.");
                decideExportAttendance(attendingStudents, classNumber, className);
                break;
            case "t":
            case "T":
                AttendanceMonitor.selectClass();
            case "x":
            case "X": System.exit(0);
            default: System.out.println("Entry was invalid. Try again.");
                decideExportAttendance(attendingStudents, classNumber, className);
                break;
        }
    }

   /*
   METHOD NAME: exportAttendance()
   PARAMETERS: ArrayList<Student>, String, String
   RETURN VALUES: none
   DESCRIPTION: Export one classes' daily attendance to a text file
   HELPER METHODS: none
    */
    public static void exportAttendance(ArrayList<Student> attendingStudents,
                                        String classNumber, String className)
    {
        //Set date at instantiation. We will learn to get a real date
        dateTimeString = dtf1.format(LocalDateTime.now());
        dateStringFileNaming = dtf2.format(LocalDate.now());

        //Capture current date and time.
        LocalDateTime now = LocalDateTime.now();

        // Exception check in the event the app has no permissions to build file.
        try
        {
            //Use file output class and instantiate with file name and path as arg
            String outputFile = String.format("src/Attendance_%s_%s_%s.txt",
                    classNumber, className, dateStringFileNaming);
            FileOutputStream fis = new FileOutputStream(outputFile);
            //Instantiate a printwriter
            PrintWriter pw = new PrintWriter(fis);

            //DEBUG: Check print to console that className is working
            //System.out.println(classNumber + ": " + className + " " + dateTimeString);
            //Write to file first line of className and date
            pw.println("DAILY ATTENDANCE REPORT");
            pw.println(classNumber + ": " + className + " " + dateTimeString);

            //Loop through student list to print every student in array
            for(int i = 0; i < attendingStudents.size(); ++i)
            {
                if(attendingStudents.get(i) != null)
                {
                    String line = attendingStudents.get(i).getStudentID() + " " +
                            attendingStudents.get(i).getFirstName() + " " +
                            attendingStudents.get(i).getLastName();

                    //DEBUG: Check print to console that student info is working
                    //System.out.println( line);

                    pw.println(line);
                }
            }

            //Must flush and close, kind of like airplane toilets all PrintWriter objects
            pw.flush();
            pw.close();

            Scanner scnr = new Scanner(System.in);
            System.out.println("Do you want to activate the Random Student Picker? (Y)es, (N)o, E(x)it.");
            String userInput = scnr.next();

            switch (userInput)
            {
                case "y":
                case "Y": System.out.println("Activating use Random Student Picker.");
                    RandomStudentPicker.initRandomStudentPicker(attendingStudents, classNumber, className);
                    break;
                case "n":
                case"N": System.out.println("You elected not to use Random Student Picker.");
                    //Must nullify Array of attendingStudents so that future takeAttendance() calls
                    //from other courses do not get added to this attendance
                    attendingStudents = new ArrayList<Student>();
                    System.out.println("The attendance record has been exported, and deleted from memory.");
                    break;
                case "x":
                case "X": System.exit(0);
                default: System.out.println("Entry invalid. Try again.");
                    exportAttendance(attendingStudents, classNumber, className);
            }
        }catch(IOException excpt)
        {
            //In case of rare bad news that file could not be created.
            System.out.println("Output file is inaccessible. " + excpt.getMessage());
            System.exit(-1);
        }
    }

    /*
       METHOD NAME: printAttendance()
       PARAMETERS: ArrayList<Student>, String, String
       RETURN VALUES: none
       DESCRIPTION: Prints ArrayList data to screen
       HELPER METHODS: decideExportAttendance()
        */
    public static void printAttendance(ArrayList<Student> attendingStudents, String classNumber, String className)
    {
        System.out.println(classNumber + ": " + className + " " + dateTimeString);
        for(Student student: attendingStudents)
        {
            System.out.println(student.getStudentID() + " " + student.getFirstName() + " " + student.getLastName());
        }
        decideExportAttendance(attendingStudents, classNumber, className);
    }

    /*
       METHOD NAME: exportRoles()
       PARAMETERS: Class
       RETURN VALUES: none
       DESCRIPTION: Exports to text fil all class roles.
       HELPER METHODS: <Class>.getClassNumber(), <Class>.getClassName()
       OVERLOADED (Yes/No): Yes
        */
    public static void exportRoles(Class classToExport)
    {
        //Set date at instantiation. We will learn to get a real date
        dateTimeString = dtf1.format(LocalDateTime.now());
        dateStringFileNaming = dtf2.format(LocalDate.now());

        //Capture current date and time.
        LocalDateTime now = LocalDateTime.now();

        // Exception check in the event the app has no permissions to build file.
        try
        {
            //Use file output class and instantiate with file name and path as arg
            String outputFile = String.format("src/Role_%s_%s.txt", classToExport.getClassName(), dateStringFileNaming);
            FileOutputStream fis = new FileOutputStream(outputFile);
            //Instantiate a printwriter
            PrintWriter pw = new PrintWriter(fis);

            //DEBUG: Check print to console that className is working
            //System.out.println(classNumber + ": " + className + " " + dateTimeString);
            pw.println("CLASS ROLE");
            //Write to file first line of className and date
            pw.println(classToExport.getClassNumber() + ": " + classToExport.getClassName() + " " + dateTimeString);

            //Loop through student list to print every student in array
            for(int i = 0; i < classToExport.getStudents().length; ++i)
            {
                if(classToExport.getStudents()[i] != null)
                {
                    String line = classToExport.getStudents()[i].getStudentID() + " " +
                            classToExport.getStudents()[i].getFirstName() + " " +
                            classToExport.getStudents()[i].getLastName();

                    //DEBUG: Check print to console that student info is working
                    //System.out.println( line);

                    pw.println(line);
                }
            }

            //Must flush and close, kind of like airplane toilets all PrintWriter objects
            pw.flush();
            pw.close();

        }catch(IOException excpt)
        {
            //In case of rare bad news that file could not be created.
            System.out.println("Output file is inaccessible. " + excpt.getMessage());
            System.exit(-1);
        }
    }

    /*
       METHOD NAME: exportRoles()
       PARAMETERS: Class
       RETURN VALUES: none
       DESCRIPTION: Exports to text fil all class roles.
       HELPER METHODS: <Class>.getClassNumber(), <Class>.getClassName()
       OVERLOADED (Yes/No): Yes
        */
    public void exportRoles(Student[] students, String className, String classNumber) //throws IOException
    {
        //Pass argument to instance variable.
        this.className = className;

        //Capture current date and time.
        LocalDateTime now = LocalDateTime.now();

        // Exception check in the event the app has no permissions to build file.
        try
        {
            //Use file output class and instantiate with file name and path as arg
            String outputFile = String.format("src/Role_%s_%s.txt", this.className, dateStringFileNaming);
            FileOutputStream fis = new FileOutputStream(outputFile);
            //Instantiate a printwriter
            PrintWriter pw = new PrintWriter(fis);

            //DEBUG: Check print to console that className is working
            //System.out.println(classNumber + ": " + className + " " + dateTimeString);
            //Write to file first line of className and date
            pw.println(classNumber + ": " + className + " " + dateTimeString);

            //Loop through student list to print every student in array
            for(int i = 0; i < students.length; ++i)
            {
                if(students[i] != null)
                {
                    String line = students[i].getStudentID() + " " + students[i].getFirstName() + " " + students[i].getLastName();

                    //DEBUG: Check print to console that student info is working
                    //System.out.println( line);

                    pw.println(line);
                }
            }

            //Must flush and close, kind of like airplane toilets all PrintWriter objects
            pw.flush();
            pw.close();

        }catch(IOException excpt)
        {
            //In case of rare bad news that file could not be created.
            System.out.println("Output file is inaccessible. " + excpt.getMessage());
            System.exit(-1);
        }
    }
}
