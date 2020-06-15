package main.dataStructures;

import java.util.LinkedList;

// Queue implementation using doubly linked list (provided by Java)
// Generic type data
// Source: https://www.youtube.com/watch?v=RBSGKlAvoiM&t=3510s
// Does not support null elements
public class Queue <T> implements Iterable <T> {

    private LinkedList<T> list = new LinkedList <T> ();

    public Queue() {}

    public Queue(T firstElement) {
        enqueue(firstElement);
    }

    // Return the size of the queue
    public int size() {
        return list.size();
    }

    // Returns whether or not the queue is empty
    public boolean isEmpty() {
        return size() == 0;
    }

    // Peek the element the front of the queue
    // The method throws an error if the queue is empty
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Queue Empty");
        }
        return list.peekFirst();
    }

    // Poll an element from the front of the queue (removes from the queue)
    // The method throws an error if the queue is empty
    public T dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Queue Empty");
        }
        return list.removeFirst();
    }

    // Add an element to the back of the queue
    public void enqueue(T element) {
        list.addLast(element);
    }

    // Return an iterator to allow the user to traverse
    // through the elements found inside the queue
    @Override public java.util.Iterator <T> iterator () {
        return list.iterator();
    }
}