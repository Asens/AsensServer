import cn.asens.Starter;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class TestServer {

    public static void main(String[] args) throws Exception {
        new TestServer().start();
    }

    private void start() throws Exception {
        Socket socket = new Socket("127.0.0.1", 8080);
        OutputStream os = socket.getOutputStream();
        for (int i = 0; i < 30; i++) {

            os.write(("aa"+i).getBytes());

        }
        os.close();
        socket.close();
        System.out.println();
    }

}
