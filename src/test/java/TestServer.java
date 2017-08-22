import cn.asens.Starter;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class TestServer {

    public static void main(String[] args) throws Exception {
        new TestServer().start();
    }

    private void start() throws Exception {
        Socket socket = new Socket("127.0.0.1", 6666);
        OutputStream os = socket.getOutputStream();
        for (int i = 0; i < 3; i++) {

            System.out.println("aaa");
            os.write("aa".getBytes());
            Thread.sleep(1270);
        }

        os.close();
        socket.close();
        System.out.println();
    }

}
