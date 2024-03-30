package com.example.ifit.IFitApp.Presentation.Components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ifit.IFitApp.Domain.Model.History

@Composable
fun PieChart(
    data: List<History>,
    today : History?,
    radius:Float =320f,
    innerRadius:Float = 180f,
    transsparentWidth:Float = 70f,
) {
    val colorList : List<Color> = listOf(Color.Green)
    val sum = data.sumOf { it.totalSteps }
    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }
        Box(
            modifier = Modifier.size(220.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val width = size.width
                val height = size.height
                circleCenter = Offset(x= width/2f,y= height/2f)

                //val anglePerValue = 360f/sum
                var currentStartAngle = 0f

                colorList.forEach { pieChartInput ->
                    val scale = 1.0f
                    val angleToDraw = 360f
                    scale(scale){
                        drawArc(
                            color = Color.Green,
                            startAngle = currentStartAngle,
                            sweepAngle = angleToDraw,
                            useCenter = true,
                            size = Size(
                                width = radius*2f,
                                height = radius*2f
                            ),
                            topLeft = Offset(
                                (width-radius*2f)/2f,
                                (height-radius*2f)/2f
                            ),
                        )
                        currentStartAngle += angleToDraw
                    }
            }

                drawContext.canvas.nativeCanvas.apply {
                    drawCircle(
                        circleCenter.x,
                        circleCenter.y,
                        innerRadius,
                        Paint().apply {
                            color = Color.White.copy(alpha = 0.6f).toArgb()
                            setShadowLayer(10f,0f,0f,Color.Gray.toArgb())
                        }
                    )
                }
                drawCircle(
                    color = Color.White.copy(alpha = 0.2f),
                    radius = innerRadius+transsparentWidth/2f
                )
        }
            if(today!=null){
                Text(text = "You took ${today.totalSteps} steps.!", modifier = Modifier.width(Dp(innerRadius/1.5f)).padding(25.dp), fontWeight = FontWeight.SemiBold, fontSize = 15.sp, textAlign = TextAlign.Center)

            }else{
                Text(text = "You took ${sum} steps so far!", modifier = Modifier.width(Dp(innerRadius/1.5f)).padding(25.dp), fontWeight = FontWeight.SemiBold, fontSize = 15.sp, textAlign = TextAlign.Center)
            }
    }
}