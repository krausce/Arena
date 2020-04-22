module Arena {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires java.base;
    requires java.logging;
    requires org.junit.jupiter.api;

    opens main.UI;
}