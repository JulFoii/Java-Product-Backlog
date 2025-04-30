package com.example.application.service;

import com.example.application.data.Ticket;
import com.example.application.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository repository;

    public TicketService(TicketRepository repository) {
        this.repository = repository;
    }

    public List<Ticket> findAll() {
        return repository.findAllByOrderByPriorityAsc();
    }

    public Ticket save(Ticket ticket) {
        return repository.save(ticket);
    }

    public void delete(Ticket ticket) {
        repository.delete(ticket);
    }
}
