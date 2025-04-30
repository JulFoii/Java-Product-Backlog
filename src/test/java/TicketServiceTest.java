import com.example.application.data.Ticket;
import com.example.application.repository.TicketRepository;
import com.example.application.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository repository;

    @InjectMocks
    private TicketService service;

    private Ticket t1, t2;

    @BeforeEach
    void setUp() {
        t1 = new Ticket("A", "desc A", "Sprint 1", 1);
        t1.setId(1L);
        t2 = new Ticket("B", "desc B", "Sprint 2", 2);
        t2.setId(2L);
    }

    @Test
    void findAll_returnsOrderedList() {
        when(repository.findAllByOrderByPriorityAsc()).thenReturn(Arrays.asList(t1, t2));

        List<Ticket> all = service.findAll();

        assertThat(all).containsExactly(t1, t2);
        verify(repository, times(1)).findAllByOrderByPriorityAsc();
    }

    @Test
    void save_delegatesToRepository() {
        when(repository.save(t1)).thenReturn(t1);

        Ticket saved = service.save(t1);

        assertThat(saved).isSameAs(t1);
        verify(repository).save(t1);
    }

    @Test
    void delete_delegatesToRepository() {
        doNothing().when(repository).delete(t2);

        service.delete(t2);

        verify(repository).delete(t2);
    }
}
