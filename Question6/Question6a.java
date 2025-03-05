package Question6;

class NumberPrinter {
    private int n;
    private int current = 1;
    private boolean zeroTurn = true; // true means it's zero's turn

    public NumberPrinter(int n) {
        this.n = n;
    }

    // This method prints a zero n times
    public synchronized void printZero() throws InterruptedException {
        for (int i = 1; i <= n; i++) {
            while (!zeroTurn) { // Wait if it's not zero's turn
                wait();
            }
            System.out.print("0"); // Print zero
            zeroTurn = false; // Switch turn to number printing
            notifyAll(); // Notify other waiting threads
        }
    }

    // This method prints even numbers when it is their turn
    public synchronized void printEven() throws InterruptedException {
        while (current <= n) {
            while (zeroTurn || (current % 2 != 0)) { // Wait if it's zero's turn or current is odd
                if (current > n) break; // Ensure we do not exceed n
                wait();
            }
            if (current > n) break; // Double-check after waking up
            System.out.print(current); // Print even number
            current++; // Move to the next number
            zeroTurn = true; // Switch turn back to zero
            notifyAll(); // Notify other waiting threads
        }
    }

    // This method prints odd numbers when it is their turn
    public synchronized void printOdd() throws InterruptedException {
        while (current <= n) {
            while (zeroTurn || (current % 2 == 0)) { // Wait if it's zero's turn or current is even
                if (current > n) break; // Ensure we do not exceed n
                wait();
            }
            if (current > n) break; // Double-check after waking up
            System.out.print(current); // Print odd number
            current++; // Move to the next number
            zeroTurn = true; // Switch turn back to zero
            notifyAll(); // Notify other waiting threads
        }
    }
}

public class Question6a {
    public static void main(String[] args) {
        int n = 5; // For example, to print sequence "0102030405"
        NumberPrinter np = new NumberPrinter(n);

        // Thread that prints zeros
        Thread zeroThread = new Thread(() -> {
            try {
                np.printZero();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Thread that prints even numbers
        Thread evenThread = new Thread(() -> {
            try {
                np.printEven();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Thread that prints odd numbers
        Thread oddThread = new Thread(() -> {
            try {
                np.printOdd();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Start all threads
        zeroThread.start();
        evenThread.start();
        oddThread.start();

        // Wait for threads to finish
        try {
            zeroThread.join();
            evenThread.join();
            oddThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
