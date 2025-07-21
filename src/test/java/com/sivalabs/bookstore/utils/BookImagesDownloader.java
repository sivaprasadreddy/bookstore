package com.sivalabs.bookstore.utils;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

public class BookImagesDownloader {
    private static final String BOOKS_FILE = "data/books.jsonlines";
    private static final String IMAGES_DIR = "src/main/resources/static/images/books";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Path imagesDir = Paths.get(IMAGES_DIR);

    public static void main(String[] args) throws IOException {
        Files.createDirectories(imagesDir);
        downloadImages();
    }

    private static void downloadImages() throws IOException {
        AtomicInteger counter = new AtomicInteger(0);
        try (InputStream is = BookImagesDownloader.class.getClassLoader().getResourceAsStream(BOOKS_FILE);
                var reader = new BufferedReader(new InputStreamReader(is, UTF_8))) {

            int skip = 11775;
            AtomicInteger skipCounter = new AtomicInteger(0);
            reader.lines().forEach(line -> {
                int rowNo = skipCounter.incrementAndGet();
                // System.out.println(skip+"::"+rowNo);
                if (rowNo > skip) {
                    String fileName = processLine(line);
                    System.out.printf("Downloaded image %d: %s%n", rowNo, fileName);
                    counter.incrementAndGet();
                }
            });
        }
        System.out.printf("Completed downloading %d images%n", counter.get());
    }

    private static String processLine(String line) {
        try {
            Book book = objectMapper.readValue(line, Book.class);
            String imageUrl = book.coverImg();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                String fileName = "book_" + book.id() + getFileExtension(imageUrl);
                Path imagePath = imagesDir.resolve(fileName);
                downloadImage(imageUrl, imagePath);
                return fileName;
            }
        } catch (IOException e) {
            System.err.printf("Failed to process book: %s%n", line);
            e.printStackTrace();
        }
        return null;
    }

    private static void downloadImage(String imageUrl, Path destination) throws IOException {
        try (InputStream in = new URL(imageUrl).openStream();
                OutputStream out = Files.newOutputStream(destination)) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    private static String getFileExtension(String url) {
        String extension = url.substring(url.lastIndexOf('.'));
        return extension.contains("?") ? extension.substring(0, extension.indexOf('?')) : extension;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Book(
            Long id,
            String title,
            String description,
            String language,
            @JsonProperty("cover_img") String coverImg,
            String price) {}
}
