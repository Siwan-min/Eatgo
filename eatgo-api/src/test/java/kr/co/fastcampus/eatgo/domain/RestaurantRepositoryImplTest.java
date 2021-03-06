package kr.co.fastcampus.eatgo.domain;

import org.junit.jupiter.api.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantRepositoryImplTest {

    @Test
    public void save(){

        RestaurantRepository repository = new RestaurantRepositoryImpl();

        int oldCount = repository.findAll().size();

        Restaurant restaurant = new Restaurant("BeRyong", "Busan");
        repository.save(restaurant);

        assertThat(restaurant.getId(), is(1234L));

        int newCount = repository.findAll().size();
        assertThat(newCount - oldCount, is(1));

    }
}