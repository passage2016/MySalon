package com.example.mysalon.utils

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder


class QRCodeUtils {

    private val IMAGE_HALFWIDTH = 50 //宽度值，影响中间图片大小

    private var barcodeEncoder: BarcodeEncoder? = BarcodeEncoder()


    /**
     * 生成二维码,默认500大小
     * @param contents 需要生成二维码的文字、网址等
     * @return bitmap
     */
    fun createQRCode(contents: String?): Bitmap? {
        try {
            return barcodeEncoder!!.encodeBitmap(contents, BarcodeFormat.QR_CODE, 500, 500)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 生成二维码
     * @param contents 需要生成二维码的文字、网址等
     * @param size 需要生成二维码的大小（）
     * @return bitmap
     */
    fun createQRCode(contents: String?, size: Int): Bitmap? {
        try {
            return barcodeEncoder!!.encodeBitmap(contents, BarcodeFormat.QR_CODE, size, size)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }


    /**
     * 生成二维码
     * @param contents 需要生成二维码的文字、网址等
     * @param size 需要生成二维码的大小（）
     * @param whiteBorderScale 白边宽度比例，最低1，也就是二维码图片的1%白边
     * @return bitmap
     */
    fun createQRCode(contents: String?, size: Int, whiteBorderScale: Int): Bitmap? {
        try {
            val hints: HashMap<EncodeHintType, Any?> = HashMap()
            hints[EncodeHintType.MARGIN] = if (whiteBorderScale < 0) 1 else whiteBorderScale
            return barcodeEncoder!!.encodeBitmap(contents, BarcodeFormat.QR_CODE, size, size, hints)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }
}