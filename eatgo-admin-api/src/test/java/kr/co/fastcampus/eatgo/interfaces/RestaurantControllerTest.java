package kr.co.fastcampus.eatgo.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.fastcampus.eatgo.application.RestaurantService;
import kr.co.fastcampus.eatgo.domain.MenuItem;
import kr.co.fastcampus.eatgo.domain.Restaurant;
import kr.co.fastcampus.eatgo.domain.RestaurantNotFoundException;
import kr.co.fastcampus.eatgo.domain.Review;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(RestaurantController.class)
public class RestaurantControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RestaurantService restaurantService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void list() throws Exception {

        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add( Restaurant.builder()
                .id(1004L)
                .categoryId(1L)
                .name("JOKER House")
                .address("Seoul")
                .build());

        given(restaurantService.getRestaurants()).willReturn(restaurants);

        mvc.perform(get("/restaurants"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("\"id\":1004")
                ))
                .andExpect(content().string(
                        containsString("\"name\":\"JOKER House\"")
                ));
    }

    @Test
    public void detailWithExisted() throws Exception {

        Restaurant restaurant = Restaurant.builder()
                .id(1004L)
                .categoryId(1L)
                .name("JOKER House")
                .address("Seoul")
                .build();
        MenuItem menuItem = MenuItem.builder()
                .name("Kimchi")
                .build();

        restaurant.setMenuItems(Arrays.asList(menuItem));
        Review review = Review.builder()
                    .name("JOKER")
                    .score(5)
                    .description("Great!")
                    .build();
        restaurant.setReviews(Arrays.asList(review));

        given(restaurantService.getRestaurant(1004L)).willReturn(restaurant);

        mvc.perform(get("/restaurants/1004"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("\"id\":1004")
                ))
                .andExpect(content().string(
                        containsString("\"name\":\"JOKER House\"")
                ))
                .andExpect(content().string(
                        containsString("Kimchi")
                ))
                .andExpect(content().string(
                        containsString("Great!")
                ));

    }

    @Test
    public void detailWithNotExisted() throws Exception {
        given(restaurantService.getRestaurant(404L))
                .willThrow(new RestaurantNotFoundException(404L));

        mvc.perform(get("/restaurants/404"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("{}"));
    }


    @Test
    public void createWithValidData() throws Exception {

        Map<String, Object> input = new HashMap<>();
        input.put("categoryId", 1L);
        input.put("name", "BeRyong");
        input.put("address", "Busan");

        given(restaurantService.addRestaurant(any())).will(invocation -> {
            Restaurant restaurant = invocation.getArgument(0);
            return Restaurant.builder()
                    .id(1234L)
                    .categoryId(1L)
                    .name("BeRyong")
                    .address("Busan")
                    .build();
        });

        mvc.perform(post("/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                // .content(objectMapper.writeValueAsString(input)))
                .content("{\"categoryId\":1,\"name\":\"Beryong\"," +
                        "\"address\":\"Busan\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/restaurants/1234"))
                .andExpect(content().string("{}"));

        verify(restaurantService).addRestaurant(any());
    }

    @Test
    public void createWithInvalidData() throws Exception {

        Map<String, String> input = new HashMap<>();
        input.put("name", "");
        input.put("address", "");

        mvc.perform(post("/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                //.content(objectMapper.writeValueAsString(input)))
                .content("{\"categoryId\":1,\"name:\":\"\",\"address\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateWithValidData() throws Exception {
        Map<String, String> input = new HashMap<>();
        input.put("name", "JOKER Bar");
        input.put("address", "Busan");

        mvc.perform(patch("/restaurants/1004")
                .contentType(MediaType.APPLICATION_JSON)
                // .content(objectMapper.writeValueAsString(input)))
                .content("{\"categoryId\":1,\"name\":\"JOKER Bar\"," +
                        "\"address\":\"Busan\"}"))
                .andExpect(status().isOk());

        verify(restaurantService).updateRestaurant(1004L, "JOKER Bar", "Busan");
    }

    @Test
    public void updateWithInvalidData() throws Exception {
        Map<String, String> input = new HashMap<>();
        input.put("name", "");
        input.put("address", "");

        mvc.perform(patch("/restaurants/1004")
                .contentType(MediaType.APPLICATION_JSON)
                //.content(objectMapper.writeValueAsString(input)))
                .content("{\"categoryId\":1,\"name:\":\"\",\"address\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateWithoutName() throws Exception {
        Map<String, String> input = new HashMap<>();
        input.put("name", "");
        input.put("address", "Busan");

        mvc.perform(patch("/restaurants/1004")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }
}