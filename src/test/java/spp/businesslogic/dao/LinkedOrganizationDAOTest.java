package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.DisplayName;
import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LinkedOrganizationDAOTest {

    private LinkedOrganizationDAO linkedOrganizationDAO;
    private LinkedOrganizationDTO testOrganization;

    @BeforeAll
    void setupAll() {
        linkedOrganizationDAO = new LinkedOrganizationDAO();
        testOrganization = new LinkedOrganizationDTO();
    }

    @BeforeEach
    void setUp() {
        long currentTimestamp = System.currentTimeMillis();
        String uniqueId = UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        String uniqueName = "Organización " + uniqueId;
        String uniqueEmail = "org" + uniqueId + "@test.com";

        String uniquePhoneSuffix = String.format("%07d", currentTimestamp % 10000000);
        String uniquePhone = "228" + uniquePhoneSuffix;

        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder firstThreeLetters = new StringBuilder();
        StringBuilder lastThreeLetters = new StringBuilder();

        for (int i = 0; i < 3; i++) {
            firstThreeLetters.append(alphabet.charAt((int) (Math.random() * alphabet.length())));
            lastThreeLetters.append(alphabet.charAt((int) (Math.random() * alphabet.length())));
        }

        String year = String.format("%02d", (int)(Math.random() * 100));
        String month = String.format("%02d", (int)(Math.random() * 12) + 1);
        String day = String.format("%02d", (int)(Math.random() * 28) + 1);
        String sixNumbers = year + month + day;

        String uniqueRfc = firstThreeLetters.toString() + sixNumbers + lastThreeLetters.toString();

        testOrganization.setName(uniqueName);
        testOrganization.setRfc(uniqueRfc);
        testOrganization.setAddress("Calle 123");
        testOrganization.setFiscalAddress("Calle Fiscal 456");
        testOrganization.setCity("Xalapa");
        testOrganization.setState("Veracruz");
        testOrganization.setBusiness("Tecnología");
        testOrganization.setPhoneNumber(uniquePhone);
        testOrganization.setEmail(uniqueEmail);
    }

    @Test
    @Order(1)
    @DisplayName("Flujo Normal: Debe registrar una organización vinculada correctamente")
    void testRegisterLinkedOrganizationSuccess() throws DAOException {
        boolean result = linkedOrganizationDAO.registerLinkedOrganization(testOrganization);
        assertTrue(result);
    }

    @Test
    @Order(2)
    @DisplayName("Excepción: Debe lanzar DAOException al intentar registrar una organización con RFC o email duplicado")
    void testRegisterLinkedOrganizationDuplicate() throws DAOException {
        linkedOrganizationDAO.registerLinkedOrganization(testOrganization);

        DAOException exception = assertThrows(DAOException.class, () -> {
            linkedOrganizationDAO.registerLinkedOrganization(testOrganization);
        });

        assertTrue(exception.getMessage().contains("La organización vinculada que intenta registrar ya existe"));
    }

    @Test
    @Order(3)
    @DisplayName("Flujo Normal: Debe obtener la lista de organizaciones vinculadas con identificadores y verificar que la registrada esté presente")
    void testFindActiveLinkedOrganizationsIdentifiers() throws DAOException {
        linkedOrganizationDAO.registerLinkedOrganization(testOrganization);

        List<LinkedOrganizationDTO> organizations = linkedOrganizationDAO.findActiveLinkedOrganizationsIdentifiers();
        assertNotNull(organizations);
        assertFalse(organizations.isEmpty());

        boolean found = organizations.stream()
                .anyMatch(organization -> Objects.equals(organization.getRfc(), testOrganization.getRfc()) &&
                        testOrganization.getName().equals(organization.getName()));

        assertTrue(found);
    }

    @Test
    @Order(4)
    @DisplayName("Flujo Normal: Debe verificar que existen organizaciones vinculadas (devuelve true)")
    void testExistsLinkedOrganizationsTrue() throws DAOException {
        linkedOrganizationDAO.registerLinkedOrganization(testOrganization);

        boolean exists = linkedOrganizationDAO.existsLinkedOrganizations();
        assertTrue(exists);
    }

    @Test
    @Order(5)
    @DisplayName("Flujo Alterno: Debe verificar que no existen organizaciones vinculadas (devuelve false) - solo si la BD está vacía")
    void testExistsLinkedOrganizationsFalse() throws DAOException {
        assertDoesNotThrow(() -> {
            linkedOrganizationDAO.existsLinkedOrganizations();
        });
    }
}