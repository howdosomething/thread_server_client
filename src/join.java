

public class join {
    public  static void main(String[] args) {
        Thread t2 = new SampleThread(5);
        t2.start();

        System.out.println("Invoking join");
        try {
            t2.join(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Returned from join");
    }


    static  class SampleThread extends Thread {
        public int processingCount = 0;
    SampleThread(int processingCount) {
        this.processingCount = processingCount;
        System.out.println("Create");
    }

    @Override
    public void run() {
        System.out.println("Thread " + this.getName() + " started");
        while (processingCount > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Thread " + this.getName() + " interrupted");
            }
            processingCount--;
        }
        System.out.println("Thread " + this.getName() + " exiting");
    }
}
}
