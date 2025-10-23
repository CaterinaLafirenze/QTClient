package defaultpackage;

import java.net.SocketException;

/**
 * Eccezione che viene lanciata quando la comunicazione client-server non viene eseguita corretamente.
 */
public class ServerException extends Exception{
    public ServerException(String msg) {super(msg);}
}
