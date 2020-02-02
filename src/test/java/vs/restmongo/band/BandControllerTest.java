package vs.restmongo.band;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import vs.restmongo.band.exceptions.BandExistsException;
import vs.restmongo.band.exceptions.BandNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class BandControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BandService bandService;

    @Test
    @DisplayName("get all bands")
    public void getAllBands() throws Exception {
        mockMvc.perform(get("/band")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("get band by name")
    public void getBandByName() throws Exception {
        var sabatonDto = new BandDto("Sabaton", "Power Metal");

        when(bandService.findByName(anyString())).thenReturn(sabatonDto);

        MvcResult resultActions = mockMvc.perform(get("/band/{name}", "Sabaton"))
                .andExpect(status().isOk()).andReturn();

        var resultDto = mapper.readValue(resultActions.getResponse().getContentAsString(), BandDto.class);

        assertThat(resultDto.getName()).isEqualTo(sabatonDto.getName());
        assertThat(resultDto.getGenre()).isEqualTo(sabatonDto.getGenre());
    }

    @Test
    @DisplayName("get band by name - not exists")
    public void getBandByNameThrowsException() throws Exception {
        when(bandService.findByName(anyString())).thenThrow(new BandNotFoundException(""));
        mockMvc.perform(get("/band/{name}", "Sabaton")).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("save new band")
    public void saveNewBand() throws Exception {
        var sabatonDto = new BandDto("Sabaton", "Power Metal");

        when(bandService.save(sabatonDto)).thenReturn(sabatonDto);

        mockMvc.perform(post("/band")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(sabatonDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("save new band with space in the name")
    public void saveNewBandWithSpaceInTheName() throws Exception {
        var blackSabbathDto = new BandDto("Black Sabbath","Heavy Metal");

        when(bandService.save(blackSabbathDto)).thenReturn(blackSabbathDto);

        MvcResult result = mockMvc.perform(post("/band")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(blackSabbathDto)))
                .andExpect(status().isCreated())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();

        // mvn will product /Black_Sabbath, so I'm removing the leading /
        String location = response.getHeader("Location").substring(1);

        assertThat(location).isEqualTo(blackSabbathDto.getName().replace(' ','_'));
    }

    @Test
    @DisplayName("save new band - empty name - throws exception")
    public void saveNewBandEmptyNameThrowsException() throws Exception {
        var sabatonDto = new BandDto("","Power Metal");

        mockMvc.perform(post("/band")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(sabatonDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("save new band - empty genre - throws exception")
    public void saveNewBandEmptyGenreThrowsException() throws Exception {
        var sabatonDto = new BandDto("Sabaton","");

        mockMvc.perform(post("/band")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(sabatonDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("save new band - empty name and genre - throws exception")
    public void saveNewBandEmptyNameAndGenreThrowsException() throws Exception {
        var sabatonDto = new BandDto("","");

        mockMvc.perform(post("/band")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(sabatonDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("save new band - null name - throws exception")
    public void saveNewBandNullNameThrowsException() throws Exception {
        var sabatonDto = new BandDto(null,"Power Metal");

        mockMvc.perform(post("/band")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(sabatonDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("save new band - null genre - throws exception")
    public void saveNewBandNullGenreThrowsException() throws Exception {
        var sabatonDto = new BandDto("Sabaton",null);

        mockMvc.perform(post("/band")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(sabatonDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("save new band - null name and genre - throws exception")
    public void saveNewBandNullNameAndGenreThrowsException() throws Exception {
        var sabatonDto = new BandDto(null,null);

        mockMvc.perform(post("/band")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(sabatonDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("save new band - band already exists")
    public void saveNewBandAlreadyExists() throws Exception {
        var sabatonDto = new BandDto("Sabaton", "Power Metal");

        when(bandService.save(sabatonDto)).thenThrow(new BandExistsException(""));

        mockMvc.perform(post("/band")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(sabatonDto)))
                .andExpect(status().isConflict());
    }
}