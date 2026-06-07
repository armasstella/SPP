package spp.businesslogic.interfaces;


import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.exceptions.DAOException;


public interface ILinkedOrganizationDAO {

    boolean addLinkedOrganization(LinkedOrganizationDTO linkedOrganization) throws DAOException;

}
