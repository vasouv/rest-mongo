package vs.restmongo.band;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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
    public void getAll() throws Exception {
        mockMvc.perform(get("/band")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getByName() throws Exception {
        var sabatonDto = new BandDto("Sabaton", "Power Metal");

        when(bandService.findByName(anyString())).thenReturn(sabatonDto);

        MvcResult resultActions = mockMvc.perform(get("/band/{name}", "Sabaton"))
                .andExpect(status().isOk()).andReturn();

        var resultDto = mapper.readValue(resultActions.getResponse().getContentAsString(), BandDto.class);

        assertThat(resultDto.getName()).isEqualTo(sabatonDto.getName());
        assertThat(resultDto.getGenre()).isEqualTo(sabatonDto.getGenre());
    }

    @Test
    public void getByNameThrowsException() throws Exception {
        when(bandService.findByName(anyString())).thenThrow(new BandNotFoundException(""));
        mockMvc.perform(get("/band/{name}", "Sabaton")).andExpect(status().isNotFound());
    }
}