package spp.businesslogic.interfaces;


import spp.businesslogic.exceptions.DAOException;

import java.util.List;

public interface ITermDAO {

    public List<String> findTermNames() throws DAOException;

}
