package spp.businesslogic.interfaces;


import spp.businesslogic.dto.IndicatorFilterDTO;
import spp.businesslogic.dto.IndicatorReportDTO;
import spp.businesslogic.exceptions.DAOException;


public interface IIndicatorReportDAO {

    IndicatorReportDTO getStaticsByIndicators(IndicatorFilterDTO filters) throws DAOException;

}
