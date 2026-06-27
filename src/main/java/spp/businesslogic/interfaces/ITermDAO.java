package spp.businesslogic.interfaces;


import spp.businesslogic.exceptions.DAOException;

import java.util.List;

public interface ITermDAO {

    public List<String> findTermNames() throws DAOException;
    String findActiveTermName() throws DAOException;
    boolean deactivateCurrentTerm() throws DAOException;
    boolean insertTerm(String termName) throws DAOException;
    int findActiveTermId() throws DAOException;

}
