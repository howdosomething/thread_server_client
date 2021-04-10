public class thread {
    public thread() {

        Thread m = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true)
                    System.out.println("hi");
            }
        });
        m.start();
    }
}
