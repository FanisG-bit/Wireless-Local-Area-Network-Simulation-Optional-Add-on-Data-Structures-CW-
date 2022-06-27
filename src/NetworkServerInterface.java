public  interface NetworkServerInterface {
    /**
     * it reads the file of user-names and passwords (server.txt) into an appropriate
     * structure (array-based-list) in main memory.
     */
    void startServer();

    /**
     *  adds a new user composed of
     *  @param userName the user to be added
     *  @param password  his/her password
     *  to the data structure.
     *  Be aware: Same user cant be added twice
     */
    void addUser(String userName, String password);

    /**
     *  removes an already existing user
     *  @param userName the user to be removed
     *  from the data structure.
     *  Be aware: User needs to exist, A user who is currently connected cant be removed
     */
    void removeUser(String userName);

    /**
     * it lists all computers currently connected to the network (IP Address, HostName,
     * Username)
     */
    void viewConnectedComputers();
}