# 📋 Requirements — E-Learning Platform Backend

> Bu doküman platformun **fonksiyonel gereksinimlerini, veri modelini (entity'ler, alanlar, ilişkiler) ve iş/validasyon kurallarını** tanımlar. Mimari ve kodlama standartları için bkz. [agent.md](./agent.md).
> **Kapsam notu:** Eğitim amaçlı bir projedir. Aşağıdaki iş kuralları, verilen şemaya analist yorumu eklenerek üretilmiştir.

---

## 1. Genel Tanım

E-Learning, eğitmenlerin (Instructor) ücretli/ücretsiz online kurslar yayınladığı, kullanıcıların (User) bu kursları satın alıp (Order/Payment) değerlendirebildiği (Review) bir online eğitim platformudur. Kurslar çok seviyeli kategori ağacında (Category) sınıflandırılır ve bir dilde (Language) sunulur.

### Aktörler (Roller)
| Rol | Açıklama |
|---|---|
| **User** | Kursları görüntüler, satın alır, yorum yapar. |
| **Instructor** | Kurs oluşturur/yönetir. (User'ın yetkilendirilmiş hali) |
| **Admin** | Kategori, dil, rol ve genel platform yönetimi yapar. |

---

## 2. Veri Modeli — Genel Kurallar

- Tüm entity'ler **`BaseEntity`**'den türeyecektir (sonraki adımda eklenecek). `createdAt`, `updatedAt`, `id` gibi ortak alanlar **tek tek entity'lere yazılmaz**, BaseEntity'den gelir.
- Tüm PK'lar `id` (Long, auto-increment) olacaktır.
- İsimlendirme: entity tekil PascalCase, tablo çoğul snake_case, kolon snake_case (bkz. [agent.md](./agent.md)).
- Para alanları (`price`, `unitPrice`, `totalPrice`) ondalık hassasiyet için **`BigDecimal`** olacaktır (float/double kullanılmaz).
- **DÜZELTME — adlandırma:** FK alanı `orderId` ("OrdersId" değil); adres alanı `address` ("adress" değil).

### İlişki Haritası (Özet)
```
Role 1───* User
Language 1───* Course
Category 1───* Category   (self-referencing, parentId nullable)
Category 1───* Course     (sadece leaf kategoriye)
User (Instructor) 1───* Course
User 1───* Review        *───1 Course
User 1───1 Cart 1───* CartItem *───1 Course   (geçici / değişken)
User 1───* Order 1───* OrderItem *───1 Course (kesinleşmiş / immutable)
Order 1───1 Payment
```

**Sepet → Sipariş akışı:** Sepet (Cart) geçici ve değişkendir; satın alma kesinleşmiş ve değişmezdir. Checkout anında `Cart → Order + OrderItem (+ Payment)` dönüşümü yapılır, fiyatlar o an snapshot'lanır (`unitPrice`) ve sepet temizlenir.

---

## 3. Modüller, Entity Yapısı ve Alanlar

### 3.1 Role
| Alan | Tip | Kısıt |
|---|---|---|
| id | Long | PK |
| name | String | NOT NULL, UNIQUE |

- **Örnekler:** `User`, `Instructor`, `Admin`.
- **İlişki:** 1 Role → N User.

**İş/Validasyon Kuralları**
- `name`: boş olamaz (`@NotBlank`), sistemde **benzersiz** olmalı.
- `name` uzunluğu makul sınırda (örn. 2–50 karakter).
- Bir role bağlı kullanıcı varken silinememeli (referential integrity).

---

### 3.2 Language
| Alan | Tip | Kısıt |
|---|---|---|
| id | Long | PK |
| languageName | String | NOT NULL, UNIQUE |

- **Örnekler:** `Turkish`, `English`.
- **İlişki:** 1 Language → N Course.

**İş/Validasyon Kuralları**
- `languageName`: boş olamaz, **benzersiz** olmalı.
- Uzunluk 2–50 karakter.
- Bir dile bağlı kurs varken silinememeli.

---

### 3.3 Category (Self-Referencing / Çok Seviyeli Ağaç)
| Alan | Tip | Kısıt |
|---|---|---|
| id | Long | PK |
| parentId | Long | **NULLABLE**, FK → Category.id |
| name | String | NOT NULL |

- **DÜZELTME:** Ayrı bir `SubCategory` tablosu **YOKTUR**. Hiyerarşi tek tablo üzerinde self-referencing ile kurulur.
- `parentId = null` → üst (kök/root) kategori.
- `parentId = X` → X'in alt kategorisi. Alt kategoriler de kendi altlarına bölünebilir → **çok seviyeli ağaç** (örn. Programlama → Backend → Java).
- **İlişki:** Category 1 → N Category (children); Category 1 → N Course (yalnız leaf).

**İş/Validasyon Kuralları**
- `name`: boş olamaz; **aynı parent altında** benzersiz olmalı (kardeşler arasında çakışma yok).
- `parentId` kendi `id`'sine **eşit olamaz** (self-loop yasak).
- `parentId` ile döngü (cycle) oluşturulamaz — bir kategori kendi alt ağacındaki bir düğümü parent olarak seçemez.
- Belirtilen `parentId` mevcut bir kategori olmalı.
- Altında çocuk kategorisi veya bağlı kursu olan kategori silinememeli (ya da silme cascade kuralı netleştirilmeli).

---

### 3.4 Course
| Alan | Tip | Kısıt |
|---|---|---|
| id | Long | PK |
| categoryId | Long | FK → Category.id, NOT NULL |
| instructorId | Long | FK → User.id, NOT NULL |
| languageId | Long | FK → Language.id, NOT NULL |
| courseName | String | NOT NULL |
| price | BigDecimal | NOT NULL, >= 0 |
| description | String (TEXT) | NULLABLE |
| difficulty | Enum | NOT NULL (BEGINNER / INTERMEDIATE / ADVANCED) |

- **DÜZELTME:** Course doğrudan **leaf (en alt) Category**'e bağlanır. Üst/ara kategorilere kurs bağlanamaz.
- **İlişki:** Course *→1 Category, *→1 User(Instructor), *→1 Language; Course 1→N Review; Course 1→N OrderItem.

**İş/Validasyon Kuralları**
- `courseName`: boş olamaz (`@NotBlank`), makul uzunluk (örn. 3–150 karakter).
- `price`: negatif olamaz (`>= 0`). 0 → ücretsiz kurs kabul edilir.
- `categoryId`: işaret ettiği kategori **leaf olmalı** (çocuğu olan kategoriye kurs bağlanamaz).
- `instructorId`: işaret ettiği kullanıcının rolü **Instructor** olmalı.
- `categoryId`, `languageId`, `instructorId`: mevcut kayıtları işaret etmeli.
- `difficulty`: tanımlı enum değerlerinden biri olmalı.
- Aynı eğitmenin aynı isimde birden fazla kursu olmaması önerilir (opsiyonel iş kuralı).

---

### 3.5 User
| Alan | Tip | Kısıt |
|---|---|---|
| id | Long | PK |
| roleId | Long | FK → Role.id, NOT NULL |
| fullName | String | NOT NULL |
| mail | String | NOT NULL, UNIQUE |
| passwordHash | String | NOT NULL (**bcrypt hash**) |
| phone | String | NULLABLE |
| birthDate | LocalDate | NULLABLE |

- **DÜZELTME:** `passwordHash` düz sayı/metin **DEĞİL**, **bcrypt** ile hashlenmiş olarak saklanır. Düz şifre asla veritabanına yazılmaz, log'lanmaz veya DTO ile dışarı dönülmez.
- **İlişki:** User *→1 Role; User 1→N Course (instructor olarak); User 1→N Review; User 1→N Order.

**İş/Validasyon Kuralları**
- `fullName`: boş olamaz, makul uzunluk (2–100).
- `mail`: boş olamaz, **geçerli e-posta formatında** (`@Email`), sistemde **benzersiz**.
- `passwordHash`: kullanıcıdan alınan ham şifre, kayıt/güncelleme sırasında bcrypt ile hashlenir. Ham şifre için minimum güvenlik kuralı (örn. min 8 karakter) **request DTO** seviyesinde kontrol edilir.
- `phone`: verilirse geçerli telefon formatında olmalı.
- `birthDate`: verilirse geçmiş bir tarih olmalı (gelecek tarih yasak); platform yaş alt sınırı uygulanabilir (örn. >= 13/18).
- `roleId`: mevcut bir rolü işaret etmeli.
- **Güvenlik:** `passwordHash` hiçbir Response DTO'da yer almaz.

---

### 3.6 Review
| Alan | Tip | Kısıt |
|---|---|---|
| id | Long | PK |
| courseId | Long | FK → Course.id, NOT NULL |
| userId | Long | FK → User.id, NOT NULL |
| rating | int | NOT NULL, 1–5 |
| comment | String (TEXT) | NULLABLE |

- **İlişki:** Review *→1 Course, *→1 User.

**İş/Validasyon Kuralları**
- `rating`: **1 ile 5 arası** (dahil) tam sayı (`@Min(1) @Max(5)`).
- `comment`: opsiyonel; verilirse maksimum uzunluk sınırı (örn. <= 1000 karakter).
- Bir kullanıcı **aynı kursa yalnızca bir yorum** yapabilir (userId + courseId unique) — opsiyonel ama önerilen iş kuralı.
- İş kuralı (önerilen): kullanıcı yorumu, kursu **satın almışsa** yapabilir.
- `courseId`, `userId`: mevcut kayıtları işaret etmeli.

---

### 3.7 Cart (Sepet — Geçici / Değişken)
| Alan | Tip | Kısıt |
|---|---|---|
| id | Long | PK |
| userId | Long | FK → User.id, NOT NULL, **UNIQUE** |

- **Amaç:** Kullanıcının satın almadan önce biriktirdiği kursların listesi. Geçici ve değişken bir yapıdır; Order ile **karıştırılmaz**.
- Kullanıcı başına **en fazla bir aktif sepet** olur (`userId` unique → 1:1).
- E-learning özelinde sepet yalındır: **adet (quantity), stok ve kargo yoktur**. Aynı kurs sepette en fazla bir kez bulunur.
- **İlişki:** Cart 1↔1 User; Cart 1→N CartItem.

**İş/Validasyon Kuralları**
- `userId`: mevcut bir kullanıcıyı işaret etmeli; aynı kullanıcı için ikinci sepet oluşturulamaz.
- **Sepet oluşumu (lazy):** Sepet, kullanıcı **ilk kursu eklediğinde** oluşturulur. Kullanıcı kaydında boş sepet açılmaz. Kullanıcının aktif sepeti yoksa ilk "sepete ekle" işlemi önce Cart'ı yaratır, sonra CartItem'ı ekler (tek transaction).
- Checkout (sipariş oluşturma) tamamlandığında sepet içeriği temizlenir.

---

### 3.8 CartItem (Sepet Kalemi)
| Alan | Tip | Kısıt |
|---|---|---|
| id | Long | PK |
| cartId | Long | FK → Cart.id, NOT NULL |
| courseId | Long | FK → Course.id, NOT NULL |

- **Not:** `quantity` ve `unitPrice` **YOKTUR**. Kurs ya sahip olunur ya olunmaz (adet anlamsız). Fiyat, sepette tutulmaz; checkout anında Course'tan okunup OrderItem'a **snapshot** edilir.
- **İlişki:** CartItem *→1 Cart, *→1 Course.

**İş/Validasyon Kuralları**
- `cartId`, `courseId`: mevcut kayıtları işaret etmeli.
- **[ZORUNLU]** Aynı sepette **aynı kurs iki kez** eklenemez (`cartId` + `courseId` unique).
- **[ZORUNLU]** Kullanıcının **zaten satın aldığı** bir kurs sepete eklenemez (`OrderItem` geçmişine bakılır).
- **[ZORUNLU]** Kullanıcı **kendi eğitmeni olduğu** kursu sepete ekleyemez.

---

### 3.9 Order
| Alan | Tip | Kısıt |
|---|---|---|
| id | Long | PK |
| userId | Long | FK → User.id, NOT NULL |
| totalPrice | BigDecimal | NOT NULL, >= 0 |

- **İlişki:** Order *→1 User; Order 1→N OrderItem; Order 1→1 Payment.
- **Oluşma şekli:** Order doğrudan elle değil, **sepetten (Cart) checkout** ile üretilir. Kesinleşmiş ve **immutable** kabul edilir; sonradan içeriği değişmez.

**İş/Validasyon Kuralları**
- `userId`: mevcut bir kullanıcıyı işaret etmeli.
- `totalPrice`: negatif olamaz; **OrderItem'ların `unitPrice` toplamına eşit** olmalı (tutarlılık kuralı, business katmanında hesaplanır/doğrulanır).
- Bir sipariş en az **bir OrderItem** içermelidir (boş sipariş oluşturulamaz) → boş sepetle checkout yapılamaz.

---

### 3.10 OrderItem
| Alan | Tip | Kısıt |
|---|---|---|
| id | Long | PK |
| orderId | Long | FK → Order.id, NOT NULL |
| courseId | Long | FK → Course.id, NOT NULL |
| unitPrice | BigDecimal | NOT NULL, >= 0 |

- **DÜZELTME:** FK adı `orderId` ("OrdersId" değil).
- **İlişki:** OrderItem *→1 Order, *→1 Course.

**İş/Validasyon Kuralları**
- `orderId`, `courseId`: mevcut kayıtları işaret etmeli.
- `unitPrice`: negatif olamaz; sipariş anındaki kurs fiyatını yansıtmalı (fiyat anlık snapshot — kurs fiyatı sonradan değişse de sipariş etkilenmez).
- Aynı sipariş içinde **aynı kurs iki kez** yer almamalı (courseId, orderId unique) — önerilen kural.
- Kullanıcının zaten satın aldığı bir kurs tekrar satın alınamaz (önerilen iş kuralı).

---

### 3.11 Payment
| Alan | Tip | Kısıt |
|---|---|---|
| id | Long | PK |
| orderId | Long | FK → Order.id, NOT NULL |
| paymentMethod | Enum/String | NOT NULL (CREDIT_CARD / DEBIT_CARD / TRANSFER ...) |
| date | LocalDateTime | NOT NULL |
| address | String | NOT NULL |
| status | Enum | NOT NULL (PENDING / COMPLETED / FAILED / REFUNDED) |

- **DÜZELTME:** Payment'ta **`userId` TUTULMAZ**; kullanıcıya `Payment → Order → User` zinciri üzerinden ulaşılır. (Veri tekrarını ve tutarsızlık riskini önler.)
- **DÜZELTME:** Alan adı `address` ("adress" değil).
- **İlişki:** Payment *→1 Order (1 Order'a 1 Payment — one-to-one).

**İş/Validasyon Kuralları**
- `orderId`: mevcut bir siparişi işaret etmeli; **bir Order'ın en fazla bir Payment'ı** olur.
- `paymentMethod`: tanımlı enum değerlerinden biri olmalı.
- `address`: boş olamaz (`@NotBlank`).
- `status`: tanımlı enum değerlerinden biri; iş akışına uygun geçiş yapılmalı (örn. PENDING → COMPLETED).
- `date`: ödeme oluşturulurken set edilir; gelecek tarih olmamalı.

---

## 4. Çapraz Kesişen (Cross-Cutting) Kurallar

- **Para birimi:** Tüm para alanları aynı para biriminde ve `BigDecimal` ile tutulur; ölçek (scale) tutarlı olmalı (örn. 2 ondalık).
- **Benzersizlik özeti:** `Role.name`, `Language.languageName`, `User.mail` benzersizdir. Category `name` aynı parent altında benzersizdir. `Cart.userId` benzersizdir (kullanıcı başına tek sepet); `CartItem (cartId, courseId)` ve `OrderItem (orderId, courseId)` benzersizdir.
- **Referans bütünlüğü:** Tüm FK alanları, oluşturma/güncelleme anında hedef kaydın varlığı doğrulanarak set edilir; bulunamazsa `404 / NotFound` business hatası fırlatılır.
- **Soft vs hard delete:** Bağlı kaydı olan üst kayıtlar (Role, Language, Category, Course) doğrudan silinemez; iş kuralı ihlali olarak ele alınır.
- **Validasyon katmanı:** Format/zorunluluk kuralları **Request DTO** üzerinde Bean Validation ile; varlık/ilişki/iş mantığı kuralları **business.rules** içinde uygulanır (bkz. [agent.md](./agent.md)).

---

## 5. Açık Kalan / Sonraki Adımda Netleşecek Konular

- `BaseEntity` alanları (`id`, `createdAt`, `updatedAt`, opsiyonel `isDeleted`) sonraki adımda tanımlanacak.
- Kimlik doğrulama / yetkilendirme (JWT vb.) bu aşamada kapsam dışı — yalnızca `passwordHash` (bcrypt) saklama kuralı geçerli.
- ~~Sepet (cart) akışı~~ → **KARAR VERİLDİ:** Ayrı `Cart` + `CartItem` tabloları kullanılacak (Seçenek B). Order temiz/immutable kalır; checkout'ta `Cart → Order + OrderItem (+ Payment)` dönüşümü yapılır. Bkz. §3.7–3.8. Sepet oluşumu: **lazy** — kullanıcı ilk kursu eklediğinde sepet yaratılır (karar verildi).
- Enum değerlerinin (difficulty, paymentMethod, status) nihai listesi kod aşamasında sabitlenecek.
