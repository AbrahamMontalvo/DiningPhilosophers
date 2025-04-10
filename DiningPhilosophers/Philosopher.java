package DiningPhilosophers;

public class Philosopher implements Runnable {

    private final Thread thread;
    private int dumplingsEaten = 0;
    private static int totalChopsticks;
    private int chopsticksOwned = 0;
    private int threadNum;
    public volatile boolean thinkAndEat;
    private static final int NUM_PHILOSOPHERS = 5;
    public static final long PROCESSING_TIME = 5 * 1000;
    private volatile boolean myTurn = false;

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

    public void eat() {
        System.out.println("Philosopher #" + threadNum + " has started eating...");
        System.out.println("There is (are) " + totalChopsticks + " chopstick(s) left.\n");
        dumplingsEaten += 1;
        delay(500, "Eating interrupted"); // simulate eating
        release();
    }

    public void release() {
        totalChopsticks += 2;
        chopsticksOwned -= 2;
        System.out.println("Philosopher #" + threadNum + " has resumed thinking...");
        System.out.println("There is (are) " + totalChopsticks + " chopstick(s) left.\n");
    }

    public Philosopher(int i) {
        thread = new Thread(this, "Philosopher #" + i);
        this.threadNum = i;
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
        if (totalChopsticks >= 2) {
            myTurn = true;
            totalChopsticks -= 2;
            chopsticksOwned += 2;
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
        Philosopher[] philosophers = new Philosopher[NUM_PHILOSOPHERS];
        totalChopsticks = NUM_PHILOSOPHERS;
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