import java.util.ArrayList;
import java.util.Scanner;

//  All the methods that are not implementations of the NetworkServerInterface are written as static cause there is
//  no reason either conceptually or practical in having to use a server object in order to perform operations.
//  Meaning that we only have one server, we don't use a lot of servers (we don't create server objects)so that's why it
//  doesn't make sense in having to create server objects. On the other hand the methods that implement NetworkServerInterface
//  are not static because the only way we could do that would be by writing their implementation on the interface itself. That
//  would male things more complicated so that's why they are not defined as static.

public class Server implements NetworkServerInterface{
    private static ArrayBasedList userNames_passwords = new ArrayBasedList(); //main data structure for All user-names and passwords
    private static ArrayList<Computer> computerDevices = new ArrayList<>(); // this stores the devices / computers
    private static ArrayList<Object> mappedUsersToComputers = new ArrayList<>(); //this is used in order to store the users
    // who are connected as well as the computers that they are using
    private static QueueArrayBased ip_addresses = new QueueArrayBased(); //data structure of IP addresses
    private static Scanner scanner = new Scanner(System.in);
    public Server(){} // default constructor
    @Override
    public void startServer() {
        FileRead.readFromFile("server.txt");
        ArrayBasedList fileItems = FileRead.getListOfGeneratedItems(); //items retrieved by the file
        ArrayList<Object> names = new ArrayList<>(); //temporary storage for names
        ArrayList<Object> passwords = new ArrayList<>(); //temporary storage for passwords
        int tempN=0;   // temporal variable for names
        int tempP=0;   // temporal variable for passwords
        // the lines of the file that are even are the passwords whereas the ones that are odd are the user-names
        for (int i = 1; i <= fileItems.size(); i++) {
            if (i % 2 != 0){
                names.add(tempN, fileItems.get(i));
                tempN++;
            }
            else {
                passwords.add(tempP, fileItems.get(i));
                tempP++;
            }
        }
        int temp=1;
        User[] userArray = new User[fileItems.size()];
        for (int i = 0; i < names.size(); i++) {
            userArray[i] = new User((String) names.get(i), (String) passwords.get(i)); // crete user and adds in data structure
            userNames_passwords.add(temp, userArray[i]);
            temp++;
        }
        generateIPAddress(); // Creates the data structure of available IPs
        System.out.println("Server started successfully");
    }

    @Override
    public void addUser(String userName, String password)  {
        boolean exists;
        String choice = "1", input = "0"; // if user presses "1" then the operation will be performed again (only in the
        // case in which the username that he entered is duplicate)
        do{
            exists = false;
            if (input.equals("1")){
                boolean repeat;
                do {
                    repeat= false;
                    System.out.println("Please enter a username: ");
                    userName = scanner.nextLine();
                    System.out.println("Please enter a password: ");
                    password = scanner.nextLine();
                    input = "0";
                    if (userName.equals("") || password.equals("")){
                        System.out.println("None of the inputs can be left unwritten.\nPlease re-enter your data.");
                        repeat = true;
                    }
                }while (repeat);
            }
            try{
                // searches if user already exist in database
                for (int i = 1; i <= userNames_passwords.size(); i++) {
                    User tempU = (User) userNames_passwords.get(i);
                    if (userName.equals(tempU.getUserName())){
                        exists = true;
                        throw new InputDuplicateException("Name is duplicate. Cannot insert username.");
                    }
                }
            }catch (InputDuplicateException e){
                boolean repeat;
                System.out.println(e.getMessage());
                do {    // we give the user the option to re-enter another username as long as he wants to
                    System.out.println("Do you want to re-enter another username? - Press '1' for Yes, '0' for No.");
                    input = scanner.nextLine();
                    repeat = false;
                    if (!input.equals("0") && !input.equals("1")) {
                        System.out.println("Input must be either 0 or 1.");
                        repeat = true;
                    }
                }while (repeat);
            }
        }while(choice.equals(input));
        if (!exists){  // if user doesn't already exist in database, then we add him
            User user = new User(userName, password);
            int lastPlace = userNames_passwords.size() + 1;
            userNames_passwords.add(lastPlace, user);
            System.out.println("User was added successfully");
        }
    }

    @Override
    public void removeUser(String userName) {
        boolean errorThrow = false;
        String choice = "1", input = "0"; // if user presses "1" then the operation will be performed again (only in the
        // case in which the username that he entered does not exist in the data structure)
        do {
            if (input.equals("1")){
                boolean repeat;
                do {
                    repeat= false;
                    System.out.println("Please enter the name of the user that you wish to remove: ");
                    userName = scanner.nextLine();
                    input = "0";
                    if (userName.equals("")){
                        System.out.println("The username cannot be left unwritten.\nPlease re-enter your data.");
                        repeat = true;
                    }
                }while (repeat);
            }
            boolean found = false;
            for (int i = 1; i <= userNames_passwords.size(); i++) {
                User tempU = (User) userNames_passwords.get(i);
                if (userName.equals(tempU.getUserName()) && !tempU.getConnectionStatus()) {
                    userNames_passwords.remove(i);
                    System.out.println("User was removed successfully");
                    errorThrow = false;
                    found = true;
                }
                if (userName.equals(tempU.getUserName()) && tempU.getConnectionStatus()){
                    System.out.println("The user you are trying to remove is connected to the network.\nTherefore cannot be removed.");
                    errorThrow = true;
                    break;
                }
                if (i == userNames_passwords.size() && !found){
                    System.out.println("The user you are trying to remove does not exist.");
                    errorThrow = true;
                }
            }
            boolean repeat = false;
            do {
                if (errorThrow) {
                    System.out.println("Do you want to re-enter another username to remove? - Press '1' for Yes, '0' for No.");
                    input = scanner.nextLine();
                    repeat = false;
                    if (!input.equals("0") && !input.equals("1")) {
                        System.out.println("Input must be either 0 or 1.");
                        repeat = true;
                    }
                }
            } while (repeat);
        }while (choice.equals(input));
    }

    /**
     * This method adds the computer in the "computerDevices" list
     * Happens when computer objects are created
     * @param computer
     */
    public static void addComputer(Object computer){
        computerDevices.add(computerDevices.size(), (Computer) computer);
    }

    /**
     *  Checks availability of computer
     * @param deviceName
     * @return true or false if available or not
     */
    public static boolean deviceAvailableToUse(String deviceName){
        boolean canUse = false;
        for (int i=0;i<computerDevices.size();i++) {
            Computer c = computerDevices.get(i);
            if (c.getHostName().equals(deviceName)) {
                if (c.isAvailable()) {
                    canUse = true;
                    break;
                } else {
                    System.out.println("This device is not Available,\nIt is currently used by another user.");
                    break;
                }
            }
            if (i == computerDevices.size() - 1 && !c.getHostName().equals(deviceName)) {
                System.out.println("There is no device with such name that you can use.");
            }
        }
        return canUse;
    }

    /**
     *  Used in various places within the program in order to return the computer with the corresponding host name
     * @param deviceName
     * @return
     */
    public static Computer getDeviceByHN(String deviceName){
        Computer device = null;
        for (Computer c: computerDevices) {
            if (c.getHostName().equals(deviceName)){
                device = c;
                break;
            }
        }
        return device;
    }

    /**
     * Used in various places within the program in order to return the computer with the corresponding IP Address
     * @param ip
     * @return
     */
    public static Computer getDeviceByIP(String ip){
        Computer device = null;
        for (Computer c: computerDevices) {
            try{
                if (c.getIP_address().equals(ip)){
                    device = c;
                    break;
                }
            }catch (NullPointerException e){
                //Nothing to do specifically. This just prevents for an
                //exception to be raised when we are comparing a String to null,
                //because the devices that don't have an IP assigned are assigned as null.
            }
        }
        return device;
    }

    /**
     * Returns the user object by giving a string name
     * @param userName
     * @return
     */
    public static User getUser(String userName){
        User user = null;
        for (int i = 1; i <= userNames_passwords.size() ; i++) {
            User tempU = (User) userNames_passwords.get(i);
            if (tempU.getUserName().equals(userName)){
                user = tempU;
                break;
            }
            else if(i == userNames_passwords.size()){
                System.out.println("The user you're trying to reach does not exist.");
            }
        }
        return user;
    }

    @Override
    public void viewConnectedComputers(){
        System.out.println("UserName\tIP Address\tHost Name");
        System.out.println("________________________________");
        if (mappedUsersToComputers.isEmpty()){
            System.out.println("There are no mappings yet - (No users connected to the network.)");
        }
        else {
            for (int i = 0; i < mappedUsersToComputers.size(); i++) {
                User tempUser = null;
                Computer tempComp = null;
                if (mappedUsersToComputers.get(i) instanceof User) {
                    tempUser = (User) mappedUsersToComputers.get(i);
                } else {
                    tempComp = (Computer) mappedUsersToComputers.get(i);
                }
                if (tempUser != null) {
                    System.out.print(tempUser.getUserName());
                }
                if (tempComp != null) {
                    System.out.println("\t" + tempComp.getIP_address() + "\t" + tempComp.getHostName());
                }
            }
        }
    }

    //NOT REQUIRED METHOD
    public static void viewUserNamePasswords(){
        for (int i = 1; i <= userNames_passwords.size() ; i++) {
            System.out.println(userNames_passwords.get(i));
        }
    }

    //NOT REQUIRED METHOD
    public static void viewAvailableComputers() {
        for (Computer c: computerDevices) {
            System.out.println(c);
        }
    }

    // even index will contain users
    // odd index will contain mapped computers (their users are one pos behind)

    public static void mapUserToComputer(User user, Computer computer){
        mappedUsersToComputers.add(user);
        mappedUsersToComputers.add(computer);
    }

    /**
     *  Can find the computer that the user uses to connect to the network
     * @param user
     * @return the corresponding computer
     */
    public static Computer getMappedComputer(User user){
        Computer c = null;
        if (mappedUsersToComputers.contains(user)){
            int positionOfUser = mappedUsersToComputers.indexOf(user);
            c = (Computer) mappedUsersToComputers.get(positionOfUser + 1);
        }
        else {
            System.out.println("User does not exist or isn't mapped to a computer.");
        }
        return c;
    }

    /**
     *  when user disconnects form network, the user no longer uses the computer. So we eliminate the user-computer mapping
     * @param user
     */
    public static void removeMapping(User user){
        if (mappedUsersToComputers.contains(user)){
            int positionOfUser = mappedUsersToComputers.indexOf(user);
            mappedUsersToComputers.remove(positionOfUser); //removes user
            mappedUsersToComputers.remove(positionOfUser); //removes mapped computer too
            // when removed user, the computer took the index of computer.
        }
        else {
            System.out.println("User does not exist or isn't mapped to a computer.");
        }
    }

    private void generateIPAddress(){
        //Start server: enqueue IP addresses in the list
        //  stores the ips from the available.txt file
        FileRead.resetList(); // resets the items list in order to not have any conflicts with the previously stored elements
        FileRead.readFromFile("available.txt");
        ArrayBasedList ip_addressesFromFile = FileRead.getListOfGeneratedItems();
        for (int i = 1; i <= ip_addressesFromFile.size(); i++) {
            ip_addresses.enqueue(String.valueOf(ip_addressesFromFile.get(i)));
        }
        /*ip_addresses.enqueue("198.168.10.157");
        ip_addresses.enqueue("198.168.10.158");
        ip_addresses.enqueue("198.168.10.159");
        ip_addresses.enqueue("198.168.10.168");
        ip_addresses.enqueue("198.168.10.169");
        ip_addresses.enqueue("198.168.10.170");
        ip_addresses.enqueue("198.168.10.171");*/
    }

    public static String giveIPAddress(){
        //Connect to network: dequeue when a person authenticates
        String ip = null;
        if (ip_addresses.isEmpty()){
            System.out.println("There is no available IP address.");
        }
        else {
            ip = (String) ip_addresses.dequeue();
        }
        return ip;
    }

    public static void returnIPAddress(String ipAddress){
        //Disconnect to network: enqueues IP addresses when user disconnects from LAN
        //Makes it available to use again
        ip_addresses.enqueue(ipAddress);
    }

    /**
     * this is used in order to print the list of the ips when we end the program (update available.txt)
     * we convert it in a typical ArrayBasedList because that's the second valid parameter for the method
     * writeToFile( String fileName, ArrayBasedList list) - FileWrite class
     * @return
     */
    public static ArrayBasedList getIPList(){
        ArrayBasedList ipArrayForFileWrite = new ArrayBasedList();
        int i = 1;
        while(!ip_addresses.isEmpty()) {
           String element = (String) ip_addresses.dequeue();
           ipArrayForFileWrite.add(i, element);
           i++;
        }
        return ipArrayForFileWrite;
    }

    // Method not required!
    public static void printQueue(){
        while(!ip_addresses.isEmpty()) {
            System.out.println(ip_addresses.dequeue());
        }
    }

    /**
     * Used by FileWrite in order to print the updated version of the username - passwords in the server.txt
     * @return
     */
    public static ArrayBasedList getUserNames_passwordsUserName(){
        return userNames_passwords;
    }

    /**
     * Used by FileWrite in order to print the leased IP addresses with their respective host-names and user-names
     * Since mappedUsersToComputers is an array-list collection and we need an array_base_list data structure,
     * we copy all the elements and then we return it.
     * @return Mapping in an ArrayBasedList form (for writing in the file leases.txt purpose)
     */
    public static ArrayBasedList getMappedUsersToComputers(){
        ArrayBasedList tempListForFileWrite = new ArrayBasedList();
        int counter = 1;
        for (int i = 0; i < mappedUsersToComputers.size(); i++) {
            tempListForFileWrite.add(counter, mappedUsersToComputers.get(i));
            counter++;
        }
        return tempListForFileWrite;
    }

}