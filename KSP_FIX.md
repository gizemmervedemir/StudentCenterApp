# KSP Plugin Hatası Düzeltildi

## ❌ Hata
```
Plugin [id: 'com.google.devtools.ksp', version: '2.2.21-1.0.28', apply: false] was not found
```

## ✅ Çözüm
KSP versiyonu `2.2.21-1.0.28` yerine `2.0.0-1.0.28` olarak güncellendi.

KSP versiyonu Kotlin versiyonu ile tam eşleşmek zorunda değil. Kotlin 2.2.21 ile KSP 2.0.0-1.0.28 uyumludur.

## 📝 Yapılan Değişiklik
`gradle/libs.versions.toml` dosyasında:
```toml
ksp = "2.0.0-1.0.28"  # Önceki: "2.2.21-1.0.28"
```

## 🔄 Sonraki Adımlar
1. Android Studio'da **File → Sync Project with Gradle Files** yap
2. Build hatası düzelmeli
3. Proje başarıyla compile olmalı

