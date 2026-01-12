package com.example.studentcenterapp.ui.student

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.studentcenterapp.R
import com.example.studentcenterapp.model.Appointment
import com.example.studentcenterapp.ui.theme.DarkText
import com.example.studentcenterapp.ui.theme.Figtree
import com.example.studentcenterapp.ui.theme.GreyBox
import com.example.studentcenterapp.ui.theme.PrimaryBlue
import com.example.studentcenterapp.ui.theme.lightText

@Composable
fun AppointmentCard(
    appointment: Appointment,
    onEditClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize() // Açılırken yumuşak geçiş yapar
            .background(GreyBox, RoundedCornerShape(25.dp))
            .clickable { expanded = !expanded } // Kartın tamamı veya ok açar
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = appointment.appointmentDate,
                        style = TextStyle(
                            fontFamily = Figtree,
                            fontWeight = FontWeight.Bold,

                            fontSize = 16.sp,
                            color = DarkText
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.AccessTime,contentDescription = "saat", modifier = Modifier.size(16.dp), tint = DarkText)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = appointment.startTime, style = TextStyle(fontFamily = Figtree, fontSize = 14.sp)) // Saati date'den çekebilirsin
                    }
                }

                // Sağdaki Açma/Kapama Oku
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,

                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Servis Bilgisi

            Row(verticalAlignment = Alignment.Top) {
                Icon(painterResource(R.drawable.solar_calendar_bold), contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = appointment.serviceName, style = TextStyle(fontFamily = Figtree, fontWeight = FontWeight.SemiBold, fontSize = 14.sp))
                    Text(text = appointment.departmentName, style = TextStyle(fontFamily = Figtree, fontSize = 12.sp), color = lightText)
                }
            }

            // --- GENİŞLEYEN KISIM (Butonlar) ---
            if (expanded) {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Düzenle Butonu (Figma: Gri/Beyaz)
                    OutlinedButton(
                        onClick = onEditClick,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color.Gray)
                    ) {
                        Text("Düzenle", color = DarkText)
                    }

                    // İptal Et Butonu (Figma: Pembe/Kırmızı)
                    Button(
                        onClick = onCancelClick,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
                    ) {
                        Text("İptal Et", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun CancelConfirmationDialog(
    appointment: Appointment,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "İptal etmek istediğine emin misin?",
                    style = TextStyle(fontFamily = Figtree, fontWeight = FontWeight.Bold, fontSize = 18.sp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Randevu Detay Özeti (Dialog içinde)
                Row {
                    Icon(Icons.Default.Schedule, contentDescription = null)
                    Text(appointment.appointmentDate)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Onayla Butonu (Yeşilimsi)
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4DB6AC))
                    ) {
                        Text("Onayla")
                    }
                    // Geri Dön Butonu
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Geri Dön")
                    }
                }
            }
        }
    }
}