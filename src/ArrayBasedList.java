/**
 * Array-based implementation of the ADT list
 */
public class ArrayBasedList implements ListInterface {
    private int MAX_SIZE = 2;
    private Object dataItems[];
    private int numItems;

	/**
	 * Creates an empty list
	 */
	public ArrayBasedList() {
        dataItems = new Object[MAX_SIZE];
        numItems = 0;
    }

    public boolean isEmpty() {
        return (numItems == 0);
    }

    public int size() {
        return numItems;
    }

	/**
	 * Retrieves the content of a specific position.
	 * @param index The index of the position ranges 1..size()
	 * @return The content of the list in the specified position
	 * @throws ListIndexOutOfBoundsException If index value is illegal
	 */
    public Object get(int index)
            throws ListIndexOutOfBoundsException {
        if (index >= 1 && index <= numItems)
            return dataItems[translate(index)];
        else {
            throw new ListIndexOutOfBoundsException(
                    "ListIndexOutOfBoundsException on get");
        }
    }

	/**
	 * Inserts a new element at specific position shifting those that follow it.
	 * @param index The index ranges from 1..size+1 (after the last element).
	 * @param newDataItem The new element's value
	 * @throws ListIndexOutOfBoundsException If index value is illegal
	 */
    public void add(int index, Object newDataItem)
            throws ListIndexOutOfBoundsException {
        if (index < 1 || index > numItems + 1)
            throw new ListIndexOutOfBoundsException("ListIndexOutOfBoundsException on add");

        if (numItems == MAX_SIZE)
            resize("expand");

        for (int pos = numItems; pos >= index; pos--)
            dataItems[translate(pos + 1)] = dataItems[translate(pos)];

        dataItems[translate(index)] = newDataItem;
        numItems++;
    }

	/**
	 * Removes the element at specific position and closes the gap.
	 * @param index The index of the position ranges 1..size().
	 * @throws ListIndexOutOfBoundsException If index value is illegal
	 */
	public void remove(int index)
            throws ListIndexOutOfBoundsException {
        if (index < 1 || index > numItems) {
            throw new ListIndexOutOfBoundsException(
                    "ListIndexOutOfBoundsException on remove");
        }

        for (int pos = index + 1; pos <= size(); pos++)
            dataItems[translate(pos - 1)] = dataItems[translate(pos)];
        numItems--;

        if (numItems == (MAX_SIZE / 2))
            resize("shrink");
    }

    private int translate(int position) {
        return position - 1;
    }

	/**
	 * Resizes the collection.
	 * @param allocation Doubles size if "expand", halves it otherwise
	 */
	private void resize(String allocation) {
        if (allocation == "expand")
            MAX_SIZE = MAX_SIZE * 2;
        else
            MAX_SIZE = MAX_SIZE / 2;

        Object[] newDataItems = new Object[MAX_SIZE];

        for (int i = 0; i < numItems; i++)
            newDataItems[i] = dataItems[i];
        dataItems = newDataItems;
    }

}