package vs.restmongo.band;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BandTest {

    @Test
    @DisplayName("convert band pojo to band dto")
    public void toBandDto() {

        var sabaton = new Band("Sabaton","Power Metal");
        var sabatonDto = sabaton.toBandDto();

        assertThat(sabatonDto).isNotNull();
        assertThat(sabatonDto.getName()).isEqualTo(sabaton.getName());
        assertThat(sabatonDto.getGenre()).isEqualTo(sabaton.getGenre());

    }
}