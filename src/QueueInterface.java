public interface QueueInterface {

    /**
     * Determines whether a queue is empty.
     *
     * @return Returns true if the queue is empty; otherwise returns false.
     */
    public boolean isEmpty();

    /**
     * Determines whether a queue is full.
     *
     * @return Returns true if the queue is full; otherwise returns false.
     */
    public boolean isFull();

    /**
     * Adds an item at the back of a queue.
     *
     * @param newItem the item to be inserted
     * @throws QueueException if newItem cannot be added to the queue (Some implementations)
     */
    public void enqueue(Object newItem) throws QueueException;

    /**
     * Retrieves and removes the front of a queue.
     *
     * @return the item that was added to the queue earliest
     * @throws QueueException if the queue is empty
     */
    public Object dequeue() throws QueueException;

    /**
     * Removes all items of a queue.
     */
    public void dequeueAll();

    /**
     * Retrieves the item at the front of a queue.
     *
     * @return the item that was added to the queue earliest
     * @throws QueueException if the queue is empty
     */
    public Object peek() throws QueueException;

}