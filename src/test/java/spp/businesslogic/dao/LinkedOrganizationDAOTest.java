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
        testLinkedOrganization = new LinkedOrganizationDTO();
    }

    @BeforeEach
    void setUp() {
        String uniqueSuffix = String.valueOf(System.currentTimeMillis());
        String shortSuffix = uniqueSuffix.substring(uniqueSuffix.length() - 5);
        String uniqueRfc = "DEV" + shortSuffix + "XYZ";

        testLinkedOrganization.setName("TechSolutions " + shortSuffix);
        testLinkedOrganization.setRfc(uniqueRfc);
        testLinkedOrganization.setAddress("Av. Mártires 28 de Agosto 111");
        testLinkedOrganization.setFiscalAddress("Av. Mártires 28 de Agosto 111");
        testLinkedOrganization.setBusiness("Desarrollo de Software");
        testLinkedOrganization.setPhoneNumber("228123" + shortSuffix.substring(0, 4));
        testLinkedOrganization.setEmail("tech" + shortSuffix + "@techsolutions.mx");
    }

    @Test
    @DisplayName("Debe insertar una organización vinculada exitosamente")
    void testAddLinkedOrganizationSuccess() throws DAOException {
        boolean result = linkedOrganizationDAO.addLinkedOrganization(testLinkedOrganization);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe agregar organización con giro modificado")
    void testAddLinkedOrganizationWithBusinessSuccess() throws DAOException {
        testLinkedOrganization.setBusiness("Consultoría de TI");
        boolean result = linkedOrganizationDAO.addLinkedOrganization(testLinkedOrganization);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar exactamente la misma organización dos veces")
    void testAddLinkedOrganizationFailedDuplicatedData() throws DAOException {
        linkedOrganizationDAO.addLinkedOrganization(testLinkedOrganization);
        assertThrows(DAOException.class, () -> {
            linkedOrganizationDAO.addLinkedOrganization(testLinkedOrganization);
        });
    }

    @Test
    @DisplayName("Debe agregar organización con dirección fiscal distinta a la dirección física")
    void testAddLinkedOrganizationWithDifferentAddressesSuccess() throws DAOException {
        testLinkedOrganization.setFiscalAddress("Calle Nueva 999, Centro");
        boolean result = linkedOrganizationDAO.addLinkedOrganization(testLinkedOrganization);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar organización sin nombre")
    void testAddLinkedOrganizationFailedNullName() {
        testLinkedOrganization.setName(null);
        assertThrows(DAOException.class, () -> {
            linkedOrganizationDAO.addLinkedOrganization(testLinkedOrganization);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar organización sin RFC")
    void testAddLinkedOrganizationFailedNullRFC() {
        testLinkedOrganization.setRfc(null);
        assertThrows(DAOException.class, () -> {
            linkedOrganizationDAO.addLinkedOrganization(testLinkedOrganization);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar organización sin dirección")
    void testAddLinkedOrganizationFailedNullAddress() {
        testLinkedOrganization.setAddress(null);
        assertThrows(DAOException.class, () -> {
            linkedOrganizationDAO.addLinkedOrganization(testLinkedOrganization);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar organización sin correo")
    void testAddLinkedOrganizationFailedNullEmail() {
        testLinkedOrganization.setEmail(null);
        assertThrows(DAOException.class, () -> {
            linkedOrganizationDAO.addLinkedOrganization(testLinkedOrganization);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar organización sin teléfono")
    void testAddLinkedOrganizationFailedNullPhoneNumber() {
        testLinkedOrganization.setPhoneNumber(null);
        assertThrows(DAOException.class, () -> {
            linkedOrganizationDAO.addLinkedOrganization(testLinkedOrganization);
        });
    }

    @Test
    @DisplayName("Debe lanzar error al insertar un RFC que ya le pertenece a otra empresa")
    void testAddLinkedOrganizationFailedDuplicateRFC() throws DAOException {
        linkedOrganizationDAO.addLinkedOrganization(testLinkedOrganization);
        LinkedOrganizationDTO duplicateOrganization = new LinkedOrganizationDTO();
        duplicateOrganization.setName("Consultores de Sistemas Alternos");
        duplicateOrganization.setAddress("Calle Falsa 123");
        duplicateOrganization.setFiscalAddress("Calle Falsa 123");
        duplicateOrganization.setBusiness("Auditoría");
        duplicateOrganization.setPhoneNumber("2289999999");
        duplicateOrganization.setEmail("auditoria@sistemas.mx");

        duplicateOrganization.setRfc(testLinkedOrganization.getRfc());

        assertThrows(DAOException.class, () -> {
            linkedOrganizationDAO.addLinkedOrganization(duplicateOrganization);
        });
    }
}