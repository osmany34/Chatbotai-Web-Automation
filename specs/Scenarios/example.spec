# Specification Heading

Senaryo 1: Uygulamaya ilk giriş
---

//* "20" saniye bekle

* Chatbot Web sayfası açılır.
* "NewChat_Button" li elemente tıkla
* Mesaj bölümüne "Merhaba" değerini yaz.

//* "ChatInput_Textarea" li elemente tıkla

## Senaryo 1: TC-001 - Ana sayfa yüklenme doğrulaması
* Kullanıcı ChatbotAI ana sayfasını açar
* Sayfanın tamamen yüklendiği doğrulanır
* Ana sayfa logosu, "Giriş Yap" ve "Ücretsiz Başla" butonlarının görünür olduğu kontrol edilir

## Senaryo 2: TC-002 - Yeni kullanıcı kayıt işlemi (Pozitif Senaryo)
* Kullanıcı ana sayfada bulunur
* "Ücretsiz Başla" butonuna tıklanır
* Kayıt formunun açıldığı doğrulanır
* Geçerli e-posta ve güçlü şifre alanlara girilir
* Kayıt formu gönderilir
* Başarılı kayıt mesajı veya yönlendirme doğrulanır

## Senaryo 3: TC-003 - Var olan kullanıcıyla giriş işlemi (Pozitif Senaryo)
* Kullanıcı ana sayfada bulunur
* "Giriş Yap" butonuna tıklanır
* Giriş formunun açıldığı doğrulanır
* Geçerli kullanıcı bilgileri form alanlarına girilir
* Giriş formu gönderilir
* Kullanıcının kontrol paneline yönlendirildiği doğrulanır

## Senaryo 4: TC-004 - Hatalı şifreyle giriş denemesi (Negatif Senaryo)
* Kullanıcı giriş formunu açar
* Hatalı kullanıcı bilgileri girilir
* Giriş formu gönderilir
* "Geçersiz kullanıcı bilgileri" uyarısının görüntülendiği doğrulanır

## Senaryo 5: TC-005 - Chatbot penceresinin açılması
* Kullanıcı sisteme giriş yapmıştır
* Chatbot simgesine tıklanır
* Sohbet penceresinin açıldığı doğrulanır

## Senaryo 6: TC-006 - Chatbot’a mesaj gönderilmesi
* Kullanıcı sohbet penceresini açar
* Mesaj kutusuna "Merhaba" yazılır
* Gönder butonuna tıklanır
* Chatbot cevabının ekranda görüntülendiği doğrulanır

## Senaryo 7: TC-007 - Sohbet geçmişinin yüklenmesi
* Kullanıcı sohbet ekranını açar
* Önceki mesajların yüklendiği doğrulanır

## Senaryo 8: TC-008 - Chatbot cevap gecikme kontrolü
* Kullanıcı "Hava nasıl?" mesajını gönderir
* 10 saniyeden uzun sürede cevap alınırsa
* “Lütfen bekleyin...” mesajının görüntülendiği doğrulanır

## Senaryo 9: TC-009 - Footer bağlantısı kontrolü
* Kullanıcı ana sayfada bulunur
* "Gizlilik Politikası" bağlantısına tıklanır
* Gizlilik politikası sayfasının yeni sekmede açıldığı doğrulanır

## Senaryo 10: TC-010 - Mobil görünüm menü kontrolü
* Kullanıcı siteye mobil cihazla giriş yapar
* Sayfanın yüklendiği doğrulanır
* Hamburger menü ikonunun görüntülendiği kontrol edilir
* Menü açıldığında navigasyon bağlantılarının listelendiği doğrulanır
sbk-pojd-hxq