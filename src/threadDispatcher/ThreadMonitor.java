package threadDispatcher;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class ThreadMonitor extends ThreadedTask{
    private volatile boolean threadChangedFlag = false;
    private volatile boolean workingFlag = true;
    ThreadMonitor(){
        name = "ThreadMonitor";
    }
    @Override
    public void run() {
        ThreadDispatcher threadDispatcher = ThreadDispatcher.getInstance();
        try {
            writeInfo(threadDispatcher);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (workingFlag){
            try {
                if (threadChangedFlag) {
                    writeInfo(threadDispatcher);
                    threadChangedFlag = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        finish();
        System.exit(1);
    }

    public void setThreadChangedFlag(){
        threadChangedFlag = true;
    }

    public void setWorkingFlag(){
        workingFlag = true;
    }

    private void writeInfo(ThreadDispatcher threadDispatcher) throws IOException {
        Map<Long, String> workingThreads = threadDispatcher.getWorkingThreads();
        FileWriter fileWriter = new FileWriter("WorkingThreads.txt");
        fileWriter.write(String.valueOf(workingThreads));
        fileWriter.close();
    }
}
