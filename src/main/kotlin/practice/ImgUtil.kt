import org.bytedeco.javacpp.BytePointer
import org.bytedeco.opencv.global.opencv_core.*
import org.bytedeco.opencv.global.opencv_imgproc.*
import org.bytedeco.opencv.opencv_core.Mat
import org.bytedeco.opencv.opencv_core.MatVector
import processing.core.PImage
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Convert a PImage to an OpenCV Mat (BGRA) format.
 *
 * @param img PImage
 * @return an OpenCV Mat (BGRA) format converted from PImage
 */
fun toBGRAMat(img: PImage): Mat {
    val image: BufferedImage = img.getNative() as BufferedImage
    val matPixels: IntArray = (image.getRaster().getDataBuffer() as DataBufferInt).getData()
    val bb = ByteBuffer.allocate(matPixels.size * 4)
    val ib = bb.asIntBuffer()
    ib.put(matPixels)
    val result = toBGRAfromARGB(Mat(img.height, img.width, CV_8UC4, BytePointer(bb)))
    return result
}

/**
 * Convert an OpenCV Mat (BGRA) format to a PImage.
 *
 * @param mat an OpenCV Mat (BGRA) format
 * @return PImage converted from an OpenCV Mat (BGRA) format
 */
fun toPImage(mat: Mat): PImage {
    val result: PImage = PImage(mat.cols(), mat.rows(), PImage.ARGB)
    if (mat.channels() == 3) {
        val convMat = Mat()
        cvtColor(mat, convMat, COLOR_RGB2RGBA)
        result.pixels = matToARGBPixels(convMat)
    } else if (mat.channels() == 1) {
        val convMat = Mat()
        cvtColor(mat, convMat, COLOR_GRAY2RGBA)
        result.pixels = matToARGBPixels(convMat)
    } else if (mat.channels() == 4) {
        result.pixels = matToARGBPixels(mat)
    }
    result.updatePixels()
    return result
}

/**
 * Convert a Pimage to an OpenCV Mat (RGBA) format.
 *
 * @param img PImage
 * @return an OpenCV Mat (RGBA) format converted from PImage
 */
fun toRGBAMat(img: PImage): Mat {
    val image: BufferedImage = img.getNative() as BufferedImage
    val matPixels: IntArray = (image.getRaster().getDataBuffer() as DataBufferInt).getData()
    val bb = ByteBuffer.allocate(matPixels.size * 4)
    val ib = bb.asIntBuffer()
    ib.put(matPixels)
    val result = toRGBAfromARGB(Mat(img.height, img.width, CV_8UC4, BytePointer(bb)))
    return result
}

//Convert an OpenCV Mat (RGBA) format to an OpenCV Mat (ARGB) format.
private fun toRGBAfromARGB(argb: Mat): Mat {
    val splitMat = MatVector(4)
    split(argb, splitMat)
    val result = Mat(argb.rows(), argb.cols(), CV_8UC4)
    val rgbaVector = MatVector(splitMat[1], splitMat[2], splitMat[3], splitMat[0])
    merge(rgbaVector, result)
    return result
}

//Convert an OpenCV Mat (BGRA) format to an OpenCV Mat (ARGB) format.
private fun toBGRAfromARGB(argb: Mat): Mat {
    val splitMat: MatVector = MatVector()
    split(argb, splitMat)
    val result = Mat(argb.rows(), argb.cols(), CV_8UC4)
    val bgraVector = MatVector(splitMat[3], splitMat[2], splitMat[1], splitMat[0])
    merge(bgraVector, result)
    return result
}

//Convert an OpenCV Mat object with 4 channel to 4 channel ARGB PImage's pixel array.
private fun matToARGBPixels(mat: Mat): IntArray {
    val pImageChannels = 4
    val numPixels: Int = mat.cols() * mat.rows()
    val intPixels = IntArray(numPixels)
    val matPixels = ByteArray(numPixels * pImageChannels)
    mat.data().get(matPixels)

    ByteBuffer.wrap(matPixels).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer()[intPixels]
    return intPixels
}
