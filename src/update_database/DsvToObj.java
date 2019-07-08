import java.io.*;
import java.util.concurrent.TimeUnit;
public class DsvToObj
{
	public static void main(String[] args) 
	{
		//Directories and make serialized folder
		File cwd = new File(System.getProperty("user.dir"));
		String dir = cwd.getParentFile().getParentFile().toString();

		final int FILES = new File(dir+"\\Data\\raw\\dsv\\").list().length;

		File serDataFold = new File(dir+"\\Data\\SerializedData");
		if (!serDataFold.exists())
		{
			serDataFold.mkdir();
		}
		double percentage, perRd;

		System.out.println("SERIALIZING DATABASE:");
		Unit unit = null;
		long startTime = System.nanoTime();

		for(int i = 1; i <= FILES; i++)
		{
			percentage = ((double)i/3503*100);
			perRd = (double)Math.round(percentage*1000000) / 1000000;

			System.out.print("\r"+perRd+"%");

			String filename = dir+"\\Data\\raw\\dsv\\"+i+".dsv";
	        FileInputStream fileStrm = null;
	        InputStreamReader rdr;
	        BufferedReader bufRdr;
	        String st = "";
	        String prereq = ""; //nothing by default
	        String preLeads = "";
	        try
	        {
	            fileStrm = new FileInputStream(filename);
	            rdr = new InputStreamReader(fileStrm);
	            bufRdr = new BufferedReader(rdr);

	            //Reads file into string (only one like so...)
				st = bufRdr.readLine();

				String[] unitLeads = st.split("UNITLEADS\\$");
				String[] info = st.split("\\$");
				//System.out.println(i+" "+info.length);

				// //if there ARE UTLT's then get 'em
				try
				{
					preLeads = unitLeads[1].replace("$", "\n");						
					System.out.println(preLeads);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{}

				//create units brother
				unit = new Unit(info[0], info[1],info[3],info[5],
					info[7],info[9],info[11], preLeads);
				try 
				{
					//.curtin because - haha
					FileOutputStream f = new FileOutputStream(new File(dir+"\\Data\\SerializedData\\"+i+".curtin"));
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
				//System.out.println(unit.toString());

	        }

	        catch (IOException e)
	        {
	            if (fileStrm != null)
	            {
	                try
	                {
	                    fileStrm.close();
	                }
	                catch (IOException ex2){}
	            }
	            System.out.println("Error in file processing: " + e.getMessage());      
	        }	
		}

		long endTime = System.nanoTime();
		System.out.println("\nTIME ELAPSED: "+(endTime-startTime)/1000000000+" seconds");
	}
}