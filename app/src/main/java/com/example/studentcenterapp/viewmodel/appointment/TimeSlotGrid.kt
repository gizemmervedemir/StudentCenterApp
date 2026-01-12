package com.example.studentcenterapp.viewmodel.appointment

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.model.TimeSlot

@Composable
fun TimeSlotGrid(
    slots: List<TimeSlot>,
    selectedSlotId: String?,
    onSlotClick: (TimeSlot) -> Unit,
    modifier: Modifier = Modifier
) {
    // Saatleri her satırda 3 adet olacak şekilde grupluyoruz (Görseldeki gibi)
    val rows = slots.chunked(3)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        rows.forEach { rowSlots ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowSlots.forEach { slot ->
                    TimeSlotItem(
                        slot = slot,
                        isSelected = slot.id == selectedSlotId,
                        onClick = { onSlotClick(slot) },
                        modifier = Modifier.weight(1f) // Her kutucuk eşit genişlikte
                    )
                }

                // Eğer satırda 3'ten az eleman varsa (örn: son satırda 1 veya 2 slot kaldıysa)
                // hizalamanın bozulmaması için boş weight ekliyoruz.
                if (rowSlots.size < 3) {
                    repeat(3 - rowSlots.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}