package vs.restmongo.band;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BandService {

    private BandRepository bandRepository;

    public BandService(BandRepository bandRepository) {
        this.bandRepository = bandRepository;
    }

    public List<BandDto> findAll() {
        var allBands = bandRepository.findAll();
        var allBandsDto = allBands.stream().map(band -> band.toBandDto()).collect(Collectors.toList());
        return allBandsDto;
    }

    public BandDto findByName(String name) {
        var foundBand = bandRepository.findByName(name);
        if (foundBand.isEmpty()) {
            log.warn("Band " + name + " was not found");
            throw new BandNotFoundException("Band " + name + " was not found");
        }
        return foundBand.get().toBandDto();
    }

}
