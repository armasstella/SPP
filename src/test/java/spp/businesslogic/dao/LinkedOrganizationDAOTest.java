package spp.businesslogic.dao;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.exceptions.DAOException;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LinkedOrganizationDAOTest {

    private LinkedOrganizationDAO linkedOrganizationDAO;
    private LinkedOrganizationDTO testLinkedOrganization;

    @BeforeAll
    void setUpAll() {
        linkedOrganizationDAO = new LinkedOrganizationDAO();

    }

    @BeforeEach
    void setUp() {
        testLinkedOrganization = new LinkedOrganizationDTO();
        testLinkedOrganization.setName("OV 1");
        testLinkedOrganization.setRfc("1234");
        testLinkedOrganization.setAddress("Dirección 1");
        testLinkedOrganization.setFiscalAddress("Dirección fiscal 1");
        testLinkedOrganization.setBusiness("Giro");
        testLinkedOrganization.setPhoneNumber("9241564162");
        testLinkedOrganization.setEmail("linkedo@gmail.com");

    }

    @Test
    @DisplayName("Debe insertar una organización vinculada exitosamente")
    void testAddLinkedOrganizationSuccess() throws DAOException {
        boolean result = linkedOrganizationDAO.addLinkedOrganization(testLinkedOrganization);
        assertTrue(result, "El método debería retornar true al insertar exitosamente");

    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar un dato duplicado")
    void testAddLinkedOrganizationFailedDuplicatedData() throws DAOException {
        linkedOrganizationDAO.addLinkedOrganization(testLinkedOrganization);
        assertThrows(DAOException.class, () ->
            linkedOrganizationDAO.addLinkedOrganization(testLinkedOrganization));

    }

}
