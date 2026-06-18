# 🧠 Project Brain — E-Learning Platform Backend

> Bu doküman projenin **tek doğruluk kaynağıdır (single source of truth)**. Yeni eklenen her kod, burada tanımlı mimari, isimlendirme ve standartlara **uymak zorundadır**. Standart dışı bir ihtiyaç doğarsa, önce bu doküman güncellenir, sonra kod yazılır.

---

## 1. Genel Bakış

- **Proje:** E-Learning (online eğitim) platformu — Backend / REST API
- **Group / Base Package:** `com.etiya.elearning`
- **Mimari:** N-Layered (Katmanlı) Mimari
- **Stack:** Java 17, Spring Boot 4.1.0, Spring Web (MVC), Spring Data JPA, Bean Validation, Lombok, H2 Database

---

## 2. Mimari & Katmanlar

Bağımlılık yönü **tek yönlüdür** ve yukarıdan aşağıya akar. Üst katman bir alttakini bilir; alt katman üsttekini **asla bilmez**.

```
api  ──►  business  ──►  dataAccess  ──►  entities
                │
              core (cross-cutting: tüm katmanlar kullanabilir)
```

### Paket Yapısı

```
com.etiya.elearning
├── api
│   └── controllers          → REST Controller'lar (@RestController)
├── business
│   ├── abstracts            → Servis arayüzleri (interface)
│   ├── concretes            → Servis implementasyonları (@Service)
│   ├── rules                → İş kuralları (business rules) sınıfları
│   ├── requests             → İstek DTO'ları (CreateXRequest, UpdateXRequest)
│   ├── responses            → Yanıt DTO'ları (XResponse, GetXResponse)
│   └── mappers              → Entity ↔ DTO dönüştürücüler (XMapper, @Component)
├── dataAccess
│   └── abstracts            → Repository arayüzleri (JpaRepository)
├── entities
│   └── concretes            → JPA Entity'leri (@Entity)
└── core                     → Cross-cutting concerns (her katman kullanabilir)
    └── exceptions           → Global exception handling, özel exception'lar
```

### 2.1 `entities` — Veri Modeli Katmanı
- Veritabanı tablolarını temsil eden **JPA Entity** sınıfları (`@Entity`).
- Yalnızca veri tutar; iş mantığı içermez.
- Hiçbir katmana bağımlı değildir (en alt katman).
- **Kural:** Entity'ler katman dışına (özellikle `api`'ye) **asla** sızdırılmaz. Bkz. [DTO Politikası](#5-dto-politikası-zorunlu).

### 2.2 `dataAccess` — Veri Erişim Katmanı
- Veritabanı CRUD operasyonları. `JpaRepository`'den türeyen **repository arayüzleri**.
- Sadece veri erişiminden sorumludur; **iş kuralı içermez**.
- `entities` katmanını bilir.

### 2.3 `business` — İş Mantığı Katmanı
- Uygulamanın **kalbi**. Tüm iş kuralları, doğrulamalar ve orkestrasyon burada.
- `abstracts` (arayüz) → `concretes` (implementasyon) ayrımı zorunludur. Controller her zaman **arayüze** bağımlı olur (DIP — Dependency Inversion).
- DTO ↔ Entity dönüşümleri (mapping) bu katmanda yapılır.
- `dataAccess` ve `entities` katmanlarını bilir; `api`'yi bilmez.

### 2.4 `api` — Sunum Katmanı
- Dış dünyaya açılan **REST uçları** (`@RestController`).
- Sorumluluğu: HTTP isteğini almak, DTO doğrulamak, business servisini çağırmak, yanıtı döndürmek.
- **İçinde iş kuralı / veri erişimi OLMAZ.** İnce (thin) tutulur.
- Sadece `business` katmanını (arayüzleri) ve DTO'ları bilir.

### 2.5 `core` — Cross-Cutting Concerns
- Katmanlar arası ortak yapılar: global exception handling, ortak yardımcı sınıflar.
- Tüm katmanlar tarafından kullanılabilir.

---

## 3. İsimlendirme Kuralları (Java Standartları)

| Öğe | Kural | Örnek |
|---|---|---|
| **Package** | tamamı küçük harf, tekil/kısa | `com.etiya.elearning.business.concretes` |
| **Class / Interface / Enum** | PascalCase (UpperCamelCase) | `CourseManager`, `CourseService`, `Role` |
| **Method** | camelCase, fiil ile başlar | `getById()`, `createCourse()` |
| **Field / Değişken** | camelCase | `courseName`, `createdAt` |
| **Constant (`static final`)** | UPPER_SNAKE_CASE | `MAX_PAGE_SIZE` |
| **JPA Entity sınıfı** | tekil isim, PascalCase | `Course`, `Instructor`, `Enrollment` |
| **DB tablo adı** | çoğul, snake_case | `courses`, `enrollments` |
| **DB kolon adı** | snake_case | `course_name`, `created_at` |

### Katman Bazlı İsimlendirme Konvansiyonu

| Tip | Konvansiyon | Örnek |
|---|---|---|
| Servis arayüzü | `<Entity>Service` | `CourseService` |
| Servis implementasyonu | `<Entity>Manager` | `CourseManager` |
| Repository | `<Entity>Repository` | `CourseRepository` |
| Controller | `<Entity>sController` (çoğul) | `CoursesController` |
| İş kuralı | `<Entity>BusinessRules` | `CourseBusinessRules` |
| İstek DTO | `<Fiil><Entity>Request` | `CreateCourseRequest`, `UpdateCourseRequest` |
| Yanıt DTO | `<Bağlam><Entity>Response` | `CreatedCourseResponse`, `GetCourseResponse` |

---

## 4. Katman Sorumlulukları & İş Kuralı Standartları

1. **Tek Sorumluluk:** Her sınıf tek bir işe odaklanır. Controller HTTP, Manager iş mantığı, Repository veri erişimi yapar.
2. **Arayüze Programla:** `business.concretes` içindeki sınıflar daima `business.abstracts` içindeki bir arayüzü implemente eder. Bağımlılıklar **constructor injection** ile verilir.
3. **İş kuralları `rules` paketinde:** Doğrulama / kontrol mantığı (örn. "kurs adı zaten var mı?") `CourseBusinessRules` gibi ayrı sınıflara çıkarılır; Manager bunları çağırır. Manager'lar şişmez.
4. **Mapping business'ta:** Entity ↔ DTO dönüşümü business katmanında yapılır. Controller ve Repository mapping yapmaz.
5. **Controller ince kalır:** Controller yalnızca isteği alır, DTO'yu `@Valid` ile doğrular, servisi çağırır, sonucu döndürür.
6. **DataAccess saftır:** Repository'lere iş mantığı yazılmaz; sadece veri erişim metotları (Spring Data query methods) bulunur.

---

## 5. DTO Politikası (ZORUNLU)

> **Entity hiçbir koşulda Controller'a girmez ve Controller'dan dönmez.** Dış dünya ile yalnızca DTO konuşulur.

- **Her istek için ayrı bir Request DTO** olur: `CreateCourseRequest`, `UpdateCourseRequest` …
- **Her yanıt için ayrı bir Response DTO** olur: `CreatedCourseResponse`, `GetCourseResponse`, `GetAllCoursesResponse` …
- Request DTO'ları `business.requests`, Response DTO'ları `business.responses` paketinde tutulur.
- Request DTO'ları **Bean Validation** anotasyonlarıyla doğrulanır (`@NotNull`, `@NotBlank`, `@Size`, `@Email` …). Controller'da `@Valid` ile tetiklenir.
- Entity ↔ DTO dönüşümü **business katmanında** yapılır.
- **Yasak:** Entity'yi request body olarak almak veya response olarak döndürmek.

---

## 6. Global Hata Yönetimi (ZORUNLU)

- Tüm uygulama genelinde **merkezi exception handling** kullanılır: `@RestControllerAdvice` ile `core.exceptions` paketinde bir `GlobalExceptionHandler`.
- İş kuralı ihlalleri için özel exception'lar (örn. `BusinessException`) tanımlanır ve `core.exceptions` içinde tutulur.
- Hatalar **standart bir hata yanıtı (ProblemDetail / ErrorResponse)** formatında döner. Tutarlı yapı: `timestamp`, `status`, `message`, (varsa) `validationErrors`.
- Doğru HTTP durum kodları kullanılır:
  - Validation hatası → `400 Bad Request`
  - İş kuralı ihlali (`BusinessException`) → `400 Bad Request`
  - Kayıt bulunamadı → `404 Not Found`
  - Beklenmeyen hata → `500 Internal Server Error`
- **Yasak:** Controller içinde dağınık `try/catch` blokları ile hata yönetimi. Hatalar yukarı fırlatılır, merkezi handler yakalar.

---

## 7. Veritabanı (H2 — KALICI / PERSISTENT)

> Veritabanı **in-memory DEĞİL**, **dosya tabanlı (file-based) kalıcı** H2 olacak. Uygulama yeniden başladığında veriler **kaybolmaz**.

- Bağlantı, in-memory (`jdbc:h2:mem:...`) değil, **file** modunda kurulur: `jdbc:h2:file:./data/elearning` benzeri bir yol.
- H2 Console aktif edilir (geliştirme amaçlı).
- `application.properties` içinde tanımlanacak temel ayarlar (kod aşamasında eklenecek):
  - File-based datasource URL (kalıcı)
  - `spring.jpa.hibernate.ddl-auto` (geliştirmede `update`)
  - H2 console yolu (örn. `/h2-console`)
- Veri dosyaları `.gitignore`'a eklenmelidir (DB dosyaları repoya commit'lenmez).

---

## 8. Uygulama Kararları (Implementation Conventions)

Business katmanı kodlanırken sabitlenen kurallar (tüm yeni kod bunlara uyar):

- **Servis adlandırma:** arayüz `XService` (business.abstracts), implementasyon `XManager` (business.concretes, `@Service`).
- **Constructor injection:** Lombok `@AllArgsConstructor` + `final` alanlar. (İstisna: enjekte edilmeyen bir alan başlatılıyorsa — örn. `UserManager` içindeki `PasswordEncoder` — `@RequiredArgsConstructor` kullanılır.)
- **Mapper:** her entity için `XMapper` (`@Component`); `toEntity`, `updateEntityFromRequest`, `toResponse`, `toResponseList`. FK'lar response'ta id'ye düzleştirilir. Entity asla response olarak dönmez; `passwordHash` hiçbir response'ta yer almaz.
- **İş kuralları:** `XBusinessRules` (`@Service`); `xMustExist(id)` ilgili entity'yi `findById(...).orElseThrow(BusinessException)` ile döner. İhlalde **`core.exceptions.BusinessException`** fırlatılır.
- **Audit otomasyonu:** `createdDate`/`updatedDate`/`isActive`, `BaseEntity`'deki `@PrePersist`/`@PreUpdate` ile otomatik dolar; manager'lar elle set etmez.
- **Şifre:** ham şifre `BCryptPasswordEncoder` ile hashlenip `passwordHash`'e yazılır (`spring-security-crypto`).
- **Silme:** varsayılan **hard delete** (`deleteById`), önce varlık + referans (parent) kontrolleri yapılır.
- **Aggregate alt entity'leri:** `OrderItem` ve `CartItem` için ayrı servis yoktur; aggregate root (`Order` / `Cart`) üzerinden yönetilirler. Sadece response DTO + mapping'leri vardır.
- **İşlem (transaction):** çok adımlı/yazan akışlar (`checkout`, sepet temizleme, türetilmiş delete) `@Transactional`; salt-okunur okumalar `@Transactional(readOnly = true)`.

## 9. Geçerli Durum (Status)

- [x] N-Layered klasör yapısı oluşturuldu (`entities`, `dataAccess`, `business`, `api`, `core`)
- [x] Project Brain + Requirements dokümanları
- [x] `entities` katmanı (11 entity + BaseEntity + 3 enum)
- [x] `dataAccess` katmanı (11 repository)
- [x] `business` katmanı (servis + DTO + mapper + rules; tüm entity'ler + cart/checkout akışları)
- [x] Global exception handling (`@RestControllerAdvice` + `ErrorResponse`/`ValidationErrorResponse`)
- [x] `api` katmanı (9 controller, sadece DTO döner)
- [x] H2 file-based (kalıcı) datasource — restart sonrası veri korunuyor (test edildi)
- [x] Swagger/OpenAPI (springdoc) — Swagger UI aktif
- [ ] Kimlik doğrulama/yetkilendirme (JWT) — kapsam dışı (sonraki faz)

> Çalıştırma, Swagger UI ve H2 Console bilgileri için: [how-to-run.md](./how-to-run.md)

> **Not:** Bu doküman canlıdır. Mimari/standart değiştikçe **önce burası** güncellenir.
