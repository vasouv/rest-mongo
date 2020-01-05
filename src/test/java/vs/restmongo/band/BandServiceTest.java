package vs.restmongo.band;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BandServiceTest {

    @Mock
    private BandRepository bandRepository;

    @InjectMocks
    private BandService bandService = new BandService(bandRepository);

    final Band sabaton = new Band("Sabaton", "Power Metal");

    @Test
    @DisplayName("find all bands returns empty list")
    public void findAllReturnsEmptyList() {
        when(bandRepository.findAll()).thenReturn(Lists.emptyList());
        var emptyBandsDtoList = bandService.findAll();
        assertThat(emptyBandsDtoList).isEmpty();
    }

    @Test
    @DisplayName("find all bands returns bands from database")
    public void findAllReturnsBandsFromDatabase() {

        var bandsList = Lists.newArrayList(sabaton);

        when(bandRepository.findAll()).thenReturn(bandsList);

        var bandsDtoListFromDatabase = bandService.findAll();

        assertThat(bandsDtoListFromDatabase).isNotEmpty();
    }

    @Test
    @DisplayName("find band by name - exists")
    public void findByName() {
        when(bandRepository.findByName("Sabaton")).thenReturn(Optional.of(sabaton));

        var sabatonDto = bandService.findByName("Sabaton");

        assertThat(sabatonDto).isNotNull();
        assertThat(sabatonDto.getName()).isEqualTo(sabaton.getName());
        assertThat(sabatonDto.getGenre()).isEqualTo(sabaton.getGenre());
    }

    @Test
    @DisplayName("find band by name - throws bandnotexists exception")
    public void findByNameThrowsException(){
        when(bandRepository.findByName("Sabaton")).thenThrow(new BandNotFoundException("Band Sabaton does not exist"));

        assertThatThrownBy(() -> bandService.findByName("Sabaton")).isInstanceOf(BandNotFoundException.class);
    }
}