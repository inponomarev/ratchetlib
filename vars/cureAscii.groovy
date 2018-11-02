import groovy.io.FileType
@Grab('org.asciidoctor:asciidoctorj:1.5.7')
import org.asciidoctor.Asciidoctor
import org.asciidoctor.SafeMode
import org.asciidoctor.log.LogRecord
@Grab('org.apache.commons:commons-compress:1.18')
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.utils.IOUtils

import static org.asciidoctor.OptionsBuilder.options
def call(String inputFile) {
    File input = new File(inputFile)
    File output = new File(input.getParent(), "index.html")
    def options = options()
            .backend("html")
            .toFile(output)
            .safe(SafeMode.UNSAFE)
            .asMap()
    def warnings = ''
    def asciidoctor = Asciidoctor.Factory.create()
    asciidoctor.registerLogHandler { LogRecord logRecord ->
        warnings <<= "${logRecord.getMessage()}\n"
    }
    String result = asciidoctor.convertFile(input, options)
    if (result != null || !result.isEmpty())
        warnings <<= "${result}\n"

    if (warnings.length() > 0) {
        echo warnings
        error "AsciiDoctor build failed, see messages above."
    } else {
        echo "AsciiDoctor build succeeded."
    }

    def imgDir = new File(input.getParent(), "img")

    def out = new TarArchiveOutputStream(
            new GzipCompressorOutputStream(
                    new FileOutputStream(
                            new File(input.getParent(), "doc.tar.gz"))))
    out.withCloseable {
        addFileToArchive out, output, output.getName()
        imgDir.eachFileRecurse(FileType.FILES) { file ->
            addFileToArchive out, file, "img/" + file.getName()
        }
    }

    echo "Documentation compressed."
}

private static addFileToArchive(TarArchiveOutputStream out, File file, String entry) {
    out.putArchiveEntry(new TarArchiveEntry(file, entry))
    file.withInputStream {
        is -> IOUtils.copy(is, out)
    }
    out.closeArchiveEntry()
}
