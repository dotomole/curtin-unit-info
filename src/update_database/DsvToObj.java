//dotomole - 8/7/19
//Parses .dsv files into Unit objects
//and then serializes them to create a database
import java.io.*;
import java.util.concurrent.TimeUnit;
public class DsvToObj
{
    public static void main(String[] args) 
    {
        double percentage, perRd;
        Unit unit = null;
        File serDataFold;

        //Directories and make serialized folder
        File cwd = new File(System.getProperty("user.dir"));
        String dir = cwd.getParentFile().toString();
        String os = System.getProperty("os.name");
        String filename;
        int FILES;

        if (os.contains("Windows"))
        {
            FILES = new File(dir+"\\raw\\dsv\\").listFiles().length;
	        serDataFold = new File(dir+"\\SerializedData\\");
	    }
        else
        {
            FILES = new File(dir+"/raw/dsv/").listFiles().length;
	        serDataFold = new File(dir+"/SerializedData/");
	    }
	    
        //Makes SerializedData folder
        if (!serDataFold.exists())
        {
            serDataFold.mkdir();
        }

        System.out.println("SERIALIZING DATABASE:");
        long startTime = System.nanoTime();

        for(int i = 1; i <= FILES; i++)
        {
            percentage = ((double)i/3503*100);
            perRd = (double)Math.round(percentage*1000000) / 1000000;
            System.out.print("\r"+perRd+"%");

        	if (os.contains("Windows"))
            	filename = dir+"\\raw\\dsv\\"+i+".dsv";
            else
            	filename = dir+"/raw/dsv/"+i+".dsv";

            FileInputStream fileStrm = null;
            InputStreamReader rdr;
            BufferedReader bufRdr;

            String st = "";
            String prereq = ""; 
            String preLeads = "";
            try
            {
                fileStrm = new FileInputStream(filename);
                rdr = new InputStreamReader(fileStrm);
                bufRdr = new BufferedReader(rdr);

                //Reads file into string (only one line so...)
                st = bufRdr.readLine();

                String[] unitLeads = st.split("UNITLEADS\\$");
                String[] info = st.split("\\$");

                //Get all unit leads
                try
                {
                    preLeads = unitLeads[1].replace("$", "\n");                     
                    //System.out.println(preLeads);
                }
                catch (ArrayIndexOutOfBoundsException e)
                {}

                //create units brother
                unit = new Unit(info[0], info[1],info[3],info[5],
                    info[7],info[9],info[11], preLeads);
                try 
                {
                    //.curtin because - haha
                    FileOutputStream f = new FileOutputStream(new File(dir+"\\SerializedData\\"+i+".curtin"));
                    ObjectOutputStream o = new ObjectOutputStream(f);

                    // Write unit object to file
                    o.writeObject(unit);

                    o.close();
                    f.close();
                } 
                catch (IOException e) 
                {
                    System.out.println("Error initializing stream");
                }
                fileStrm.close(); 
            }

            catch (IOException e)
            {
                System.out.println("Error initializing stream");
            }   
        }
        long endTime = System.nanoTime();
        System.out.println("\nTIME ELAPSED: "+(endTime-startTime)/1000000000+" seconds");
    }
}