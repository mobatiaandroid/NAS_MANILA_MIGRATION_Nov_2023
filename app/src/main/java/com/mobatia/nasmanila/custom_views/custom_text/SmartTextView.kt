package com.mobatia.nasmanila.custom_views.custom_text

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet

import com.mobatia.nasmanila.R
import java.util.Locale

class SmartTextView : androidx.appcompat.widget.AppCompatTextView {

    private val TAG = SmartTextView::class.java.simpleName

    private val mRegisteredFonts = HashMap<FontType?, Typeface>()

    fun registerFont(fType: FontType?, fTypeface: Typeface) {
        if (mRegisteredFonts.containsKey(fType)) mRegisteredFonts.remove(fType)
        mRegisteredFonts[fType] = fTypeface
    }

    fun getFontTypeface(fType: FontType?): Typeface? {
        return mRegisteredFonts[fType]
    }

    private var mJustified = false
    private var mAllCaps = false
    private var mFontType: FontType? = null

    private var mFirstLineTextHeight = 0
    private val mLineBounds = Rect()

    constructor(context: Context) : super(context){
        initialize(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        initialize(attrs)
        setFont()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
        super(context!!, attrs, defStyleAttr){
        initialize(attrs)
        setFont()
    }

    private fun initialize(attrs: AttributeSet?) {
        var styleIndex = 0
        if (attrs != null) {
            val attrArray =
                context.theme.obtainStyledAttributes(attrs, R.styleable.SmartTextView, 0, 0)
            if (attrArray != null) {
                mJustified = attrArray.getBoolean(R.styleable.SmartTextView_justified, false)
                mFontType = SmartTextView.FontType.values()
                    .get(attrArray.getInt(R.styleable.SmartTextView_fontName, 1))
                mAllCaps =
                    attrArray.getBoolean(R.styleable.SmartTextView_android_textAllCaps, false)
                styleIndex = attrArray.getInt(R.styleable.SmartTextView_android_textStyle, 0)
            }
        }
        setTypeface(getFontTypeface(), styleIndex)
    }

    private fun getFontTypeface(): Typeface? {
        return if (!mRegisteredFonts.containsKey(mFontType)) typeface else mRegisteredFonts[mFontType]
    }

    fun setJustified(justified: Boolean) {
        mJustified = justified
    }

    fun setFontType(fType: FontType?) {
        mFontType = fType
        typeface = getFontTypeface()
    }

    fun setFontType(fType: FontType?, style: Int) {
        mFontType = fType
        setTypeface(getFontTypeface(), style)
    }

    override fun setAllCaps(allCaps: Boolean) {
        mAllCaps = allCaps
        super.setAllCaps(allCaps)
    }

    override fun setTypeface(tf: Typeface?, style: Int) {
        var tf = tf
        if (tf == null) tf = getFontTypeface()
        super.setTypeface(tf, style)
    }

    fun switchText(resId: Int) {
        switchText(resources.getString(resId))
    }

    fun switchText(newText: String?) {
        animate().alpha(0f).setDuration(150).withEndAction {
            text = newText
            animate().alpha(1f).setDuration(150).start()
        }.start()
    }

    override fun onDraw(canvas: Canvas) {
        if (!mJustified) {
            super.onDraw(canvas)
            return
        }

        // Manipulations to mTextPaint found in super.onDraw()...
        paint.color = currentTextColor
        paint.drawableState = drawableState
        val fullText =
            if (mAllCaps) text.toString().uppercase(Locale.getDefault()) else text.toString()
        val lineSpacing = lineHeight.toFloat()
        val drawableWidth = getDrawableWidth()
        var lineNum = 1
        var lineStartIndex = 0
        var lastWordEnd: Int
        var currWordEnd = 0
        if (fullText.indexOf(' ', 0) == -1) flushWord(
            canvas,
            paddingTop + lineSpacing,
            fullText
        ) else {
            while (currWordEnd >= 0) {
                lastWordEnd = currWordEnd + 1
                currWordEnd = fullText.indexOf(' ', lastWordEnd)
                if (currWordEnd != -1) {
                    paint.getTextBounds(fullText, lineStartIndex, currWordEnd, mLineBounds)
                    if (mLineBounds.width() >= drawableWidth) {
                        flushLine(canvas, lineNum, fullText.substring(lineStartIndex, lastWordEnd))
                        lineStartIndex = lastWordEnd
                        lineNum++
                    }
                } else {
                    paint.getTextBounds(fullText, lineStartIndex, fullText.length, mLineBounds)
                    if (mLineBounds.width() >= drawableWidth) {
                        flushLine(canvas, lineNum, fullText.substring(lineStartIndex, lastWordEnd))
                        rawFlushLine(canvas, ++lineNum, fullText.substring(lastWordEnd))
                    } else {
                        if (lineNum == 1) {
                            rawFlushLine(canvas, lineNum, fullText)
                        } else {
                            rawFlushLine(canvas, lineNum, fullText.substring(lineStartIndex))
                        }
                    }
                }
            }
        }
    }

    private fun getDrawableWidth(): Float {
        return (measuredWidth - paddingLeft - paddingRight).toFloat()
    }

    private fun setFirstLineTextHeight(firstLine: String) {
        paint.getTextBounds(firstLine, 0, firstLine.length, mLineBounds)
        mFirstLineTextHeight = mLineBounds.height()
    }

    private fun rawFlushLine(canvas: Canvas, lineNum: Int, line: String) {
        if (lineNum == 1) setFirstLineTextHeight(line)
        val yLine = (paddingTop + mFirstLineTextHeight + (lineNum - 1) * lineHeight).toFloat()
        canvas.drawText(line, paddingLeft.toFloat(), yLine, paint)
    }

    private fun flushLine(canvas: Canvas, lineNum: Int, line: String) {
        if (lineNum == 1) setFirstLineTextHeight(line)
        val yLine = (paddingTop + mFirstLineTextHeight + (lineNum - 1) * lineHeight).toFloat()
        val words = line.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val lineBuilder = StringBuilder()
        for (word in words) {
            lineBuilder.append(word)
        }
        var xStart = paddingLeft.toFloat()
        val wordWidth = paint.measureText(lineBuilder.toString())
        val spacingWidth = (getDrawableWidth() - wordWidth) / (words.size - 1)
        for (word in words) {
            canvas.drawText(word, xStart, yLine, paint)
            xStart += paint.measureText(word) + spacingWidth
        }
    }

    private fun flushWord(canvas: Canvas, yLine: Float, word: String) {
        var xStart = paddingLeft.toFloat()
        val wordWidth = paint.measureText(word)
        val spacingWidth = (getDrawableWidth() - wordWidth) / (word.length - 1)
        for (i in 0 until word.length) {
            canvas.drawText(word, i, i + 1, xStart, yLine, paint)
            xStart += paint.measureText(word, i, i + 1) + spacingWidth
        }
    }

    enum class FontType {
        Thin, Reg, Thick
    }

    private fun setFont() {
        val font = Typeface.createFromAsset(context.assets, "fonts/SourceSansPro-Regular.otf")
        setTypeface(font, Typeface.NORMAL)
        //        setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        setTextColor(context.resources.getColor(R.color.black))
    }
}