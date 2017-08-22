import cn.asens.Starter;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class TestServer {

    public static void main(String[] args) throws Exception {
        new TestServer().start();
    }

    private void start() throws Exception {
        for (int i = 0; i < 340000; i++) {
            Socket socket = new Socket("127.0.0.1", 6666);
            OutputStream os = socket.getOutputStream();

            os.write("aa".getBytes());
            os.close();
            socket.close();
        }


        System.out.println();
    }

}
