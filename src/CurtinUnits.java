//dotomole - 9/7/19
import java.io.*;
import java.util.*;

public class CurtinUnits
{
    //Big ol hashmap
    static HashMap<String,Unit> unitsHM = new HashMap<String,Unit>();

    public static void main(String[] args) 
    {
        String input = "", input2 = "";
        int found = 0, input3;
        boolean exit = false;
        String[] foundArr;
        Unit foundUnit;

        Scanner sc = new Scanner(System.in);

        System.out.println("-----------------------------------");
        System.out.println("CURTIN UNIT SEARCH (Updated 8/7/19)");
        System.out.println("-----------------------------------");
        System.out.print("Loading data...");
        createHashMap();
        System.out.print("done!");


        while (!exit)
        {
            found = 0;
            foundArr = new String[4000];//buffer
            System.out.print("\nSearch units (by title):> ");
            input = sc.nextLine();

            //a way to search partial strings in hashmap
            //creates foundArr - array of results found
            for (String val : unitsHM.keySet())
            {
                if (val.toUpperCase().contains(input.toUpperCase()))
                {
                    foundArr[found] = val;
                    System.out.println((found+1)+". "+val);
                    found++;
                }
            }

            //if there are some found
            if (foundArr[0] != null)
            {
                //choose from search results for unit
                System.out.print("\nChoose result (by number):> ");
                input2 = sc.nextLine();

                foundUnit = unitsHM.get(foundArr[Integer.parseInt(input2)-1]);

                System.out.println("---------");
                System.out.println("UNIT INFO");
                System.out.print("---------");                
                System.out.println(foundUnit.toString());
            }
            else
            {
                System.out.println("No units found!");
            }

            System.out.print("Search again? (y/n):> ");
            
            input3 = sc.next().charAt(0);
            sc.nextLine();
            if (input3 == 'n')
            {
                exit = true;
            }
        }
    }

    //Creates Hash map from Serialized objects
    public static void createHashMap()
    {
        String dir = System.getProperty("user.dir");
        String os = System.getProperty("os.name");
        int FILES;

        if (os.contains("Windows"))
            FILES = new File(dir+"\\SerializedData\\").listFiles().length;
        else
            FILES = new File(dir+"/SerializedData/").listFiles().length;

        for(int i = 1; i <= FILES; i++)
        {
            try 
            {
                FileInputStream fi;
                if (os.contains("Windows"))
                    fi = new FileInputStream(new File(dir+"\\SerializedData\\"+i+".curtin"));
                else
                    fi = new FileInputStream(new File(dir+"/SerializedData/"+i+".curtin"));
                
                ObjectInputStream oi = new ObjectInputStream(fi);

                // Read objects
                Unit unit = (Unit) oi.readObject();

                //put each obj in hashmap
                unitsHM.put(unit.getTitle(), unit);

                oi.close();
                fi.close(); 
            }
            catch (FileNotFoundException e) 
            {
                System.out.println("File not found");
            } 
            catch (IOException e) 
            {
                System.out.println("Error initializing stream");
            } 
            catch (ClassNotFoundException e) 
            {
                e.printStackTrace();
            }
        }       
    }
}