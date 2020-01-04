package vs.restmongo.band;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("band")
public class BandController {

    @GetMapping
    public ResponseEntity<String> getAll(){
        return ResponseEntity.ok("All bands");
    }

}
