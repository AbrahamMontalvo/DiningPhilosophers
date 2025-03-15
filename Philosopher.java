package DiningPhilosophers;

public class Philosopher implements Runnable {

    private final Thread thread;
    private int dumplingsEaten = 0;
    private static int totalChopsticks;
    private int chopsticksOwned = 0;
    private int threadNum;
    private volatile boolean thinkAndEat;
    private static final int NUM_PHILOSOPHERS = 5;
    public static final long PROCESSING_TIME = 5 * 1000;

    @Override
    public void run() {
        while (thinkAndEat) {
            synchronized (this) {
                try {
                    if (acquire()) {
                        eat();
                        Thread.sleep(50);
                    } else {
                        wait();
                    }
                } catch (InterruptedException e) {
                }
            }
        }
        System.out.println(thread.getName() + " done");
        System.out.println(getEaten());
    }

    public void eat() {
        System.out.println("Philosopher #" + threadNum + " has started eating...");
        System.out.println("There are " + totalChopsticks + " left.\n");
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
        }
        dumplingsEaten += 1;
        giveUpChopsticks();
    }

    public void giveUpChopsticks() {
        totalChopsticks += 2;
        chopsticksOwned -= 2;
        System.out.println("Philosopher #" + threadNum + " has resumed thinking...");
    }

    public Philosopher(int i) {
        thread = new Thread(this, "Philosopher #" + i);
        this.threadNum = i;
    }

    public void startPhilosopher() {
        thinkAndEat = true;
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

    public boolean acquire() {
        if (totalChopsticks >= 2) {
            totalChopsticks -= 2;
            chopsticksOwned += 2;
            return true;
        }
        return false;
    }

    public String getEaten() {
        return "Philosopher #" + threadNum + " ate " + dumplingsEaten + " dumplings";
    }

    /**
     * Waits for Plant to stop and for all Workers to stop
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