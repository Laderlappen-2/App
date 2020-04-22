package se.ju.student.hihe1788.laderappen2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.sqrt

class JoystickView(val mContext: Context, attrs: AttributeSet) : View(mContext, attrs) {

    private lateinit var mCanvasCenter: Pixel
    private lateinit var mTopHatCenter: Pixel
    private lateinit var mDirectionalRect: Rect
    private var mBoundsRadius = 0.0f
    private var mTopHatRadius = 0.0f
    private var isInsideTopHat = false

    private val mDirectionRectPaint = Paint(ANTI_ALIAS_FLAG).apply {
        this.color = mContext.getColor(R.color.colorBlack)

    }

    /**
     * The thing you move
     */
    private val mTopHatPaint = Paint(ANTI_ALIAS_FLAG).apply {
        this.color = mContext.getColor(R.color.colorNavyDark)
    }

    private val mBoundsPaint = Paint(ANTI_ALIAS_FLAG).apply {
        this.color = mContext.getColor(R.color.colorBlackOpa40)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mCanvasCenter = Pixel(w/2f, h/2f)
        mBoundsRadius = w/2 * 0.95f             // Arbitrary value

        mTopHatCenter = mCanvasCenter
        mTopHatRadius = w/2 * 0.3f    // Arbitrary value

        mDirectionalRect = Rect((mCanvasCenter.x - 10).toInt(),
                                (mCanvasCenter.y - w/2 * 0.8f).toInt(),
                                (mCanvasCenter.x + 10).toInt(),
                                (mCanvasCenter.y + w/2 * 0.8f).toInt())

    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            drawCircle(mCanvasCenter.x, mCanvasCenter.y, mBoundsRadius, mBoundsPaint)
            drawCircle(mTopHatCenter.x, mTopHatCenter.y, mTopHatRadius, mTopHatPaint)
            drawRect(mDirectionalRect, mDirectionRectPaint)

        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var value = super.onTouchEvent(event)

        val pressedPixel = Pixel(event.x, event.y)

        // To know if inside bounds
        val distToCanvasCenter = mCanvasCenter.getDistanceTo(pressedPixel)

        // To know if inside topHat
        val distToTopHatCenter = mTopHatCenter.getDistanceTo(pressedPixel)

        when(event?.action) {

            MotionEvent.ACTION_DOWN -> {
                if (distToTopHatCenter <= mTopHatRadius) { 
                    isInsideTopHat = true
                }
                return true
            }

            MotionEvent.ACTION_UP -> {
                isInsideTopHat = false
                mTopHatCenter = mCanvasCenter
                invalidate()
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                if (isInsideTopHat) {
                    if (distToCanvasCenter <= (mBoundsRadius - mTopHatRadius)) {
                        // change mTopHatCenter accordingly to chosen logic
                        mTopHatCenter = pressedPixel
                        // Other alternatives exist

                        // Evaluate new move and send new instructions to mower if needed
                        // if newTopHatCenter exceeds threshold
                        //      send data
                        //      Update threshold
                    }
                    invalidate()
                }
                return true
            }
        }
        return false
    }

    private inner class Pixel (var x : Float = 0f, var y : Float = 0f) {

        fun getDistanceTo(pixel: Pixel) : Float {
            val diffX = this.x - pixel.x
            val diffY = this.y - pixel.y
            return sqrt((diffX * diffX) + (diffY * diffY))

        }
    }

}