package spp.businesslogic.interfaces;


import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.exceptions.DAOException;
import java.util.List;


public interface IActivityDAO {

    boolean saveActivityForIntern(String studentNumber, ActivityDTO activityDTO) throws DAOException;
    List<ActivityDTO> findActivitiesByStudentNumber(String studentNumber) throws DAOException;
    boolean updateActivity(ActivityDTO activity) throws DAOException;
    boolean deleteActivity(int idActivity) throws DAOException;

}
