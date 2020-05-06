package se.ju.student.hihe1788.laderappen2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.plus
import androidx.core.view.MotionEventCompat
import androidx.core.view.MotionEventCompat.getActionIndex
import androidx.core.view.MotionEventCompat.getActionMasked

import kotlin.math.log2


class RouteCanvasView(context: Context, attrs: AttributeSet): View(context, attrs) {
    private var mScaleDetector: ScaleGestureDetector? = null
    private var mSimpleGestureDetector: GestureDetector? = null

    private var mSortedPoints = ArrayList<PointModel>()
    private var mPositionPoints = ArrayList<PointModel>()
    private var mCollisionAvoidancePoints = ArrayList<PointModel>()
    private lateinit var mOrigin: PointF
    private lateinit var mExtraCanvas: Canvas
    private lateinit var mExtraBitmap: Bitmap
    private val mBackgroundColor = ResourcesCompat.getColor(resources, R.color.colorWhite, null)
    private val mDrawPositionColor = ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, null)
    private val mDrawCollisionColor = ResourcesCompat.getColor(resources, R.color.colorSecondary, null)
    private val mDrawStartColor = ResourcesCompat.getColor(resources, R.color.colorGreen, null)
    private val mDrawStopColor = ResourcesCompat.getColor(resources, R.color.colorRed, null)
    private val mLinePaint  = Paint().apply {
        color = mDrawPositionColor
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 8f
    }

    private val mPositionPaint = Paint().apply { color = mDrawPositionColor }
    private val mCollisionPaint = Paint().apply { color = mDrawCollisionColor }
    private val mStartPositionPaint = Paint().apply { color = mDrawStartColor }
    private val mStopPositionPaint = Paint().apply { color = mDrawStopColor }
    private var mScale: Float = 10f
    private var mOffset: PointF = PointF(0f, 0f)

    fun updatePoints(route: RouteModel) {
        mPositionPoints = route.positionEvents
        mCollisionAvoidancePoints = route.collisionAvoidanceEvents

        setup()
    }

    fun setup() {
        mSortedPoints.addAll(mPositionPoints)
        mSortedPoints.addAll(mCollisionAvoidancePoints)
        mSortedPoints.sortBy { it.dateCreated }
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHieght: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHieght)
        if (::mExtraBitmap.isInitialized) mExtraBitmap.recycle()
        mExtraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        mExtraCanvas = Canvas(mExtraBitmap)
        mExtraCanvas.drawColor(mBackgroundColor)

        mOrigin = PointF((width/2).toFloat(), (height/2).toFloat())
        setup()

        mScaleDetector = ScaleGestureDetector(context, object : OnScaleGestureListener {
            override fun onScaleEnd(detector: ScaleGestureDetector) {}
            override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                return true
            }

            override fun onScale(detector: ScaleGestureDetector): Boolean {
                if(detector.scaleFactor > 1)
                    mScale += detector.scaleFactor
                else
                    mScale -= detector.scaleFactor * 5f

                if(mScale < 1) mScale = 1f

                return false
            }
        })

        mSimpleGestureDetector = GestureDetector(context, object :
            GestureDetector.SimpleOnGestureListener() {
                override fun onScroll(
                    e1: MotionEvent?,
                    e2: MotionEvent?,
                    distanceX: Float,
                    distanceY: Float
                ): Boolean {
                    Log.d("SCROLL", ""+ distanceX)
                    mOffset.plus(PointF(distanceX, distanceY))
                    Log.d("SCROLL", "Offset " + mOffset.x + ":" + mOffset.y)
                    return super.onScroll(e1, e2, distanceX, distanceY)
                }
            }
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mScaleDetector!!.onTouchEvent(event)
        mSimpleGestureDetector!!.onTouchEvent(event)

        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        for((index, value) in mSortedPoints.withIndex()) {
            if(index + 1 >= mSortedPoints.size) break // Please no out of bounds

            val posOne = PointF(value.positionX.toFloat() * mScale, value.positionY.toFloat() * mScale).plus(mOrigin).plus(mOffset)
            val posTwo = PointF(mSortedPoints[index + 1].positionX.toFloat() * mScale, mSortedPoints[index + 1].positionY.toFloat() * mScale).plus(mOrigin).plus(mOffset)

            canvas?.drawLine(posOne.x, posOne.y, posTwo.x, posTwo.y, mLinePaint)
        }

        for((index, value) in mCollisionAvoidancePoints.withIndex()) {
            val position = PointF(value.positionX.toFloat() * mScale, value.positionY.toFloat() * mScale).plus(mOrigin).plus(mOffset)
            canvas?.drawCircle(position.x, position.y, 30f, mCollisionPaint)
        }

        for((index, value) in mPositionPoints.withIndex()) {

            val position = PointF(value.positionX.toFloat() * mScale, value.positionY.toFloat() * mScale).plus(mOrigin).plus(mOffset)

            when (index) {
                0 -> { canvas?.drawCircle(position.x, position.y, 35f, mStartPositionPaint) }
                mPositionPoints.size -> { canvas?.drawCircle(position.x, position.y, 35f, mStopPositionPaint) }
                else -> { canvas?.drawCircle(position.x, position.y, 20f, mPositionPaint) }
            }
        }

        /*MIGHT NEED THIS CRAP canvas?.(mExtraBitmap, 0f, 0f, null)*/
    }
}