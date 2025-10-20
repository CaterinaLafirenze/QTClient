package defaultpackage;

import java.net.SocketException;

public class ServerException extends SocketException {
    public ServerException(String msg) {super(msg);}
}
