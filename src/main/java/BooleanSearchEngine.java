import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    private final Map<String, List<PageEntry>> filesList = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        for (File file : pdfsDir.listFiles()) {
            var doc = new PdfDocument(new PdfReader(file));
            int pageCounts = doc.getNumberOfPages();
            for (int pages = 1; pages <= pageCounts; pages++) {
                var page = doc.getPage(pages);
                var text = PdfTextExtractor.getTextFromPage(page);
                var words = text.split("\\P{IsAlphabetic}+");
                Map<String, Integer> wordsValue = new HashMap<>();
                for (var word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    wordsValue.put(word.toLowerCase(), wordsValue.getOrDefault(word, 0) + 1);
                }
                for (Map.Entry<String, Integer> entry : wordsValue.entrySet()) {
                    List<PageEntry> wordsList = new ArrayList<>();
                    if (filesList.containsKey(entry.getKey())) {
                        wordsList = filesList.get(entry.getKey());
                    }
                    wordsList.add(new PageEntry(file.getName(), pages, entry.getValue()));
                    filesList.put(entry.getKey(), wordsList);
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        if (filesList.containsKey(word.toLowerCase())) {
            return filesList.get(word.toLowerCase());
        }
        return Collections.emptyList();
    }
}
