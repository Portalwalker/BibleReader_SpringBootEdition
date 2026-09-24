package com.example.bible;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(
        origins = {
                "http://localhost:3000",
                "http://localhost:5173"
        }
)
public class BibleController {

    private final BibleService bibleService;

    public BibleController(BibleService bibleService) {
        this.bibleService = bibleService;
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        try {
            Object databaseTime = bibleService.getDatabaseTime();

            return ResponseEntity.ok(
                    Map.of(
                            "status", "ok",
                            "databaseTime", databaseTime
                    )
            );
        } catch (Exception error) {
            error.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Database connection failed"
                    ));
        }
    }

    @GetMapping("/books")
    public ResponseEntity<?> getBooks() {
        try {
            return ResponseEntity.ok(bibleService.getBooks());
        } catch (Exception error) {
            error.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Could not load books"
                    ));
        }
    }

    @GetMapping("/books/{book}/chapters")
    public ResponseEntity<?> getChapters(@PathVariable String book) {
        try {
            List<Integer> chapters = bibleService.getChapters(book);
            return ResponseEntity.ok(chapters);
        } catch (Exception error) {
            error.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Could not load chapters"
                    ));
        }
    }

    @GetMapping("/books/{book}/chapters/{chapter}")
    public ResponseEntity<?> getChapter(
            @PathVariable String book,
            @PathVariable String chapter
    ) {
        int chapterNumber;

        try {
            chapterNumber = Integer.parseInt(chapter);
        } catch (NumberFormatException error) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "error", "Invalid chapter"
                    ));
        }

        if (chapterNumber < 1) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "error", "Invalid chapter"
                    ));
        }

        try {
            BibleService.ChapterResponse response =
                    bibleService.getChapter(book, chapterNumber);

            return ResponseEntity.ok(response);
        } catch (Exception error) {
            error.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Could not load chapter"
                    ));
        }
    }
}
