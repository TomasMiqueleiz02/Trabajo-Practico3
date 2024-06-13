package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CuentaServiceTest {
@Mock
    private CuentaDao cuentaDao;
@Mock
    private ClienteService clienteService;
@InjectMocks
    private CuentaService cuentaService;

@BeforeAll
    public void setUp(){
    MockitoAnnotations.openMocks(this);
}
@Test
public void CuentaExistente() throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException {
    Cliente pepeRino = new Cliente();
    pepeRino.setDni(26456437);
    pepeRino.setNombre("Pepe");
    pepeRino.setApellido("Rino");
    pepeRino.setFechaNacimiento(LocalDate.of(1978, 3, 25));
    pepeRino.setTipoPersona(TipoPersona.PERSONA_FISICA);

    Cuenta cuenta = new Cuenta();
    cuenta.setNumeroCuenta(2322332);
    cuenta.setBalance(0);
    cuenta.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
    cuenta.setMoneda(TipoMoneda.PESOS);
    cuenta.setTitular(pepeRino);
    cuenta.setFechaCreacion(LocalDateTime.now());

    when(cuentaDao.find(2322332)).thenReturn(null);

    assertThrows(CuentaAlreadyExistsException.class,()-> cuentaService.darDeAltaCuenta(cuenta,2322332));
}
@Test
    public void cuentaNoSoportada()throws CuentaNoSoportadaException {
    Cuenta cuenta1 = new Cuenta()
            .setMoneda(TipoMoneda.DOLARES)
            .setBalance(500000)
            .setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
    assertThrows(CuentaNoSoportadaException.class, () -> cuentaService.tipoCuentaEstaSoportada(cuenta1));


}
@Test
    public void tipoDeCuentaExistente()throws  TipoCuentaAlreadyExistsException{
        Cuenta cuenta=new Cuenta();
        cuenta.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        cuenta.setMoneda(TipoMoneda.PESOS);
        Cliente clienteprueba=new Cliente();
    clienteprueba.setDni(12345678);

    when(cuentaDao.find(cuenta.getNumeroCuenta())).thenReturn(cuenta);
    doThrow(TipoCuentaAlreadyExistsException.class).when(clienteService).agregarCuenta(cuenta, clienteprueba.getDni());
    assertThrows(TipoCuentaAlreadyExistsException.class, ()-> cuentaService.darDeAltaCuenta(cuenta, clienteprueba.getDni()));
}
@Test
    public void cuentaCreadaExitosamente() throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, CuentaNoSoportadaException {
    Cliente Peperino=new Cliente();
    Peperino.setDni(44357768);
    Cuenta cuentaprueba=new Cuenta();
    cuentaprueba.setNumeroCuenta(2002);
    cuentaprueba.setTitular(Peperino);
    clienteService.agregarCuenta(cuentaprueba,44357768);
    cuentaDao.save(cuentaprueba);
    verify(cuentaDao,times(1)).save(cuentaprueba);




}
}
