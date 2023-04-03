package client.scenes;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;

abstract class FXTest {
    @BeforeAll
    static void initJfxRuntime() {
        Platform.startup(() -> {});
    }
}