package DiningPhilosophers;

public class Philosopher implements Runnable {

    private final Thread thread;
    private int dumplingsEaten = 0;
    private int totalChopsticks = 0;
    private int chopsticksOwned = 0;
    private int threadNum;
    private static boolean thinkAndEat;
    private int eatenDumplings = 0;
    private static final int NUM_PHILOSOPHERS = 5;
    public static final long PROCESSING_TIME = 5 * 1000;
    
        @Override
        public void run() {
            while(thinkAndEat){
                synchronized (this) {
                    try {
                        if(getChopsticks() >=  2) {
                            eat();
                        }
                        else{
                            wait();
                        }
                    } 
                    catch (InterruptedException e) {}
                }
            }
        }
    
        public void eat() {
            System.out.println("Philosopher #" + threadNum + " has started eating...");
        }
    
        public void giveUpChopsticks() {
            System.out.println("Philosopher #" + threadNum + " has resumed thinking...");
        }
    
        public Philosopher(int i){
            thread = new Thread(this, "Philosopher #" + threadNum);
            this.threadNum = i;
        }
    
        public void startPhilosopher(){
            thinkAndEat = true;
            thread.start();
        }
    
        /**
         * @param time - length of time we want to sleep the thread
         * @param errMsg - error message thrown when we slepe the thread and it
         * throws the InterruptedException
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
        
        public void stopPhilosopher(){
            try {
                thread.join();
            }
            catch (InterruptedException e) {
                System.err.println(thread.getName() + " stop malfunction");
            }
        }
    
        public int getChopsticks() {
            return this.chopsticksOwned;
        }

        public String getEaten() {
            return "Philosopher #" + threadNum + " ate " + eatenDumplings;
        }
    
        public static void main(String[] args) {
            Philosopher[] philosophers = new Philosopher[NUM_PHILOSOPHERS];
            int totalChopsticks = NUM_PHILOSOPHERS;
            for(int i = 0;i < NUM_PHILOSOPHERS;i++){
                philosophers[i] = new Philosopher(i);
                philosophers[i].startPhilosopher();
            }

            delay(PROCESSING_TIME, "Thinking Malfunction");

            for(int i = 0;i < NUM_PHILOSOPHERS;i++){
                philosophers[i].stopPhilosopher();
            }

            for(int i = 0;i < NUM_PHILOSOPHERS;i++){
                System.out.println(philosophers[i].getEaten());
            }
    }
}