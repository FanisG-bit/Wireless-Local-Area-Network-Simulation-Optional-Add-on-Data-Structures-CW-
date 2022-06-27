import java.util.*;

public class User implements NetworkUserInterface{
    private String userName;
    private String password;
    private boolean connectionStatus;
    private Scanner scanner = new Scanner(System.in);
    public User(){}

    /**
     * User has a name and a password
     * @param userName
     * @param password
     */
    public User(String userName, String password){
        this.userName = userName;
        this.password = password;
        this.connectionStatus = false; // user isn't connected to the network by default
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConnectionStatus(boolean status){
        connectionStatus = status;
    }

    public boolean getConnectionStatus(){
        return connectionStatus;
    }

    private void chooseComputer(){  // user must choose a computer when he wants to connect to the network
        boolean repeatQuestion;
        do {
            System.out.println("Enter the name of the device you want to use:");
            String nameOfDesiredComputer = scanner.nextLine();
            if (Server.deviceAvailableToUse(nameOfDesiredComputer)){
                System.out.println("You are now using this device.\nSuccessfully connected to the network.");
                Computer c = Server.getDeviceByHN(nameOfDesiredComputer);
                c.setAvailability(false);
                c.setIP_address(Server.giveIPAddress());
                setConnectionStatus(true);
                Server.mapUserToComputer(this, c);
                repeatQuestion = false;
            }
            else {
                String choice;
                boolean repeat;
                do {
                    repeat = false;
                    System.out.println("Do you want to enter another computer name? - Press '1' for Yes, '0' for No.");
                    choice = scanner.nextLine();
                    if (!choice.equals("0") && !choice.equals("1")){
                        System.out.println("Input must be either '0' or '1'.");
                        repeat = true;
                    }
                }while (repeat);
                if (choice.equals("1")){
                    repeatQuestion = true;
                }
                else {
                    repeatQuestion = false;
                }
            }
        }while(repeatQuestion);
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", connectionStatus=" + connectionStatus +
                '}';
    }

    @Override
    public void connectToNetwork() {
        if (!getConnectionStatus()){
            String choice;
            do {
               try {
                   System.out.println("Please enter your Username: ");
                   String uN = scanner.nextLine();
                   System.out.println("Please enter your Password: ");
                   String psw = scanner.nextLine();
                   if (Server.getUser(getUserName()).getUserName().equals(uN)) { // gets the username by the "userNames_passwords" data structure ->
                       // -> it passes as a string the username of the particular object
                       if (Server.getUser(getUserName()).getPassword().equals(psw)) { // similar way
                           chooseComputer();
                           choice = "0";
                       } else {
                           throw new InvalidAuthenticationDataException("The password that you entered for the username {" +
                                    uN  + "} is invalid.");
                       }
                   } else {
                       throw new InvalidAuthenticationDataException("The Username that you entered is invalid.");
                   }
               }catch (InvalidAuthenticationDataException e){
                   System.out.println(e.getMessage());
                   boolean repeat;
                   do{
                   repeat = false;
                       System.out.println("If you want to repeat the process for the user {" + this.getUserName() + "} press '1'," +
                               " else press '0'.");
                       choice = scanner.nextLine();
                       if (!choice.equals("0") && !choice.equals("1")) {
                           System.out.println("Input must be either 0 or 1.");
                           repeat = true;
                       }
                   }while (repeat);
               }
           }while (choice.equals("1"));
        }
        else {
            System.out.println("User {" + getUserName() + "} has already authenticated from a Computer with IP Address: "+
                    Server.getMappedComputer(this).getIP_address());
        }
    }

    @Override
    public void disconnectFromNetwork() {
        if (getConnectionStatus()){
            setConnectionStatus(false);
            Server.getMappedComputer(this).setAvailability(true);
            Server.returnIPAddress(Server.getMappedComputer(this).getIP_address());
            Server.getMappedComputer(this).setIP_address(null);
            Server.removeMapping(this);
            System.out.println("User was disconnected from the network successfully");
        }
        else {
            System.out.println("You are not connected to the network. Therefore you cannot perform this operation.");
            boolean repeat;
            do {
                repeat = false;
                System.out.println("Do you want to connect to the network? - Press '1' for Yes, '0' for No.");
                String choice = scanner.nextLine();
                if (!choice.equals("0") && !choice.equals("1")) {
                    System.out.println("Input must be either 0 or 1.");
                    repeat = true;
                }
                else if (choice.equals("1")){
                    connectToNetwork();
                    repeat = false;
                }
            }while (repeat);
        }
    }

    @Override
    public void pingComputer() {
        if (getConnectionStatus()){
            boolean askAgain;
            do {
                askAgain = false;
                System.out.println("Search if a computer is connected. Press:\n" +
                        " 'H' -> search by its Host Name\n" +
                        " 'IP -> search by its IP");
                String choice = scanner.nextLine();
                switch (choice) {
                    case "H":
                        System.out.println("Enter the Host Name: ");
                        String hn = scanner.nextLine();
                        if (Server.getDeviceByHN(hn) != null) {
                            System.out.println(Server.getDeviceByHN(hn).isAvailable() ? hn + " is not connected to the " +
                                    "network." : hn + " is connected to the network.");
                        } else {
                            System.out.println("Host Name does not exist.");
                        }
                        break;
                    case "IP":
                        System.out.println("Enter the IP Address: ");
                        String ip = scanner.nextLine();
                        if (Server.getDeviceByIP(ip) != null) {
                            System.out.println(Server.getDeviceByIP(ip).isAvailable() ? ip + " is not connected to the " +
                                    "network." : ip + " is connected to the network.");
                        } else {
                            System.out.println("IP Address does not exist.");
                        }
                        break;
                    default:
                        System.out.println("You need to choose one of the two options.");
                        askAgain = true;
                        break;
                }
            }while (askAgain);
        }
        else {
            System.out.println("You are not connected to the network. Therefore you cannot perform this operation.");
            boolean repeat;
            do {
                repeat = false;
                System.out.println("Do you want to connect to the network? - Press '1' for Yes, '0' for No.");
                String choice = scanner.nextLine();
                if (!choice.equals("0") && !choice.equals("1")) {
                    System.out.println("Input must be either 0 or 1.");
                    repeat = true;
                }
                else if (choice.equals("1")){
                    connectToNetwork();
                    repeat = false;
                }
            }while (repeat);
        }
    }
}