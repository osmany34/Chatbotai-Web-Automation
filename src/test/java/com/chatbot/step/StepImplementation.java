package com.chatbot.step;

import com.thoughtworks.gauge.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;
import com.thoughtworks.gauge.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;



public class StepImplementation {



    private static final Logger logger = LoggerFactory.getLogger(StepImplementation.class);

    private static WebDriver driver;
    private static WebDriverWait wait;

    // JSON okuma helper’ı
    private static ElementHelper elementHelper;

    // Adım log/süre takibi
    private final ThreadLocal<Long> stepStart = new ThreadLocal<>();


    // Screenshot klasörü
    private static final Path screenshotsDir = Paths.get("reports", "screenshots");

    // Dosya adı için basit temizleyici
    private static String sanitize(String s) {
        return s.replaceAll("[^a-zA-Z0-9._-]+", "_");
    }
    public StepImplementation() {}

    // @AfterStep
    public void afterStep(ExecutionContext context) {
        if (context.getCurrentStep().getIsFailing()) {
            System.exit(0);
        }
    }

    @BeforeScenario
    public void setup() {
        logger.info("Test senaryosu başlatılıyor...");

        if (driver == null) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
            driver.manage().window().maximize();

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            // JSON'u classpath'ten yükle (src/test/resources/element-infos/element.json)
            if (elementHelper == null) {
                elementHelper = new ElementHelper("/element-infos/element.json");
                logger.info("element.json yüklendi");
            }
            try {
                Files.createDirectories(screenshotsDir);
                logger.info("Screenshot klasörü hazır: {}", screenshotsDir.toAbsolutePath());
            } catch (Exception e) {
                logger.warn("Screenshot klasörü oluşturulamadı: {}", e.getMessage());
            }
            logger.info("Chrome Driver başarıyla başlatıldı");
        } else {
            logger.info("Var olan WebDriver kullanılacak.");
        }
    }

    @AfterScenario
    public void close() {
        logger.info("Test senaryosu sonlandırılıyor...");
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}

        if (driver != null) {
            driver.quit();
            driver = null;
            wait = null;
            logger.info("Chrome Driver başarıyla kapatıldı");
        }
    }
    @BeforeStep
    public void beforeStepLog(ExecutionContext ctx) {
        stepStart.set(System.currentTimeMillis());
        logger.info("➡ STEP START: {}", ctx.getCurrentStep().getText());
    }

    @AfterStep
    public void afterStepLog(ExecutionContext ctx) {
        long dur = System.currentTimeMillis() - stepStart.get();
        String text = ctx.getCurrentStep().getText();

        if (ctx.getCurrentStep().getIsFailing()) {
            // Hata → screenshot al
            try {
                String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
                Path out = screenshotsDir.resolve(ts + "_" + sanitize(text) + ".png");
                logger.error("✖ STEP FAIL ({} ms): {}  [screenshot: {}]", dur, text, out.toString());
                Gauge.writeMessage("Screenshot: %s", out.toString());
            } catch (Exception ssEx) {
                logger.error("Screenshot alınamadı: {}", ssEx.getMessage(), ssEx);
            }
        } else {
            logger.info("✔ STEP PASS ({} ms): {}", dur, text);
        }
    }
    // ------------------- NAVIGATION -------------------
    @Step("https://chatbotai.com/ URL'ini aç")
    public void openURL() {
        logger.info("URL açılıyor: https://chatbotai.com/");
        driver.get("https://chatbotai.com/");
    }

    @Step("<url> adresine git")
    public void goToUrl(String url) {
        driver.get(url);
    }

    @Step("Tarayıcıyı kapat")
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
            driver = null;
            wait = null;
        }
    }

    // ------------------- GENERIC STEPS -------------------
    @Step({"<key> li elemente tıkla", "find element <key> and click"})
    public void clickElement(String key) {
        // 1) JSON’da tanımlıysa, adayları sırayla dene
        if (elementHelper != null && elementHelper.has(key)) {
            List<By> candidates = elementHelper.getCandidates(key);
            Integer pick = elementHelper.getIndex(key);
            WebElement target = null;

            if (pick != null && pick >= 0) {
                for (By by : candidates) {
                    List<WebElement> els = driver.findElements(by);
                    if (els.size() > pick) {
                        WebElement e = els.get(pick);
                        if (isDisplayedSafe(e)) { target = e; break; }
                    }
                }
            } else {
                target = findFirstDisplayed(candidates.toArray(new By[0]));
            }

            if (target != null) { safeClick(target); return; }
        }

        // 2) ChatInput_Textarea için özel fallback
        if ("ChatInput_Textarea".equals(key)) {
            By[] candidates = new By[] {
                    getLocatorByKey("ChatInput_Textarea"),
                    By.cssSelector("[contenteditable='true']:not([aria-hidden='true'])"),
                    By.cssSelector("[data-testid*='input' i], [data-qa*='input' i], [data-test*='input' i]")
            };

            long end = System.currentTimeMillis() + 15000;
            WebElement target = null;
            while (System.currentTimeMillis() < end && target == null) {
                target = findFirstDisplayed(candidates);
                if (target == null) { try { Thread.sleep(250); } catch (InterruptedException ignored) {} }
            }
            if (target == null) throw new TimeoutException("ChatInput_Textarea bulunamadı/görünür değil.");

            safeClick(target);
            return;
        }

        // 3) Genel fallback
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(byOrRaw(key)));
        try {
            element.click();
        } catch (WebDriverException e) {
            safeClick(element);
        }
    }

    // ------------------- HELPER & COMMON STEPS -------------------
    private boolean isDisplayedSafe(WebElement e) {
        try { return e != null && e.isDisplayed(); }
        catch (StaleElementReferenceException ignored) { return false; }
    }

    private WebElement findFirstDisplayed(By... candidates) {
        for (By by : candidates) {
            List<WebElement> els = driver.findElements(by);
            for (WebElement e : els) if (isDisplayedSafe(e)) return e;
        }
        return null;
    }

    private void scrollIntoView(WebElement el) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'nearest'});", el);
    }

    private void safeClick(WebElement el) {
        try {
            scrollIntoView(el);
            new Actions(driver).moveToElement(el).pause(Duration.ofMillis(100)).click(el).perform();
        } catch (Exception actionsFail) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    // ------------------- LOCATOR RESOLVER -------------------
    private By byOrRaw(String key) {
        if (key == null) throw new RuntimeException("Key null olamaz");

        if (elementHelper != null && elementHelper.has(key)) {
            By by = elementHelper.getPrimaryBy(key);
            if (by != null) return by;
        }

        if (key.startsWith("css:"))   return By.cssSelector(key.substring(4));
        if (key.startsWith("xpath:")) return By.xpath(key.substring(6));
        if (key.startsWith("id:"))    return By.id(key.substring(3));
        if (key.startsWith("name:"))  return By.name(key.substring(5));
        if (key.startsWith("tag:"))   return By.tagName(key.substring(4));
        if (key.startsWith("link:"))  return By.linkText(key.substring(5));
        if (key.startsWith("plink:")) return By.partialLinkText(key.substring(6));
        if (key.startsWith("class:")) return By.className(key.substring(6));

        return getLocatorByKey(key);
    }

    // ------------------- LOCAL FALLBACK MAP -------------------
    private By getLocatorByKey(String key) {
        switch (key) {
            case "loginButton": return By.id("login-button");
            case "usernameField": return By.name("username");
            case "passwordField": return By.name("password");
            case "submitButton": return By.cssSelector(".submit-btn");
            case "ChatInput_Textarea":
                return By.cssSelector(
                        "textarea#chat-input, " +
                                "textarea[name='chatInput'], " +
                                "textarea[placeholder*='mesaj' i], " +
                                "textarea[placeholder*='message' i], " +
                                "textarea[aria-label*='mesaj' i], " +
                                "textarea[aria-label*='message' i], " +
                                "[contenteditable='true']:not([aria-hidden='true']):not([tabindex='-1'])"
                );
            default:
                throw new RuntimeException("Locator tanımlı değil: " + key);
        }
    }

    // --- INNER: ElementHelper (JSON'dan locator okur) ---
    private static class ElementHelper {
        public static class LocatorDef {
            public String type;
            public Object value;
            public Integer index;
        }

        private final java.util.Map<String, LocatorDef> map;

        ElementHelper(String classpathJson) {
            this.map = loadFromClasspath(classpathJson);
        }

        boolean has(String key) { return map != null && map.containsKey(key); }

        org.openqa.selenium.By getPrimaryBy(String key) {
            LocatorDef def = map.get(key);
            if (def == null) return null;
            String type = (def.type != null ? def.type.trim().toLowerCase() : "css");
            String first = firstValue(def.value);
            if (first == null) return null;
            return by(type, first);
        }

        java.util.List<org.openqa.selenium.By> getCandidates(String key) {
            LocatorDef def = map.get(key);
            if (def == null) return java.util.Collections.emptyList();
            String type = (def.type != null ? def.type.trim().toLowerCase() : "css");
            java.util.List<String> values = allValues(def.value);
            java.util.List<org.openqa.selenium.By> out = new java.util.ArrayList<>();
            for (String v : values) out.add(by(type, v));
            return out;
        }

        Integer getIndex(String key) {
            LocatorDef def = map.get(key);
            return def != null ? def.index : null;
        }

        // ------------------- JSON Loader -------------------
        private java.util.Map<String, LocatorDef> loadFromClasspath(String path) {
            try (java.io.InputStream is = getClass().getResourceAsStream(path)) {
                if (is == null)
                    throw new RuntimeException("JSON bulunamadı (classpath): " + path);

                com.fasterxml.jackson.databind.ObjectMapper om = new com.fasterxml.jackson.databind.ObjectMapper();
                com.fasterxml.jackson.databind.JsonNode root = om.readTree(is);

                java.util.Map<String, LocatorDef> out = new java.util.LinkedHashMap<>();

                if (root.isObject()) {
                    // Format: { "key": { "type": "...", "value": ... } }
                    java.util.Iterator<String> it = root.fieldNames();
                    while (it.hasNext()) {
                        String key = it.next();
                        com.fasterxml.jackson.databind.JsonNode node = root.get(key);
                        LocatorDef def = om.convertValue(node, LocatorDef.class);
                        out.put(key, def);
                    }
                    return out;
                } else if (root.isArray()) {
                    // Format: [ { "key": "...", "type": "...", "value": ... }, ... ]
                    for (com.fasterxml.jackson.databind.JsonNode n : root) {
                        com.fasterxml.jackson.databind.JsonNode keyNode = n.get("key");
                        if (keyNode == null || keyNode.isNull()) {
                            throw new RuntimeException("Array formatında 'key' alanı eksik bir öğe var.");
                        }
                        String key = keyNode.asText();
                        com.fasterxml.jackson.databind.node.ObjectNode clone = n.deepCopy();
                        clone.remove("key");
                        LocatorDef def = om.convertValue(clone, LocatorDef.class);
                        out.put(key, def);
                    }
                    return out;
                } else {
                    throw new RuntimeException("Desteklenmeyen JSON kök tipi. Obje veya dizi olmalı.");
                }
            } catch (Exception e) {
                throw new RuntimeException("element.json yüklenemedi: " + e.getMessage(), e);
            }
        }

        private org.openqa.selenium.By by(String type, String value) {
            switch (type) {
                case "css": return org.openqa.selenium.By.cssSelector(value);
                case "xpath": return org.openqa.selenium.By.xpath(value);
                case "id": return org.openqa.selenium.By.id(value);
                case "name": return org.openqa.selenium.By.name(value);
                case "tag": return org.openqa.selenium.By.tagName(value);
                case "link": return org.openqa.selenium.By.linkText(value);
                case "plink": return org.openqa.selenium.By.partialLinkText(value);
                case "class": return org.openqa.selenium.By.className(value);
                default: throw new IllegalArgumentException("Desteklenmeyen locator type: " + type);
            }
        }

        private String firstValue(Object value) {
            if (value == null) return null;
            if (value instanceof String) return (String) value;
            if (value instanceof java.util.List) {
                java.util.List<?> l = (java.util.List<?>) value;
                return l.isEmpty() ? null : String.valueOf(l.get(0));
            }
            return String.valueOf(value);
        }

        private java.util.List<String> allValues(Object value) {
            if (value == null) return java.util.Collections.emptyList();
            if (value instanceof String) return java.util.Collections.singletonList((String) value);
            if (value instanceof java.util.List) {
                java.util.List<?> l = (java.util.List<?>) value;
                java.util.List<String> out = new java.util.ArrayList<>();
                for (Object o : l) out.add(String.valueOf(o));
                return out;
            }
            return java.util.Collections.singletonList(String.valueOf(value));
        }
    }
    @Step({"<key> li elemente <text> değerini yaz", "find element <key> and sendkey text <text>"})
    public void sendKey(String key, String text) {
        By locator = byOrRaw(key);
        WebDriverWait localWait = (wait != null) ? wait : new WebDriverWait(driver, Duration.ofSeconds(15));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        WebElement el = null;
        int retries = 0;
        RuntimeException lastError = null;

        while (retries++ < 6) {
            try {
                // 1️⃣ Elementi görünür şekilde yeniden bul
                el = localWait.until(ExpectedConditions.visibilityOfElementLocated(locator));

                // 2️⃣ Görünür ama tıklanabilir değilse odağa al
                js.executeScript("arguments[0].scrollIntoView({block:'center'});", el);
                js.executeScript("arguments[0].focus();", el);

                // 3️⃣ Eğer disabled veya readonly ise atla
                if (!el.isEnabled()) {
                    logger.warn("Element etkileşime kapalı (disabled/readonly). Yeniden denenecek...");
                    Thread.sleep(200);
                    continue;
                }

                // 4️⃣ Placeholder hâlâ varsa, kısa bekle (React henüz aktif değil)
                String placeholder = el.getAttribute("placeholder");
                if (placeholder != null && !placeholder.isEmpty()) {
                    Thread.sleep(150);
                }

                // 5️⃣ Önce temizle (bazı durumlarda JS ile güvenli)
                try { el.clear(); } catch (Exception ignore) {
                    js.executeScript("arguments[0].value = '';", el);
                }

                // 6️⃣ sendKeys dene, olmazsa JS fallback uygula
                try {
                    el.sendKeys(text);
                } catch (ElementNotInteractableException e) {
                    logger.warn("sendKeys başarısız, JS ile değer atanıyor...");
                    js.executeScript("arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input',{bubbles:true}));", el, text);
                }

                logger.info("🟢 '{}' değeri '{}' alanına başarıyla yazıldı.", text, key);
                return;
            }
            catch (StaleElementReferenceException stale) {
                logger.warn("Stale element, yeniden deneniyor... ({} / 6)", retries);
                lastError = stale;
            }
            catch (ElementNotInteractableException e) {
                logger.warn("Element henüz aktif değil ({} / 6). Bekleniyor...", retries);
                lastError = e;
            }
            catch (TimeoutException e) {
                lastError = e;
                logger.error("Element bulunamadı: {}", key);
                break;
            }
            catch (Exception e) {
                lastError = new RuntimeException("Beklenmedik hata sendKey sırasında: " + e.getMessage(), e);
                break;
            }

            try { Thread.sleep(300); } catch (InterruptedException ignored) {}
        }

        if (lastError != null) throw lastError;
    }

    @Step({"<key> li element var mı?", "find element <key> and check if it is there?"})
    public void checkElement(String key) {
        WebElement el = null;

        // 1) JSON'da tanımlıysa: index destekli dene
        if (elementHelper != null && elementHelper.has(key)) {
            List<By> candidates = elementHelper.getCandidates(key);
            Integer pick = elementHelper.getIndex(key);

            if (pick != null && pick >= 0) {
                // Her aday için listedeki 'pick' indeksindeki görünür elemana bak
                for (By by : candidates) {
                    List<WebElement> list = driver.findElements(by);
                    if (list.size() > pick) {
                        WebElement cand = list.get(pick);
                        if (isDisplayedSafe(cand)) { el = cand; break; }
                    }
                }
            } else {
                // İlk görünen adayı seç
                el = findFirstDisplayed(candidates.toArray(new By[0]));
            }
        }

        // 2) Hâlâ yoksa: genel fallback
        if (el == null) {
            By by = byOrRaw(key);
            el = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated(by));
        } else {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOf(el));
        }

        // 3) Doğrula
        assertTrue(isDisplayedSafe(el), "Element görünür değil: " + key);
    }
    // ------------------- SCROLL / SWIPE -------------------
    @Step({"<key> li elemente swipe yap", "find element <key> and swipe"})
    public void swipeToElement(String key) {
        // 1) Locator'ı çöz
        By by = byOrRaw(key);

        // 2) Presence + element nesnesini al
        WebDriverWait localWait = (wait != null) ? wait : new WebDriverWait(driver, Duration.ofSeconds(15));
        localWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));

        WebElement el = driver.findElement(by);
        JavascriptExecutor js = (JavascriptExecutor) driver;

        RuntimeException last = null;

        // 3) Birkaç kez dene (stale/overlay durumlarına dayanıklı)
        for (int i = 0; i < 6; i++) {
            try {
                // ufak dürtme – uzun sayfalarda işe yarar
                js.executeScript("window.scrollBy(0,1); window.scrollBy(0,-1);");

                // merkeze hizala
                js.executeScript(
                        "try{arguments[0].scrollIntoView({behavior:'auto', block:'center', inline:'center'});}catch(e){arguments[0].scrollIntoView(true);} ",
                        el
                );

                // sabit header için küçük offset (istersen 0 yap)
                try { js.executeScript("window.scrollBy(0,-80);"); } catch (Exception ignore) {}

                // görünür ve tıklanabilir olana kadar bekle (tıklamayacaksan da görünürlük yeter)
                localWait.until(ExpectedConditions.visibilityOf(el));
                // localWait.until(ExpectedConditions.elementToBeClickable(el)); // ihtiyaca göre aç

                return; // başarı
            } catch (StaleElementReferenceException sere) {
                // DOM yenilendiyse yeniden bul
                el = driver.findElement(by);
                last = sere;
            } catch (TimeoutException | JavascriptException | MoveTargetOutOfBoundsException e) {
                last = e;
            }
        }

        if (last != null) throw last;
    }
    @Step({"<key> li element tıklanamaz olmalı", "element <key> should not be clickable"})
    public void elementIsNotClickable(String key) {
        By by = byOrRaw(key);
        WebDriverWait localWait = (wait != null) ? wait : new WebDriverWait(driver, Duration.ofSeconds(8));

        try {
            // 1️⃣ Element DOM’da mevcut mu?
            WebElement element = localWait.until(ExpectedConditions.presenceOfElementLocated(by));

            // 2️⃣ disabled attribute kontrolü
            String disabledAttr = null;
            try {
                disabledAttr = element.getAttribute("disabled");
            } catch (Exception ignore) {}

            // 3️⃣ clickable kontrolü (2 saniyelik kısa bekleme)
            boolean clickable = false;
            try {
                localWait.withTimeout(Duration.ofSeconds(2));
                clickable = ExpectedConditions.elementToBeClickable(element).apply(driver) != null;
            } catch (Exception ignore) {}

            // 4️⃣ Beklenen: disabled attribute’u mevcut OLMALI veya clickable=false olmalı
            if (disabledAttr != null || !clickable) {
                logger.info("✅ Element tıklanamaz durumda (disabled veya clickable değil): {}", key);
            } else {
                org.junit.jupiter.api.Assertions.fail(
                        "❌ Element tıklanamaz durumda değil (disabled veya not-clickable bekleniyordu): " + key
                );
            }
        } catch (TimeoutException e) {
            org.junit.jupiter.api.Assertions.fail("❌ Element bulunamadı veya görünür değil: " + key);
        }
    }
    @Step({"<key> sn bekle", "Wait for <key> sec"})
    public void waitSeconds(String key) {
        try {
            int seconds = Integer.parseInt(key.trim());
            logger.info("⏱ {} saniye bekleniyor...", seconds);
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Bekleme adımı kesintiye uğradı!");
        } catch (NumberFormatException e) {
            logger.error("Bekleme süresi geçersiz: {}", key);
            throw new RuntimeException("Bekleme süresi sayısal olmalı: " + key);
        }
    }

}




