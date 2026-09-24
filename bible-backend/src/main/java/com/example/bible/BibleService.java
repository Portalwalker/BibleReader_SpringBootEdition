package com.example.bible;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BibleService {

    private final JdbcTemplate jdbcTemplate;

    public BibleService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Object getDatabaseTime() {
        return jdbcTemplate.queryForObject(
                "SELECT NOW()",
                Object.class
        );
    }

    public List<Map<String, Object>> getBooks() {
        String sql = """
            WITH UniqueList AS (
                SELECT DISTINCT book
                FROM Scripture
            )
            SELECT book
            FROM UniqueList
            ORDER BY
                CASE book
                    WHEN 'Genesis'          THEN 1
                    WHEN 'Exodus'           THEN 2
                    WHEN 'Leviticus'        THEN 3
                    WHEN 'Numbers'          THEN 4
                    WHEN 'Deuteronomy'      THEN 5
                    WHEN 'Joshua'           THEN 6
                    WHEN 'Judges'           THEN 7
                    WHEN 'Ruth'             THEN 8
                    WHEN '1 Samuel'         THEN 9
                    WHEN '2 Samuel'         THEN 10
                    WHEN '1 Kings'          THEN 11
                    WHEN '2 Kings'          THEN 12
                    WHEN '1 Chronicles'     THEN 13
                    WHEN '2 Chronicles'     THEN 14
                    WHEN 'Ezra'             THEN 15
                    WHEN 'Nehemiah'         THEN 16
                    WHEN 'Esther'           THEN 17
                    WHEN 'Job'              THEN 18
                    WHEN 'Psalms'           THEN 19
                    WHEN 'Proverbs'         THEN 20
                    WHEN 'Ecclesiastes'     THEN 21
                    WHEN 'Song of Solomon'  THEN 22
                    WHEN 'Isaiah'           THEN 23
                    WHEN 'Jeremiah'         THEN 24
                    WHEN 'Lamentations'     THEN 25
                    WHEN 'Ezekiel'          THEN 26
                    WHEN 'Daniel'           THEN 27
                    WHEN 'Hosea'            THEN 28
                    WHEN 'Joel'             THEN 29
                    WHEN 'Amos'             THEN 30
                    WHEN 'Obadiah'          THEN 31
                    WHEN 'Jonah'             THEN 32
                    WHEN 'Micah'             THEN 33
                    WHEN 'Nahum'             THEN 34
                    WHEN 'Habakkuk'         THEN 35
                    WHEN 'Zephaniah'        THEN 36
                    WHEN 'Haggai'            THEN 37
                    WHEN 'Zechariah'        THEN 38
                    WHEN 'Malachi'          THEN 39

                    WHEN 'Matthew'          THEN 40
                    WHEN 'Mark'             THEN 41
                    WHEN 'Luke'             THEN 42
                    WHEN 'John'             THEN 43
                    WHEN 'Acts'             THEN 44
                    WHEN 'Romans'           THEN 45
                    WHEN '1 Corinthians'    THEN 46
                    WHEN '2 Corinthians'    THEN 47
                    WHEN 'Galatians'        THEN 48
                    WHEN 'Ephesians'        THEN 49
                    WHEN 'Philippians'      THEN 50
                    WHEN 'Colossians'       THEN 51
                    WHEN '1 Thessalonians'  THEN 52
                    WHEN '2 Thessalonians'  THEN 53
                    WHEN '1 Timothy'        THEN 54
                    WHEN '2 Timothy'        THEN 55
                    WHEN 'Titus'            THEN 56
                    WHEN 'Philemon'         THEN 57
                    WHEN 'Hebrews'          THEN 58
                    WHEN 'James'            THEN 59
                    WHEN '1 Peter'          THEN 60
                    WHEN '2 Peter'          THEN 61
                    WHEN '1 John'           THEN 62
                    WHEN '2 John'           THEN 63
                    WHEN '3 John'           THEN 64
                    WHEN 'Jude'             THEN 65
                    WHEN 'Revelation'       THEN 66
                    ELSE 999
                END
            """;

        return jdbcTemplate.queryForList(sql);
    }

    public List<Integer> getChapters(String book) {
        String sql = """
            SELECT DISTINCT chapter
            FROM Scripture
            WHERE book = ?
            ORDER BY chapter
            """;

        return jdbcTemplate.query(
                sql,
                (resultSet, rowNumber) ->
                        resultSet.getInt("chapter"),
                book
        );
    }

    public ChapterResponse getChapter(String book, int chapter) {
        String sql = """
            SELECT verse, text
            FROM Scripture
            WHERE book = ?
              AND chapter = ?
            ORDER BY verse
            """;

        List<VerseResponse> verses = jdbcTemplate.query(
                sql,
                (resultSet, rowNumber) ->
                        new VerseResponse(
                                resultSet.getInt("verse"),
                                resultSet.getString("text")
                        ),
                book,
                chapter
        );

        return new ChapterResponse(book, chapter, verses);
    }

    public record VerseResponse(
            int verse,
            String text
    ) {
    }

    public record ChapterResponse(
            String book,
            int chapter,
            List<VerseResponse> verses
    ) {
    }
}
