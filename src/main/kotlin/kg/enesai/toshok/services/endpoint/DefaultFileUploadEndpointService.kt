package kg.enesai.toshok.services.endpoint

import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths


@Service
class DefaultFileUploadEndpointService: FileUploadEndpointService {

    companion object {
        const val UPLOADED_FOLDER = "/var/toshok/files/"
    }

    override fun saveUploadedFile(file: MultipartFile): String? {
        return if (!file.isEmpty) {
            val bytes = file.bytes
            val fileName = RandomStringUtils.randomAlphabetic(8) + "-" + file.originalFilename
            val path = Paths.get(UPLOADED_FOLDER + fileName)
            Files.write(path, bytes)
            fileName
        } else null
    }
}