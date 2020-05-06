package se.ju.student.hihe1788.laderappen2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import android.graphics.PointF
import androidx.core.content.res.ResourcesCompat

class RouteCanvasView(context: Context, val mRoute: RouteModel): View(context) {
    private val mSortedPoints = ArrayList<PointModel>()
    private val mPositionPoint = mRoute.positionEvents
    private val mCollisionAvoidancePoint = mRoute.collisionAvoidanceEvents
    private lateinit var mOrigin: PointF
    private lateinit var mExtraCanvas: Canvas
    private lateinit var mExtraBitmap: Bitmap
    private val mBackgroundColor = ResourcesCompat.getColor(resources, R.color.colorWhite, null)
    private val mDrawPositionColor = ResourcesCompat.getColor(resources, R.color.colorSecondaryDark, null)
    private val mDrawCollisionColor = ResourcesCompat.getColor(resources, R.color.colorCollision, null)
    private val mPositionPaint  = Paint().apply {
        color = mDrawPositionColor
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 8f
    }
    private  val mCollisionPaint = Paint().apply { color = mDrawCollisionColor }


    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHieght: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHieght)
        if (::mExtraBitmap.isInitialized) mExtraBitmap.recycle()
        mExtraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        mExtraCanvas = Canvas(mExtraBitmap)
        mExtraCanvas.drawColor(mBackgroundColor)
        mOrigin = PointF((width/2).toFloat(), (height/2).toFloat())

        mSortedPoints.addAll(mPositionPoint)
        mSortedPoints.addAll(mCollisionAvoidancePoint)
        mSortedPoints.sortBy { it.dateCreated }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        for((index, value) in mSortedPoints.withIndex()) {
            if(index + 1 >= mSortedPoints.size) break // Please no out of bounds

            val posOne = PointF(value.positionX.toFloat(), value.positionY.toFloat())
            val posTwo = PointF(mSortedPoints[index + 1].positionX.toFloat(), mSortedPoints[index + 1].positionY.toFloat())

            canvas?.drawLine(posOne.x, posOne.y, posTwo.x, posTwo.y, mPositionPaint)
        }

        /*canvas?.
        (mExtraBitmap, 0f, 0f, null)
        canvas?.drawCircle()*/
    }
}