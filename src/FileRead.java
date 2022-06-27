import java.io.*; /* Required for handling the IOExceptions */

public class FileRead
{
//	public static void main( String [ ] args )
//	{
//		readFromFile( "inputfile.txt" );
//	}
	private static ArrayBasedList items = new ArrayBasedList();
	public static void readFromFile( String fileName )
	{
		String oneLine;

		try
		{
			/* Create a FileWriter object that handles the low-level details of reading */
			FileReader theFile = new FileReader( fileName );

			/* Create a BufferedReader object to wrap around the FileWriter object */
			/* This allows the use of high-level methods like readline */			
			BufferedReader fileIn  = new BufferedReader( theFile );
		
			/* Read the first line of the file */
			oneLine = fileIn.readLine();
			
			/* Read the rest of the lines of the file and output them on the screen */
			int i=1;
			while( oneLine != null ) /* A null string indicates the end of file */
			{
//				System.out.println( oneLine );
				items.add(i, oneLine);
				oneLine = fileIn.readLine();
				i++;
			}

			/* Close the file so that it is no longer accessible to the program */
			fileIn.close( );
		}

		/* Handle the exception thrown by the FileReader constructor if file is not found */
		catch (FileNotFoundException e){
			System.out.println( "Unable to locate the file: " + fileName );
		}

		/* Handle the exception thrown by the FileReader methods */
		catch (IOException e){
			System.out.println( "There was a problem reading the file: " + fileName);
		}
	} /* End of method readFromFile */

	/**
	 * items ArrayBasedList is denoted as static. So we need to "reset it" in order to not store the new items
	 * that are going to be read by another file, in the same file.
	 */
	public static void resetList(){
		items = new ArrayBasedList();
	}

	public static ArrayBasedList getListOfGeneratedItems(){
		return items;
	}
} /* End of class FileRead */