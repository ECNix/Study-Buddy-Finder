package study.buddy.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import study.buddy.api.rating.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
public class RatingTest {
    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    public RatingService rating_Service = new RatingService();

    private Rating rating;

    @BeforeEach
    public void initializeTestCases() {
        //ratingService = new RatingService();
        rating = new Rating(1L, "joe", "not_joe",100.0);
        Assertions.assertNotNull(rating);
        //Assertions.assertNotNull(ratingRepository);
        Assertions.assertNotNull(rating_Service);
    }


    @DisplayName("Test to find all ratings")
    @Test
    public void findAllRatingsTest() {
        //List<Rating> ratingList = ratingService.getAllRatings();
        Assertions.assertNotNull(rating_Service.getAllRatings());
        //Assertions.assertNotEquals(ratingList.size(), 0, "There are no ratings");
    }

    @DisplayName("Test to make ratings")
    @Test
    public void makeRatingsTest() {
        given(ratingRepository.save(rating)).willReturn(rating);

        Rating rating2 = new Rating(1L, "joe", "joe_who", 50.0);
        given(ratingRepository.save(rating2)).willReturn(rating2);

        Rating r1 = rating_Service.makeRating(rating);
        Rating r2 = rating_Service.makeRating(1L, "joe", "joe_who", 50.0);


        Assertions.assertNotNull(r1);
        Assertions.assertNotNull(r2);

    }
    @DisplayName("Test to find rating average of a tutor")
    @Test
    public void findRatingAvgTest() {
        given(ratingRepository.save(rating)).willReturn(rating);
        String s = rating.getTutorName();
        Rating rating2 = new Rating(1L, "joe", "joe_who", 50.0);
        given(ratingRepository.save(rating2)).willReturn(rating2);
        //given(ratingRepository.findByTutorName(s)).willReturn(List.of(rating, rating2));


        Rating r1 = rating_Service.makeRating(rating);
        Rating r2 = rating_Service.makeRating(1L, "joe", "joe_who", 50.0);

        Double avgR = null;

        avgR = rating_Service.getAvgRating(s);
        Assertions.assertNotNull(avgR);
        //Assertions.assertNotEquals(avgR, 0.0, "Wow, that's bad");
    }

}