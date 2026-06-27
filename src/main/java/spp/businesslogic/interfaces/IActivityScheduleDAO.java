package spp.businesslogic.interfaces;

import spp.businesslogic.dto.ActivityScheduleDTO;
import spp.businesslogic.exceptions.DAOException;

public interface IActivityScheduleDAO {

    boolean saveActivitySchedule(ActivityScheduleDTO activityScheduleDTO, int projectId) throws DAOException;
}
