/**
 * File: Philosopher.java
 * Author(s): Abigail Brown and Abraham Montalvo
 * Task: Group Project - Dining Philosophers
 * Due Date: April 14, 2025
 * 
 * This is our official solution to the Dining Philosophers problem.
 */

public class Philosopher implements Runnable {
    private final Thread thread;

    // Shared between all instances, the total number of chopsticks on the table
    public static int totalChopsticks;

    // In each instance, the number of dumplings that a single Philosopher has eaten
    private int dumplingsEaten = 0;

    // The number of chopsticks a single Philosopher currently has
    private int chopsticksOwned = 0;

    // Used to distinguish each Philosopher instance
    private final int threadNum;

    // Used as the runner variable that stops the simulation when set to false
    public volatile boolean thinkAndEat;

    // The number of Philosopher instances we want to create
    private static final int NUM_PHILOSOPHERS = 5;

    // The length of the simulations we want to run
    public static final long PROCESSING_TIME = 5 * 1000;

    /**
     * Method refactored by ChatGPT
     * 
     * Thread-running method that we use to control the behavior of each Philsopher
     * instance
     * Synchronized on the class itself to share the totalChopsticks variable
     */
    @Override
    public void run() {
        thinkAndEat = true;
        while (thinkAndEat) {
            synchronized (Philosopher.class) {
                acquire();
            }
            delay(500, "Thinking interrupted"); // Add delay to simulate thinking
        }
        System.out.println(thread.getName() + " done");
        System.out.println(getEaten());
    }

    /**
     * Method refactored by ChatGPT
     * 
     * Allows the Philosopher to eat one dumpling before releasing the chopsticks
     */
    public void eat() {
        System.out.println("Philosopher #" + threadNum + " has started eating...");
        System.out.println("There is (are) " + totalChopsticks + " chopstick(s) left.\n");
        dumplingsEaten += 1;
        delay(500, "Eating interrupted"); // simulate eating
        release();
    }

    /**
     * Returns the chopsticks to be redistributed after eating a dumpling
     */
    public void release() {
        totalChopsticks += 2;
        chopsticksOwned -= 2;
        System.out.println("Philosopher #" + threadNum + " has resumed thinking...");
        System.out.println("There is (are) " + totalChopsticks + " chopstick(s) left.\n");
    }

    /**
     * Constructor for our Philosopher classes with uniform chopstick needs (2)
     * 
     * @param i - The unique identifier and thread number
     */
    public Philosopher(int i) {
        thread = new Thread(this, "Philosopher #" + i);
        this.threadNum = i;
    }

    /**
     * Starts the thread associated with the Philosopher
     */
    public void startPhilosopher() {
        System.out.println("Philosopher #" + threadNum + " is active.");
        thread.start();
    }

    /**
     * Method taken from JuiceBottler, thanks Nate
     * 
     * @param time   - length of time we want to sleep the thread
     * @param errMsg - error message thrown when we slepe the thread and it
     *               throws the InterruptedException
     * @return void
     */
    private static void delay(long time, String errMsg) {
        long sleepTime = Math.max(1, time);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            System.err.println(errMsg);
        }
    }

    /**
     * Sets the runner boolean to false and kills the thread
     */
    public void stopPhilosopher() {
        thinkAndEat = false;
    }

    /**
     * Getter for number of chopsticks
     * 
     * @return The number of chopsticks that this Philosopher has
     */
    public int getChopsticks() {
        return this.chopsticksOwned;
    }

    /**
     * Allows the philosopher to get a turn eating if there are enough chopsticks
     * for them to eat
     */
    public void acquire() {
        if (totalChopsticks >= 2) {
            totalChopsticks -= 2;
            chopsticksOwned += 2;
            eat();
        }
    }

    /**
     * Function used to summarize each simulation
     * 
     * @return String value that summarizes the Philosopher's ability to eat
     *         throughout the simulation
     */
    public String getEaten() {
        return "Philosopher #" + threadNum + " ate " + dumplingsEaten + " dumplings";
    }

    /**
     * Waits for Philosopher to stop
     */
    public void waitToStop() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            System.err.println(thread.getName() + " stop malfunction");
        }
    }

    /**
     * Main method the initializes PhilosopherRandom instances and runs the
     * simulation based on the parameters defined in the class
     * 
     * @param args - None taken
     */
    public static void main(String[] args) {
        Philosopher[] philosophers = new Philosopher[NUM_PHILOSOPHERS];

        // Only create full sets of chopsticks for distribution
        totalChopsticks = ((int) ((NUM_PHILOSOPHERS / 2))) * 2;

        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            philosophers[i] = new Philosopher(i + 1);
            philosophers[i].startPhilosopher();
        }

        delay(PROCESSING_TIME, "Thinking Malfunction");

        for (Philosopher p : philosophers) {
            p.stopPhilosopher();
        }

        for (Philosopher p : philosophers) {
            p.waitToStop();
            p.getEaten();
        }
    }
}