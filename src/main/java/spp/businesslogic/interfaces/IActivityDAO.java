package spp.businesslogic.interfaces;


import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.exceptions.DAOException;
import java.util.List;


public interface IActivityDAO {

    boolean saveActivity(String studentNumber, ActivityDTO activity) throws DAOException;
    List<ActivityDTO> obtainActivitiesByIntern(String studentNumber) throws DAOException;
    boolean updateActivity(ActivityDTO activity) throws DAOException;
    boolean deleteActivity(int idActivity) throws DAOException;

}
