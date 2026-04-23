package com.imglmd.physicsexps.presentation.screens.result.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.imglmd.physicsexps.presentation.core.theme.CherryRose
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Comment(
    text: String,
    onClick: (String) -> Unit,
    onClickDelete: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable{
            onClick(text)
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().background(CherryRose),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.padding(6.dp).size(40.dp).clip(CircleShape)
                .background(Color.White),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally){
                Icon(contentDescription = "comment", imageVector = Icons.Default.MailOutline,
                    modifier = Modifier.fillMaxSize().padding(5.dp))
            }
            Text(text = text, style = MaterialTheme.typography.titleMedium, color = Color.White)
            IconButton(onClick = { onClickDelete(text) }) {
                Icon(contentDescription = "Delete", imageVector = Icons.Default.Delete,
                    tint = Color.White)
            }
        }
    }
}


