//dotomole - 27/7/19
//Terminal version of program
import java.io.*;
import java.util.*;

public class CurtinUnits
{
    //Hashmap to load into memory for program
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
        System.out.println("CURTIN UNIT SEARCH (Updated 27/7/19)");
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

    public static void createHashMap()
    {
        String dir = System.getProperty("user.dir");
        String os = System.getProperty("os.name");
        Unit[] allUnits;
        int i = 0;

        try 
        {
            FileInputStream fi;
            if (os.contains("Windows"))
                fi = new FileInputStream(new File(dir+"\\SerializedData\\UNITS.curtin"));
            else
                fi = new FileInputStream(new File(dir+"/SerializedData/UNITS.curtin"));
            
            ObjectInputStream oi = new ObjectInputStream(fi);

            // Read object array
            allUnits = (Unit[]) oi.readObject();

            try
            {
                while(allUnits[i] != null)
                {
                    //put each obj in hashmap
                    unitsHM.put(allUnits[i].getTitle(), allUnits[i]);
                    i++;
                }
            }
            catch(ArrayIndexOutOfBoundsException e)
            {
                //for some reason it was going 1 over the array index...
            }
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