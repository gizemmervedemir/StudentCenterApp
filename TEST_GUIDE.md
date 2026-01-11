# Test Rehberi - Room Database & Repository Migration

## ✅ Yapılan Değişiklikler

1. **Room Database** eklendi (sabit veriler için)
2. **Database Initialization** yapıldı (departments, services, staff)
3. **Repository Migration** tamamlandı:
   - DepartmentRepository → Room Database kullanıyor
   - ServiceRepository → Room Database kullanıyor

## 🧪 Test Yöntemleri

### 1. Uygulamayı Çalıştırma (En Kolay)

```bash
# Android Studio'da Run butonuna bas
# veya terminal'de:
./gradlew installDebug
```

**Beklenen Sonuç:**
- App başlar
- Splash screen gösterilir
- Welcome screen gösterilir
- Student Login'e gir
- Departments listesi görünür (10 department olmalı)

**Kontrol Noktaları:**
- ✅ App crash olmamalı
- ✅ Departments listesi görünmeli
- ✅ Services listesi görünmeli (department seçince)

### 2. Database Inspection (Android Studio)

1. **Database Inspector'ı Aç:**
   - Android Studio → View → Tool Windows → Database Inspector
   
2. **Database'e Bağlan:**
   - App'i çalıştır (emulator veya cihaz)
   - Database Inspector'da "student_center_db" görünmeli
   
3. **Tabloları Kontrol Et:**
   - `departments` tablosu → 10 kayıt olmalı
   - `services` tablosu → 20 kayıt olmalı
   - `staff` tablosu → 10 kayıt olmalı

**SQL Sorguları:**
```sql
-- Departments sayısı
SELECT COUNT(*) FROM departments;

-- Services sayısı
SELECT COUNT(*) FROM services;

-- Staff sayısı
SELECT COUNT(*) FROM staff;

-- Bir department'ın services'lerini gör
SELECT * FROM services WHERE departmentId = "0";
```

### 3. Logcat ile Debug

App'i çalıştır ve Logcat'te şunları kontrol et:

```bash
# Android Studio → Logcat
# Filter: "AppDI" veya "DatabaseInitializer"
```

**Beklenen Log'lar:**
- Database oluşturuldu
- Initialization başladı
- Veriler yüklendi

### 4. Unit Test Yazma (İsteğe Bağlı)

Test dosyası oluşturabilirsin:
- `app/src/test/java/com/example/studentcenterapp/data/department/RoomDepartmentRepositoryTest.kt`

### 5. Manuel Test Senaryosu

1. **App'i İlk Kez Çalıştır:**
   - App açılır
   - Database oluşturulur
   - Veriler yüklenir (arka planda)

2. **Departments Listesini Gör:**
   - Student Login → Login yap (herhangi bir email/password)
   - Departments ekranı açılır
   - 10 department listesi görünür

3. **Services Listesini Gör:**
   - Bir department'a tıkla
   - Services ekranı açılır
   - O department'a ait services görünür

4. **App'i Kapat ve Tekrar Aç:**
   - Veriler database'de kalmalı
   - İnternet olmasa bile çalışmalı (offline)

## 🔍 Hata Ayıklama

### Problem: Departments listesi boş

**Kontrol Et:**
1. Database initialization çalıştı mı?
   - Database Inspector'da tabloları kontrol et
   
2. Repository doğru mu kullanılıyor?
   - AppDI.kt → `departmentRepository` → `RoomDepartmentRepository` olmalı

3. ViewModel doğru repository'yi kullanıyor mu?
   - DepartmentListViewModel → DepartmentRepository kullanmalı

### Problem: App crash oluyor

**Logcat'te hata mesajını kontrol et:**
- Room database hatası mı?
- Context initialization hatası mı?
- Missing dependency hatası mı?

## ✅ Başarı Kriterleri

- [ ] App crash olmadan açılıyor
- [ ] Departments listesi görünüyor (10 item)
- [ ] Services listesi görünüyor (department seçince)
- [ ] Database Inspector'da veriler görünüyor
- [ ] Offline çalışıyor (internet olmadan)
- [ ] App kapatılıp açılınca veriler kaybolmuyor

## 📝 Notlar

- Database initialization ilk açılışta otomatik çalışır
- Veriler Room Database'de saklanır (SQLite)
- İnternet bağlantısı gerekmez (sabit veriler için)
- Mockoon API henüz kullanılmıyor (sadece hazırlık yapıldı)

