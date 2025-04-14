import java.util.Random;

public class PhilosopherRandom implements Runnable {
    private final Thread thread;
    public static int totalChopsticks;
    private int dumplingsEaten = 0;
    private int chopsticksOwned = 0;
    private final int sticksNeeded;
    private final int threadNum;
    public volatile boolean thinkAndEat;
    private static final int NUM_PHILOSOPHERS = 5;
    public static final long PROCESSING_TIME = 5 * 1000;
    private volatile boolean myTurn = false;
    private volatile boolean starving = false;

    /**
     * Method refactored by ChatGPT
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
     */
    public void eat() {
        System.out.println("Philosopher #" + threadNum + " who needs " + sticksNeeded + " chopsticks to eat, has started eating.");
        System.out.println("There is (are) " + totalChopsticks + " chopstick(s) left.\n");
        dumplingsEaten += 1;
        delay(500, "Eating interrupted"); // simulate eating
        release();
    }

    public void release() {
        totalChopsticks += chopsticksOwned;
        chopsticksOwned = 0;
        System.out.println("Philosopher #" + threadNum + " has resumed thinking...");
        System.out.println("There is (are) " + totalChopsticks + " chopstick(s) left.\n");
    }

    public PhilosopherRandom(int i, int stick) {
        thread = new Thread(this, "Philosopher #" + i);
        this.threadNum = i;
        this.sticksNeeded = stick;
    }

    public void startPhilosopher() {
        System.out.println("Philosopher #" + threadNum + " is active.");
        thread.start();
    }

    /**
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

    public void stopPhilosopher() {
        thinkAndEat = false;
    }

    public int getChopsticks() {
        return this.chopsticksOwned;
    }

    public void acquire() {
        if (totalChopsticks >= sticksNeeded) {
            myTurn = true;
            totalChopsticks -= sticksNeeded;
            chopsticksOwned += sticksNeeded;
            eat();
        }
    }

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

    public static void main(String[] args) {
        Random randNum= new Random();
        PhilosopherRandom[] philosophers = new PhilosopherRandom[NUM_PHILOSOPHERS];
        totalChopsticks = NUM_PHILOSOPHERS;
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            philosophers[i] = new PhilosopherRandom(i + 1, randNum.nextInt(1,totalChopsticks+1));
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
