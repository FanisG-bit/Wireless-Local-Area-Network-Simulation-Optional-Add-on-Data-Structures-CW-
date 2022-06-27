public class Computer {
    private String hostName;
    private String IP_address;
    private boolean availability;

    /**
     * Standard Constructor for new Computer
     * @param hostName is the name of the computer e.g. JARVIS-PC
     */
    public  Computer(String hostName){
        this.hostName = hostName;
        this.availability = true; // the computer is available to be used when created
        addDeviceOnServerScope(); //it is picked up by the access point and it can now access the Internet
    }

    /**
     * This method adds device on server scope (for manipulation etc)
     */
    private void addDeviceOnServerScope() {
        Server.addComputer(this);
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getIP_address() {
        return IP_address;
    }

    public void setIP_address(String IP_address){ this.IP_address = IP_address; }

    public boolean isAvailable() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    @Override
    public String toString() {
        return "Computer{" +
                "hostName='" + hostName + '\'' +
                ", IP_address='" + IP_address + '\'' +
                ", availability=" + availability +
                '}';
    }
}