package vs.restmongo.band;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
@NoArgsConstructor
@Document("bands")
public class Band {

    @Id
    @Setter
    private String id;

    @Setter
    @NotBlank
    private String name;

    @Setter
    @NotBlank
    private String genre;

    public Band(@NotBlank String name, @NotBlank String genre) {
        this.name = name;
        this.genre = genre;
    }

    public BandDto toBandDto() {
        return new BandDto(this.name, this.genre);
    }

}
