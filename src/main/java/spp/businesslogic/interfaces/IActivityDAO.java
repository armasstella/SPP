package spp.businesslogic.interfaces;


import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.enums.ActivityType;
import spp.businesslogic.exceptions.DAOException;
import java.util.List;


public interface IActivityDAO {

    boolean saveActivityForIntern(String studentNumber, ActivityDTO activityDTO, ActivityType activityType) throws DAOException;
    List<ActivityDTO> findMonthlyActivitiesByStudentNumber(String studentNumber) throws DAOException;
    List<ActivityDTO> findFinalActivitiesByStudentNumber(String studentNumber) throws DAOException;
    boolean updateActivity(ActivityDTO activity) throws DAOException;
    boolean deleteActivity(int idActivity) throws DAOException;

}
