package vs.restmongo.band;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import vs.restmongo.band.exceptions.BandExistsException;
import vs.restmongo.band.exceptions.BandNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BandService {

    private final MongoTemplate mongoTemplate;

    public BandService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<BandDto> findAll() {
        var allBands = mongoTemplate.findAll(Band.class);
        return allBands.stream().map(Band::toBandDto).collect(Collectors.toList());
    }

    public BandDto findByName(String name) {

        var query = new Query().addCriteria(Criteria.where("name").is(name));

        var foundBand = mongoTemplate.findOne(query, Band.class);

        if (foundBand == null) {
            log.warn("Band " + name + " was not found");
            throw new BandNotFoundException("Band " + name + " was not found");
        }
        return foundBand.toBandDto();
    }

    public BandDto save(BandDto bandDto) {
        var bandToSave = bandDto.toBand();

        var query = new Query().addCriteria(Criteria.where("name").is(bandToSave.getName()));
        var foundBand = mongoTemplate.findOne(query, Band.class);

        if (foundBand != null) {
            log.warn("Band " + bandToSave.getName() + " exists");
            throw new BandExistsException("Band " + bandToSave.getName() + " already exists");
        }

        var savedBand = mongoTemplate.save(bandToSave);
        return savedBand.toBandDto();
    }
}
