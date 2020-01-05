package vs.restmongo.band;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class BandDto {

    @NotBlank
    private String name;

    @NotBlank
    private String genre;

    public Band toBand() {
        return new Band(this.name, this.genre);
    }

}
