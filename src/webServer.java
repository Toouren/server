import threadDispatcher.*;
import fileWorker.fileWorker;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class webServer {
    public static void main(String[] args) throws IOException {
        ThreadDispatcher threadDispatcher = ThreadDispatcher.getInstance();
        fileWorker flw = fileWorker.getInstance();

        threadDispatcher.Add(flw);

        int port = 9090;

        ServerSocket server = new ServerSocket(port);
        while (true){
            Socket client = server.accept();
            System.out.println("Connection accepted");
            threadDispatcher.Add(new clientWorker(client));
        }
    }
}
