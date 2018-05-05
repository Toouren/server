package fileWorker;

import java.io.File;
import java.security.NoSuchAlgorithmException;

public interface IExecutable {
    byte[] process(File f) throws NoSuchAlgorithmException;
    byte[] process(byte[] message) throws NoSuchAlgorithmException;
}
