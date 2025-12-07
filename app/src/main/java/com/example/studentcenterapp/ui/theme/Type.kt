package com.example.studentcenterapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val StudentCenterTypography = Typography(

    // EN BÜYÜK BAŞLIK – Bold 32
    // Örn: "Kişisel Bilgiler", admin "Merhaba, Elif."
    displayLarge = TextStyle(
        fontFamily = Figtree,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    ),

    // BÜYÜK EKRAN BAŞLIĞI – Bold 24
    // Örn: "Hoş Geldiniz", "Şifremi Sıfırla", "Doğrulama"
    titleLarge = TextStyle(
        fontFamily = Figtree,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),

    // ORTA BAŞLIK – Bold 20–22 arası şeyler buraya toplanır
    // Örn: "Hesap Oluşturun", "Randevularım", "Mesajlar"
    titleMedium = TextStyle(
        fontFamily = Figtree,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp  // 20–22 arası her şeyi buraya map et
    ),

    //ACTİON
    bodyLarge = TextStyle( // Medium 22
        fontFamily = Figtree,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp
    ),

    // NORMAL BODY – Regular 14
    // Örn: açıklama metinleri, chat balon içi mesajlar
    bodyMedium = TextStyle(
        fontFamily = Figtree,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),

    // SOFT / SECONDARY TEXT – Light 14
    // Örn: profil menüsündeki "Kişisel Bilgiler, Parola Güncelle, Oturumu Kapat",
    // chat listesinde preview mesajı vb.
    bodySmall = TextStyle(
        fontFamily = Figtree,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp
    ),

    // BUTON YAZISI – Bold 16
    // Örn: "Giriş Yap", "Üye Ol", "Gönder", "Onayla", "Tamam", "Randevu Al"
    labelLarge = TextStyle(
        fontFamily = Figtree,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),

    // TAB / KÜÇÜK BAŞLIK – Medium 14 (veya SemiBold kullanmak istersen buraya)
    // Örn: "Aktif Randevularım", "Geçmiş Randevularım"
    labelMedium = TextStyle(
        fontFamily = Figtree,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),

    // ERROR / CAPTION – Bold 12
    // Örn: "E-posta/Şifre uyuşmuyor", "Şifreler uyuşmuyor"
    labelSmall = TextStyle(
        fontFamily = Figtree,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp
    )

)