package threadDispatcher;

import java.util.HashMap;
import java.util.Map;

public class ThreadDispatcher {
    private Map<Long, String> workingThreads;
    private ThreadMonitor Monitor;

    private static ThreadDispatcher instance;

    private ThreadDispatcher(){
        workingThreads = new HashMap<>();
        Monitor = new ThreadMonitor();
        Add(Monitor);
    }

    public static synchronized ThreadDispatcher getInstance(){
        if (instance == null) {
            instance = new ThreadDispatcher();
        }
        return instance;
    }

    public void Add(ThreadedTask Task){
        Thread taskThread = new Thread(Task);
        taskThread.start();
        addToWorkingThreads(taskThread.getId(), Task.name);
        Monitor.setThreadChangedFlag();
    }

    public Map<Long, String> getWorkingThreads(){
        return workingThreads;
    }

    private void addToWorkingThreads(long id, String name){
        workingThreads.put(id, name);
    }

    public synchronized void delFromWorkingThreads(long id){
        workingThreads.remove(id);
        System.out.println(workingThreads);
        Monitor.setThreadChangedFlag();
    }
}
