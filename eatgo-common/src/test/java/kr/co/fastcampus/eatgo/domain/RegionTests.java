package kr.co.fastcampus.eatgo.domain;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class RegionTests {

    @Test
    public void creation() {
        Region region = Region.builder().name("서울").build();

        assertThat(region.getName(), is("서울"));
    }

}