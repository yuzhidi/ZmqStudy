import org.zeromq.ZMQ;

import java.util.Random;

//
//  Weather update server in Java
//  Binds PUB socket to tcp://*:5556
//  Publishes random weather updates
//
public class wuserverPubConnect {

    public static void main (String[] args) throws Exception {
        //  Prepare our context and publisher
        ZMQ.Context context = ZMQ.context(1);

        ZMQ.Socket publisher = context.socket(ZMQ.PUB);
//        publisher.connect("tcp://localhost:5556");
//        publisher.bind("tcp://*:5556");
//        publisher.bind("ipc://weather");
        publisher.connect("tcp://localhost:7116");

        //  Initialize random number generator
        Random srandom = new Random(System.currentTimeMillis());
        while (!Thread.currentThread ().isInterrupted ()) {
            //  Get values that will fool the boss
            int zipcode, temperature, relhumidity;
            zipcode = 10000 + srandom.nextInt(10000) ;
            temperature = srandom.nextInt(215) - 80 + 1;
            relhumidity = srandom.nextInt(50) + 10 + 1;

            //  Send message to all subscribers
            String update = String.format("%05d %d %d", zipcode, temperature, relhumidity);
            byte[] bytes = update.getBytes();
            for (byte b : bytes) {
                System.out.println("b:" + b);
            }
            publisher.send(update, 0);
            System.out.println("update:" + update);
            break;
        }

        publisher.close ();
        context.term ();
    }
}
