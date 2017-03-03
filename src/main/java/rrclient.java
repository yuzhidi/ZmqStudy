import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

/**
* Hello World client
* Connects REQ socket to tcp://localhost:5559
* Sends "Hello" to server, expects "World" back
*/
public class rrclient{

    public static void main(String[] args) {
        Context context = ZMQ.context(1);

        //  Socket to talk to server
        Socket requester = context.socket(ZMQ.REQ);
        requester.connect("tcp://localhost:5559");
        
        System.out.println("launch and connect client.");
        final long l = System.currentTimeMillis()/3;
        final int i = (int)( l % 10 );
        for (int request_nbr = 0; request_nbr < 100; request_nbr++) {
            requester.send("Hello:["+i+"],"+request_nbr, 0);
            String reply = requester.recvStr(0);
            System.out.println("["+i+"] Received reply " + request_nbr + " [" + reply + "]");
        }
        
        //  We never get here but clean up anyhow
        requester.close();
        context.term();
    }
}
