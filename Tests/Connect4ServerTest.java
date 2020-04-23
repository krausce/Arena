import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.stage.Stage;
import main.Core.Connect4Server;
import main.UI.Connect4Client;

class Connect4ServerTest {

    private Connect4Server testServer;

    @BeforeEach
    void setUp() {
        testServer = new Connect4Server();
    }

    @Test
    void main() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connect4Server.main(null);
                Platform.exit();
            }
        }).start();
    }

    @Test
    void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                testServer.start(new Stage());
                Platform.exit();
            }
        }).start();

        new Thread(() -> Connect4Client.main(null)).start();
        new Thread(() -> Connect4Client.main(null)).start();
    }
}