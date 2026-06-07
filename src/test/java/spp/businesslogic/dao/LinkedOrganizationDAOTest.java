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
        testLinkedOrganization.setName("Aceites de Palma A.C., de C.V");
        testLinkedOrganization.setRfc("RFC131A98WTAB12");
        testLinkedOrganization.setAddress("Guadalupe Victoria 111");
        testLinkedOrganization.setFiscalAddress("Guadalupe Victoria 311");
        testLinkedOrganization.setBusiness("Industrial");
        testLinkedOrganization.setPhoneNumber("9241564162");
        testLinkedOrganization.setEmail("aceitespalma@aceites.mx");
    }

    @Test
    @DisplayName("Debe insertar una organización vinculada exitosamente")
    void testAddLinkedOrganizationSuccess() throws DAOException {
        boolean result = linkedOrganizationDAO.addLinkedOrganization(testLinkedOrganization);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe agregar organización con giro")
    void testAddLinkedOrganizationWithBusinessSuccess() throws DAOException {
        testLinkedOrganization.setBusiness("Tecnología");
        boolean result =
                linkedOrganizationDAO.addLinkedOrganization(testLinkedOrganization);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar un dato duplicado")
    void testAddLinkedOrganizationFailedDuplicatedData() throws DAOException {
        linkedOrganizationDAO.addLinkedOrganization(testLinkedOrganization);
        assertThrows(DAOException.class, () ->
            linkedOrganizationDAO.addLinkedOrganization(testLinkedOrganization));
    }

    @Test
    @DisplayName("Debe agregar organización con dirección fiscal igual a la dirección")
    void testAddLinkedOrganizationWithSameAddressesSuccess() throws DAOException {
        testLinkedOrganization.setFiscalAddress(
                testLinkedOrganization.getAddress());
        boolean result =
                linkedOrganizationDAO.addLinkedOrganization(testLinkedOrganization);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe agregar organización con correo institucional")
    void testAddLinkedOrganizationWithEmailSuccess() throws DAOException {
        testLinkedOrganization.setEmail("contacto@palma.com.mx");
        boolean result =
                linkedOrganizationDAO.addLinkedOrganization(testLinkedOrganization);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar organización sin nombre")
    void testAddLinkedOrganizationFailedNullName() throws DAOException {
        testLinkedOrganization.setName(null);
        assertThrows(DAOException.class, () -> {
            linkedOrganizationDAO.addLinkedOrganization(testLinkedOrganization);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar organización sin RFC")
    void testAddLinkedOrganizationFailedNullRFC() throws DAOException {
        testLinkedOrganization.setRfc(null);
        assertThrows(DAOException.class, () -> {
            linkedOrganizationDAO.addLinkedOrganization(testLinkedOrganization);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar organización sin dirección")
    void testAddLinkedOrganizationFailedNullAddress() throws DAOException {
        testLinkedOrganization.setAddress(null);
        assertThrows(DAOException.class, () -> {
            linkedOrganizationDAO.addLinkedOrganization(testLinkedOrganization);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar organización sin correo")
    void testAddLinkedOrganizationFailedNullEmail() throws DAOException {
        testLinkedOrganization.setEmail(null);
        assertThrows(DAOException.class, () -> {
            linkedOrganizationDAO.addLinkedOrganization(testLinkedOrganization);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar organización sin teléfono")
    void testAddLinkedOrganizationFailedNullPhoneNumber() throws DAOException {
        testLinkedOrganization.setPhoneNumber(null);
        assertThrows(DAOException.class, () -> {
            linkedOrganizationDAO.addLinkedOrganization(testLinkedOrganization);
        });
    }

    @Test
    @DisplayName("Debe lanzar error al insertar RFC duplicado")
    void testAddLinkedOrganizationFailedDuplicateRFC() throws DAOException {
        linkedOrganizationDAO.addLinkedOrganization(testLinkedOrganization);
        LinkedOrganizationDTO duplicateOrganization = new LinkedOrganizationDTO();
        duplicateOrganization.setRfc(testLinkedOrganization.getRfc());
        assertThrows(DAOException.class, () -> {
            linkedOrganizationDAO.addLinkedOrganization(duplicateOrganization);
        });
    }
}
