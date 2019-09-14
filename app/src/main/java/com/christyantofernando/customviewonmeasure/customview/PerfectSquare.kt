package com.christyantofernando.customviewonmeasure.customview

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt

class PerfectSquare @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {
    companion object {
        val DEFAULT_SQUARE_COLOR = Color.YELLOW
        val DEFAULT_SQUARE_STROKE_COLOR = Color.DKGRAY

        val DESIRED_WIDTH = 120
        val DESIRED_HEIGHT = 120

        val SQUARE_STROKE_WIDTH = 15f
        val SQUARE_BORDER_RADIUS = 25f
    }

    /**
     * Paints
     */
    private val squareBackgroundPaint = Paint(ANTI_ALIAS_FLAG)
    private val squareStrokePaint = Paint(ANTI_ALIAS_FLAG)

    /**
     * Colors
     */
    private var squareBackgroundColor: Int = DEFAULT_SQUARE_COLOR
        set(@ColorInt color) {
            field = color
//            squareBackgroundPaint.color = color
            invalidate()
        }
    private var squareStrokeColor: Int = DEFAULT_SQUARE_STROKE_COLOR
        set(@ColorInt color) {
            field = color
//            squareStrokePaint.color = color
            invalidate()
        }

    /**
     * Shapes
     */
    private val squareRect = Rect()

    init {
        squareBackgroundPaint.apply {
            style = Paint.Style.FILL
            color = squareBackgroundColor
        }
        squareStrokePaint.apply {
            style = Paint.Style.STROKE
            color = squareStrokeColor
            strokeWidth = SQUARE_STROKE_WIDTH
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        /**
         * Usually, no need to override this, unless for specific condition,
         * e.g:
         * - Drawing a circle, radius needs to be Math.min(width, height)
         * - Your view contains another Subview
         *
         * Note:
         * ========================================================================
         * WidthMode & HeightModes: EXACTLY, AT_MOST, UNSPECIFIED
         * - EXACTLY: then actualWidth needs to be widthSize (specified from xml) and not from desiredWidth
         * - AT_MOST: then actualWidth cannot be more than widthSize -> Math.min(desiredWidth, widthSize)..
         * - UNSPECIFIED: then actualWidth can be desiredWidth
         *
         * same goes for height
         *
         * - EXACTLY means the layout_width or layout_height value was set to a specific value.
         *           You should probably make your customview this size.
         * - AT_MOST means the layout_width or layout_height value was set to match_parent
         *           where a maximum size is needed (will be calculated by parent).
         *           Your customview should not exceed the size of it
         * - UNSPECIFIED means the layout_width or layout_height value was set to wrap_content with no restrictions.
         *               You can set your customview width & height whatever value you desire.
         * ========================================================================
         *
         *
         */
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        // Declare temp values
        var actualWidth: Int = DESIRED_WIDTH
        var actualHeight: Int = DESIRED_HEIGHT

        when (widthMode) {
            MeasureSpec.EXACTLY -> actualWidth = widthSize
            MeasureSpec.AT_MOST -> actualWidth = Math.min(DESIRED_WIDTH, widthSize)
            MeasureSpec.UNSPECIFIED -> actualWidth = DESIRED_WIDTH
        }
        when (heightMode) {
            MeasureSpec.EXACTLY -> actualHeight = heightSize
            MeasureSpec.AT_MOST -> actualHeight = Math.min(DESIRED_HEIGHT, heightSize)
            MeasureSpec.UNSPECIFIED -> actualHeight = DESIRED_HEIGHT
        }

        // Must call this
        val squareLength = Math.min(actualWidth, actualHeight)
        setMeasuredDimension(squareLength, squareLength)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val contentWidth = w - paddingLeft - paddingRight
        val contentHeight = h - paddingTop - paddingBottom

        squareRect.set(
            0,
            contentHeight,
            contentWidth,
            0)
    }

    override fun onDraw(canvas: Canvas) {
        drawSquare(canvas)
    }

    fun drawSquare(canvas: Canvas) {
        canvas.drawRoundRect(RectF(squareRect), SQUARE_BORDER_RADIUS, SQUARE_BORDER_RADIUS, squareBackgroundPaint)
        canvas.drawRoundRect(RectF(squareRect), SQUARE_BORDER_RADIUS, SQUARE_BORDER_RADIUS, squareStrokePaint)
    }
}