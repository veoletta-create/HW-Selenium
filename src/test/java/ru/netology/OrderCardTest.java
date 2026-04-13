package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderCardTest {
    private WebDriver driver;

    @BeforeAll
    static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");

        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldSubmitRequest() {

        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+72898357787");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }
    @Test
    void shouldShowErrorIfNameIsEmpty() {
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79195243777");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button.button")).click();

        String expectedError = "Поле обязательно для заполнения";
        String actualError = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();

        assertEquals(expectedError, actualError);
    }

    @Test
    void shouldShowErrorIfNameIsInvalid() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Osipova Violetta");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79195243777");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button.button")).click();

        String expectedError = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actualError = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();

        assertEquals(expectedError, actualError);
    }

    @Test
    void shouldShowErrorIfPhoneIsEmpty() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Осипова Виолетта");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button.button")).click();

        String expectedError = "Поле обязательно для заполнения";
        String actualError = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();

        assertEquals(expectedError, actualError);
    }

    @Test
    void shouldShowErrorIfPhoneIsInvalid() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Осипова Виолетта");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("89283043777");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button.button")).click();

        String expectedError = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actualError = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();

        assertEquals(expectedError, actualError);
    }

    @Test
    void shouldShowErrorIfCheckboxIsUnchecked() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Осипова Веолетта");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79285543371");
        driver.findElement(By.cssSelector("button.button")).click();
        boolean isErrorDisplayed = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid")).isDisplayed();

        assertTrue(isErrorDisplayed, "Ожидалось, что чекбокс подсветится красным (получит класс input_invalid)");
    }
}