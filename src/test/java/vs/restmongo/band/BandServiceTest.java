package vs.restmongo.band;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import vs.restmongo.band.exceptions.BandExistsException;
import vs.restmongo.band.exceptions.BandNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BandServiceTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private BandService bandService;

    final Band sabaton = new Band("Sabaton", "Power Metal");

    @Test
    @DisplayName("find all bands returns empty list")
    public void findAllReturnsEmptyList() {
        when(mongoTemplate.findAll(Band.class)).thenReturn(Lists.emptyList());
        var emptyBandsDtoList = bandService.findAll();
        assertThat(emptyBandsDtoList).isEmpty();
    }

    @Test
    @DisplayName("find all bands returns bands from database")
    public void findAllReturnsBandsFromDatabase() {

        var bandsList = Lists.newArrayList(sabaton);

        when(mongoTemplate.findAll(Band.class)).thenReturn(bandsList);

        var bandsDtoListFromDatabase = bandService.findAll();

        assertThat(bandsDtoListFromDatabase).isNotEmpty();
    }

    @Test
    @DisplayName("find band by name - exists")
    public void findByName() {
        Query query = new Query().addCriteria(Criteria.where("name").is("Sabaton"));
        when(mongoTemplate.findOne(query, Band.class)).thenReturn(sabaton);

        var sabatonDto = bandService.findByName("Sabaton");

        assertThat(sabatonDto).isNotNull();
        assertThat(sabatonDto.getName()).isEqualTo(sabaton.getName());
        assertThat(sabatonDto.getGenre()).isEqualTo(sabaton.getGenre());
    }

    @Test
    @DisplayName("find band by name - throws bandnotexists exception")
    public void findByNameThrowsException() {
        Query query = new Query().addCriteria(Criteria.where("name").is("Sabaton"));
        when(mongoTemplate.findOne(query, Band.class)).thenThrow(new BandNotFoundException("Band Sabaton does not exist"));

        assertThatThrownBy(() -> bandService.findByName("Sabaton")).isInstanceOf(BandNotFoundException.class);
    }

    @Test
    @DisplayName("save new band - throws bandexists exception")
    public void saveNewBandThrowsExceptions() {
        var query = new Query().addCriteria(Criteria.where("name").is("Sabaton"));

        var sabatonWithId = new Band("Sabaton", "Power Metal");
        sabatonWithId.setId("id");

        when(mongoTemplate.findOne(query, Band.class)).thenReturn(sabatonWithId);

        assertThatThrownBy(() -> bandService.save(sabaton.toBandDto())).isInstanceOf(BandExistsException.class);

    }
}