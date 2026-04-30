package com.carbit3333333.oiiglot_bulgary

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import org.junit.Assert.assertTrue
import org.junit.Test

class AssetEncodingGuardTest {

    private val suspiciousPatterns = listOf("РЋ", "РЎ", "РЂ", "вЂ", "Ð", "Ñ", "\uFFFD")

    @Test
    fun `json assets do not contain mojibake patterns`() {
        val assetsDir = resolveAssetsDir()
        val badFiles = Files.list(assetsDir).use { stream ->
            stream
                .filter { Files.isRegularFile(it) && it.fileName.toString().endsWith(".json") }
                .map { path -> path to String(Files.readAllBytes(path), StandardCharsets.UTF_8) }
                .filter { (_, text) -> suspiciousPatterns.any(text::contains) }
                .map { (path, _) -> path.fileName.toString() }
                .toList()
        }

        assertTrue("Suspicious mojibake found in: $badFiles", badFiles.isEmpty())
    }

    private fun resolveAssetsDir(): Path {
        val directPath = Paths.get("src", "main", "assets")
        if (Files.exists(directPath)) {
            return directPath
        }

        val appPath = Paths.get("app", "src", "main", "assets")
        if (Files.exists(appPath)) {
            return appPath
        }

        return directPath
    }
}
