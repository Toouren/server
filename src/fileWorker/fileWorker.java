package fileWorker;
import threadDispatcher.ThreadedTask;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class fileWorker extends ThreadedTask {
    private static fileWorker instance;


    private boolean isRecursive;
    private boolean workFlag;
    private File currentDir;
    private byte[] resultHash = "".getBytes();

    private fileWorker(){
        name = "FileWorker";
        workFlag = true;
    }


    public static synchronized fileWorker getInstance(){
        if (instance == null) {
            instance = new fileWorker();
        }
        return instance;
    }

    public boolean getIsRecursive() {
        return isRecursive;
    }

    public void setRecursive(boolean recursive) {
        isRecursive = recursive;
    }

    private byte[] concatByteArrays(byte[] leftArray, byte[] rightArray){
        byte[] resultArray = new byte[leftArray.length + rightArray.length];
        System.arraycopy(leftArray, 0, resultArray, 0, leftArray.length);
        System.arraycopy(rightArray, 0, resultArray, leftArray.length, rightArray.length);
        return resultArray;
    }

    private void processWithFlag(File file, IExecutable command) throws NoSuchAlgorithmException {
        File[] folderEntries = file.listFiles();
        if (folderEntries != null) {
            for (File entry : folderEntries){
                if (entry.isDirectory()){
                    processWithFlag(entry, command);
                }
                else{
                    resultHash = concatByteArrays(resultHash, command.process(entry));
                }
            }
        }
    }

    private void processWithoutFlag(File file, IExecutable command) throws NoSuchAlgorithmException{
        File[] folderEntries = file.listFiles();
        if (folderEntries != null){
            for (File entry : folderEntries){
                if (entry.isFile()){
                   resultHash = concatByteArrays(resultHash, command.process(entry));
                }
            }
        }
    }

    public synchronized String execute(IExecutable command, String dir) throws NoSuchAlgorithmException {
        currentDir = new File(dir);
        if (currentDir.isDirectory())
        {
            if (isRecursive) {
                //System.out.println("Dir with flag");
                processWithFlag(currentDir, command);
                return (currentDir.getAbsolutePath() + " : " + Arrays.toString(command.process(resultHash)));
            }
            else {
                //System.out.println("Dir without flag");
                processWithoutFlag(currentDir, command);
                return (currentDir.getAbsolutePath() + " : " + Arrays.toString(command.process(resultHash)));
            }
        }
        else {
            //System.out.println("File");
            return (currentDir.getAbsolutePath() + " : " + Arrays.toString(command.process(currentDir)));
        }
    }

    public synchronized File[] list(){
        if (currentDir != null){
            return currentDir.listFiles();
        }
        else{
            return null;
        }
    }

    public void setCurrentDir(String currentDir) {
        this.currentDir = new File(currentDir);
    }

    @Override
    public void run() {
        while (workFlag){}
        finish();
    }
}
