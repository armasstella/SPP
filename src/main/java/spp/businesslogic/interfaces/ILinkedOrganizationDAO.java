package spp.businesslogic.interfaces;


import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.exceptions.DAOException;
import java.util.List;


public interface ILinkedOrganizationDAO {

    boolean addLinkedOrganization(LinkedOrganizationDTO linkedOrganization) throws DAOException;
    List<LinkedOrganizationDTO> obtainActiveLinkedOrganizations() throws DAOException;
    boolean searchLinkedOrganizationRegisters() throws DAOException;

}
