package com.example.alf.ui.match.formations

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.alf.R
import com.example.alf.data.model.match.FieldPosition

class FormationView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    companion object {
        const val FIELD_LENGTH: Float = 108F
        const val FIELD_WIDTH: Float = 68F
    }

    lateinit var fieldPositions: List<FieldPosition>

    private val paint = Paint()
    private var playerColor: Int = 0
    private var playerSize: Float = 16F
    private var orientation: Int = 0

    init {
        context?.theme?.obtainStyledAttributes(
            attrs,
            R.styleable.FormationView, 0, 0
        )?.apply {
            try {
                playerColor = getColor(
                    R.styleable.FormationView_playerColor,
                    resources.getColor(R.color.red, context.theme)
                )
                playerSize = getDimension(R.styleable.FormationView_playerRadius, 16F)
                orientation = getInt(R.styleable.FormationView_orientation, 0)
            } finally {
                recycle()
            }
        }

        paint.color = playerColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val ratio = if (orientation == 0) FIELD_WIDTH / FIELD_LENGTH else FIELD_LENGTH / FIELD_WIDTH
        val newHeight = (MeasureSpec.getSize(widthMeasureSpec) / ratio).toInt()

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), newHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            if (orientation == 0) {
                // vertical orientation
                for (fieldPosition in fieldPositions) {
                    drawCircle(
                        (width / 2 + (width / 2) * fieldPosition.widthValue).toFloat(),
                        (height / 2 - (height / 2) * fieldPosition.lengthValue).toFloat(),
                        playerSize,
                        paint
                    )
                }
            } else {
                // horizontal orientation
                for (fieldPosition in fieldPositions) {
                    drawCircle(
                        (width / 2 + (width / 2) * fieldPosition.lengthValue).toFloat(),
                        (height / 2 + (height / 2) * fieldPosition.widthValue).toFloat(),
                        playerSize,
                        paint
                    )
                }
            }

        }
    }

}