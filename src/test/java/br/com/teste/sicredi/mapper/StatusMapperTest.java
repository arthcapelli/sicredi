package br.com.teste.sicredi.mapper;

import br.com.teste.sicredi.domain.Status;
import br.com.teste.sicredi.representation.response.StatusResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class StatusMapperTest {

    private final StatusMapper statusMapper = new StatusMapper();

    @Test
    public void testToStatusResponse() {
        StatusResponse response = statusMapper.toResponse(Status.ABLE_TO_VOTE.toString());

        assertEquals("ABLE_TO_VOTE", response.getStatus());
    }

}