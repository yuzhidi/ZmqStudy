import org.zeromq.ZMQ;

import java.util.StringTokenizer;

//
//  Weather update client in Java
//  Connects SUB socket to tcp://localhost:5556
//  Collects weather updates and finds avg temp in zipcode
//
public class wuclientSub2Channel {

    public static void main (String[] args) {
        ZMQ.Context context = ZMQ.context(1);

        //  Socket to talk to server
        System.out.println("Collecting updates from weather server");
        ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
        subscriber.connect("tcp://localhost:5556");
        subscriber.connect("tcp://localhost:5557");

        //  Subscribe to zipcode, default is NYC, 10001
        String filter = (args.length > 0) ? args[0] : "10001 ";
//        subscriber.subscribe(filter.getBytes());
        subscriber.subscribe("10001".getBytes());
        subscriber.subscribe("10002".getBytes());

        //  Process 100 updates
        int update_nbr;
        long total_temp = 0;
        for (update_nbr = 0; update_nbr < 100; update_nbr++) {
            //  Use trim to remove the tailing '0' character
            String string = subscriber.recvStr(0).trim();
            System.out.println("recv:" + string);

            StringTokenizer sscanf = new StringTokenizer(string, " ");
            int zipcode = Integer.valueOf(sscanf.nextToken());
            int temperature = Integer.valueOf(sscanf.nextToken());
            int relhumidity = Integer.valueOf(sscanf.nextToken());

            total_temp += temperature;

        }
        System.out.println("Average temperature for zipcode '"
                + filter + "' was " + (int) (total_temp / update_nbr));
        
        subscriber.close();
        context.term();
    }
}
