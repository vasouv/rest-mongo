package vs.restmongo.band;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
