package com.etiya.elearning.core.seed;

import com.etiya.elearning.business.abstracts.CartService;
import com.etiya.elearning.business.abstracts.CategoryService;
import com.etiya.elearning.business.abstracts.CourseService;
import com.etiya.elearning.business.abstracts.LanguageService;
import com.etiya.elearning.business.abstracts.OrderService;
import com.etiya.elearning.business.abstracts.ReviewService;
import com.etiya.elearning.business.abstracts.RoleService;
import com.etiya.elearning.business.abstracts.UserService;
import com.etiya.elearning.business.requests.AddToCartRequest;
import com.etiya.elearning.business.requests.CreateCategoryRequest;
import com.etiya.elearning.business.requests.CreateCourseRequest;
import com.etiya.elearning.business.requests.CreateLanguageRequest;
import com.etiya.elearning.business.requests.CreateOrderRequest;
import com.etiya.elearning.business.requests.CreateReviewRequest;
import com.etiya.elearning.business.requests.CreateRoleRequest;
import com.etiya.elearning.business.requests.CreateUserRequest;
import com.etiya.elearning.entities.enums.Difficulty;
import com.etiya.elearning.entities.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Uygulama açılışında veritabanını örnek verilerle doldurur.
 * Idempotent: roller zaten varsa hiç çalışmaz (kalıcı DB'de tekrar eklenmesini önler).
 * Tüm kayıtlar servis katmanı üzerinden eklenir; böylece iş kuralları, validasyon ve
 * şifre bcrypt hashleme gerçek akışta çalışır.
 */
@Component
@AllArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoleService roleService;
    private final LanguageService languageService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final CourseService courseService;
    private final CartService cartService;
    private final OrderService orderService;
    private final ReviewService reviewService;

    @Override
    @Transactional // Tüm seed tek session içinde çalışır; LAZY ilişkiler (örn. instructor.getRole()) güvenle yüklenir.
    public void run(String... args) {
        // Zaten seed edilmişse tekrar çalışma.
        if (!roleService.getAll().isEmpty()) {
            return;
        }

        // 1) Roller
        Long adminRoleId = roleService.add(role("Admin")).getId();
        Long instructorRoleId = roleService.add(role("Instructor")).getId();
        Long userRoleId = roleService.add(role("User")).getId();

        // 2) Diller
        Long turkishId = languageService.add(language("Turkish")).getId();
        Long englishId = languageService.add(language("English")).getId();

        // 3) Kategori ağacı (çok seviyeli, self-referencing)
        //    Programlama -> Backend -> Java (leaf)
        //    Programlama -> Web (leaf)
        Long programlamaId = categoryService.add(category("Programlama", null)).getId();
        Long backendId = categoryService.add(category("Backend", programlamaId)).getId();
        Long javaId = categoryService.add(category("Java", backendId)).getId();          // leaf
        Long webId = categoryService.add(category("Web", programlamaId)).getId();        // leaf

        // 4) Kullanıcılar (şifreler bcrypt ile saklanır)
        userService.add(user(adminRoleId, "Admin User", "admin@elearning.com", "admin12345", "5550000000", LocalDate.of(1990, 1, 1)));
        Long instructor1Id = userService.add(user(instructorRoleId, "Ahmet Yılmaz", "ahmet@elearning.com", "instructor1", "5551111111", LocalDate.of(1985, 5, 20))).getId();
        Long instructor2Id = userService.add(user(instructorRoleId, "Elif Demir", "elif@elearning.com", "instructor2", "5552222222", LocalDate.of(1988, 9, 12))).getId();
        Long student1Id = userService.add(user(userRoleId, "Mehmet Kaya", "mehmet@elearning.com", "student111", "5553333333", LocalDate.of(2000, 3, 15))).getId();
        Long student2Id = userService.add(user(userRoleId, "Zeynep Şahin", "zeynep@elearning.com", "student222", "5554444444", LocalDate.of(1999, 11, 2))).getId();

        // 5) Kurslar (leaf kategoriye + Instructor rolündeki kullanıcıya bağlı)
        Long course1Id = courseService.add(course(javaId, instructor1Id, turkishId,
                "Java ile Backend Geliştirme", new BigDecimal("499.90"),
                "Sıfırdan Java ve Spring Boot ile REST API geliştirme.", Difficulty.BEGINNER)).getId();
        courseService.add(course(javaId, instructor1Id, turkishId,
                "Spring Boot ile Mikroservisler", new BigDecimal("899.00"),
                "İleri seviye mikroservis mimarisi ve dağıtık sistemler.", Difficulty.ADVANCED));
        Long course3Id = courseService.add(course(webId, instructor2Id, englishId,
                "Modern Web Development", new BigDecimal("299.00"),
                "HTML, CSS ve JavaScript ile modern web uygulamaları.", Difficulty.INTERMEDIATE)).getId();

        // 6) Sepet + Checkout (student1, iki kurs satın alır)
        cartService.addCourseToCart(addToCart(student1Id, course1Id));
        cartService.addCourseToCart(addToCart(student1Id, course3Id));
        orderService.checkout(checkout(student1Id, PaymentMethod.CREDIT_CARD, "Atatürk Cad. No:1, İstanbul"));

        // student2 tek kurs satın alır
        cartService.addCourseToCart(addToCart(student2Id, course1Id));
        orderService.checkout(checkout(student2Id, PaymentMethod.BANK_TRANSFER, "İnönü Sok. No:5, Ankara"));

        // 7) Yorumlar (yalnızca satın alınan kursa yapılabilir)
        reviewService.add(review(course1Id, student1Id, 5, "Çok faydalı ve anlaşılır bir kurs, tavsiye ederim."));
        reviewService.add(review(course1Id, student2Id, 4, "Genel olarak iyiydi, biraz daha örnek olabilirdi."));
        reviewService.add(review(course3Id, student1Id, 5, "Web tarafına başlamak için harika."));
    }

    // ---- küçük yardımcı kurucular (okunabilirlik için) ----

    private CreateRoleRequest role(String name) {
        CreateRoleRequest r = new CreateRoleRequest();
        r.setName(name);
        return r;
    }

    private CreateLanguageRequest language(String name) {
        CreateLanguageRequest r = new CreateLanguageRequest();
        r.setLanguageName(name);
        return r;
    }

    private CreateCategoryRequest category(String name, Long parentId) {
        CreateCategoryRequest r = new CreateCategoryRequest();
        r.setName(name);
        r.setParentId(parentId);
        return r;
    }

    private CreateUserRequest user(Long roleId, String fullName, String mail, String password, String phone, LocalDate birthDate) {
        CreateUserRequest r = new CreateUserRequest();
        r.setRoleId(roleId);
        r.setFullName(fullName);
        r.setMail(mail);
        r.setPassword(password);
        r.setPhone(phone);
        r.setBirthDate(birthDate);
        return r;
    }

    private CreateCourseRequest course(Long categoryId, Long instructorId, Long languageId,
                                       String courseName, BigDecimal price, String description, Difficulty difficulty) {
        CreateCourseRequest r = new CreateCourseRequest();
        r.setCategoryId(categoryId);
        r.setInstructorId(instructorId);
        r.setLanguageId(languageId);
        r.setCourseName(courseName);
        r.setPrice(price);
        r.setDescription(description);
        r.setDifficulty(difficulty);
        return r;
    }

    private AddToCartRequest addToCart(Long userId, Long courseId) {
        AddToCartRequest r = new AddToCartRequest();
        r.setUserId(userId);
        r.setCourseId(courseId);
        return r;
    }

    private CreateOrderRequest checkout(Long userId, PaymentMethod paymentMethod, String address) {
        CreateOrderRequest r = new CreateOrderRequest();
        r.setUserId(userId);
        r.setPaymentMethod(paymentMethod);
        r.setAddress(address);
        return r;
    }

    private CreateReviewRequest review(Long courseId, Long userId, int rating, String comment) {
        CreateReviewRequest r = new CreateReviewRequest();
        r.setCourseId(courseId);
        r.setUserId(userId);
        r.setRating(rating);
        r.setComment(comment);
        return r;
    }
}
