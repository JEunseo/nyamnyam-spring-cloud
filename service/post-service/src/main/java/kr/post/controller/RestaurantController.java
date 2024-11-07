package kr.post.controller;

import kr.post.component.RestaurantModel;
import kr.post.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;

    // 맛집 상세보기
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantModel> getRestaurant(@PathVariable Long id) {
        ResponseEntity<RestaurantModel> restaurantOpt = restaurantService.getOneRestaurant(id);
        return restaurantOpt;
    }

}
