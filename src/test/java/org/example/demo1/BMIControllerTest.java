package org.example.demo1;

import javafx.application.Platform;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class BMIControllerTest {

    @BeforeAll
    static void startJavaFxToolkit() {
        try {
            Platform.startup(() -> {
            });
        } catch (IllegalStateException ignored) {
            // Another JavaFX test may have initialized the shared toolkit.
        }
    }

    private BMIController controller;
    private Label weightLabel;
    private Label heightLabel;
    private Label resultLabel;
    private Label timeLabel;
    private TextField weightField;
    private TextField heightField;
    private Button calculateButton;
    private VBox root;

    @BeforeEach
    void setUp() {
        controller = new BMIController();
        weightLabel = new Label();
        heightLabel = new Label();
        resultLabel = new Label();
        timeLabel = new Label();
        weightField = new TextField();
        heightField = new TextField();
        calculateButton = new Button();
        root = new VBox();

        controllerFields().put("lblWeight", weightLabel);
        controllerFields().put("lblHeight", heightLabel);
        controllerFields().put("lblResult", resultLabel);
        controllerFields().put("lblLocalTime", timeLabel);
        controllerFields().put("tfWeight", weightField);
        controllerFields().put("tfHeight", heightField);
        controllerFields().put("btnCalculate", calculateButton);
        controllerFields().put("rootVBox", root);
    }

    @Test
    void initializeLoadsEnglishLabelsAndLeftToRightLayout() {
        try (MockedStatic<Platform> ignored = mockPlatform()) {
            controller.initialize();
        }

        assertEquals("Weight (kg)", weightLabel.getText());
        assertEquals("Height (m)", heightLabel.getText());
        assertEquals("Calculate BMI", calculateButton.getText());
        assertEquals(NodeOrientation.LEFT_TO_RIGHT, root.getNodeOrientation());
        assertEquals("-fx-text-alignment: left;", weightField.getStyle());
        assertEquals("-fx-text-alignment: left;", heightField.getStyle());
    }

    @Test
    void languageButtonsUpdateFrenchAndFarsiTextDirection() {
        try (MockedStatic<Platform> ignored = mockPlatform()) {
            controller.onFRClick(null);
            assertEquals("Poids (kg)", weightLabel.getText());
            assertEquals(NodeOrientation.LEFT_TO_RIGHT, root.getNodeOrientation());

            controller.onFAClick(null);
            assertEquals("وزن", weightLabel.getText());
            assertEquals(NodeOrientation.RIGHT_TO_LEFT, root.getNodeOrientation());
            assertEquals("-fx-text-alignment: right;", weightField.getStyle());
            assertEquals("-fx-text-alignment: right;", heightField.getStyle());
        }
    }

    @Test
    void calculatesAndStoresBmiUsingMeters() {
        weightField.setText("70");
        heightField.setText("175");

        try (MockedStatic<Platform> ignoredPlatform = mockPlatform();
             MockedStatic<BMIResultService> database = mockStatic(BMIResultService.class)) {
            controller.initialize();
            controller.onCalculateClick(null);

            assertEquals("Your BMI is:  22.86", resultLabel.getText());
            database.verify(() -> BMIResultService.saveResult(
                    70.0, 1.75, 70.0 / (1.75 * 1.75), "en"));
        }
    }

    @Test
    void displaysLocalizedMessageForInvalidNumbers() {
        weightField.setText("not-a-number");
        heightField.setText("175");

        try (MockedStatic<Platform> ignored = mockPlatform()) {
            controller.initialize();
            controller.onCalculateClick(null);
        }

        assertEquals("invalid input, please enter valid numbers.", resultLabel.getText());
    }

    private MockedStatic<Platform> mockPlatform() {
        MockedStatic<Platform> platform = mockStatic(Platform.class);
        platform.when(() -> Platform.runLater(any(Runnable.class)))
                .thenAnswer(invocation -> {
                    invocation.getArgument(0, Runnable.class).run();
                    return null;
                });
        return platform;
    }

    private java.util.Map<String, Object> controllerFields() {
        return new java.util.AbstractMap<>() {
            @Override
            public Object put(String key, Object value) {
                try {
                    var field = BMIController.class.getDeclaredField(key);
                    field.setAccessible(true);
                    Object previous = field.get(controller);
                    field.set(controller, value);
                    return previous;
                } catch (ReflectiveOperationException e) {
                    throw new AssertionError(e);
                }
            }

            @Override
            public java.util.Set<Entry<String, Object>> entrySet() {
                return java.util.Set.of();
            }
        };
    }
}
