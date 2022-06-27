public interface NetworkUserInterface {
    /**
     * The user can choose this option in order to connect to the network
     * A user who is already connected, cannot perform this operation at the same time
     */
    void connectToNetwork();

    /**
     * The user can choose this option in order to disconnect from the network
     * The user must be already connected to in order to perform this operation
     */
    void disconnectFromNetwork();

    /**
     * The user can choose this option in order to see if another user is connected
     * to the network. He can choose to do this ether via IP address or host name
     */
    void pingComputer();
}