package se.ju.student.hihe1788.laderappen2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.*
import android.view.ScaleGestureDetector.OnScaleGestureListener
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.plus

class RouteCanvasView(context: Context, attrs: AttributeSet): View(context, attrs) {
    private var mScaleDetector: ScaleGestureDetector? = null
    private var mSortedPoints = ArrayList<PointModel>()
    private var mPositionPoints = ArrayList<PointModel>()
    private var mCollisionAvoidancePoints = ArrayList<PointModel>()
    private lateinit var mOrigin: PointF
    private var mScale: Float = 10f
    private var mOffset: PointF = PointF(0f, 0f)
    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop
    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f
    private var currentX = 0f
    private var currentY = 0f

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
                    mScale -= detector.scaleFactor

                if(mScale < 1) mScale = 1f

                return false
            }
        })
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mScaleDetector!!.onTouchEvent(event)

        motionTouchEventX = event.x
        motionTouchEventY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
        }

        invalidate()
        return true
    }

    private fun touchStart() {
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }

    private fun touchMove() {
        val dx = Math.abs(motionTouchEventX - currentX)
        val dy = Math.abs(motionTouchEventY - currentY)
        if (dx >= touchTolerance || dy >= touchTolerance) {

            mOffset.set((dx + currentX).minus(mOrigin.x), (dy + currentY).minus(mOrigin.y))

            currentX = motionTouchEventX
            currentY = motionTouchEventY
        }
        invalidate()
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
                (mPositionPoints.size - 1) -> { canvas?.drawCircle(position.x, position.y, 35f, mStopPositionPaint) }
                else -> { canvas?.drawCircle(position.x, position.y, 20f, mPositionPaint) }
            }
        }
    }
}