import org.zeromq.ZMQ;

import java.util.Random;

//
//  Weather update server in Java
//  Binds PUB socket to tcp://*:5556
//  Publishes random weather updates
//
public class wuserverPub10001 {

    public static void main (String[] args) throws Exception {
        //  Prepare our context and publisher
        ZMQ.Context context = ZMQ.context(1);

        ZMQ.Socket publisher = context.socket(ZMQ.PUB);
        publisher.bind("tcp://*:5556");
        publisher.bind("ipc://weather");

        //  Initialize random number generator
        Random srandom = new Random(System.currentTimeMillis());
        while (!Thread.currentThread ().isInterrupted ()) {
            //  Get values that will fool the boss
            int zipcode, temperature, relhumidity;
            zipcode = 10000 + srandom.nextInt(10000) ;
            temperature = srandom.nextInt(215) - 80 + 1;
            relhumidity = srandom.nextInt(50) + 10 + 1;

            Thread.sleep(1000);
            //  Send message to all subscribers
            String update = String.format("%05d %d %d", 10001, temperature, relhumidity);
            publisher.send(update, 0);
        }

        publisher.close ();
        context.term ();
    }
}
