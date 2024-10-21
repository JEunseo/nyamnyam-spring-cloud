package kr.post.controller;

import kr.post.entity.ImageEntity;
import kr.post.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {
    private final ImageService service;

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<ImageEntity>> findByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(service.findByPostId(postId));
    }
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<String>> findImageByRestaurantId(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(service.findImagesByRestaurantId(restaurantId));
    }

    @GetMapping("/post/{postId}/imageIds")
    public ResponseEntity<List<Long>> findImageIdsByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(service.findImageIdsByPostId(postId));
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<Boolean> delete(@PathVariable Long imageId) {
        return ResponseEntity.ok(service.deleteById(imageId));
    }
    @GetMapping("/group")
    public ResponseEntity<List<ImageEntity>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/fileName")
    public ResponseEntity<String> getFileName(@RequestParam String fileName) {
        return ResponseEntity.ok(service.getFileName(fileName));
    }

}