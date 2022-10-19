import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;
import java.util.concurrent.TimeUnit;

public class LEDClient {
    private ZContext zctx;
    private ZMQ.Socket zsocket;
    private Gson gson;
    private String connStr;
    private final String topic = "GPIO";

    private static final int[] OFF = {0, 0, 0};

    public LEDClient(String protocol, String host, int port) {
        zctx = new ZContext();
        zsocket = zctx.createSocket(SocketType.PUB);
        this.connStr = String.format("%s://%s:%d", protocol, "*", port);
        zsocket.bind(connStr);
        this.gson = new Gson();
    }

    public void send(int[] color) throws InterruptedException {
        JsonArray ja = gson.toJsonTree(color).getAsJsonArray();
        String message = topic + " " + ja.toString();
        System.out.println(message);
        zsocket.send(message);
    }

    public void blinkN(int[] color, int times, int miliseconds) throws  InterruptedException{
        for(int i=0; i<times; i++) {
            send(color);
            TimeUnit.MILLISECONDS.sleep(miliseconds);
            send(LEDClient.OFF);
            TimeUnit.MILLISECONDS.sleep(miliseconds);
        }
    }

    public void close() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2); // Allow the socket a chance to flush.
        this.zsocket.close();
        this.zctx.close();
    }
    public static void meMethod() throws InterruptedException {
        LEDClient rainbowness = new LEDClient("tcp", "192.168.1.116", 5001);
//        ledClient.blinkN(blue, 5, 1000);
//        ledClient.close();

        int[] color = {0,0,0};
        while (color[0] != 255 || color[1] != 255 || color[2] != 255){
            if (color[0] != 255) {
                color[0]++;
            }else if (color[1] != 255) {
                color[1]++;
            }else if (color[2] != 255) {
                color[2]++;
            }
            rainbowness.send(color);
            TimeUnit.MILLISECONDS.sleep(10);

        }

        while (color[0] != 0 || color[1] != 0 || color[2] != 0){
            if (color[0] != 0) {
                color[0]--;
            }else if (color[1] != 0) {
                color[1]--;
            }else if (color[2] != 0) {
                color[2]--;
            }
            rainbowness.send(color);
            TimeUnit.MILLISECONDS.sleep(10);

        }

    }

    public static void main(String[] args) {
//        LEDClient ledClient = new LEDClient("tcp", "192.168.1.117", 5001);
        try {
//            int[] color = {0, 0, 255};
//            ledClient.blinkN(color, 5, 1000);
//            ledClient.close();
              meMethod();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}


/*
    public static void main(String[] args) {
        LEDClient ledClient = new LEDClient("tcp", "192.168.86.250", 5001);
        try {
            int[] color = {0, 0, 255};
            ledClient.blinkN(color, 5, 1000);
            ledClient.close();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
 */