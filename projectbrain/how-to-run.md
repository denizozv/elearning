# 🚀 Çalıştırma Rehberi — E-Learning Backend

Bu doküman projeyi nasıl çalıştıracağını, Swagger UI ve H2 Console adreslerini ve bağlantı bilgilerini içerir.

---

## 1. Gereksinimler

- **Java 17** (JDK kurulu olmalı; `java -version`)
- Maven gerekmez — proje **Maven Wrapper** (`mvnw`) ile gelir.
- İnternet (ilk çalıştırmada bağımlılıklar indirilir).

---

## 2. Uygulamayı Çalıştırma

Proje kök dizininde (`c:\EtiyaAkademi\elearning\elearning`):

### Windows (PowerShell / CMD)
```powershell
.\mvnw.cmd spring-boot:run
```

### Linux / macOS / Git Bash
```bash
./mvnw spring-boot:run
```

Alternatif (jar üretip çalıştırma):
```powershell
.\mvnw.cmd clean package
java -jar target\elearning-0.0.1-SNAPSHOT.jar
```

> Uygulama varsayılan olarak **http://localhost:8080** üzerinde ayağa kalkar.
> Durdurmak için terminalde **Ctrl + C**.

---

## 3. Swagger UI (API Dokümantasyonu / Test)

| | Adres |
|---|---|
| **Swagger UI** | http://localhost:8080/swagger-ui/index.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |

Tüm endpoint'ler buradan görülebilir ve denenebilir. Mevcut controller grupları:
`Roles`, `Languages`, `Categories`, `Users`, `Courses`, `Reviews`, `Carts`, `Orders`, `Payments`.

---

## 4. H2 Console (Veritabanı Arayüzü)

| | Değer |
|---|---|
| **Adres** | http://localhost:8080/h2-console |
| **JDBC URL** | `jdbc:h2:file:./data/elearning` |
| **Driver Class** | `org.h2.Driver` |
| **User Name** | `sa` |
| **Password** | *(boş)* |

> ⚠️ H2 Console'da **JDBC URL alanını** yukarıdaki değerle birebir doldurmayı unutma; varsayılan `jdbc:h2:~/test` gelir.

---

## 5. Veritabanı — KALICI (File-Based / Persistent)

- Veriler **bellekte (in-memory) değil**, disk üzerinde tutulur: proje kökünde
  **`./data/elearning.mv.db`** dosyası.
- Uygulama **kapatılıp tekrar açıldığında veriler KAYBOLMAZ** (test edildi ✅).
- Şema, `spring.jpa.hibernate.ddl-auto=update` ile entity'lere göre otomatik
  oluşturulur/güncellenir.
- `data/` klasörü ve `*.mv.db` dosyaları `.gitignore`'dadır (repoya commit'lenmez).
- Veritabanını sıfırlamak istersen: uygulamayı durdur, `data/` klasörünü sil,
  tekrar başlat (şema yeniden oluşur).

İlgili ayarlar [src/main/resources/application.properties](../src/main/resources/application.properties) içindedir.

---

## 6. Hızlı Duman Testi (Smoke Test)

Uygulama ayaktayken (örnek `curl`):

```bash
# Rol oluştur
curl -X POST http://localhost:8080/api/v1/roles -H "Content-Type: application/json" -d "{\"name\":\"Instructor\"}"

# Rolleri listele
curl http://localhost:8080/api/v1/roles

# Kullanıcı oluştur (şifre bcrypt ile saklanır, response'ta DÖNMEZ)
curl -X POST http://localhost:8080/api/v1/users -H "Content-Type: application/json" \
  -d "{\"roleId\":1,\"fullName\":\"Remzi Kilinc\",\"mail\":\"remzi@test.com\",\"password\":\"secret123\"}"
```

### Hata yanıtı formatları (Global Exception Handling)
- **Validasyon hatası (400):**
  ```json
  { "timestamp":"...", "status":400, "error":"Bad Request",
    "message":"Doğrulama hatası", "validationErrors":{"name":"Rol adı 2-50 karakter olmalıdır"} }
  ```
- **İş kuralı ihlali (400):**
  ```json
  { "timestamp":"...", "status":400, "error":"Bad Request",
    "message":"Bu isimde bir rol zaten mevcut: Instructor" }
  ```

---

## 7. Tipik Akış (Önerilen Test Sırası)

1. `POST /api/v1/roles` → "Instructor" ve "User" rolleri.
2. `POST /api/v1/languages` → "Turkish" / "English".
3. `POST /api/v1/categories` → kök kategori (parentId boş) → alt kategori (parentId = kök id).
4. `POST /api/v1/users` → biri Instructor rolünde, biri normal User.
5. `POST /api/v1/courses` → leaf kategori + Instructor kullanıcı ile.
6. `POST /api/v1/carts/items` → kullanıcı sepetine kurs ekle (ilk eklemede sepet otomatik oluşur).
7. `POST /api/v1/orders/checkout` → sepetten sipariş + ödeme oluştur, sepet temizlenir.
8. `GET /api/v1/payments/orders/{orderId}` → ödeme bilgisini gör.
9. `POST /api/v1/reviews` → satın alınan kursa yorum (rating 1-5).

---

## 8. Sorun Giderme

| Sorun | Çözüm |
|---|---|
| Port 8080 dolu | Çalışan Java sürecini kapat veya `server.port` ekle. |
| H2 "Feature not supported" | JDBC URL'i tam `jdbc:h2:file:./data/elearning` olmalı (ek bayrak ekleme). |
| H2 Console'a giremiyorum | URL/username/password alanlarını §4'teki değerlerle doldur. |
| Veri sıfırlanmıyor | `data/` klasörünü silip uygulamayı yeniden başlat. |
