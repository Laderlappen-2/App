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
    private var isThrust = false


    private val mDirectionRectPaint = Paint(ANTI_ALIAS_FLAG).apply {
        this.color = mContext.getColor(R.color.colorBlackOpa50)
    }

    /**
     * The thing you move
     */
    private val mTopHatPaint = Paint(ANTI_ALIAS_FLAG).apply {
        this.color = mContext.getColor(R.color.colorNavyDarkOpa50)
    }

    private val mBoundsPaint = Paint(ANTI_ALIAS_FLAG).apply {
        this.color = mContext.getColor(R.color.transparent)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mCanvasCenter = Pixel(w/2f, h/2f)
        mBoundsRadius = w/2 * 0.95f             // Arbitrary value

        mTopHatCenter = Pixel(w/2f, h/2f)
        mTopHatRadius = w/2 * 0.4f    // Arbitrary value

        if (isThrust) {
            mDirectionalRect = Rect((mCanvasCenter.x - 2).toInt(),
                                    (mCanvasCenter.y - (mBoundsRadius-mTopHatRadius)).toInt(),
                                    (mCanvasCenter.x + 2).toInt(),
                                    (mCanvasCenter.y + (mBoundsRadius-mTopHatRadius)).toInt())
        } else {
            mDirectionalRect = Rect((mCanvasCenter.x - (mBoundsRadius-mTopHatRadius)).toInt(),
                                    (mCanvasCenter.y - 2).toInt(),
                                    (mCanvasCenter.x + (mBoundsRadius-mTopHatRadius)).toInt(),
                                    (mCanvasCenter.y + 2).toInt())
        }


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

        val currentPixel = Pixel(event.x, event.y)
        var distanceInAxis = 0f

        // To know if inside bounds
        val distToCanvasCenter = mCanvasCenter.getDistanceTo(currentPixel)

        // To know if inside topHat
        val distToTopHatCenter = mTopHatCenter.getDistanceTo(currentPixel)

        if (isThrust) {
            distanceInAxis = mCanvasCenter.y - currentPixel.y
        } else {
            distanceInAxis = currentPixel.x - mCanvasCenter.x
        }


        when(event?.action) {

            MotionEvent.ACTION_DOWN -> {
                if (distToTopHatCenter <= mTopHatRadius) { 
                    isInsideTopHat = true
                }
                return true
            }

            MotionEvent.ACTION_UP -> {
                isInsideTopHat = false
                mTopHatCenter.clone(mCanvasCenter)
                invalidate()
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                if (isInsideTopHat) {
                    if (distToCanvasCenter <= (mBoundsRadius - mTopHatRadius)) {
                        // change mTopHatCenter accordingly to chosen logic
                        //mTopHatCenter = currentPixel
                        // Other alternatives exist

                        if (isThrust) {
                            mTopHatCenter.y = currentPixel.y
                            //mTopHatCenter.x = mCanvasCenter.x
                        } else {
                            mTopHatCenter.x = currentPixel.x
                            //mTopHatCenter.y = mCanvasCenter.y
                        }

                        val force = ( distanceInAxis / (mBoundsRadius - mTopHatRadius) )
                        println(force)
                        //println(force)
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

    fun setToThrust(yesOrNo: Boolean) {
        isThrust = yesOrNo
    }

    private inner class Pixel (var x : Float = 0f, var y : Float = 0f) {

        fun getDistanceTo(pixel: Pixel) : Float {
            val diffX = this.x - pixel.x
            val diffY = this.y - pixel.y
            return sqrt((diffX * diffX) + (diffY * diffY))

        }

        fun clone(pixel: Pixel) {
            this.x = pixel.x
            this.y = pixel.y
        }

    }

}