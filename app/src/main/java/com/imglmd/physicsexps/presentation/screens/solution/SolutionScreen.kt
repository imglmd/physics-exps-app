package com.imglmd.physicsexps.presentation.screens.solution

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hrm.latex.renderer.Latex
import com.hrm.latex.renderer.LatexAutoWrap
import com.hrm.latex.renderer.model.LatexConfig
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

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(end = 12.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary
            ) {
                Text(
                    text = index.toString(),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Spacer(Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(60.dp)
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    MaterialTheme.colorScheme.surface
            ).padding(16.dp)
        ) {

            when (step) {

                is SolutionStep.Theory -> {
                    StepTitle(step.title, Icons.Default.Info)
                    Spacer(Modifier.height(8.dp))

                    Text(
                        step.body,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                is SolutionStep.Formula -> {
                    StepTitle(step.description, Icons.Default.Build)
                    Spacer(Modifier.height(12.dp))

                    FormulaBox(step.expression)
                }

                is SolutionStep.Substitution -> {
                    StepTitle(step.description, ImageVector.vectorResource(R.drawable.rocket))

                    Spacer(Modifier.height(8.dp))

                    FormulaBox(step.expression)

                    Spacer(Modifier.height(8.dp))

                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        LatexAutoWrap(
                            latex = step.result,
                            modifier = Modifier.padding(12.dp),
                            config = LatexConfig(
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }

                is SolutionStep.Result -> {
                    StepTitle("Ответ", Icons.Default.CheckCircle)

                    Spacer(Modifier.height(12.dp))

                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                step.quantity.label,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(Modifier.height(4.dp))

                            Latex(
                                latex = "${step.quantity.symbol} = ${"%.3g".format(step.quantity.value)} \\text{${step.quantity.unit}}",
                                config = LatexConfig(
                                    fontSize = 22.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FormulaBox(expression: String) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        LatexAutoWrap(
            latex = expression,
            modifier = Modifier.padding(12.dp),
            config = LatexConfig(
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}

@Composable
private fun StepTitle(
    title: String,
    icon: ImageVector
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
    }
}