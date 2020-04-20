package se.ju.student.hihe1788.laderappen2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class JoystickView(val mContext: Context, attrs: AttributeSet) : View(mContext, attrs) {

    private lateinit var mCanvasCenter: Point
    private lateinit var mTopHatCenter: Point
    private lateinit var mCanvas: Canvas
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

        mCanvasCenter = Point(w/2f, h/2f)
        mBoundsRadius = w/2 * 0.95f             // Arbitrary value

        mTopHatCenter = mCanvasCenter
        mTopHatRadius = mBoundsRadius * 0.3f    // Arbitrary value

        mDirectionalRect = Rect((mCanvasCenter.x - 10).toInt(),
                                ((mCanvasCenter.y - mBoundsRadius) * 0.8).toInt(),
                                (mCanvasCenter.x + 10).toInt(),
                                ((mCanvasCenter.y + mBoundsRadius) * 0.8).toInt())

    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            drawCircle(mCanvasCenter.x, mCanvasCenter.y, mBoundsRadius, mBoundsPaint)
            drawCircle(mTopHatCenter.x, mTopHatCenter.y, mTopHatRadius, mTopHatPaint)
            drawRect(mDirectionalRect, mDirectionRectPaint)

        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var value = super.onTouchEvent(event)

        // Calculate isInsideTopHat
        // Calculate dist from topHat
        // Calculate dist from CanvasCenter

        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                // Calculate pixel to tophat center
                if (true) {

                    isInsideTopHat = true
                }
                return true
            }
            MotionEvent.ACTION_UP -> {
                isInsideTopHat = false
                mTopHatCenter = mCanvasCenter
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                if (isInsideTopHat) {
                    // Do stuff
                }
                var newPoint = Point(event.x, event.y)
                
            }
        }

        return false
    }

    private inner class Point (var x : Float = 0f, var y : Float = 0f)

}