package hellokotlin.controller

/**
 * Created by dongxie on 2017/5/7.
 */

import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest
import java.io.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class FileUploadController {

    @RequestMapping("/upload")
    @ResponseBody
    fun handleFileUpload(@RequestParam("file") file: MultipartFile): String {
        if (!file.isEmpty) {
            try {
                val out = BufferedOutputStream(FileOutputStream(File(file.originalFilename)))
                out.write(file.bytes)
                out.flush()
                out.close()
            } catch (e: IOException) {
                e.printStackTrace()
                return "upload failed" + e.message
            }

            return "upload success"
        } else {
            return "upload failed,because file is empty"
        }
    }


    @RequestMapping(value = "/multiUpload", method = arrayOf(RequestMethod.POST))
    @ResponseBody
    fun handleFileUpload(request: HttpServletRequest): String {
        val files = (request as MultipartHttpServletRequest)
                .getFiles("file")
        var file: MultipartFile?
        var stream: BufferedOutputStream?
        for (i in files.indices) {
            file = files[i]
            if (!file!!.isEmpty) {
                try {
                    val bytes = file.bytes
                    stream = BufferedOutputStream(FileOutputStream(
                            File(file.originalFilename)))
                    stream.write(bytes)
                    stream.close()

                } catch (e: Exception) {
                    return "You failed to upload $i => " + e.message

                }

            } else {
                return "You failed to upload $i because the file was empty."
            }

        }
        return "upload successful"

    }

    @RequestMapping(value = "/multipleSave", method = arrayOf(RequestMethod.POST))
    @ResponseBody
    fun multipleSave(@RequestParam("file") files: Array<MultipartFile>): String {
        var stream: BufferedOutputStream? = null
        for (file in files) {
            if (!file.isEmpty) {
                try {
                    val bytes = file.bytes
                    stream = BufferedOutputStream(FileOutputStream(
                            File(file.originalFilename)))
                    stream.write(bytes)
                    stream.flush()
                    stream.close()

                } catch (e: Exception) {
                    stream = null
                    return "You failed to upload " + file.originalFilename + " => " + e.message
                } finally {
                    if (null != stream) {
                        try {
                            stream.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    }
                }
            } else {
                return "You failed to upload " + file.originalFilename + " because the file was empty."
            }

        }
        return "upload successful"

    }

    @RequestMapping(value = "/download", method = arrayOf(RequestMethod.GET))
    fun downloadFile(res: HttpServletResponse) {
        val fileName = "spring_test.jpg"
        res.setHeader("content-type", "application/octet-stream")
        res.contentType = "application/octet-stream"
        res.setHeader("Content-Disposition", "attachment;filename=" + fileName)
        val buff = ByteArray(1024)
        var bis: BufferedInputStream? = null
        var os: OutputStream? = null
        try {
            os = res.outputStream
            bis = BufferedInputStream(FileInputStream(File(fileName)))
            while (bis.read(buff) != -1) {
                os!!.write(buff, 0, buff.size)
                os.flush()
            }

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (null != os) {
                try {
                    os.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            if (null != bis) {
                try {
                    bis.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    @RequestMapping("/")
    fun index(map: ModelMap): String {
        map.addAttribute("host", "http://blog.didispace.com")
        return "index"
    }

}
