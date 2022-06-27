public class QueueArrayBased implements QueueInterface {

    private final int MAX_SIZE = 2000; // the size of the array
    private Object[] items; // the placeholder array
    private int back;       // the end where items are inserted
    private int front;      // the end where items are removed
    private int count;      // the number of items in the queue

    public QueueArrayBased() {
        items = new Object[MAX_SIZE];
        back = MAX_SIZE - 1;
        front = 0;
        count = 0;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public boolean isFull() {
        return count == MAX_SIZE;
    }

    public void enqueue(Object newItem) {
        if (!isFull()) {
            back = (back + 1) % (MAX_SIZE);
            items[back] = newItem;
            ++count;
        } else
            throw new QueueException("QueueException on enqueue: " + "Queue full");
    }

    public Object dequeue() throws QueueException {
        if (!isEmpty()) {
            Object queueFront = items[front];
            front = (front + 1) % (MAX_SIZE);
            --count;
            return queueFront;
        } else
            throw new QueueException("QueueException on dequeue: " + "Queue empty");
    }

    public void dequeueAll() {
        items = new Object[MAX_SIZE];
        front = 0;
        back = MAX_SIZE - 1;
        count = 0;
    }

    public Object peek() throws QueueException {
        if (!isEmpty())
            return items[front];
        else
            throw new QueueException("Queue exception on peek: " + "Queue empty");
    }
}