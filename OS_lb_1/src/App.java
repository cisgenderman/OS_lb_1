public class App 
{
    /*
    //Функция-поставщик для Java
    void synchronized provide()
    {
        if (ready)
        return;
        ready = true;
        notify();
    }
    //Функция-потребитель для Java
    void synchronized consume()
    {
        while(!ready)
        wait();
        ready = false;
    }
    */
    
    //object sinhroniz
    private static final Object lock = new Object();
    
    private static boolean ready = false;

    private static void provide(){
        synchronized (lock){
            if (ready)
                return;
            //pomeshaem v buffer
            ready = true;
            System.out.println("Provided");
            //uvedomlyaem consumer chto dannie gotovi
            lock.notify();
        }
    }

    private static void consume(){
        synchronized (lock){
            while (!ready){
                try{
                    lock.wait();
                    System.out.println("Awoke");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }        
            }
            //potreblyaem dannie
            ready = false;
            System.out.println("Consumed");
        }
    }

    //provider
    private static class Provider extends Thread{
        @Override
        public void run(){
            while (true){
                provide();
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    //Consumer
    private static class Consumer extends Thread{
        @Override
        public void run(){
            while (true){
                consume();
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception 
    {
        System.out.println("Hello, World!");
        Thread providerThread = new Provider();
        Thread consumerThread = new Consumer();

        providerThread.start();
        consumerThread.start();

        try{
            providerThread.join();
            consumerThread.join();
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}
