package spp.businesslogic.interfaces;


import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.exceptions.DAOException;


public interface IActivityDAO {

    boolean addActivity(ActivityDTO activityDTO) throws DAOException;

}
