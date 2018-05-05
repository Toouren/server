package threadDispatcher;

public abstract class ThreadedTask implements Runnable{
    protected String name;

    protected void finish(){
        ThreadDispatcher threadDispatcher = ThreadDispatcher.getInstance();
        long threadId = Thread.currentThread().getId();
        threadDispatcher.delFromWorkingThreads(threadId);
        System.out.println(threadId);
    }
}