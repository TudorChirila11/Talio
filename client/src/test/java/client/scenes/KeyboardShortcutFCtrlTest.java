package client.scenes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class KeyboardShortcutFCtrlTest {

    @InjectMocks
    private KeyboardShortcutFCtrl controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
}