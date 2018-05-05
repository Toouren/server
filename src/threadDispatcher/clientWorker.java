package threadDispatcher;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import fileWorker.*;

public class clientWorker extends ThreadedTask {

    private Socket client;
    private Pattern pattern;
    private boolean workFlag;
    private fileWorker flWorker;

    public clientWorker(Socket sock){
        workFlag = true;
        client = sock;
        flWorker = fileWorker.getInstance();
        pattern = Pattern.compile("hash (.*)");
        name = "ClientWorker";
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);

            String command;

            label:
            while (workFlag) {
                System.out.println("Trying to get command...");
                command = in.readLine();
                System.out.println("Command: " + command);
                switch (command) {
                    case "list":
                        File[] listResult = flWorker.list();
                        for (File file : listResult){
                            out.println(file.getName());
                        }
                        out.flush();
                        break;
                    case "stop":
                        workFlag = false;
                        out.println("Stopped");
                        out.close();
                        in.close();
                        client.close();
                        finish();
                        break label;
                    default:
                        Matcher matcher = pattern.matcher(command);
                        String dir = matcher.group(1);
                        String hash = flWorker.execute(new Md5Executer(), dir);
                        out.println(hash);
                        out.flush();
                        break;
                }
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            workFlag = false;
            finish();
        }
    }
}
