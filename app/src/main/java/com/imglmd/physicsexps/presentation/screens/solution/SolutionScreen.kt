package com.imglmd.physicsexps.presentation.screens.solution

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.imglmd.physicsexps.R
import com.imglmd.physicsexps.domain.model.SolutionStep
import com.imglmd.physicsexps.presentation.screens.home.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SolutionScreen(
    navigateBack: () -> Unit,
    viewModel: SolutionViewModel = koinViewModel()
) {
    val steps = viewModel.solutionSteps.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.goBackFlow.collect {
            navigateBack()
        }
    }

    Scaffold() { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding(),
                start = 16.dp,
                end = 16.dp
            )
        ) {
            itemsIndexed(steps.value) { index, step ->
                SolutionStepCard(index = index + 1, step = step)
            }
        }
    }
}

@Composable
private fun SolutionStepCard(index: Int, step: SolutionStep) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(24.dp))
            .padding(16.dp)
    ) {
        when (step) {
            is SolutionStep.Theory -> {
                StepHeader(index, step.title, Icons.Default.Info)
                Spacer(Modifier.height(8.dp))
                Text(step.body, style = MaterialTheme.typography.bodyMedium)
            }
            is SolutionStep.Formula -> {
                StepHeader(index, step.description, Icons.Default.Build)
                Spacer(Modifier.height(8.dp))
                FormulaBox(step.expression)
            }
            is SolutionStep.Substitution -> {
                StepHeader(index, step.description, ImageVector.vectorResource(R.drawable.rocket))
                Spacer(Modifier.height(8.dp))
                Text(step.expression, style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(step.result, style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary)
            }
            is SolutionStep.Result -> {
                StepHeader(index, "Результат", Icons.Default.CheckCircle)
                Spacer(Modifier.height(4.dp))
                Text(
                    "${step.quantity.label}: ${step.quantity.symbol} = " +
                            "${"%.3g".format(step.quantity.value)} ${step.quantity.unit}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun FormulaBox(expression: String) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = expression,
            modifier = Modifier.padding(12.dp),
            fontFamily = FontFamily.Monospace,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun StepHeader(
    index: Int,
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        Surface(
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        ) {
            Text(
                text = index.toString(),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )

        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}