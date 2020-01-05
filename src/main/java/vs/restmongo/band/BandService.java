package vs.restmongo.band;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BandService {

    private final BandRepository bandRepository;

    public BandService(BandRepository bandRepository) {
        this.bandRepository = bandRepository;
    }

    public List<BandDto> findAll() {
        var allBands = bandRepository.findAll();
        return allBands.stream().map(Band::toBandDto).collect(Collectors.toList());
    }

    public BandDto findByName(String name) {
        var foundBand = bandRepository.findByName(name);
        if (foundBand.isEmpty()) {
            log.warn("Band " + name + " was not found");
            throw new BandNotFoundException("Band " + name + " was not found");
        }
        return foundBand.get().toBandDto();
    }

    public BandDto save(BandDto bandDto) {
        var bandToSave = bandDto.toBand();
        Optional<Band> foundBand = bandRepository.findByName(bandToSave.getName());
        if (foundBand.isPresent()) {
            log.warn("Band " + bandToSave.getName() + " exists");
            throw new BandExistsException("Band " + bandToSave.getName() + " already exists");
        }

        Band savedBand = bandRepository.save(bandToSave);
        return savedBand.toBandDto();
    }
}
