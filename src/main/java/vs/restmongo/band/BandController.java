package vs.restmongo.band;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("band")
public class BandController {

    private final BandService bandService;

    public BandController(BandService bandService) {
        this.bandService = bandService;
    }

    @GetMapping
    public ResponseEntity<List<BandDto>> getAll() {
        return ResponseEntity.ok(bandService.findAll());
    }

    @GetMapping("{name}")
    public ResponseEntity<BandDto> getByName(@PathVariable("name") String name) {
        var nameWithSpaces = name.replace('_', ' ');
        return ResponseEntity.ok(bandService.findByName(nameWithSpaces));
    }

    @PostMapping
    public ResponseEntity<BandDto> save(HttpServletRequest request, @Valid @RequestBody BandDto band) {
        var savedBandDto = bandService.save(band);
        var savedBandDtoUri = UriComponentsBuilder.fromUriString(request.getServletPath()).path("/" + savedBandDto.getName()).build().toUri();
        return ResponseEntity.created(savedBandDtoUri).body(savedBandDto);
    }

}
