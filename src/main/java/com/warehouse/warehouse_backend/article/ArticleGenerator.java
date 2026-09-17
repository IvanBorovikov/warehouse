package com.warehouse.warehouse_backend.article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Component
public class ArticleGenerator {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Pattern STOP_WORDS = Pattern.compile(
            "(?i)(новый|новинка|подарок|оригинал|уценка|распродажа|скидка)"
    );

    private static final Pattern SPECIAL_CHARS = Pattern.compile(
            "[^a-zA-Zа-яА-Я0-9]"
    );

    private static final Pattern MULTIPLE_SPACES = Pattern.compile(
            "\\s+"
    );

    /**
     * Генерация артикула
     */
    @Transactional
    public String generateArticle(String productName) {
        // 1. Проверяем, есть ли товар с таким именем
        String existingArticle = findExistingArticle(productName);
        if (existingArticle != null) {
            return existingArticle;
        }

        // 2. Генерируем префикс
        String prefix = generatePrefix(productName);

        // 3. Получаем следующий номер из SEQUENCE
        Long nextNumber = getNextSequenceValue();

        // 4. Формируем артикул
        String newArticle = String.format("%s-%05d", prefix, nextNumber);

        // 5. Проверка на коллизию (на случай, если артикул уже существует)
        while (existsByArticle(newArticle)) {
            nextNumber = getNextSequenceValue();
            newArticle = String.format("%s-%05d", prefix, nextNumber);
        }

        return newArticle;
    }

    /**
     * Поиск существующего артикула по имени
     */
    private String findExistingArticle(String productName) {
        String sql = "SELECT article FROM products WHERE name = ? LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, productName);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Проверка существования артикула в БД
     */
    private boolean existsByArticle(String article) {
        String sql = "SELECT COUNT(*) FROM products WHERE article = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, article);
        return count != null && count > 0;
    }

    /**
     * Получение следующего значения из SEQUENCE
     */
    private Long getNextSequenceValue() {
        String sql = "SELECT nextval('article_number_seq')";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    /**
     * Генерация префикса из названия
     */
    private String generatePrefix(String productName) {
        // 1. Убираем стоп-слова
        String cleaned = STOP_WORDS.matcher(productName).replaceAll("");

        // 2. Заменяем специальные символы на пробел
        cleaned = SPECIAL_CHARS.matcher(cleaned).replaceAll(" ");

        // 3. Сжимаем множественные пробелы
        cleaned = MULTIPLE_SPACES.matcher(cleaned).replaceAll(" ");

        // 4. Обрезаем
        cleaned = cleaned.trim();

        // 5. Если после очистки меньше 3 символов - берем оригинал
        if (cleaned.length() < 3) {
            cleaned = productName;
            // Убираем стоп-слова из оригинала
            cleaned = STOP_WORDS.matcher(cleaned).replaceAll("");
            cleaned = SPECIAL_CHARS.matcher(cleaned).replaceAll(" ");
            cleaned = MULTIPLE_SPACES.matcher(cleaned).replaceAll(" ");
            cleaned = cleaned.trim();
        }

        // 6. Берем первые 3 символа и транслитерируем
        String prefix = translit(cleaned.substring(0, Math.min(3, cleaned.length()))).toUpperCase();

        // 7. Если префикс состоит из цифр или пустой - добавляем 'X'
        if (prefix.matches("^[0-9]+$") || prefix.isEmpty()) {
            prefix = "X" + prefix;
        }

        return prefix;
    }

    /**
     * Транслитерация кириллицы в латиницу
     */
    private String translit(String text) {
        StringBuilder result = new StringBuilder();

        for (char ch : text.toCharArray()) {
            result.append(translitChar(ch));
        }

        return result.toString();
    }

    /**
     * Транслитерация одного символа
     */
    private String translitChar(char ch) {
        switch (ch) {
            case 'А': return "A";
            case 'Б': return "B";
            case 'В': return "V";
            case 'Г': return "G";
            case 'Д': return "D";
            case 'Е': return "E";
            case 'Ё': return "E";
            case 'Ж': return "ZH";
            case 'З': return "Z";
            case 'И': return "I";
            case 'Й': return "Y";
            case 'К': return "K";
            case 'Л': return "L";
            case 'М': return "M";
            case 'Н': return "N";
            case 'О': return "O";
            case 'П': return "P";
            case 'Р': return "R";
            case 'С': return "S";
            case 'Т': return "T";
            case 'У': return "U";
            case 'Ф': return "F";
            case 'Х': return "KH";
            case 'Ц': return "TS";
            case 'Ч': return "CH";
            case 'Ш': return "SH";
            case 'Щ': return "SHCH";
            case 'Ы': return "Y";
            case 'Э': return "E";
            case 'Ю': return "YU";
            case 'Я': return "YA";

            case 'а': return "a";
            case 'б': return "b";
            case 'в': return "v";
            case 'г': return "g";
            case 'д': return "d";
            case 'е': return "e";
            case 'ё': return "e";
            case 'ж': return "zh";
            case 'з': return "z";
            case 'и': return "i";
            case 'й': return "y";
            case 'к': return "k";
            case 'л': return "l";
            case 'м': return "m";
            case 'н': return "n";
            case 'о': return "o";
            case 'п': return "p";
            case 'р': return "r";
            case 'с': return "s";
            case 'т': return "t";
            case 'у': return "u";
            case 'ф': return "f";
            case 'х': return "kh";
            case 'ц': return "ts";
            case 'ч': return "ch";
            case 'ш': return "sh";
            case 'щ': return "shch";
            case 'ы': return "y";
            case 'э': return "e";
            case 'ю': return "yu";
            case 'я': return "ya";

            default: return String.valueOf(ch);
        }
    }
}
