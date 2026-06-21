package spp.businesslogic.interfaces;


import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.exceptions.DAOException;
import java.util.List;


public interface ILinkedOrganizationDAO {

    boolean registerLinkedOrganization(LinkedOrganizationDTO linkedOrganization) throws DAOException;
    List<LinkedOrganizationDTO> findActiveLinkedOrganizationsIdentifiers() throws DAOException;
    boolean existsLinkedOrganizations() throws DAOException;

}
