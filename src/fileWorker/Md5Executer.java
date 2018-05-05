package fileWorker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Executer implements IExecutable {

    public byte[] process(File file)
    {
        byte[] hash = "".getBytes();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            DigestInputStream digestInputStream = new DigestInputStream(new FileInputStream(file), messageDigest);
            while (digestInputStream.read() >= 0);
            hash = messageDigest.digest();
        }
        catch (NoSuchAlgorithmException | IOException exception){
            exception.printStackTrace();
        }
        return hash;
    }
    public byte[] process(byte[] message){
        byte[] hash = "".getBytes();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            hash = messageDigest.digest(message);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }
}
