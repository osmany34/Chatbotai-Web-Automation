Specification Heading
=====================

1. Smoke Test – Ana Sayfa Erişimi (5 adım)
---
Tarayıcıyı açın ve https://chatbotai.com/ adresine gidin.
Sayfanın tamamen yüklendiğini doğrulayın.
Ana başlık ve sloganın göründüğünü kontrol edin.
Menü ve model listelerinin yüklendiğini doğrulayın.
Sayfanın responsive olduğunu test edin (mobil ve desktop görünüm).

2. Smoke Test – Model Seçimi (7 adım)
---
Ana sayfayı açın.
Listeden “GPT-5” modelini seçin.
Sohbet penceresinin açıldığını doğrulayın.
“Merhaba” mesajını gönderin.
Yanıtın geldiğini doğrulayın.
Yanıtın anlamlı olduğunu kontrol edin.
Pencereyi kapatın ve sayfaya geri dönün.

3. Fonksiyonel Pozitif – Chat Mesajı Gönderme (10 adım)
---
Tarayıcıyı açın ve siteyi yükleyin.
“GPT-4o” modelini seçin.
Sohbet penceresini açın.
“Hava durumu nasıl?” mesajını yazın.
Gönder butonuna tıklayın.
Yanıtın geldiğini doğrulayın.
Yanıtın anlamlı ve doğru olduğunu kontrol edin.
Aynı mesajı tekrar gönderin.
Yanıtın tekrar geldiğini doğrulayın.
Pencereyi kapatın.

4. Fonksiyonel Negatif – Boş Mesaj Gönderme (6 adım)
---
Sohbet penceresini açın.
Mesaj alanını boş bırakın.
Gönder butonuna tıklayın.
Hata mesajının çıktığını doğrulayın.
Mesajın gönderilmediğini kontrol edin.
Alanın temiz olduğunu doğrulayın.

5. Regresyon – Modellar Arası Geçiş (12 adım)
---
Ana sayfayı açın.
“GPT-5” modelini seçin ve bir mesaj gönderin.
Yanıtın geldiğini doğrulayın.
“GPT-4o” modeline geçin.
Mesaj gönderin.
Yanıtın geldiğini doğrulayın.
“DeepSeek” modeline geçin.
Mesaj gönderin.
Yanıtın geldiğini doğrulayın.
“Gemini 2.0” modeline geçin.
Yanıtın anlamlı olduğunu doğrulayın.
Tüm pencereleri kapatın.

6. Pozitif – Çoklu Mesaj Testi (8 adım)
---
Sohbet penceresini açın.
“Merhaba” mesajını gönderin.
Yanıtın geldiğini doğrulayın.
“Bugün hava nasıl?” mesajını gönderin.
Yanıtın geldiğini doğrulayın.
“Bana bir şaka yap” mesajını gönderin.
Yanıtın anlamlı olduğunu doğrulayın.
Pencereyi kapatın.

7. Negatif – Çok Uzun Mesaj Gönderme (7 adım)
---
Sohbet penceresini açın.
5000 karakterlik bir mesaj yazın.
Gönder butonuna tıklayın.
Hata mesajını doğrulayın.
Mesajın gönderilmediğini kontrol edin.
Mesaj alanının temiz olduğunu doğrulayın.
Pencereyi kapatın.

8. Smoke – Menü Navigasyonu (5 adım)
---
Ana sayfayı açın.
Menüdeki tüm bağlantılara tıklayın.
Sayfaların yüklendiğini doğrulayın.
Ana sayfaya geri dönün.
Menü responsive çalışıyor mu kontrol edin.

9. Regresyon – Hız Testi (6 adım)
---
Ana sayfayı açın.
Modeli seçin ve mesaj gönderin.
Yanıtın 2 saniye içinde geldiğini doğrulayın.
Mesajı tekrar gönderin.
Yanıt süresini kontrol edin.
Yanıt süresinin tutarlı olduğunu doğrulayın.

10. Pozitif – Farklı Tarayıcı Testi (9 adım)
---
Chrome’da siteyi açın.
Mesaj gönderin ve yanıtı doğrulayın.
Firefox’da siteyi açın.
Mesaj gönderin ve yanıtı doğrulayın.
Edge’de siteyi açın.
Mesaj gönderin ve yanıtı doğrulayın.
Safari’de siteyi açın.
Mesaj gönderin ve yanıtı doğrulayın.
Tüm tarayıcıların aynı çıktıyı verdiğini doğrulayın.

11. Negatif – Geçersiz Model Seçimi (5 adım)
---
Siteyi açın.
Listede olmayan bir model ID’si girin.
Sohbet penceresi açılmamalı.
Hata mesajını doğrulayın.
Ana sayfaya geri dönün.

12. Smoke – Sayfa Scroll Testi (6 adım)
---
Ana sayfayı açın.
Sayfayı tamamen aşağıya kaydırın.
Tüm model başlıklarının göründüğünü doğrulayın.
Sayfayı yukarı kaydırın.
Menü ve logo görünürlüğünü kontrol edin.
Scroll sırasında hiçbir elementin kaymadığını doğrulayın.

13. Fonksiyonel Pozitif – Sesli Yanıt Testi (10 adım)
---
GPT-5 modelini açın.
“Merhaba” mesajını yazın.
Gönderin.
Yanıt geldiğinde ses ikonunu tıklayın.
Sesin çaldığını doğrulayın.
Ses durdur butonunu kontrol edin.
Farklı mesaj gönderin.
Ses çıktısını tekrar kontrol edin.
Ses kalitesini doğrulayın.
Sohbet penceresini kapatın.

14. Negatif – Hatalı Karakter Gönderme (6 adım)
---
Sohbet penceresini açın.
Özel karakterler (~!@#$%^&*()_+) ile mesaj yazın.
Gönder butonuna tıklayın.
Yanıtın anlamlı olup olmadığını kontrol edin.
Hata oluşursa doğrulayın.
Pencereyi kapatın.

15. Smoke – Footer Linkleri (5 adım)
---
Ana sayfayı açın.
Footer’daki tüm linklere tıklayın.
Sayfaların açıldığını doğrulayın.
Geri tuşuyla ana sayfaya dönün.
Linklerin doğru yönlendirdiğini doğrulayın.

16. Regresyon – Tekrar Eden Mesajlar (7 adım)
---
Sohbet penceresini açın.
“Test mesajı” gönderin.
Yanıtı doğrulayın.
Aynı mesajı tekrar gönderin.
Yanıtın tekrar geldiğini kontrol edin.
Yanıtların tutarlı olduğunu doğrulayın.
Pencereyi kapatın.

17. Pozitif – Chat Temizleme Butonu (5 adım)
---
Sohbet penceresini açın.
Birkaç mesaj gönderin.
“Temizle” butonuna tıklayın.
Tüm mesajların silindiğini doğrulayın.
Pencereyi kapatın.

18. Negatif – Sayfa Yenileme Sırasında Mesaj Kaybı (6 adım)
---
Sohbet penceresini açın.
Bir mesaj gönderin.
Sayfayı yenileyin.
Mesajın kaybolduğunu doğrulayın.
Hata mesajı veya uyarı varsa kontrol edin.
Pencereyi kapatın.

19. Fonksiyonel Pozitif – Tema Değişikliği (8 adım)
---
Ayarlar menüsünü açın.
Tema seçeneğine tıklayın.
Farklı bir tema seçin.
Sayfanın temasının değiştiğini doğrulayın.
Sohbet penceresini açın.
Mesaj gönderin.
Yanıtın doğru geldiğini doğrulayın.
Temayı varsayılan yapın.

20. Smoke – URL Doğrulama (5 adım)
---
Tarayıcıda siteyi açın.
Tüm modelleri sırayla tıklayın.
URL’nin doğru değiştiğini doğrulayın.
Yanlış URL girildiğinde hata sayfasının geldiğini kontrol edin.
Ana sayfaya geri dönün.