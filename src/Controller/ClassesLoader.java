package Controller;
//Controller classes are used for program logic and event management.
//Controller classes minimize persistent data from models classes.
//For this reason, Controller classes use few instance variables unless
//they are more event managers

import Model.*;
import Model.Class;

import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;

import java.util.Arrays;
import java.util.Scanner;

public class ClassesLoader
{
    //STATIC VARIABLES used by all methods in class
    static  File classesFile; //Holds a file as File type.
    static FileInputStream classesFileStream; //Holds the single pass stream of file characters
                                              //Think of a stream, it flows one-way, never goes back
    static Class[] classesArray; //Holds a collection of Class objects.
    static int classCount = 0; //Keeps a running sum of all classes in the system.
    static String inputString = ""; //Holds the file as a complete string with
                                    //carriage returns "\n"
    static int[] indexes; //Counts the instances of indexes values where a
                          // substring was found in inputString


    /*
    METHOD NAME: loadClass()
    PARAMETERS: none
    RETURN VALUES: Class[]
    DESCRIPTION: Loads data from Classess.txt file
    HELPER METHODS: loadStudents() used to assigned array of Student[] to each Class in array Class[]
     */
    public static Class[] loadClass() throws IOException
    {
        classesArray = new Class[0]; //Initialize return value with empty. Value will be destroyed.

        Scanner scnr = new Scanner(System.in);

        System.out.println("Is your class data file at: src/Classes.txt? (Y)es, (N)o, E(x)it");
        String userInput = scnr.nextLine();

        switch (userInput)
        {
            case "y":
            case "Y": System.out.println("Ingesting at: src/Classes.txt");
                //Name of file to ingest. Can be designed to be keyed-in later.
                classesFile = new File("src/Classes.txt");
                break;
            case "n":
            case "N": System.out.println("Key in the name of the file in src/: ");
                userInput = scnr.nextLine();
                classesFile = new File("src/" + userInput);
                break;
            case "x":
            case "X": System.out.println("You selected Exit.");
                System.exit(0);
                break;
            default: System.out.println("Entry invalid. Try again.");
                loadClass();
        }

        //try must be used with all IO in the event of deleted or missing files or
        //permission access denials during runtime
        try {
            classesFileStream = new FileInputStream(classesFile);

            //BUILD THE inputString THAT HOLDS ALL FILE AS STRING -----------------------
            // so file will have all line wraps
            int charCode = 0;
            //<FileStream>.read() allows one pass of character-by-character assignment
            //to a receiving variable, usually a char. The character comes in as int and the
            //value must be converted using type casting: (char) charCode)
            //When there are no more characters to read, method read() sends value -1
            while((charCode = classesFileStream.read()) != -1)
            {
                //System.out.print((char) charCode);
                inputString += String.valueOf((char) charCode);
            }

            //DEBUG: check to see that document is there and correctly formatted
            System.out.println("The following courses and students have been loaded.");
            System.out.println(inputString);
            //Add a new line for ease of reading DEBUG above
            System.out.println("- - - ");

            //GATHER ALL INSTANCES of "Class Name: " STRING --------------------
            int stringIndex = 0;
            int  localCount = 0;
            indexes = new int[inputString.length()];
            Arrays.fill(indexes,0,inputString.length(), -1);
            //Assigns -1 to last element because fill() above gives last value 0
            indexes[indexes.length - 1] = -1;
            //DEBUG: Checks the content of lineNumbers
            //System.out.println(Arrays.toString(indexes));

            //Loop runs through all of inputString searching for instance of
            //"Class Name: ". When found, sends index number in indexString to
            //stringIndex. Checks for -1 value, which means end of string reached.
            while(stringIndex != -1)
            {
                stringIndex = inputString.indexOf("Class Name:", stringIndex);
                //DEBUG: Confirms index of found character C from "Class Name:"
                //System.out.println("stringIndex: " + stringIndex);

                //DEBUG: Confirms the target substring was found
                //if(stringIndex != -1)
                //    System.out.println("1 | Class Name: " + stringIndex + " for \"Class Name: \"");

                //If -1 value, end of inputString reached.
                if(stringIndex != -1)
                {
                    //DEBUG: Confirms entry into condition to count
                    //System.out.println("2 | Inner counting condition met.");

                    //Saves value at stringIndex where "Class Name:" was found
                    indexes[localCount] = stringIndex;
                    //Creates a new starting point for next loop of search
                    stringIndex++;
                    //Increments how many searches have resulted in success for this loop
                    localCount++;
                    //Keeps a running count of all instances of "Class Name:" for every
                    //run of method, not just this loop session. This variable is static.
                    classCount++;
                }
            }

            //DEBUG: Prints indexes where "Class Name:" was found
            //System.out.println(Arrays.toString(indexes));
            //DEBUG: Checks class classCount
            //System.out.println("classCount:" + classCount);

            //GATHER STUDENTS ENROLLED IN EACH CLASS -----------------------------

            //Declares as many array elements as there are classes counted in classCount value
            classesArray = new Class[classCount];

            //Loops through entire classesArray for each class assigned above
            for(int i = 0; i < classesArray.length; ++i)
            {
                //Assigning to className the name and number of the class found from indexes[i] to the
                // first found "Class Number"
                String classData = inputString.substring(indexes[i], inputString.indexOf("\n", indexes[i]));

                //DEBUG: Checks classData value.
                //System.out.println("classData: " + classData);

                //Captures value for className.
                String className = inputString.substring(indexes[i] + "Class Name: ".length(),
                        inputString.indexOf("Class Number: ", indexes[i]));
                //DEBUG: Checks value for className.
                //System.out.println("className: " + className);
                //Captures value for classNumber
                String classNumber = classData.substring(classData.indexOf("Class Number: ") +
                        "Class Number: ".length(), classData.length()-1);
                //DEBUG: Checks value for classNumber
                //System.out.println("classNumber: " + classNumber);

                //LOAD Student[] ARRAY TO setStudents() INSIDE A CLASS OBJECT IN Class[] ARRAY---------
                //Load Class object into array of classes
                classesArray[i] = new Class(className.trim(), classNumber.trim(), true);
                //Gets all students in classes that are not from the last class in the loading file.
                if(i < classesArray.length - 1)
                {
                    //DEBUG: Check value of i
                    //System.out.println("i: " + i + " i < classesArray.length - 1");

                    //Calls loadStudents() to assign a Student[] to each of i classes in classesArray[]
                    classesArray[i].setStudents(  loadStudents( classesArray[i].getClassName(),
                            classesArray[i].getClassNumber(), indexes[i], indexes[i+1] )  );
                }else
                //Gets all the students in the last class in the loading file.
                {
                    //DEBUG: Check value of i
                    //System.out.println("i: " + i + " i < classesArray.length");

                    classesArray[i].setStudents(  loadStudents( classesArray[i].getClassName(),
                            classesArray[i].getClassNumber(), indexes[i], -1 )  );
                }

                //DEBUG: Checks to make sure that the load succeeded
                //System.out.println("classesArray[i].getClassName(): " + classesArray[i].getClassName() +
                //        " classesArray[i].getClassNumber(): " + classesArray[i].getClassNumber());
            }

            //DEBUG: Shows the arrays that stores the index numbers in the inputString
            //for "\n"
            //System.out.println(Arrays.toString(lineNumbers));
            //for "Class Name:"
            //System.out.println(Arrays.toString(indexes));

        } catch(IOException excpt)
        {
            System.out.println("The Classes.txt file is missing. " + excpt.getMessage());
        }

        //Clear the static variables for future reloading.
        inputString = "";
        classCount = 0;
        classesFileStream = new FileInputStream(classesFile);
        return classesArray;
    }

    /*
    METHOD NAME: loadStudents()
    PARAMETERS: String className, String classNumber, int firstStudentIndex, int lastStudentIndex
    RETURN VALUES: Student[]
    DESCRIPTION: Provides a Student[] to loadClass() to load into each Class in a Class[]
    HELPER METHODS: none
     */

    public static Student[] loadStudents(String className, String classNumber, int firstStudentIndex, int lastStudentIndex)
    {
        //DEBUG: Checks value of income parameters
        //System.out.println("loadStudents() | className: " + className);
        //System.out.println("loadStudents() | firstStudentIndex: " + firstStudentIndex);
        //System.out.println("loadStudents() | lastStudentIndex: " + lastStudentIndex);

        //Initialize return value. The value assigned will be destroyed.
        //Destructible value assigned for assuring return statement that variable is not null
        Student[] students = new Student[0];

        //SETUP VALUES FOR OPERATIONS
        int classIndex = 0;
        int[] classIndexes = new int[inputString.length()];
        int classCount = 0;
        Arrays.fill(classIndexes,0,inputString.length(), -1);
        //Assigns -1 to last element because fill() above gives last value 0
        classIndexes[classIndexes.length - 1] = -1;


        //FIND "Class Name: " IN inputString TO ASSOCIATE WITH CLASS' STUDENTS-----------------------------
        while(classIndex != -1)
        {
            classIndex = inputString.indexOf(  "Class Name: " + className.trim(),   classIndex);

            //DEBUG: Check the updated classIndex
            //System.out.println("New classIndex: " + classIndex);

            //DEBUG: Confirms the target substring was found
            //if(classIndex != -1)
            //    System.out.println("Class Name: " + className + " found at " + classIndex);

            if(classIndex != -1)
            {
                //DEBUG: Confirms entry into condition to count
                //System.out.println("3 | Inner counting condition met.");

                classIndexes[classCount] = classIndex;
                classIndex++;
                classCount++;
            }
        }

        //DEBUG: Shows values of arrays classIndexes that have index positions of inputString
        //where "Class Name: " was found
        //System.out.println(Arrays.toString(classIndexes));

        //Load students belonging to found className in inputString. ------------------------------
        //Gets all students in classes that are not from the last class in the loading file.
        int studentDataIndex;
        int[] studentIndexes;
        int localCount;

        //For all Classes of Students in the inputString that ARE NOT THE LAST
        if(lastStudentIndex != -1)
        {
            localCount = 0;
            studentDataIndex = firstStudentIndex;
            studentIndexes = new int[lastStudentIndex - firstStudentIndex];
            students = new Student[lastStudentIndex - firstStudentIndex];

            while(studentDataIndex < lastStudentIndex)
            {
                //DEBUG: Confirms conditions met to enter loop
                //System.out.println("I'M IN while()");

                //Assign index where instance of studentData tuple occurs in inputString.
                studentDataIndex = inputString.indexOf("Student Name ",   studentDataIndex);


                //DEBUG: Checks value of studentDataIndex and lastStudentIndex
                //System.out.println("studentIndex: " + studentDataIndex + ", lastStudentIndex: " + lastStudentIndex);

                //Condition added because last studentDataIndex search for next instance of "Student Name "
                //(: intentionally missing) may cause  studentDataIndex > lastStudentIndex before loop catches it
                if(studentDataIndex < lastStudentIndex)
                {
                    //Passes substring from position of value of current studentDataIndex next
                    //found new line carriage return "\n"
                    String studentData = inputString.substring(studentDataIndex,
                            inputString.indexOf("\n", studentDataIndex));

                    //DEBUG: Checks value of studentData
                    //System.out.println("studentData: " + studentData);

                    //Assign studentData tuple to new Student object to local variables.
                    String firstName = studentData.substring(studentData.indexOf("First: ") + "First: ".length(),
                            studentData.length() - 1);
                    String lastName = studentData.substring(studentData.indexOf("Last: ") +
                            "Last: ".length(), studentData.indexOf("First: "));
                    String studentNumber = studentData.substring(studentData.indexOf("Name ") + "Name ".length(),
                            studentData.indexOf(":"));

                    //Create and assign new Student object to Student[] array students at localCount index
                    students[localCount] = new Student(firstName.trim(), lastName.trim(), studentNumber.trim());

                    //DEBUG: Confirms the target substring was found
                    //if(studentDataIndex != -1)
                    //    System.out.println("studentData: " + studentData + " found at " + studentDataIndex);
                }

                //This is the control condition of the while loop.
                if(studentDataIndex != -1)
                {
                    //DEBUG: Confirms entry into condition to count
                    //System.out.println("inputString fully checked for ALL BUT NOT LAST CLASS.");

                    //Store index where instance of studentData tuple occurs
                    studentIndexes[localCount] = studentDataIndex;
                    //Increment studentDataIndex for next found line of studentData
                    studentDataIndex++;
                    localCount++;
                }
            }
        } else
        //For THE LAST Class of Students in the inputString
        {
            localCount = 0;
            studentDataIndex = firstStudentIndex;
            studentIndexes = new int[inputString.length() - firstStudentIndex];
            students = new Student[inputString.length() - firstStudentIndex];

            while(studentDataIndex != -1)
            {
                //DEBUG: Indicates the last class of Classes_OLD.txt has been reached
                //System.out.println("I'M IN while() AND IN LAST CLASS");

                studentDataIndex = inputString.indexOf("Student Name ",   studentDataIndex);

                //DEBUG: Check studentDataIndex
                //System.out.println("studentDataIndex: " + studentDataIndex);

                //For a studentData tuples that is NOT THE LAST STUDENT
                String studentData;
                if(inputString.indexOf("\n", studentDataIndex) != -1 && studentDataIndex != -1)
                {
                    studentData = inputString.substring(studentDataIndex,
                            inputString.indexOf("\n", studentDataIndex));

                    //Assign studentData tuple to new Student object.
                    String firstName = studentData.substring(  studentData.indexOf("First: ") +
                            "First: ".length(), studentData.length() -1  );
                    String lastName = studentData.substring(  studentData.indexOf("Last: ") +
                            "Last: ".length(), studentData.indexOf("First: "));
                    String studentNumber = studentData.substring(  studentData.indexOf("Name ") +
                            "Name ".length(), studentData.indexOf(":"));
                    students[localCount] = new Student(firstName.trim(), lastName.trim(), studentNumber.trim());

                }else if (studentDataIndex != -1)
                //For the studentData tuple that IS THE LAST STUDENT
                {
                    studentData = inputString.substring(studentDataIndex);

                    //DEBUG: Conditions met for last student in last class, end of file Classes_OLD.txt
                    //System.out.println(studentData  + " I'M IN AS LAST STUDENT IN LAST CLASS");

                    //Assign studentData tuple to new Student object.
                    String firstName = studentData.substring(  studentData.indexOf("First: ") +
                            "First: ".length()  );
                    String lastName = studentData.substring(  studentData.indexOf("Last: ") +
                            "Last: ".length(), studentData.indexOf("First: "));
                    String studentNumber = studentData.substring(  studentData.indexOf("Name ") +
                            "Name ".length(), studentData.indexOf(":"));
                    students[localCount] = new Student(firstName.trim(), lastName.trim(), studentNumber.trim());

                    //Set condition to end while loop. We arrived at last student. End the routine.
                    studentDataIndex = -1;
                }

                //System.out.println("studentData: " + studentData);

                //DEBUG: Confirms the target substring was found
                //if(studentDataIndex != -1)
                //  System.out.println("studentData: " + studentData + " found at " + studentDataIndex);

                //This is the control condition of the while loop.
                if(studentDataIndex != -1)
                {
                    //DEBUG: Confirms entry into condition to count
                    //System.out.println("inputString fully checked for LAST CLASS.");

                    studentIndexes[localCount] = studentDataIndex;
                    studentDataIndex++;
                    localCount++;
                }
            }
        }

        //DEBUG: Check valaues in studentIndexes array of int
        //System.out.println("studentIndexes" + Arrays.toString(studentIndexes));

        return students;
    }

}
