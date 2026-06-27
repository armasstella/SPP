package spp.businesslogic.interfaces;

import spp.businesslogic.dto.PresentationTemplateDTO;
import spp.businesslogic.exceptions.DAOException;

public interface IPresentationTemplateDAO {

    boolean saveDocument(String personalNumber, PresentationTemplateDTO presentationTemplateDTO) throws DAOException;

}
