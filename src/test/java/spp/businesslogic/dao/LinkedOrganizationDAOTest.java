package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.exceptions.DAOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class LinkedOrganizationDAOTest {

    private LinkedOrganizationDAO linkedOrganizationDAO;
    private LinkedOrganizationDTO testLinkedOrganization;
    private String uniqueSuffix;

    @BeforeAll
    void setUpAll() {
        linkedOrganizationDAO = new LinkedOrganizationDAO();
        testLinkedOrganization = new LinkedOrganizationDTO();
    }

    @BeforeEach
    void setUpEach() {
        uniqueSuffix = String.valueOf(System.currentTimeMillis());
        String uniqueRfc = "ABC" + uniqueSuffix.substring(uniqueSuffix.length() - 6) + "XYZ";
        String uniqueEmail = "org" + uniqueSuffix.substring(uniqueSuffix.length() - 8) + "@test.com";
        String uniquePhone = "55" + uniqueSuffix.substring(uniqueSuffix.length() - 8);

        String uniqueName = "Org " + uniqueSuffix;

        testLinkedOrganization.setName(uniqueName);
        testLinkedOrganization.setRfc(uniqueRfc);
        testLinkedOrganization.setAddress("Calle Falsa 123");
        testLinkedOrganization.setFiscalAddress("Calle Fiscal 456");
        testLinkedOrganization.setCity("Xalapa");
        testLinkedOrganization.setState("Veracruz");
        testLinkedOrganization.setBusiness("Tecnología");
        testLinkedOrganization.setPhoneNumber(uniquePhone);
        testLinkedOrganization.setEmail(uniqueEmail);
    }

    @Test
    @Order(1)
    @DisplayName("Debe devolver false si no hay organizaciones (base de datos vacía)")
    void testSearchLinkedOrganizationRegistersFalse() throws DAOException {
        boolean exists = linkedOrganizationDAO.searchLinkedOrganizationRegisters();
        assertFalse(exists);
    }

    @Test
    @Order(2)
    @DisplayName("Debe lanzar DAOException si se recibe una organización nula")
    void testAddLinkedOrganizationNullDTO() {
        assertThrows(DAOException.class, () -> linkedOrganizationDAO.addLinkedOrganization(null));
    }

    @Test
    @Order(3)
    @DisplayName("Debe insertar una organización vinculada exitosamente")
    void testAddLinkedOrganizationSuccess() throws DAOException {
        boolean result = linkedOrganizationDAO.addLinkedOrganization(testLinkedOrganization);
        assertTrue(result);
    }

    @Test
    @Order(4)
    @DisplayName("Debe lanzar DAOException al insertar organización con nombre duplicado")
    void testAddLinkedOrganizationDuplicateName() throws DAOException {
        linkedOrganizationDAO.addLinkedOrganization(testLinkedOrganization);
        LinkedOrganizationDTO duplicate = new LinkedOrganizationDTO();
        duplicate.setName(testLinkedOrganization.getName());
        duplicate.setRfc("XYZ" + uniqueSuffix.substring(uniqueSuffix.length() - 6) + "ABC");
        duplicate.setAddress("C. Jamaica 11011");
        duplicate.setFiscalAddress("Av. Xalapa 1");
        duplicate.setCity("Puebla");
        duplicate.setState("Puebla");
        duplicate.setBusiness("Industrial");
        duplicate.setPhoneNumber("4234567890");
        duplicate.setEmail("aceitesdepalma@aceites.com.mx");

        assertThrows(DAOException.class, () -> linkedOrganizationDAO.addLinkedOrganization(duplicate));
    }

    @Test
    @Order(5)
    @DisplayName("Debe lanzar DAOException al insertar organización con RFC duplicado")
    void testAddLinkedOrganizationDuplicateRfc() throws DAOException {
        linkedOrganizationDAO.addLinkedOrganization(testLinkedOrganization);
        LinkedOrganizationDTO duplicate = new LinkedOrganizationDTO();
        duplicate.setName("Aceites de Palma");
        duplicate.setRfc(testLinkedOrganization.getRfc());
        duplicate.setAddress("Calle Aceites de Palma 121");
        duplicate.setFiscalAddress("Calle  Aceites de Palma 1201");
        duplicate.setCity("Minatitlán");
        duplicate.setState("Veracruz");
        duplicate.setBusiness("Industrial");
        duplicate.setPhoneNumber("9876543210");
        duplicate.setEmail("palmiste@palmiste.com.mx");

        assertThrows(DAOException.class, () -> linkedOrganizationDAO.addLinkedOrganization(duplicate));
    }

    @Test
    @Order(6)
    @DisplayName("Debe obtener lista de organizaciones (puede estar vacía)")
    void testObtainActiveLinkedOrganizations() throws DAOException {
        var list = linkedOrganizationDAO.obtainActiveLinkedOrganizations();
        assertNotNull(list);
    }
}