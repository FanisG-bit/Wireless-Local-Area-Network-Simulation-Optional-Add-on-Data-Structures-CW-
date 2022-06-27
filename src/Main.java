import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        // Create the Computers that will automatically be available for the users at the beginning
        Computer pc1 = new Computer("THANOS-PC");
        Computer pc2 = new Computer("DIMITRIS-PC");
        Computer pc3 = new Computer("IRAKLIS-PC");
        Computer pc4 = new Computer("FANIS-PC");
        Computer pc5 = new Computer("KEVIN-PC");
        Computer pc6 = new Computer("ALEX-PC");
        System.out.println("**THIS PROGRAM IS A SIMULATION OF HOW COMPUTERS ARE CONNECTING TO A WIRELESS LAN**" +
                "\n\t\t\t\t\t\t*PROJECT MANAGER - THEOFANIS GOUFAS*" +
                "\n\t\t\t\t\t\t\t*PARTNER 1 - KEVIN ROSALES*" +
                "\n\t\t\t\t\t\t\t*PARTNER 2 - ALEX KOROVESIS*");
        System.out.println("**********************************************************************************");
        System.out.println("\n");
        Server server = new Server(); // Crete our server
        String sub_answer; // just a variable that is being used to store the answer of the question that comes after
        // the user selects to perform a server or a user operation.
        boolean lockChoice = false;   // variable that makes sure that most of the operations cannot be performed without
        // having started the server.
        do {
        System.out.println("What actions do you want to perform? S - Server's ||| U - User's ||| E - Finish Program.");
        String answer = scanner.nextLine();
            switch (answer){
                case "S":
                    System.out.println("Choose one of the options:\n1 -> Start Server\n2 -> Add User\n3 -> Remove User" +
                            "\n4 -> View Computers");
                    sub_answer = scanner.nextLine();
                    boolean doServerOperation = true;
                    switch (sub_answer){
                        case "1":
                            if (!lockChoice){
                                server.startServer();
                                lockChoice = true;
                            }
                            else {
                                System.out.println("You cannot start the server more than once.");
                            }
                            break;
                        case "2":
                            if (!lockChoice){
                                System.out.println("You have not started the server. Could not perform operation.");
                                doServerOperation = false;
                            }
                            if (doServerOperation){
                                boolean repeat;
                                String userToAdd;
                                String passwordOfThatUser;
                                do {
                                    repeat = false;
                                    System.out.println("Please enter a username: ");
                                    userToAdd = scanner.nextLine();
                                    System.out.println("Please enter a password: ");
                                    passwordOfThatUser = scanner.nextLine();
                                    if (userToAdd.equals("") || passwordOfThatUser.equals("")){
                                        System.out.println("None of the inputs can be left unwritten.\nPlease re-enter your data.");
                                        repeat = true;
                                    }
                                }while (repeat);
                                server.addUser(userToAdd, passwordOfThatUser);
                            }
                            break;
                        case "3":
                            if (!lockChoice){
                                System.out.println("You have not started the server. Could not perform operation.");
                                doServerOperation = false;
                            }
                            if (doServerOperation){
                                System.out.println("Please enter the name of the user that you wish to remove: ");
                                String userToRemove = scanner.nextLine();
                                server.removeUser(userToRemove);
                            }
                            break;
                        case "4":
                            server.viewConnectedComputers();
                            break;
                        default:
                            System.out.println("Choices are between 1 to 4 for the Server operations. No other input is valid.");
                            break;
                    }
                break;
                case "U":
                    boolean doUserOperation = true;
                    if (!lockChoice){
                        System.out.println("You have not started the server. Could not perform operation.");
                        doUserOperation = false;
                    }
                    if (doUserOperation){
                        String user;
                        boolean repeat;
                        do{
                            System.out.println("Type the username of the person of which you're controlling (simulating moves): ");
                            user = scanner.nextLine();
                            if (Server.getUser(user) == null){
                                repeat = true;
                            }
                            else {
                                repeat = false;
                            }
                        }while (repeat);
                        System.out.println("\nChoose one of the options:\n1 -> Connect to network\n2 -> Disconnect from network\n3 -> Ping Computer");
                        sub_answer = scanner.nextLine();
                        switch (sub_answer){
                            case "1":
                                Server.getUser(user).connectToNetwork();
                                break;
                            case "2":
                                Server.getUser(user).disconnectFromNetwork();
                                break;
                            case "3":
                                Server.getUser(user).pingComputer();
                                break;
                            default:
                                System.out.println("Choices are between 1 to 3 for the User operations. No other input is valid.");
                            break;
                        }
                    }
                break;
                case "E":
                    FileWrite.writeToFile("server.txt", Server.getUserNames_passwordsUserName(), "1");
                    FileWrite.writeToFile("available.txt", Server.getIPList(), "2");
                    FileWrite.writeToFile("leases.txt", Server.getMappedUsersToComputers(), "3");
                    System.exit(0);
                break;
            }
            /*System.out.println("\n");
            Server.viewUserNamePasswords();    SOS NOTE
            Server.viewAvailableComputers();   Uncomment in order to make debugging process much faster
            System.out.println("\n");*/
        }while (true);
    }
}