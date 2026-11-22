package org.example.demo1;

import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocalizationServiceTest {

    @Test
    void loadsEnglishStrings() {
        Map<String, String> strings =
                LocalizationService.getLocalizedStrings(Locale.US);

        assertEquals("Weight (kg)", strings.get("weight"));
        assertEquals("Height (m)", strings.get("height"));
        assertEquals("Calculate BMI", strings.get("calculate"));
        assertEquals("Your BMI is: ", strings.get("result"));
        assertEquals("invalid input, please enter valid numbers.", strings.get("invalid"));
        assertEquals("Current time", strings.get("localTime"));
    }

    @Test
    void loadsAllSupportedTranslations() {
        assertEquals("Poids (kg)",
                LocalizationService.getLocalizedStrings(Locale.FRANCE).get("weight"));
        assertEquals("وزن",
                LocalizationService.getLocalizedStrings(new Locale("fa", "IR")).get("weight"));
        assertEquals("وزن (کلوگرام)",
                LocalizationService.getLocalizedStrings(new Locale("ur", "PA")).get("weight"));
        assertEquals("Cân nặng (kg)",
                LocalizationService.getLocalizedStrings(new Locale("vi", "VI")).get("weight"));
    }

    @Test
    void returnsEmptyMapWhenOnlyEmptyBaseBundleIsAvailable() {
        Map<String, String> strings =
                LocalizationService.getLocalizedStrings(new Locale("de", "DE"));

        assertTrue(strings.isEmpty());
    }
}
