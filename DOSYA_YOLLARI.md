# 📁 Oluşturulan/Değiştirilen Dosyalar

## 🆕 YENİ OLUŞTURULAN DOSYALAR

### 1. Room Database Yapısı
```
app/src/main/java/com/example/studentcenterapp/data/local/
├── database/
│   ├── AppDatabase.kt                    ⭐ YENİ
│   ├── DatabaseInitializer.kt            ⭐ YENİ
│   ├── dao/
│   │   ├── DepartmentDao.kt              ⭐ YENİ
│   │   ├── ServiceDao.kt                 ⭐ YENİ
│   │   └── StaffDao.kt                   ⭐ YENİ
│   └── entity/
│       ├── DepartmentEntity.kt           ⭐ YENİ
│       ├── ServiceEntity.kt              ⭐ YENİ
│       └── StaffEntity.kt                ⭐ YENİ
└── preferences/
    └── UserSessionManager.kt             ⭐ YENİ
```

### 2. Repository Implementation'ları
```
app/src/main/java/com/example/studentcenterapp/data/
├── department/
│   └── RoomDepartmentRepository.kt       ⭐ YENİ
└── service/
    └── RoomServiceRepository.kt          ⭐ YENİ
```

### 3. API Interface
```
app/src/main/java/com/example/studentcenterapp/data/remote/
└── api/
    └── StudentCenterApi.kt               ⭐ YENİ
```

### 4. Application Class
```
app/src/main/java/com/example/studentcenterapp/
└── StudentCenterApplication.kt           ⭐ YENİ
```

---

## ✏️ DEĞİŞTIRİLEN DOSYALAR

### 1. AppDI.kt
**Konum:** `app/src/main/java/com/example/studentcenterapp/data/AppDI.kt`

**Değişiklikler:**
- Room Database instance eklendi
- Retrofit API client eklendi
- UserSessionManager eklendi
- Repository'ler güncellendi (Room kullanıyor)

### 2. AndroidManifest.xml
**Konum:** `app/src/main/AndroidManifest.xml`

**Değişiklikler:**
- Application class eklendi: `android:name=".StudentCenterApplication"`

### 3. build.gradle.kts
**Konum:** 
- `app/build.gradle.kts` (app seviyesi)
- `gradle/libs.versions.toml` (version catalog)

**Değişiklikler:**
- Room Database dependencies eklendi
- DataStore dependency eklendi
- KSP plugin eklendi

---

## 🔍 Android Studio'da Nasıl Bulunur?

### Yöntem 1: Project View
1. Android Studio'yu aç
2. Sol tarafta **Project** tab'ını aç
3. Şu path'e git:
   ```
   app/src/main/java/com/example/studentcenterapp/data/
   ```
4. `local/` klasörünü aç → `database/` ve `preferences/` klasörlerini göreceksin

### Yöntem 2: Search ile Bulma
1. **Ctrl+Shift+N** (Windows/Linux) veya **Cmd+Shift+O** (Mac) bas
2. Dosya adını yaz:
   - `AppDatabase.kt`
   - `DatabaseInitializer.kt`
   - `RoomDepartmentRepository.kt`
   - `UserSessionManager.kt`

### Yöntem 3: Android View'dan
1. Sol tarafta **Android** tab'ını aç
2. `app/java/com.example.studentcenterapp.data` altında
3. `local` klasörü altında `database` ve `preferences` göreceksin

### Yöntem 4: Sync Yap
Eğer dosyalar görünmüyorsa:

1. **File → Sync Project with Gradle Files**
2. Veya **File → Invalidate Caches / Restart**
3. Veya sağ tık → **Synchronize**

---

## 📋 Dosya Listesi (Kopyala-Yapıştır İçin)

**Yeni Dosyalar:**
- `app/src/main/java/com/example/studentcenterapp/data/local/database/AppDatabase.kt`
- `app/src/main/java/com/example/studentcenterapp/data/local/database/DatabaseInitializer.kt`
- `app/src/main/java/com/example/studentcenterapp/data/local/database/dao/DepartmentDao.kt`
- `app/src/main/java/com/example/studentcenterapp/data/local/database/dao/ServiceDao.kt`
- `app/src/main/java/com/example/studentcenterapp/data/local/database/dao/StaffDao.kt`
- `app/src/main/java/com/example/studentcenterapp/data/local/database/entity/DepartmentEntity.kt`
- `app/src/main/java/com/example/studentcenterapp/data/local/database/entity/ServiceEntity.kt`
- `app/src/main/java/com/example/studentcenterapp/data/local/database/entity/StaffEntity.kt`
- `app/src/main/java/com/example/studentcenterapp/data/local/preferences/UserSessionManager.kt`
- `app/src/main/java/com/example/studentcenterapp/data/department/RoomDepartmentRepository.kt`
- `app/src/main/java/com/example/studentcenterapp/data/service/RoomServiceRepository.kt`
- `app/src/main/java/com/example/studentcenterapp/data/remote/api/StudentCenterApi.kt`
- `app/src/main/java/com/example/studentcenterapp/StudentCenterApplication.kt`

**Değiştirilen Dosyalar:**
- `app/src/main/java/com/example/studentcenterapp/data/AppDI.kt`
- `app/src/main/AndroidManifest.xml`
- `app/build.gradle.kts`
- `gradle/libs.versions.toml`
- `build.gradle.kts` (root)

---

## ⚠️ Eğer Hala Göremiyorsan

1. **Gradle Sync Yap:**
   - File → Sync Project with Gradle Files
   
2. **Cache Temizle:**
   - File → Invalidate Caches / Restart → Invalidate and Restart
   
3. **Project View Değiştir:**
   - Project View dropdown'dan "Project" seç (Android değil)
   
4. **Manuel Kontrol:**
   - Finder/Explorer'da dosya yoluna git
   - Dosyalar orada mı kontrol et
   - Android Studio'yu kapat-aç

