package study.buddy.api.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {
    @Autowired
    RatingRepository ratingRepository;

    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }
    public Rating makeRating(Long sessionID, String tutor, String rater, Double rating) {
        Optional<Rating> prevRate = ratingRepository.findBySessionIDAndTutorNameAndRaterName(sessionID, tutor, rater);
        if(prevRate.isEmpty())
            return ratingRepository.save(new Rating(sessionID, tutor, rater, rating));
        else {
            Rating r = prevRate.get();
            r.setRating(rating);
            return ratingRepository.save(r);
        }
    }

    public Rating makeRating(Rating rating) {
        Optional<Rating> prevRate = ratingRepository.findBySessionIDAndTutorNameAndRaterName(rating.getSessionID(), rating.getTutorName(), rating.getRaterName());
        if(prevRate.isEmpty())
            return ratingRepository.save(rating);
        else {
            Rating r = prevRate.get();
            r.setRating(rating.getRating());
            return ratingRepository.save(r);
        }
    }

    public Double getAvgRating(String tutor) {
//        List<Object[]> results = ratingRepository.averageTutorRating(tutor);
//        if(results.size() > 0) {
//            Double avg = (Double)(results.get(0)[0]);
//            return avg;
//        }
        return ratingRepository.averageTutorRating(tutor);
    }
}
