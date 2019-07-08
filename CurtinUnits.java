import java.io.*;
import java.util.*;

public class CurtinUnits
{
    static final String dir = System.getProperty("user.dir");
    static final int FILES = new File(dir+"\\Data\\SerializedData\\").list().length;

    //Big ol hashmap
    static HashMap<String,Unit> unitsHM = new HashMap<String,Unit>();

    public static void main(String[] args) 
    {
        String input = "", input2 = "";
        int found = 0, input3;
        boolean exit = false;
        String[] foundArr; //buffer
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
            foundArr = new String[4000];
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
        for(int i = 1; i <= FILES; i++)
        {
            try 
            {
                FileInputStream fi = new FileInputStream(new File(dir+"\\Data\\SerializedData\\"+i+".curtin"));
                ObjectInputStream oi = new ObjectInputStream(fi);

                // Read objects
                Unit unit = (Unit) oi.readObject();

                unitsHM.put(unit.getTitle(), unit);
                // System.out.println(unit.toString());

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
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }       
    }
}