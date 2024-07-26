package edu.qsp.restorent_management_system.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.qsp.restorent_management_system.model.SittingTable;
import edu.qsp.restorent_management_system.repository.SittingTableRepository;
@Service
public class ReservationService {
    @Autowired
    SittingTableRepository sittingTable;

    @SuppressWarnings("null")
    public SittingTable reservTable(Integer id)
    {
        Optional<SittingTable> opt= sittingTable.findById(id);
        SittingTable st= opt.isPresent()?opt.get():null;
        st.setStatus("Reserved");
        System.out.print(st);
        return st;
    }

    
}
