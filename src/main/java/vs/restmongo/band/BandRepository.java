package vs.restmongo.band;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BandRepository extends MongoRepository<Band, String> {

    Optional<Band> findByName(String name);

}
