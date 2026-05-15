package com.cyberpath.smartlearn.controlador._test;

import com.cyberpath.smartlearn.configuracion.seguridad.correo.CorreoServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/smartlearn/api")
@RequiredArgsConstructor
public class Test {
    private final CorreoServicio correoServicio;

    @PostMapping("/enviar-correo")
    public ResponseEntity<String> enviarCorreoPrueba(
            @RequestParam String email,
            @RequestParam(defaultValue = "Juan") String nombre,
            @RequestParam(defaultValue = "123456") String codigo) {

        try {
            correoServicio.enviarCodigoVerificacion(email, nombre, codigo);
            return ResponseEntity.ok("Correo enviado correctamente a: " + email);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al enviar correo: " + e.getMessage());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        try {
            return ResponseEntity.ok("Servidor Funcionando");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error en el Backend: " + e.getMessage());
        }

    }
}
