import java.util.Random;

public class PhilosopherRandom implements Runnable {
    private final Thread thread;

    // Shared between all instances, the total number of chopsticks on the table
    public static int totalChopsticks;

    // In each instance, the number of dumplings that a single Philosopher has eaten
    private int dumplingsEaten = 0;

    // The number of chopsticks a single Philosopher currently has
    private int chopsticksOwned = 0;

    // For the randomness simulation, the number of chopsticks a Philosopher needs
    // to eat
    private final int sticksNeeded;

    // Used to distinguish each Philosopher instance
    private final int threadNum;

    // Used as the runner variable that stops the simulation when set to false
    public volatile boolean thinkAndEat;

    // The number of Philosopher instances we want to create
    private static final int NUM_PHILOSOPHERS = 5;

    // The length of the simulations we want to run
    public static final long PROCESSING_TIME = 10 * 1000;

    // Boolean value that dictates whether or not it is the Philosopher's turn to
    // eat
    private volatile boolean myTurn = false;

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
            synchronized (PhilosopherRandom.class) {
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
        System.out.println(
                "Philosopher #" + threadNum + " who needs " + sticksNeeded + " chopsticks to eat, has started eating.");
        System.out.println("There is (are) " + totalChopsticks + " chopstick(s) left.\n");
        dumplingsEaten += 1;
        delay(500, "Eating interrupted"); // Simulate eating
        release();
    }

    /**
     * Returns the chopsticks to be redistributed after eating a dumpling
     */
    public void release() {
        totalChopsticks += chopsticksOwned;
        chopsticksOwned = 0;
        System.out.println("Philosopher #" + threadNum + " has resumed thinking...");
        System.out.println("There is (are) " + totalChopsticks + " chopstick(s) left.\n");
    }

    /**
     * Constructor for our Philosopher classes with randomized chopstick needs
     * 
     * @param i     - The unique identifier and thread number
     * @param stick - The number of chopsticks required to eat
     */
    public PhilosopherRandom(int i, int stick) {
        thread = new Thread(this, "Philosopher #" + i);
        this.threadNum = i;
        this.sticksNeeded = stick;
    }

    /**
     * Starts the thread associated with the Philosopher
     */
    public void startPhilosopher() {
        System.out.println("Philosopher #" + threadNum + " is active.");
        thread.start();
    }

    /**
     * Taken from the JuiceBottler lab, thanks Nate
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
        if (totalChopsticks >= sticksNeeded) {
            myTurn = true;
            totalChopsticks -= sticksNeeded;
            chopsticksOwned += sticksNeeded;
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
     * Waits for Philosopher to stop by using the join() method on the thread
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
        Random randNum = new Random();
        PhilosopherRandom[] philosophers = new PhilosopherRandom[NUM_PHILOSOPHERS];
        totalChopsticks = NUM_PHILOSOPHERS;
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            philosophers[i] = new PhilosopherRandom(i + 1, randNum.nextInt(1, totalChopsticks + 1));
            philosophers[i].startPhilosopher();
        }

        delay(PROCESSING_TIME, "Thinking Malfunction");

        for (PhilosopherRandom p : philosophers) {
            p.stopPhilosopher();
        }

        for (PhilosopherRandom p : philosophers) {
            p.waitToStop();
            p.getEaten();
        }
    }
}