import java.io.*; /* Required for handling the IOExceptions */

public class FileWrite
{
//	public static void main( String [ ] args ) {
//		writeToFile( "outputfile.txt" );
//	}

	/* Method that writes contents to a file with the file name 'fileName' */
	public static void writeToFile( String fileName, ArrayBasedList list, String command)
	{
//		ArrayBasedList list = Server.getUserNames_passwordsUserName();
		try {
			/* Create a FileWriter object that handles the low-level details of writing */
			FileWriter theFile = new FileWriter(fileName);

			/* Create a PrintWriter object to wrap around the FileWriter object */
			/* This allows the use of high-level methods like println */
			PrintWriter fileOut = new PrintWriter(theFile);

			if (command.equals("1")) {
				User temp = null;
				for (int i = 1; i <= list.size(); i++) {
					temp = (User) list.get(i);
					if (temp != null) {
						fileOut.println(temp.getUserName());
						fileOut.println(temp.getPassword());
					}
				}
			}
			if (command.equals("2")){
				for (int i = 1; i <= list.size(); i++) {
					fileOut.println(list.get(i));
				}
			}
			if (command.equals("3")){
				fileOut.println("UserName\tIP Address\tHost Name");
				fileOut.println("________________________________");
				for (int i = 1; i <= list.size(); i++) {
					User tempUser = null;
					Computer tempComp = null;
					if (list.get(i) instanceof User) {
						tempUser = (User) list.get(i);
					} else {
						tempComp = (Computer) list.get(i);
					}
					if (tempUser != null) {
						fileOut.print(tempUser.getUserName());
					}
					if (tempComp != null) {
						fileOut.println("\t" + tempComp.getIP_address() + "\t" + tempComp.getHostName());
					}
				}
			}

			/* Close the file so that it is no longer accessible to the program */
			fileOut.close( );
		}

		/* Handle the exception thrown by the FileWriter methods */
		catch( IOException e ) {
			System.out.println( "Problem writing to the file" );
		}
	} /* End of method writeToFile */
} /* End of class FileWrite */