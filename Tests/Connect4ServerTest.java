import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.stage.Stage;
import main.Core.Connect4Server;
import main.UI.Connect4Client;

class Connect4ServerTest {

    private static Connect4Server testServer;

    @BeforeAll
    static void setUp() {
        testServer = new Connect4Server();
    }

    @AfterAll
    static void tearDown() {
        System.exit(0);
    }

    @Test
    void main() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connect4Server.main(null);
            }
        }).start();
    }

    @Test
    void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                testServer.start(new Stage());
            }
        }).start();

        new Thread(() -> Connect4Client.main(null)).start();
        new Thread(() -> Connect4Client.main(null)).start();
    }
}