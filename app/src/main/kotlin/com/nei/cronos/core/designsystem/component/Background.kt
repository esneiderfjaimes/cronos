package com.nei.cronos.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.theme.HorizontalPadding

/**
 * The main background for the app.
 * Uses [LocalAbsoluteTonalElevation] to set elevation of a [Surface].
 *
 * @param modifier Modifier to be applied to the background.
 * @param content The background content.
 */
@Composable
fun CronosBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Surface(
        modifier = modifier,
    ) {
        CompositionLocalProvider(LocalAbsoluteTonalElevation provides 0.dp) {
            Box { content(this) }
        }
    }
}

@Composable
fun DotPatternBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = modifier) {
        Box(
            Modifier
                .drawWithCache {
                    val width = this.size.width
                    val height = this.size.height

                    val space = 25.dp.toPx()
                    val dotSize = 1.dp.toPx()

                    val initWith = (width / 2) % space
                    val initHeight = (height / 2) % space

                    var x = initWith
                    var y = initHeight

                    onDrawBehind {
                        while (y <= height) {
                            while (x <= width) {
                                drawCircle(
                                    color = Color(0xFF2B2D3C),
                                    center = Offset(x, y),
                                    radius = dotSize
                                )
                                x += space
                            }
                            x = initWith
                            y += space
                        }
                    }
                },
        ) {
            content()
        }
    }
}



@Composable
fun NiaGradientBackgroundPreview3() {
    CronosTheme {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color(0xFF5A618D))
                .padding(HorizontalPadding),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFF020518))
                        .patternDot(
                            space = 10.dp
                        )
                        .defaultMinSize(
                            minWidth = ButtonDefaults.MinWidth,
                            minHeight = ButtonDefaults.MinHeight
                        )
                        .padding(ButtonDefaults.ContentPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Button")
                }
            }

            Box(
                modifier = Modifier
                    .size(150.dp)
                    .patternDot(
                        shape = RoundedCornerShape(32.dp)
                    )
            )

            Card(
                modifier = Modifier
                    .size(150.dp)
                    .patternDot(
                        shape = CardDefaults.shape
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Card",
                    Modifier
                        .padding(HorizontalPadding)
                        .align(CenterHorizontally)
                )
            }

            OutlinedCard(
                modifier = Modifier
                    .size(150.dp)
                    .patternDot(
                        shape = CardDefaults.outlinedShape
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Card",
                    Modifier
                        .padding(HorizontalPadding)
                        .align(CenterHorizontally)
                )
            }


        }
    }
}

@Preview
@Composable
fun NiaGradientBackgroundPreview() {
    var pointerOffset by remember {
        mutableStateOf(Offset(0f, 0f))
    }

    CronosTheme {
        CronosBackground {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .patternDot(
                    ),
                contentPadding = PaddingValues(32.dp),
            ) {
                items(count = 100) {
                    Card2(pointerOffset) {
                        pointerOffset = it
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun NiaGradientBackgroun5dPreview() {
    var pointerOffset by remember {
        mutableStateOf(Offset(0f, 0f))
    }

    CronosTheme {
        CronosBackground {

            Card2(pointerOffset) {
                pointerOffset = it
            }
        }
    }
}


@Composable
fun Card2(
    pointerOffset: Offset = Offset(0f, 0f),
    updatePointerOffset: (Offset) -> Unit = {},
) {
    val shapeSize = 64.dp
    val shape = RoundedCornerShape(shapeSize)

    val border = 10.dp


    Column(
        Modifier
            .clip(shape)
            .background(Color(0xFFFFFFFF).copy(0.25f))
            .pointerInput("dragging") {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    //  pointerOffset = Offset(x =  dragAmount.x, y = dragAmount.y)
                    updatePointerOffset(
                        Offset(
                            x = change.position.x + dragAmount.x,
                            y = change.position.y + dragAmount.y
                        )
                    )
                }
            }
            .onSizeChanged {
                updatePointerOffset(Offset(it.width / 2f, it.height / 2f))
            }
            .drawWithContent {
                // draws a fully black area with a small keyhole at pointerOffset that’ll show part of the UI.
                drawRect(
                    Brush.radialGradient(
                        listOf(Color.Red, Color.Transparent),
                        center = pointerOffset,
                        radius = 250.dp.toPx(),
                    )
                )


                val innerShape = shapeSize - border

                val borderPx = border.toPx()


                drawRoundRect(
                    color = Color(0xFF020518),
                    topLeft = Offset(borderPx, borderPx),
                    size = Size(
                        size.width - (borderPx * 2),
                        size.height - (borderPx * 2)
                    ),
                    cornerRadius = CornerRadius(
                        x = innerShape.toPx(),
                        y = innerShape.toPx()
                    ),
                )

                val width = this.size.width - (borderPx * 2)
                val height = this.size.height - (borderPx * 2)

                val space = 10.dp.toPx()
                val dotSize = 1.dp.toPx()

                val initWith = ((width / 2) % space) + borderPx
                val initHeight = ((height / 2) % space) + borderPx

                var x = initWith
                var y = initHeight


                val list = mutableListOf<Offset>()
                while (y <= height) {
                    while (x <= width) {
                        drawCircle(
                            color = Color(0xFF2B2D3C),
                            center = Offset(x, y),
                            radius = dotSize
                        )
                        list.add(Offset(x, y))
                        x += space

                    }
                    x = initWith
                    y += space
                }

                val red = Color.Black
                drawPoints(
                    points = (list),
                    pointMode = PointMode.Points,
                    brush = Brush.linearGradient(
                        listOf(Color.Blue, Color.Red),
                    ),
                    cap = StrokeCap.Round,
                    strokeWidth = dotSize,
                )

                val sizeArc = Size((innerShape * 2).toPx(), (innerShape * 2).toPx())
                drawArc(
                    brush = Brush.radialGradient(
                        listOf(Color.Transparent, red),
                        center = Offset(
                            x = (innerShape).toPx() + borderPx,
                            y = (innerShape).toPx() + borderPx
                        ),
                        radius = (innerShape).toPx()
                    ),
                    size = sizeArc,
                    topLeft = Offset(
                        x = borderPx,
                        y = borderPx
                    ),
                    startAngle = 180f,
                    sweepAngle = 90f,
                    useCenter = true,
                )

                drawLine(
                    brush = Brush.verticalGradient(
                        listOf(Color.Transparent, red),
                        startY = (innerShape).toPx() + borderPx,
                        endY = borderPx,
                    ),
                    start = Offset(
                        x = borderPx + (innerShape).toPx(),
                        y = (innerShape / 2).toPx() + borderPx
                    ),
                    end = Offset(
                        x = size.width - (innerShape).toPx() - borderPx,
                        y = (innerShape / 2).toPx() + borderPx
                    ),
                    strokeWidth = (innerShape).toPx()
                )

                drawArc(
                    brush = Brush.radialGradient(
                        listOf(Color.Transparent, red),
                        center = Offset(
                            x = size.width - (innerShape).toPx() - borderPx,
                            y = (innerShape).toPx() + borderPx
                        ),
                        radius = (innerShape).toPx()
                    ),
                    size = sizeArc,
                    topLeft = Offset(
                        x = size.width - (innerShape * 2).toPx() - borderPx,
                        y = borderPx
                    ),
                    startAngle = -90f,
                    sweepAngle = 90f,
                    useCenter = true,
                )

                drawLine(
                    brush = Brush.horizontalGradient(
                        listOf(Color.Transparent, red),
                        startX = (innerShape).toPx() + borderPx,
                        endX = borderPx,
                    ),
                    start = Offset(
                        x = borderPx + (innerShape / 2).toPx(),
                        y = (innerShape).toPx() + borderPx
                    ),
                    end = Offset(
                        x = borderPx + (innerShape / 2).toPx(),
                        y = size.height - (innerShape).toPx() - borderPx
                    ),
                    strokeWidth = (innerShape).toPx()
                )


                drawLine(
                    brush = Brush.horizontalGradient(
                        listOf(Color.Transparent, red),
                        startX = size.width- (innerShape).toPx() - borderPx,
                        endX = size.width- borderPx,
                    ),
                    start = Offset(
                        x = size.width-  borderPx - (innerShape / 2).toPx(),
                        y = (innerShape).toPx() + borderPx
                    ),
                    end = Offset(
                        x = size.width-  borderPx - (innerShape / 2).toPx(),
                        y = size.height - (innerShape).toPx() - borderPx
                    ),
                    strokeWidth = (innerShape).toPx()
                )


                drawArc(
                    brush = Brush.radialGradient(
                        listOf(Color.Transparent, red),
                        center = Offset(
                            x = borderPx + (innerShape).toPx(),
                            y = size.height - (innerShape).toPx() - borderPx
                        ),
                        radius = (innerShape).toPx()
                    ),
                    size = sizeArc,
                    topLeft = Offset(
                        x = borderPx,
                        y = size.height - (innerShape * 2).toPx() - borderPx
                    ),
                    startAngle = 90f,
                    sweepAngle = 90f,
                    useCenter = true,
                )
                drawLine(
                    brush = Brush.verticalGradient(
                        listOf(Color.Transparent, red),
                        startY = size.height - (innerShape).toPx() - borderPx,
                        endY = size.height - borderPx,
                    ),
                    start = Offset(
                        x = borderPx + (innerShape).toPx(),
                        y = size.height - (innerShape / 2).toPx() - borderPx
                    ),
                    end = Offset(
                        x = size.width - (innerShape).toPx() - borderPx,
                        y = size.height - (innerShape / 2).toPx() - borderPx
                    ),
                    strokeWidth = (innerShape).toPx()
                )
                drawArc(
                    brush = Brush.radialGradient(
                        listOf(Color.Transparent, red),
                        center = Offset(
                            x = size.width - (innerShape).toPx() - borderPx,
                            y = size.height - (innerShape).toPx() - borderPx
                        ),
                        radius = (innerShape).toPx()
                    ),
                    size = sizeArc,
                    topLeft = Offset(
                        x = size.width - (innerShape * 2).toPx() - borderPx,
                        y = size.height - (innerShape * 2).toPx() - borderPx
                    ),
                    startAngle = 0f,
                    sweepAngle = 90f,
                    useCenter = true,
                )


                while (y <= height) {
                    while (x <= width) {
                        drawCircle(
                            color = Color(0xFF2B2D3C),
                            center = Offset(x, y),
                            radius = dotSize
                        )
                        x += space
                    }
                    x = initWith
                    y += space
                }


                //    drawContent()
            }
    ) {
        Text(
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque nec erat mattis, facilisis est ac, tincidunt erat. Duis scelerisque faucibus consectetur.",
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        )
        Button(
            onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ), modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = shape
        ) {
            Text(text = "Get lifetime Access")
        }
    }
}
